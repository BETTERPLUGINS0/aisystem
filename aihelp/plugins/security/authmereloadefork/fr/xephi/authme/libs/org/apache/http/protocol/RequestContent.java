package fr.xephi.authme.libs.org.apache.http.protocol;

import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpEntityEnclosingRequest;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpRequest;
import fr.xephi.authme.libs.org.apache.http.HttpRequestInterceptor;
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
public class RequestContent implements HttpRequestInterceptor {
   private final boolean overwrite;

   public RequestContent() {
      this(false);
   }

   public RequestContent(boolean overwrite) {
      this.overwrite = overwrite;
   }

   public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
      Args.notNull(request, "HTTP request");
      if (request instanceof HttpEntityEnclosingRequest) {
         if (this.overwrite) {
            request.removeHeaders("Transfer-Encoding");
            request.removeHeaders("Content-Length");
         } else {
            if (request.containsHeader("Transfer-Encoding")) {
               throw new ProtocolException("Transfer-encoding header already present");
            }

            if (request.containsHeader("Content-Length")) {
               throw new ProtocolException("Content-Length header already present");
            }
         }

         ProtocolVersion ver = request.getRequestLine().getProtocolVersion();
         HttpEntity entity = ((HttpEntityEnclosingRequest)request).getEntity();
         if (entity == null) {
            request.addHeader("Content-Length", "0");
            return;
         }

         if (!entity.isChunked() && entity.getContentLength() >= 0L) {
            request.addHeader("Content-Length", Long.toString(entity.getContentLength()));
         } else {
            if (ver.lessEquals(HttpVersion.HTTP_1_0)) {
               throw new ProtocolException("Chunked transfer encoding not allowed for " + ver);
            }

            request.addHeader("Transfer-Encoding", "chunked");
         }

         if (entity.getContentType() != null && !request.containsHeader("Content-Type")) {
            request.addHeader(entity.getContentType());
         }

         if (entity.getContentEncoding() != null && !request.containsHeader("Content-Encoding")) {
            request.addHeader(entity.getContentEncoding());
         }
      }

   }
}
