package org.apache.commons.io.output;

import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.input.UnsynchronizedByteArrayInputStream;

public final class UnsynchronizedByteArrayOutputStream extends AbstractByteArrayOutputStream {
   public UnsynchronizedByteArrayOutputStream() {
      this(1024);
   }

   public UnsynchronizedByteArrayOutputStream(int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException("Negative initial size: " + var1);
      } else {
         this.needNewBuffer(var1);
      }
   }

   public void write(byte[] var1, int var2, int var3) {
      if (var2 >= 0 && var2 <= var1.length && var3 >= 0 && var2 + var3 <= var1.length && var2 + var3 >= 0) {
         if (var3 != 0) {
            this.writeImpl(var1, var2, var3);
         }
      } else {
         throw new IndexOutOfBoundsException(String.format("offset=%,d, length=%,d", var2, var3));
      }
   }

   public void write(int var1) {
      this.writeImpl(var1);
   }

   public int write(InputStream var1) {
      return this.writeImpl(var1);
   }

   public int size() {
      return this.count;
   }

   public void reset() {
      this.resetImpl();
   }

   public void writeTo(OutputStream var1) {
      this.writeToImpl(var1);
   }

   public static InputStream toBufferedInputStream(InputStream var0) {
      return toBufferedInputStream(var0, 1024);
   }

   public static InputStream toBufferedInputStream(InputStream var0, int var1) {
      UnsynchronizedByteArrayOutputStream var2 = new UnsynchronizedByteArrayOutputStream(var1);

      InputStream var3;
      try {
         var2.write(var0);
         var3 = var2.toInputStream();
      } catch (Throwable var6) {
         try {
            var2.close();
         } catch (Throwable var5) {
            var6.addSuppressed(var5);
         }

         throw var6;
      }

      var2.close();
      return var3;
   }

   public InputStream toInputStream() {
      return this.toInputStream(UnsynchronizedByteArrayInputStream::new);
   }

   public byte[] toByteArray() {
      return this.toByteArrayImpl();
   }
}
