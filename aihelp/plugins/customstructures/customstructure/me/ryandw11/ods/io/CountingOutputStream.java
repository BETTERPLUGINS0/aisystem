package me.ryandw11.ods.io;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CountingOutputStream extends FilterOutputStream {
   private long count = 0L;

   public CountingOutputStream(OutputStream out) {
      super(out);
   }

   public void write(int b) throws IOException {
      this.out.write(b);
      ++this.count;
   }

   public void write(byte[] b) throws IOException {
      this.out.write(b);
      this.count += (long)b.length;
   }

   public void write(byte[] b, int off, int len) throws IOException {
      this.out.write(b, off, len);
      this.count += (long)len;
   }

   public int getCount() {
      return (int)this.count;
   }

   public long getByteCount() {
      return this.count;
   }

   public void resetCount() {
      this.count = 0L;
   }
}
