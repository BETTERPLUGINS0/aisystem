package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpResponse;
import fr.xephi.authme.libs.org.apache.http.HttpResponseInterceptor;
import fr.xephi.authme.libs.org.apache.http.HttpVersion;
import fr.xephi.authme.libs.org.apache.http.ProtocolException;
import fr.xephi.authme.libs.org.apache.http.ProtocolVersion;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class ResponseContent implements HttpResponseInterceptor {
   private final boolean overwrite;

   public ResponseContent() {
      this(false);
   }

   public ResponseContent(boolean overwrite) {
      this.overwrite = overwrite;
   }

   public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
      Args.notNull(response, "HTTP response");
      if (this.overwrite) {
         response.removeHeaders("Transfer-Encoding");
         response.removeHeaders("Content-Length");
      } else {
         if (response.containsHeader("Transfer-Encoding")) {
            throw new ProtocolException("Transfer-encoding header already present");
         }

         if (response.containsHeader("Content-Length")) {
            throw new ProtocolException("Content-Length header already present");
         }
      }

      ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
      HttpEntity entity = response.getEntity();
      if (entity != null) {
         long len = entity.getContentLength();
         if (entity.isChunked() && !ver.lessEquals(HttpVersion.HTTP_1_0)) {
            response.addHeader("Transfer-Encoding", "chunked");
         } else if (len >= 0L) {
            response.addHeader("Content-Length", Long.toString(entity.getContentLength()));
         }

         if (entity.getContentType() != null && !response.containsHeader("Content-Type")) {
            response.addHeader(entity.getContentType());
         }

         if (entity.getContentEncoding() != null && !response.containsHeader("Content-Encoding")) {
            response.addHeader(entity.getContentEncoding());
         }
      } else {
         int status = response.getStatusLine().getStatusCode();
         if (status != 204 && status != 304 && status != 205) {
            response.addHeader("Content-Length", "0");
         }
      }

   }
}
