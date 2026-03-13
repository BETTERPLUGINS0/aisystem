package org.apache.commons.io.input.buffer;

import java.io.InputStream;
import java.util.Objects;
import org.apache.commons.io.IOUtils;

public class CircularBufferInputStream extends InputStream {
   protected final InputStream in;
   protected final CircularByteBuffer buffer;
   protected final int bufferSize;
   private boolean eof;

   public CircularBufferInputStream(InputStream var1, int var2) {
      if (var2 <= 0) {
         throw new IllegalArgumentException("Invalid bufferSize: " + var2);
      } else {
         this.in = (InputStream)Objects.requireNonNull(var1, "inputStream");
         this.buffer = new CircularByteBuffer(var2);
         this.bufferSize = var2;
         this.eof = false;
      }
   }

   public CircularBufferInputStream(InputStream var1) {
      this(var1, 8192);
   }

   protected void fillBuffer() {
      if (!this.eof) {
         int var1 = this.buffer.getSpace();
         byte[] var2 = IOUtils.byteArray(var1);

         while(var1 > 0) {
            int var3 = this.in.read(var2, 0, var1);
            if (var3 == -1) {
               this.eof = true;
               return;
            }

            if (var3 > 0) {
               this.buffer.add(var2, 0, var3);
               var1 -= var3;
            }
         }

      }
   }

   protected boolean haveBytes(int var1) {
      if (this.buffer.getCurrentNumberOfBytes() < var1) {
         this.fillBuffer();
      }

      return this.buffer.hasBytes();
   }

   public int read() {
      return !this.haveBytes(1) ? -1 : this.buffer.read() & 255;
   }

   public int read(byte[] var1) {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) {
      Objects.requireNonNull(var1, "targetBuffer");
      if (var2 < 0) {
         throw new IllegalArgumentException("Offset must not be negative");
      } else if (var3 < 0) {
         throw new IllegalArgumentException("Length must not be negative");
      } else if (!this.haveBytes(var3)) {
         return -1;
      } else {
         int var4 = Math.min(var3, this.buffer.getCurrentNumberOfBytes());

         for(int var5 = 0; var5 < var4; ++var5) {
            var1[var2 + var5] = this.buffer.read();
         }

         return var4;
      }
   }

   public void close() {
      this.in.close();
      this.eof = true;
      this.buffer.clear();
   }
}
