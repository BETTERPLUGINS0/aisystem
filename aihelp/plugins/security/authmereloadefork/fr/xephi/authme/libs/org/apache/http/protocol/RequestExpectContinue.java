package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpEntityEnclosingRequest;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestInterceptor;
import fr.xephi.authme.libs.org.apache.http.HttpVersion;
import fr.xephi.authme.libs.org.apache.http.ProtocolVersion;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class RequestExpectContinue implements HttpRequestInterceptor {
   private final boolean activeByDefault;

   /** @deprecated */
   @Deprecated
   public RequestExpectContinue() {
      this(false);
   }

   public RequestExpectContinue(boolean activeByDefault) {
      this.activeByDefault = activeByDefault;
   }

   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      if (!request.containsHeader("Expect") && request instanceof HttpEntityEnclosingRequest) {
         ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
         HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
         if (entity != null && entity.getContentLength() != 0L && !ver.lessEquals(HttpVersion.HTTP_1_0)) {
            boolean active = request.getParams().getBooleanParameter("http.protocol.expect-continue", this.activeByDefault);
            if (active) {
               request.addHeader("Expect", "100-continue");
            }
         }
      }

   }
}
