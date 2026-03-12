package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpResponseInterceptor;
import fr.xephi.authme.libs.org.apache.http.HttpVersion;
import fr.xephi.authme.libs.org.apache.http.ProtocolVersion;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class ResponseConnControl implements HttpResponseInterceptor {
   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
      Args.notNull(response, "HTTP response");
      HttpCoreContext corecontext = HttpCoreContext.adapt(context);
      int status = response.getStatusLine().getStatusCode();
      if (status != 400 && status != 408 && status != 411 && status != 413 && status != 414 && status != 503 && status != 501) {
         Header explicit = response.getFirstHeader("Connection");
         if (explicit == null || !"Close".equalsIgnoreCase(explicit.getValue())) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
               ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
               if (entity.getContentLength() < 0L && (!entity.isChunked() || ver.lessEquals(HttpVersion.HTTP_1_0))) {
                  response.setHeader("Connection", "Close");
                  return;
               }
            }

            HttpRequest request = corecontext.getRequest();
            if (request != null) {
               Header header = request.getFirstHeader("Connection");
               if (header != null) {
                  response.setHeader("Connection", header.getValue());
               } else if (request.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
                  response.setHeader("Connection", "Close");
               }
            }

         }
      } else {
         response.setHeader("Connection", "Close");
      }
   }
}
