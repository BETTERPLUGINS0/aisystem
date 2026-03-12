package fr.xephi.authme.libs.org.apache.http.client.entity;

import fr.xephi.authme.libs.org.apache.http.Header;
import fr.xephi.authme.libs.org.apache.http.HttpEntity;
import fr.xephi.authme.libs.org.apache.http.entity.HttpEntityWrapper;
import fr.xephi.authme.libs.org.apache.http.message.BasicHeader;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class GzipCompressingEntity extends HttpEntityWrapper {
   private static final String GZIP_CODEC = "gzip";

   public GzipCompressingEntity(HttpEntity entity) {
      super(entity);
   }

   public Header getContentEncoding() {
      return new BasicHeader("Content-Encoding", "gzip");
   }

   public long getContentLength() {
      return -1L;
   }

   public boolean isChunked() {
      return true;
   }

   public InputStream getContent() throws IOException {
      throw new UnsupportedOperationException();
   }

   public void writeTo(OutputStream outStream) throws IOException {
      Args.notNull(outStream, "Output stream");
      GZIPOutputStream gzip = new GZIPOutputStream(outStream);
      this.wrappedEntity.writeTo(gzip);
      gzip.close();
   }
}
