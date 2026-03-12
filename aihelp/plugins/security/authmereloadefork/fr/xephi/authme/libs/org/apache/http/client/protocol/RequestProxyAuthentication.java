package fr.xephi.authme.libs.org.apache.http.client.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.auth.AuthState;
import fr.xephi.authme.libs.org.apache.http.conn.HttpRoutedConnection;
import fr.xephi.authme.libs.org.apache.http.conn.routing.HttpRoute;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class RequestProxyAuthentication extends RequestAuthenticationBase {
   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      Args.notNull(context, "HTTP context");
      if (!request.containsHeader("Proxy-Authorization")) {
         HttpRoutedConnection conn = (HttpRoutedConnection)context.getAttribute("http.connection");
         if (conn == null) {
            this.log.debug("HTTP connection not set in the context");
         } else {
            HttpRoute route = conn.getRoute();
            if (!route.isTunnelled()) {
               AuthState authState = (AuthState)context.getAttribute("http.auth.proxy-scope");
               if (authState == null) {
                  this.log.debug("Proxy auth state not set in the context");
               } else {
                  if (this.log.isDebugEnabled()) {
                     this.log.debug("Proxy auth state: " + authState.getState());
                  }

                  this.process(authState, request, context);
               }
            }
         }
      }
   }
}
