package github.nighter.smartspawner.libs.mariadb.plugin.authentication.addon.gssapi;

import github.nighter.smartspawner.libs.mariadb.client.ReadableByteBuf;
import github.nighter.smartspawner.libs.mariadb.client.socket.Reader;
import github.nighter.smartspawner.libs.mariadb.client.socket.Writer;
import github.nighter.smartspawner.libs.mariadb.util.ThreadUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UncheckedIOException;
import java.security.PrivilegedExceptionAction;
import java.sql.SQLException;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;

public class StandardGssapiAuthentication implements GssapiAuth {
   public void authenticate(Writer out, Reader in, String servicePrincipalName, String mechanisms) throws SQLException, IOException {
      if ("".equals(servicePrincipalName)) {
         throw new SQLException("No principal name defined on server. Please set server variable \"gssapi-principal-name\" or set option \"servicePrincipalName\"", "28000");
      } else {
         if (System.getProperty("java.security.auth.login.config") == null) {
            File jaasConfFile;
            try {
               jaasConfFile = File.createTempFile("jaas.conf", (String)null);
               PrintStream bos = new PrintStream(new FileOutputStream(jaasConfFile));

               try {
                  bos.print("Krb5ConnectorContext {\ncom.sun.security.auth.module.Krb5LoginModule required useTicketCache=true debug=true renewTGT=true doNotPrompt=true; };");
               } catch (Throwable var12) {
                  try {
                     bos.close();
                  } catch (Throwable var9) {
                     var12.addSuppressed(var9);
                  }

                  throw var12;
               }

               bos.close();
               jaasConfFile.deleteOnExit();
            } catch (IOException var13) {
               throw new UncheckedIOException(var13);
            }

            System.setProperty("java.security.auth.login.config", jaasConfFile.getCanonicalPath());
         }

         try {
            LoginContext loginContext = new LoginContext("Krb5ConnectorContext");
            loginContext.login();
            Subject mySubject = loginContext.getSubject();
            if (!mySubject.getPrincipals().isEmpty()) {
               try {
                  PrivilegedExceptionAction<Void> action = () -> {
                     try {
                        Oid krb5Mechanism = new Oid("1.2.840.113554.1.2.2");
                        GSSManager manager = GSSManager.getInstance();
                        GSSName peerName = manager.createName(servicePrincipalName, GSSName.NT_USER_NAME);
                        GSSContext context = manager.createContext(peerName, krb5Mechanism, (GSSCredential)null, 0);
                        context.requestMutualAuth(true);
                        byte[] inToken = new byte[0];

                        while(true) {
                           byte[] outToken = context.initSecContext(inToken, 0, inToken.length);
                           if (outToken != null) {
                              out.writeBytes(outToken);
                              out.flush();
                           }

                           if (context.isEstablished()) {
                              return null;
                           }

                           ReadableByteBuf buf = in.readReusablePacket();
                           if (buf.getByte() == 1) {
                              buf.skip();
                           }

                           inToken = new byte[buf.readableBytes()];
                           buf.readBytes(inToken);
                        }
                     } catch (GSSException var10) {
                        throw new SQLException("GSS-API authentication exception", "28000", 1045, var10);
                     }
                  };
                  ThreadUtils.callAs(mySubject, () -> {
                     return action;
                  });
               } catch (Exception var10) {
                  throw new SQLException("GSS-API authentication exception", "28000", 1045, var10);
               }
            } else {
               throw new SQLException("GSS-API authentication exception : no credential cache not found.", "28000", 1045);
            }
         } catch (LoginException var11) {
            throw new SQLException("GSS-API authentication exception", "28000", 1045, var11);
         }
      }
   }
}
