package fr.xephi.authme.libs.org.postgresql.core;

import java.io.IOException;
import java.io.OutputStream;

public class FixedLengthOutputStream extends OutputStream {
   private final int size;
   private final OutputStream target;
   private int written;

   public FixedLengthOutputStream(int size, OutputStream target) {
      this.size = size;
      this.target = target;
   }

   public void write(int b) throws IOException {
      this.verifyAllowed(1);
      ++this.written;
      this.target.write(b);
   }

   public void write(byte[] buf, int offset, int len) throws IOException {
      if (offset >= 0 && len >= 0 && offset + len <= buf.length) {
         if (len != 0) {
            this.verifyAllowed(len);
            this.target.write(buf, offset, len);
            this.written += len;
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public int remaining() {
      return this.size - this.written;
   }

   private void verifyAllowed(int wanted) throws IOException {
      if (this.remaining() < wanted) {
         throw new IOException("Attempt to write more than the specified " + this.size + " bytes");
      }
   }
}
