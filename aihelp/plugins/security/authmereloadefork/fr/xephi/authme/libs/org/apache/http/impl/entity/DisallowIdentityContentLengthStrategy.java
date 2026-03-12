package fr.xephi.authme.libs.org.apache.http.impl.entity;

import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpMessage;
import fr.xephi.authme.libs.org.apache.http.ProtocolException;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.entity.ContentLengthStrategy;

@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class DisallowIdentityContentLengthStrategy implements ContentLengthStrategy {
   public static final DisallowIdentityContentLengthStrategy INSTANCE = new DisallowIdentityContentLengthStrategy(new LaxContentLengthStrategy(0));
   private final ContentLengthStrategy contentLengthStrategy;

   public DisallowIdentityContentLengthStrategy(ContentLengthStrategy contentLengthStrategy) {
      this.contentLengthStrategy = contentLengthStrategy;
   }

   public long determineLength(HttpMessage message) throws HttpException {
      long result = this.contentLengthStrategy.determineLength(message);
      if (result == -1L) {
         throw new ProtocolException("Identity transfer encoding cannot be used");
      } else {
         return result;
      }
   }
}
