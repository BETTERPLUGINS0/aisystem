package fr.xephi.authme.libs.org.apache.http.impl.entity;

import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.HttpException;
import fr.xephi.authme.libs.org.apache.http.HttpMessage;
import fr.xephi.authme.libs.org.apache.http.annotation.Contract;
import fr.xephi.authme.libs.org.apache.http.annotation.ThreadingBehavior;
import fr.xephi.authme.libs.org.apache.http.entity.ContentLengthStrategy;
import fr.xephi.authme.libs.org.apache.http.impl.io.ChunkedOutputStream;
import fr.xephi.authme.libs.org.apache.http.impl.io.ContentLengthOutputStream;
import fr.xephi.authme.libs.org.apache.http.impl.io.IdentityOutputStream;
import fr.xephi.authme.libs.org.apache.http.io.SessionOutputBuffer;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.io.OutputStream;

/** @deprecated */
@Deprecated
@Contract(
   threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL
)
public class EntitySerializer {
   private final ContentLengthStrategy lenStrategy;

   public EntitySerializer(ContentLengthStrategy lenStrategy) {
      this.lenStrategy = (ContentLengthStrategy)Args.notNull(lenStrategy, "Content length strategy");
   }

   protected OutputStream doSerialize(SessionOutputBuffer outbuffer, HttpMessage message) throws HttpException, IOException {
      long len = this.lenStrategy.determineLength(message);
      if (len == -2L) {
         return new ChunkedOutputStream(outbuffer);
      } else {
         return (OutputStream)(len == -1L ? new IdentityOutputStream(outbuffer) : new ContentLengthOutputStream(outbuffer, len));
      }
   }

   public void serialize(SessionOutputBuffer outbuffer, HttpMessage message, HttpEntity entity) throws HttpException, IOException {
      Args.notNull(outbuffer, "Session output buffer");
      Args.notNull(message, "HTTP message");
      Args.notNull(entity, "HTTP entity");
      OutputStream outStream = this.doSerialize(outbuffer, message);
      entity.writeTo(outStream);
      outStream.close();
   }
}
