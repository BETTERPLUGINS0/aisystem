package fr.xephi.authme.libs.org.mariadb.jdbc.client.socket.impl;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ReadAheadBufferedStream extends FilterInputStream {
   private static final int BUF_SIZE = 16384;
   private final byte[] buf = new byte[16384];
   private int end = 0;
   private int pos = 0;

   public ReadAheadBufferedStream(InputStream in) {
      super(in);
   }

   public int read(byte[] externalBuf, int off, int len) throws IOException {
      if (len == 0) {
         return 0;
      } else {
         int totalReads = 0;

         do {
            int reads;
            if (this.end - this.pos <= 0) {
               if (len - totalReads >= this.buf.length) {
                  reads = super.read(externalBuf, off + totalReads, len - totalReads);
                  if (reads <= 0) {
                     return totalReads == 0 ? -1 : totalReads;
                  }

                  return totalReads + reads;
               }

               this.fillingBuffer(len - totalReads);
               if (this.end <= 0) {
                  return totalReads == 0 ? -1 : totalReads;
               }
            }

            reads = Math.min(len - totalReads, this.end - this.pos);
            System.arraycopy(this.buf, this.pos, externalBuf, off + totalReads, reads);
            this.pos += reads;
            totalReads += reads;
         } while(totalReads < len && super.available() > 0);

         return totalReads;
      }
   }

   private void fillingBuffer(int minNeededBytes) throws IOException {
      int lengthToReallyRead = Math.min(16384, Math.max(super.available(), minNeededBytes));
      this.end = super.read(this.buf, 0, lengthToReallyRead);
      this.pos = 0;
   }

   public boolean markSupported() {
      return false;
   }

   public void close() throws IOException {
      super.close();
      this.end = 0;
      this.pos = 0;
   }

   public int available() throws IOException {
      return this.end - this.pos + super.available();
   }

   public int read() throws IOException {
      throw new IOException("read() from socket not implemented");
   }

   public long skip(long n) throws IOException {
      throw new IOException("Skip from socket not implemented");
   }

   public void reset() throws IOException {
      throw new IOException("reset from socket not implemented");
   }
}
