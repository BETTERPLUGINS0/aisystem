package fr.xephi.authme.libs.org.apache.http.impl.entity;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HeaderElement;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpMessage;
import fr.xephi.authme.libs.org.apache.http.ParseException;
import fr.xephi.authme.libs.org.apache.http.ProtocolException;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.entity.ContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.util.Args;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE
)
public class LaxContentLengthStrategy implements ContentLengthStrategy {
   public static final LaxContentLengthStrategy INSTANCE = new LaxContentLengthStrategy();
   private final int implicitLen;

   public LaxContentLengthStrategy(int implicitLen) {
      this.implicitLen = implicitLen;
   }

   public LaxContentLengthStrategy() {
      this(-1);
   }

   public long determineLength(HttpMessage message) throws HttpException {
      Args.notNull(message, "HTTP message");
      Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
      if (transferEncodingHeader != null) {
         HeaderElement[] encodings;
         try {
            encodings = transferEncodingHeader.getElements();
         } catch (ParseException var10) {
            throw new ProtocolException("Invalid Transfer-Encoding header value: " + transferEncodingHeader, var10);
         }

         int len = encodings.length;
         if ("identity".equalsIgnoreCase(transferEncodingHeader.getValue())) {
            return -1L;
         } else {
            return len > 0 && "chunked".equalsIgnoreCase(encodings[len - 1].getName()) ? -2L : -1L;
         }
      } else {
         Header contentLengthHeader = message.getFirstHeader("Content-Length");
         if (contentLengthHeader == null) {
            return (long)this.implicitLen;
         } else {
            long contentLen = -1L;
            Header[] headers = message.getHeaders("Content-Length");
            int i = headers.length - 1;

            while(i >= 0) {
               Header header = headers[i];

               try {
                  contentLen = Long.parseLong(header.getValue());
                  break;
               } catch (NumberFormatException var11) {
                  --i;
               }
            }

            return contentLen >= 0L ? contentLen : -1L;
         }
      }
   }
}
