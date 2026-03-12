package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpConnection;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpHost;
import fr.xephi.authme.libs.org.apache.http.HttpInetConnection;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestInterceptor;
import fr.xephi.authme.libs.org.apache.http.HttpVersion;
import fr.xephi.authme.libs.org.apache.http.ProtocolException;
import fr.xephi.authme.libs.org.apache.http.ProtocolVersion;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.net.InetAddress;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class RequestTargetHost implements HttpRequestInterceptor {
   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      HttpCoreContext coreContext = HttpCoreContext.adapt(context);
      ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
      String method = request.getRequestLine().getMethod();
      if (!method.equalsIgnoreCase("CONNECT") || !ver.lessEquals(HttpVersion.HTTP_1_0)) {
         if (!request.containsHeader("Host")) {
            HttpHost targetHost = coreContext.getTargetHost();
            if (targetHost == null) {
               HttpConnection conn = coreContext.getConnection();
               if (conn instanceof HttpInetConnection) {
                  InetAddress address = ((HttpInetConnection)conn).getRemoteAddress();
                  int port = ((HttpInetConnection)conn).getRemotePort();
                  if (address != null) {
                     targetHost = new HttpHost(address.getHostName(), port);
                  }
               }

               if (targetHost == null) {
                  if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
                     return;
                  }

                  throw new ProtocolException("Target host missing");
               }
            }

            request.addHeader("Host", targetHost.toHostString());
         }

      }
   }
}
