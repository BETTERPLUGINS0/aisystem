package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.http.HttpConnection;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.auth.AuthScheme;
import fr.xephi.authme.libs.org.apache.http.auth.AuthState;
import fr.xephi.authme.libs.org.apache.http.auth.Credentials;
import fr.xephi.authme.libs.org.apache.http.client.UserTokenHandler;
import fr.xephi.authme.libs.org.apache.http.client.protocol.HttpClientContext;
import fr.xephi.authme.libs.org.apache.http.conn.ManagedHttpClientConnection;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import java.security.Principal;
import javax.net.ssl.SSLSession;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class DefaultUserTokenHandler implements UserTokenHandler {
   public static final DefaultUserTokenHandler INSTANCE = new DefaultUserTokenHandler();

   public Object getUserToken(HttpContext context) {
      HttpClientContext clientContext = HttpClientContext.adapt(context);
      Principal userPrincipal = null;
      AuthState targetAuthState = clientContext.getTargetAuthState();
      if (targetAuthState != null) {
         userPrincipal = getAuthPrincipal(targetAuthState);
         if (userPrincipal == null) {
            AuthState proxyAuthState = clientContext.getProxyAuthState();
            userPrincipal = getAuthPrincipal(proxyAuthState);
         }
      }

      if (userPrincipal == null) {
         HttpConnection conn = clientContext.getConnection();
         if (conn.isOpen() && conn instanceof ManagedHttpClientConnection) {
            SSLSession sslsession = ((ManagedHttpClientConnection)conn).getSSLSession();
            if (sslsession != null) {
               userPrincipal = sslsession.getLocalPrincipal();
            }
         }
      }

      return userPrincipal;
   }

   private static Principal getAuthPrincipal(AuthState authState) {
      AuthScheme scheme = authState.getAuthScheme();
      if (scheme != null && scheme.isComplete() && scheme.isConnectionBased()) {
         Credentials creds = authState.getCredentials();
         if (creds != null) {
            return creds.getUserPrincipal();
         }
      }

      return null;
   }
}
