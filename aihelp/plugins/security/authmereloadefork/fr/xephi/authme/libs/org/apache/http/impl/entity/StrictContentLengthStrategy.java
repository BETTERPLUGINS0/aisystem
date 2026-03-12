package fr.xephi.authme.libs.org.apache.http.impl.entity;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpMessage;
import fr.xephi.authme.libs.org.apache.http.HttpVersion;
import fr.xephi.authme.libs.org.apache.http.ProtocolException;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.entity.ContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.util.Args;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class StrictContentLengthStrategy implements ContentLengthStrategy {
   public static final StrictContentLengthStrategy INSTANCE = new StrictContentLengthStrategy();
   private final int implicitLen;

   public StrictContentLengthStrategy(int implicitLen) {
      this.implicitLen = implicitLen;
   }

   public StrictContentLengthStrategy() {
      this(-1);
   }

   public long determineLength(HttpMessage message) throws HttpException {
      Args.notNull(message, "HTTP message");
      Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
      if (transferEncodingHeader != null) {
         String s = transferEncodingHeader.getValue();
         if ("chunked".equalsIgnoreCase(s)) {
            if (message.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
               throw new ProtocolException("Chunked transfer encoding not allowed for " + message.getProtocolVersion());
            } else {
               return -2L;
            }
         } else if ("identity".equalsIgnoreCase(s)) {
            return -1L;
         } else {
            throw new ProtocolException("Unsupported transfer encoding: " + s);
         }
      } else {
         Header contentLengthHeader = message.getFirstHeader("Content-Length");
         if (contentLengthHeader != null) {
            String s = contentLengthHeader.getValue();

            try {
               long len = Long.parseLong(s);
               if (len < 0L) {
                  throw new ProtocolException("Negative content length: " + s);
               } else {
                  return len;
               }
            } catch (NumberFormatException var7) {
               throw new ProtocolException("Invalid content length: " + s);
            }
         } else {
            return (long)this.implicitLen;
         }
      }
   }
}
