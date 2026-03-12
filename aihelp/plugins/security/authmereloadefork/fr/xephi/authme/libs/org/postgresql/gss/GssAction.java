package fr.xephi.authme.libs.org.postgresql.gss;

import fr.xephi.authme.libs.org.postgresql.core.PGStream;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.ServerErrorMessage;
import java.io.IOException;
import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

class GssAction implements PrivilegedAction<Exception>, Callable<Exception> {
   private static final Logger LOGGER = Logger.getLogger(GssAction.class.getName());
   private final PGStream pgStream;
   private final String host;
   private final String kerberosServerName;
   private final String user;
   private final boolean useSpnego;
   @Nullable
   private final Subject subject;
   private final boolean logServerErrorDetail;

   GssAction(PGStream pgStream, @Nullable Subject subject, String host, String user, String kerberosServerName, boolean useSpnego, boolean logServerErrorDetail) {
      this.pgStream = pgStream;
      this.subject = subject;
      this.host = host;
      this.user = user;
      this.kerberosServerName = kerberosServerName;
      this.useSpnego = useSpnego;
      this.logServerErrorDetail = logServerErrorDetail;
   }

   private static boolean hasSpnegoSupport(GSSManager manager) throws GSSException {
      Oid spnego = new Oid("1.3.6.1.5.5.2");
      Oid[] mechs = manager.getMechs();
      Oid[] var3 = mechs;
      int var4 = mechs.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Oid mech = var3[var5];
         if (mech.equals(spnego)) {
            return true;
         }
      }

      return false;
   }

   @Nullable
   public Exception run() {
      try {
         GSSManager manager = GSSManager.getInstance();
         GSSCredential clientCreds = null;
         Oid[] desiredMechs = new Oid[1];
         GSSCredential gssCredential = null;
         if (this.subject != null) {
            Set<GSSCredential> gssCreds = this.subject.getPrivateCredentials(GSSCredential.class);
            if (gssCreds != null && !gssCreds.isEmpty()) {
               gssCredential = (GSSCredential)gssCreds.iterator().next();
            }
         }

         Principal principal;
         if (gssCredential == null) {
            if (this.useSpnego && hasSpnegoSupport(manager)) {
               desiredMechs[0] = new Oid("1.3.6.1.5.5.2");
            } else {
               desiredMechs[0] = new Oid("1.2.840.113554.1.2.2");
            }

            String principalName = this.user;
            if (this.subject != null) {
               Set<Principal> principals = this.subject.getPrincipals();
               Iterator<Principal> principalIterator = principals.iterator();
               principal = null;
               if (principalIterator.hasNext()) {
                  principal = (Principal)principalIterator.next();
                  principalName = principal.getName();
               }
            }

            GSSName clientName = manager.createName(principalName, GSSName.NT_USER_NAME);
            clientCreds = manager.createCredential(clientName, 28800, desiredMechs, 1);
         } else {
            desiredMechs[0] = new Oid("1.2.840.113554.1.2.2");
            clientCreds = gssCredential;
         }

         GSSName serverName = manager.createName(this.kerberosServerName + "@" + this.host, GSSName.NT_HOSTBASED_SERVICE);
         GSSContext secContext = manager.createContext(serverName, desiredMechs[0], clientCreds, 0);
         secContext.requestMutualAuth(true);
         byte[] inToken = new byte[0];
         principal = null;
         boolean established = false;

         while(!established) {
            byte[] outToken = secContext.initSecContext(inToken, 0, inToken.length);
            if (outToken != null) {
               LOGGER.log(Level.FINEST, " FE=> Password(GSS Authentication Token)");
               this.pgStream.sendChar(112);
               this.pgStream.sendInteger4(4 + outToken.length);
               this.pgStream.send(outToken);
               this.pgStream.flush();
            }

            if (!secContext.isEstablished()) {
               int response = this.pgStream.receiveChar();
               switch(response) {
               case 69:
                  int elen = this.pgStream.receiveInteger4();
                  ServerErrorMessage errorMsg = new ServerErrorMessage(this.pgStream.receiveErrorString(elen - 4));
                  LOGGER.log(Level.FINEST, " <=BE ErrorMessage({0})", errorMsg);
                  return new PSQLException(errorMsg, this.logServerErrorDetail);
               case 82:
                  LOGGER.log(Level.FINEST, " <=BE AuthenticationGSSContinue");
                  int len = this.pgStream.receiveInteger4();
                  int type = this.pgStream.receiveInteger4();
                  inToken = this.pgStream.receive(len - 8);
                  break;
               default:
                  return new PSQLException(GT.tr("Protocol error.  Session setup failed."), PSQLState.CONNECTION_UNABLE_TO_CONNECT);
               }
            } else {
               established = true;
            }
         }

         return null;
      } catch (IOException var15) {
         return var15;
      } catch (GSSException var16) {
         return new PSQLException(GT.tr("GSS Authentication failed"), PSQLState.CONNECTION_FAILURE, var16);
      }
   }

   @Nullable
   public Exception call() throws Exception {
      return this.run();
   }
}
