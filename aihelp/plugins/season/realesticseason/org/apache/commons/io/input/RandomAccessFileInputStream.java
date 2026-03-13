package org.apache.commons.io.input;

import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Objects;

public class RandomAccessFileInputStream extends InputStream {
   private final boolean closeOnClose;
   private final RandomAccessFile randomAccessFile;

   public RandomAccessFileInputStream(RandomAccessFile var1) {
      this(var1, false);
   }

   public RandomAccessFileInputStream(RandomAccessFile var1, boolean var2) {
      this.randomAccessFile = (RandomAccessFile)Objects.requireNonNull(var1, "file");
      this.closeOnClose = var2;
   }

   public int available() {
      long var1 = this.availableLong();
      return var1 > 2147483647L ? Integer.MAX_VALUE : (int)var1;
   }

   public long availableLong() {
      return this.randomAccessFile.length() - this.randomAccessFile.getFilePointer();
   }

   public void close() {
      super.close();
      if (this.closeOnClose) {
         this.randomAccessFile.close();
      }

   }

   public RandomAccessFile getRandomAccessFile() {
      return this.randomAccessFile;
   }

   public boolean isCloseOnClose() {
      return this.closeOnClose;
   }

   public int read() {
      return this.randomAccessFile.read();
   }

   public int read(byte[] var1) {
      return this.randomAccessFile.read(var1);
   }

   public int read(byte[] var1, int var2, int var3) {
      return this.randomAccessFile.read(var1, var2, var3);
   }

   private void seek(long var1) {
      this.randomAccessFile.seek(var1);
   }

   public long skip(long var1) {
      if (var1 <= 0L) {
         return 0L;
      } else {
         long var3 = this.randomAccessFile.getFilePointer();
         long var5 = this.randomAccessFile.length();
         if (var3 >= var5) {
            return 0L;
         } else {
            long var7 = var3 + var1;
            long var9 = var7 > var5 ? var5 - 1L : var7;
            if (var9 > 0L) {
               this.seek(var9);
            }

            return this.randomAccessFile.getFilePointer() - var3;
         }
      }
   }
}
