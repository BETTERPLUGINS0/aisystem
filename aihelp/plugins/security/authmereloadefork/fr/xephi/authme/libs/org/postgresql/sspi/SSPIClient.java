package fr.xephi.authme.libs.org.postgresql.sspi;

import com.sun.jna.LastErrorException;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Sspi;
import com.sun.jna.platform.win32.Win32Exception;
import fr.xephi.authme.libs.org.postgresql.core.PGStream;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.HostSpec;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import fr.xephi.authme.libs.org.postgresql.util.internal.Nullness;
import fr.xephi.authme.libs.waffle.windows.auth.IWindowsCredentialsHandle;
import fr.xephi.authme.libs.waffle.windows.auth.impl.WindowsCredentialsHandleImpl;
import fr.xephi.authme.libs.waffle.windows.auth.impl.WindowsSecurityContextImpl;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SSPIClient implements ISSPIClient {
   public static final String SSPI_DEFAULT_SPN_SERVICE_CLASS = "POSTGRES";
   private static final Logger LOGGER = Logger.getLogger(SSPIClient.class.getName());
   private static final Constructor<? extends Sspi.SecBufferDesc> SEC_BUFFER_DESC_FACTORY;
   private final PGStream pgStream;
   private final String spnServiceClass;
   private final boolean enableNegotiate;
   @Nullable
   private IWindowsCredentialsHandle clientCredentials;
   @Nullable
   private WindowsSecurityContextImpl sspiContext;
   @Nullable
   private String targetName;

   public SSPIClient(PGStream pgStream, String spnServiceClass, boolean enableNegotiate) {
      this.pgStream = pgStream;
      if (spnServiceClass == null || spnServiceClass.isEmpty()) {
         spnServiceClass = "POSTGRES";
      }

      this.spnServiceClass = spnServiceClass;
      this.enableNegotiate = enableNegotiate;
   }

   public boolean isSSPISupported() {
      try {
         if (!Platform.isWindows()) {
            LOGGER.log(Level.FINE, "SSPI not supported: non-Windows host");
            return false;
         } else {
            Class.forName("fr.xephi.authme.libs.waffle.windows.auth.impl.WindowsSecurityContextImpl");
            return true;
         }
      } catch (NoClassDefFoundError var2) {
         LOGGER.log(Level.WARNING, "SSPI unavailable (no Waffle/JNA libraries?)", var2);
         return false;
      } catch (ClassNotFoundException var3) {
         LOGGER.log(Level.WARNING, "SSPI unavailable (no Waffle/JNA libraries?)", var3);
         return false;
      }
   }

   private String makeSPN() throws PSQLException {
      HostSpec hs = this.pgStream.getHostSpec();

      try {
         return NTDSAPIWrapper.instance.DsMakeSpn(this.spnServiceClass, hs.getHost(), (String)null, (short)0, (String)null);
      } catch (LastErrorException var3) {
         throw new PSQLException("SSPI setup failed to determine SPN", PSQLState.CONNECTION_UNABLE_TO_CONNECT, var3);
      }
   }

   public void startSSPI() throws SQLException, IOException {
      String securityPackage = this.enableNegotiate ? "negotiate" : "kerberos";
      LOGGER.log(Level.FINEST, "Beginning SSPI/Kerberos negotiation with SSPI package: {0}", securityPackage);

      try {
         IWindowsCredentialsHandle clientCredentials;
         try {
            clientCredentials = WindowsCredentialsHandleImpl.getCurrent(securityPackage);
            this.clientCredentials = clientCredentials;
            clientCredentials.initialize();
         } catch (Win32Exception var6) {
            throw new PSQLException("Could not obtain local Windows credentials for SSPI", PSQLState.CONNECTION_UNABLE_TO_CONNECT, var6);
         }

         WindowsSecurityContextImpl sspiContext;
         try {
            String targetName = this.makeSPN();
            this.targetName = targetName;
            LOGGER.log(Level.FINEST, "SSPI target name: {0}", targetName);
            this.sspiContext = sspiContext = new WindowsSecurityContextImpl();
            sspiContext.setPrincipalName(targetName);
            sspiContext.setCredentialsHandle(clientCredentials);
            sspiContext.setSecurityPackage(securityPackage);
            sspiContext.initialize((Sspi.CtxtHandle)null, (Sspi.SecBufferDesc)null, targetName);
         } catch (Win32Exception var5) {
            throw new PSQLException("Could not initialize SSPI security context", PSQLState.CONNECTION_UNABLE_TO_CONNECT, var5);
         }

         this.sendSSPIResponse(sspiContext.getToken());
         LOGGER.log(Level.FINEST, "Sent first SSPI negotiation message");
      } catch (NoClassDefFoundError var7) {
         throw new PSQLException("SSPI cannot be used, Waffle or its dependencies are missing from the classpath", PSQLState.NOT_IMPLEMENTED, var7);
      }
   }

   public void continueSSPI(int msgLength) throws SQLException, IOException {
      WindowsSecurityContextImpl sspiContext = this.sspiContext;
      if (sspiContext == null) {
         throw new IllegalStateException("Cannot continue SSPI authentication that we didn't begin");
      } else {
         LOGGER.log(Level.FINEST, "Continuing SSPI negotiation");
         byte[] receivedToken = this.pgStream.receive(msgLength);

         Sspi.SecBufferDesc continueToken;
         try {
            continueToken = (Sspi.SecBufferDesc)SEC_BUFFER_DESC_FACTORY.newInstance(2, receivedToken);
         } catch (ReflectiveOperationException var6) {
            continueToken = new Sspi.SecBufferDesc(2, receivedToken);
         }

         sspiContext.initialize(sspiContext.getHandle(), continueToken, (String)Nullness.castNonNull(this.targetName));
         byte[] responseToken = sspiContext.getToken();
         if (responseToken.length > 0) {
            this.sendSSPIResponse(responseToken);
            LOGGER.log(Level.FINEST, "Sent SSPI negotiation continuation message");
         } else {
            LOGGER.log(Level.FINEST, "SSPI authentication complete, no reply required");
         }

      }
   }

   private void sendSSPIResponse(byte[] outToken) throws IOException {
      this.pgStream.sendChar(112);
      this.pgStream.sendInteger4(4 + outToken.length);
      this.pgStream.send(outToken);
      this.pgStream.flush();
   }

   public void dispose() {
      if (this.sspiContext != null) {
         this.sspiContext.dispose();
         this.sspiContext = null;
      }

      if (this.clientCredentials != null) {
         this.clientCredentials.dispose();
         this.clientCredentials = null;
      }

   }

   static {
      Class klass;
      try {
         klass = Class.forName("com.sun.jna.platform.win32.SspiUtil$ManagedSecBufferDesc").asSubclass(Sspi.SecBufferDesc.class);
      } catch (ReflectiveOperationException var3) {
         klass = Sspi.SecBufferDesc.class;
      }

      try {
         SEC_BUFFER_DESC_FACTORY = klass.getConstructor(Integer.TYPE, byte[].class);
      } catch (NoSuchMethodException var2) {
         throw new IllegalStateException(GT.tr("Unable to instantiate SecBufferDesc, so SSPI is unavailable", klass.getName()), var2);
      }
   }
}
