package fr.xephi.authme.libs.org.apache.http.entity;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class HttpEntityWrapper implements HttpEntity {
   protected HttpEntity wrappedEntity;

   public HttpEntityWrapper(HttpEntity wrappedEntity) {
      this.wrappedEntity = (HttpEntity)Args.notNull(wrappedEntity, "Wrapped entity");
   }

   public boolean isRepeatable() {
      return this.wrappedEntity.isRepeatable();
   }

   public boolean isChunked() {
      return this.wrappedEntity.isChunked();
   }

   public long getContentLength() {
      return this.wrappedEntity.getContentLength();
   }

   public Header getContentType() {
      return this.wrappedEntity.getContentType();
   }

   public Header getContentEncoding() {
      return this.wrappedEntity.getContentEncoding();
   }

   public InputStream getContent() throws IOException {
      return this.wrappedEntity.getContent();
   }

   public void writeTo(OutputStream outStream) throws IOException {
      this.wrappedEntity.writeTo(outStream);
   }

   public boolean isStreaming() {
      return this.wrappedEntity.isStreaming();
   }

   /** @deprecated */
   @Deprecated
   public void consumeContent() throws IOException {
      this.wrappedEntity.consumeContent();
   }
}
