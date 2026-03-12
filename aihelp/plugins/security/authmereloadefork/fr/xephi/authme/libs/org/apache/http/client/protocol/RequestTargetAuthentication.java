package fr.xephi.authme.libs.org.apache.http.client.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.auth.AuthState;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class RequestTargetAuthentication extends RequestAuthenticationBase {
   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      Args.notNull(context, "HTTP context");
      String method = request.getRequestLine().getMethod();
      if (!method.equalsIgnoreCase("CONNECT")) {
         if (!request.containsHeader("Authorization")) {
            AuthState authState = (AuthState)context.getAttribute("http.auth.target-scope");
            if (authState == null) {
               this.log.debug("Target auth state not set in the context");
            } else {
               if (this.log.isDebugEnabled()) {
                  this.log.debug("Target auth state: " + authState.getState());
               }

               this.process(authState, request, context);
            }
         }
      }
   }
}
