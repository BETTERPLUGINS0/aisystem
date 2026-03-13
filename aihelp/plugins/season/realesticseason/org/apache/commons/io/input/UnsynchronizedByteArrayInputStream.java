package org.apache.commons.io.input;

import java.io.InputStream;
import java.util.Objects;

public class UnsynchronizedByteArrayInputStream extends InputStream {
   public static final int END_OF_STREAM = -1;
   private final byte[] data;
   private final int eod;
   private int offset;
   private int markedOffset;

   public UnsynchronizedByteArrayInputStream(byte[] var1) {
      this.data = (byte[])Objects.requireNonNull(var1, "data");
      this.offset = 0;
      this.eod = var1.length;
      this.markedOffset = this.offset;
   }

   public UnsynchronizedByteArrayInputStream(byte[] var1, int var2) {
      Objects.requireNonNull(var1, "data");
      if (var2 < 0) {
         throw new IllegalArgumentException("offset cannot be negative");
      } else {
         this.data = var1;
         this.offset = Math.min(var2, var1.length > 0 ? var1.length : var2);
         this.eod = var1.length;
         this.markedOffset = this.offset;
      }
   }

   public UnsynchronizedByteArrayInputStream(byte[] var1, int var2, int var3) {
      if (var2 < 0) {
         throw new IllegalArgumentException("offset cannot be negative");
      } else if (var3 < 0) {
         throw new IllegalArgumentException("length cannot be negative");
      } else {
         this.data = (byte[])Objects.requireNonNull(var1, "data");
         this.offset = Math.min(var2, var1.length > 0 ? var1.length : var2);
         this.eod = Math.min(this.offset + var3, var1.length);
         this.markedOffset = this.offset;
      }
   }

   public int available() {
      return this.offset < this.eod ? this.eod - this.offset : 0;
   }

   public int read() {
      return this.offset < this.eod ? this.data[this.offset++] & 255 : -1;
   }

   public int read(byte[] var1) {
      Objects.requireNonNull(var1, "dest");
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) {
      Objects.requireNonNull(var1, "dest");
      if (var2 >= 0 && var3 >= 0 && var2 + var3 <= var1.length) {
         if (this.offset >= this.eod) {
            return -1;
         } else {
            int var4 = this.eod - this.offset;
            if (var3 < var4) {
               var4 = var3;
            }

            if (var4 <= 0) {
               return 0;
            } else {
               System.arraycopy(this.data, this.offset, var1, var2, var4);
               this.offset += var4;
               return var4;
            }
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public long skip(long var1) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("Skipping backward is not supported");
      } else {
         long var3 = (long)(this.eod - this.offset);
         if (var1 < var3) {
            var3 = var1;
         }

         this.offset = (int)((long)this.offset + var3);
         return var3;
      }
   }

   public boolean markSupported() {
      return true;
   }

   public void mark(int var1) {
      this.markedOffset = this.offset;
   }

   public void reset() {
      this.offset = this.markedOffset;
   }
}
