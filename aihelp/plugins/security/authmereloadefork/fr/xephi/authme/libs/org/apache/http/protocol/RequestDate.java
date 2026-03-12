package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpEntityEnclosingRequest;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestInterceptor;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.SAFE
)
public class RequestDate implements HttpRequestInterceptor {
   private static final HttpDateGenerator DATE_GENERATOR = new HttpDateGenerator();

   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      if (request instanceof HttpEntityEnclosingRequest && !request.containsHeader("Date")) {
         String httpdate = DATE_GENERATOR.getCurrentDate();
         request.setHeader("Date", httpdate);
      }

   }
}
