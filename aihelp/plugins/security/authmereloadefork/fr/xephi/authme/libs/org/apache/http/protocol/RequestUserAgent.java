package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestInterceptor;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.params.HttpParams;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class RequestUserAgent implements HttpRequestInterceptor {
   private final String userAgent;

   public RequestUserAgent(String userAgent) {
      this.userAgent = userAgent;
   }

   public RequestUserAgent() {
      this((String)null);
   }

   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      if (!request.containsHeader("User-Agent")) {
         String s = null;
         HttpParams params = request.getParams();
         if (params != null) {
            s = (String)params.getParameter("http.useragent");
         }

         if (s == null) {
            s = this.userAgent;
         }

         if (s != null) {
            request.addHeader("User-Agent", s);
         }
      }

   }
}
