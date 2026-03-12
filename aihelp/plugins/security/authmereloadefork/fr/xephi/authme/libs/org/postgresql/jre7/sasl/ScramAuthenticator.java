package fr.xephi.authme.libs.org.postgresql.jre7.sasl;

import fr.xephi.authme.libs.org.postgresql.core.PGStream;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.client.ScramClient;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.client.ScramSession;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.exception.ScramException;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.exception.ScramInvalidServerSignatureException;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.exception.ScramParseException;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.exception.ScramServerErrorException;
import fr.xephi.authme.libs.org.postgresql.shaded.com.ongres.scram.common.stringprep.StringPreparations;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ScramAuthenticator {
   private static final Logger LOGGER = Logger.getLogger(ScramAuthenticator.class.getName());
   private final String user;
   private final String password;
   private final PGStream pgStream;
   @Nullable
   private ScramClient scramClient;
   @Nullable
   private ScramSession scramSession;
   private ScramSession.ClientFinalProcessor clientFinalProcessor;

   private void sendAuthenticationMessage(int bodyLength, ScramAuthenticator.BodySender bodySender) throws IOException {
      this.pgStream.sendChar(112);
      this.pgStream.sendInteger4(4 + bodyLength);
      bodySender.sendBody(this.pgStream);
      this.pgStream.flush();
   }

   public ScramAuthenticator(String user, String password, PGStream pgStream) {
      this.user = user;
      this.password = password;
      this.pgStream = pgStream;
   }

   public void processServerMechanismsAndInit() throws IOException, PSQLException {
      ArrayList mechanisms = new ArrayList();

      do {
         mechanisms.add(this.pgStream.receiveString());
      } while(this.pgStream.peekChar() != 0);

      int c = this.pgStream.receiveChar();

      assert c == 0;

      if (mechanisms.isEmpty()) {
         throw new PSQLException(GT.tr("No SCRAM mechanism(s) advertised by the server"), PSQLState.CONNECTION_REJECTED);
      } else {
         ScramClient scramClient;
         try {
            scramClient = ScramClient.channelBinding(ScramClient.ChannelBinding.NO).stringPreparation(StringPreparations.SASL_PREPARATION).selectMechanismBasedOnServerAdvertised((String[])mechanisms.toArray(new String[0])).setup();
         } catch (IllegalArgumentException var5) {
            throw new PSQLException(GT.tr("Invalid or unsupported by client SCRAM mechanisms", var5), PSQLState.CONNECTION_REJECTED);
         }

         if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, " Using SCRAM mechanism {0}", scramClient.getScramMechanism().getName());
         }

         this.scramClient = scramClient;
         this.scramSession = scramClient.scramSession("*");
      }
   }

   public void sendScramClientFirstMessage() throws IOException {
      ScramSession scramSession = this.scramSession;
      String clientFirstMessage = ((ScramSession)Nullness.castNonNull(scramSession)).clientFirstMessage();
      LOGGER.log(Level.FINEST, " FE=> SASLInitialResponse( {0} )", clientFirstMessage);
      ScramClient scramClient = this.scramClient;
      String scramMechanismName = ((ScramClient)Nullness.castNonNull(scramClient)).getScramMechanism().getName();
      final byte[] scramMechanismNameBytes = scramMechanismName.getBytes(StandardCharsets.UTF_8);
      final byte[] clientFirstMessageBytes = clientFirstMessage.getBytes(StandardCharsets.UTF_8);
      this.sendAuthenticationMessage(scramMechanismNameBytes.length + 1 + 4 + clientFirstMessageBytes.length, new ScramAuthenticator.BodySender() {
         public void sendBody(PGStream pgStream) throws IOException {
            pgStream.send(scramMechanismNameBytes);
            pgStream.sendChar(0);
            pgStream.sendInteger4(clientFirstMessageBytes.length);
            pgStream.send(clientFirstMessageBytes);
         }
      });
   }

   public void processServerFirstMessage(int length) throws IOException, PSQLException {
      String serverFirstMessage = this.pgStream.receiveString(length);
      LOGGER.log(Level.FINEST, " <=BE AuthenticationSASLContinue( {0} )", serverFirstMessage);
      ScramSession scramSession = this.scramSession;
      if (scramSession == null) {
         throw new PSQLException(GT.tr("SCRAM session does not exist"), PSQLState.UNKNOWN_STATE);
      } else {
         ScramSession.ServerFirstProcessor serverFirstProcessor;
         try {
            serverFirstProcessor = scramSession.receiveServerFirstMessage(serverFirstMessage);
         } catch (ScramException var7) {
            throw new PSQLException(GT.tr("Invalid server-first-message: {0}", serverFirstMessage), PSQLState.CONNECTION_REJECTED, var7);
         }

         if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.log(Level.FINEST, " <=BE AuthenticationSASLContinue(salt={0}, iterations={1})", new Object[]{serverFirstProcessor.getSalt(), serverFirstProcessor.getIteration()});
         }

         this.clientFinalProcessor = serverFirstProcessor.clientFinalProcessor(this.password);
         String clientFinalMessage = this.clientFinalProcessor.clientFinalMessage();
         LOGGER.log(Level.FINEST, " FE=> SASLResponse( {0} )", clientFinalMessage);
         final byte[] clientFinalMessageBytes = clientFinalMessage.getBytes(StandardCharsets.UTF_8);
         this.sendAuthenticationMessage(clientFinalMessageBytes.length, new ScramAuthenticator.BodySender() {
            public void sendBody(PGStream pgStream) throws IOException {
               pgStream.send(clientFinalMessageBytes);
            }
         });
      }
   }

   public void verifyServerSignature(int length) throws IOException, PSQLException {
      String serverFinalMessage = this.pgStream.receiveString(length);
      LOGGER.log(Level.FINEST, " <=BE AuthenticationSASLFinal( {0} )", serverFinalMessage);
      ScramSession.ClientFinalProcessor clientFinalProcessor = this.clientFinalProcessor;
      if (clientFinalProcessor == null) {
         throw new PSQLException(GT.tr("SCRAM client final processor does not exist"), PSQLState.UNKNOWN_STATE);
      } else {
         try {
            clientFinalProcessor.receiveServerFinalMessage(serverFinalMessage);
         } catch (ScramParseException var5) {
            throw new PSQLException(GT.tr("Invalid server-final-message: {0}", serverFinalMessage), PSQLState.CONNECTION_REJECTED, var5);
         } catch (ScramServerErrorException var6) {
            throw new PSQLException(GT.tr("SCRAM authentication failed, server returned error: {0}", var6.getError().getErrorMessage()), PSQLState.CONNECTION_REJECTED, var6);
         } catch (ScramInvalidServerSignatureException var7) {
            throw new PSQLException(GT.tr("Invalid server SCRAM signature"), PSQLState.CONNECTION_REJECTED, var7);
         }
      }
   }

   private interface BodySender {
      void sendBody(PGStream var1) throws IOException;
   }
}
