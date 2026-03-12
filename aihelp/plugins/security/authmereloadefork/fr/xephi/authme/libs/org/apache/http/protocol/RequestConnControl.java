package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestInterceptor;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class RequestConnControl implements HttpRequestInterceptor {
   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      String method = request.getRequestLine().getMethod();
      if (!method.equalsIgnoreCase("CONNECT")) {
         if (!request.containsHeader("Connection")) {
            request.addHeader("Connection", "Keep-Alive");
         }

      }
   }
}
