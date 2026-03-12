package fr.xephi.authme.libs.org.apache.http.client.protocol;

import fr.xephi.authme.libs.org.apache.commons.logging.Log;
import fr.xephi.authme.libs.org.apache.commons.logging.LogFactory;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestInterceptor;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.conn.routing.RouteInfo;
import fr.xephi.authme.libs.org.apache.http.protocol.HttpContext;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class RequestClientConnControl implements HttpRequestInterceptor {
   private final Log log = LogFactory.getLog(this.getClass());
   private static final String PROXY_CONN_DIRECTIVE = "Proxy-Connection";

   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      String method = request.getRequestLine().getMethod();
      if (method.equalsIgnoreCase("CONNECT")) {
         request.setHeader("Proxy-Connection", "Keep-Alive");
      } else {
         HttpClientContext clientContext = HttpClientContext.adapt(context);
         RouteInfo route = clientContext.getHttpRoute();
         if (route == null) {
            this.log.debug("Connection route not set in the context");
         } else {
            if ((route.getHopCount() == 1 || route.isTunnelled()) && !request.containsHeader("Connection")) {
               request.addHeader("Connection", "Keep-Alive");
            }

            if (route.getHopCount() == 2 && !route.isTunnelled() && !request.containsHeader("Proxy-Connection")) {
               request.addHeader("Proxy-Connection", "Keep-Alive");
            }

         }
      }
   }
}
