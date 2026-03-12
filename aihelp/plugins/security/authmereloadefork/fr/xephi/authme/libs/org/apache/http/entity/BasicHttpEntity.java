package fr.xephi.authme.libs.org.apache.http.entity;

import fr.xephi.authme.libs.org.apache.http.impl.io.EmptyInputStream;
import fr.xephi.authme.libs.org.apache.http.util.Args;
import fr.xephi.authme.libs.org.apache.http.util.Asserts;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BasicHttpEntity extends AbstractHttpEntity {
   private InputStream content;
   private long length = -1L;

   public long getContentLength() {
      return this.length;
   }

   public InputStream getContent() throws IllegalStateException {
      Asserts.check(this.content != null, "Content has not been provided");
      return this.content;
   }

   public boolean isRepeatable() {
      return false;
   }

   public void setContentLength(long len) {
      this.length = len;
   }

   public void setContent(InputStream inStream) {
      this.content = inStream;
   }

   public void writeTo(OutputStream outStream) throws IOException {
      Args.notNull(outStream, "Output stream");
      InputStream inStream = this.getContent();

      try {
         byte[] tmp = new byte[4096];

         int l;
         while((l = inStream.read(tmp)) != -1) {
            outStream.write(tmp, 0, l);
         }
      } finally {
         inStream.close();
      }

   }

   public boolean isStreaming() {
      return this.content != null && this.content != EmptyInputStream.INSTANCE;
   }
}
