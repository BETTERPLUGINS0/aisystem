package fr.xephi.authme.libs.org.apache.http.impl.client;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.auth.AuthState;
import fr.xephi.authme.libs.org.apache.http.client.AuthenticationStrategy;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;

/** @deprecated */
@Deprecated
public class HttpAuthenticator extends fr.xephi.authme.libs.org.apache.http.impl.auth.HttpAuthenticator {
   public HttpAuthenticator(Log log) {
      super(log);
   }

   public HttpAuthenticator() {
   }

   public boolean authenticate(HttpHost host, HttpResponse response, AuthenticationStrategy authStrategy, AuthState authState, HttpContext context) {
      return this.handleAuthChallenge(host, response, authStrategy, authState, context);
   }
}
