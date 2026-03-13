package com.volmit.iris.util.io;

import java.io.DataInputStream;
import java.io.InputStream;
import lombok.Generated;
import org.jetbrains.annotations.NotNull;

public class CountingDataInputStream extends DataInputStream {
   private final CountingDataInputStream.Counter counter;

   private CountingDataInputStream(@NotNull InputStream in) {
      super(var1);
      if (var1 instanceof CountingDataInputStream.Counter) {
         CountingDataInputStream.Counter var2 = (CountingDataInputStream.Counter)var1;
         this.counter = var2;
      } else {
         throw new IllegalArgumentException("Underlying stream must be a Counter");
      }
   }

   public static CountingDataInputStream wrap(@NotNull InputStream in) {
      return new CountingDataInputStream(new CountingDataInputStream.Counter(var0));
   }

   public long count() {
      return this.counter.count;
   }

   public void skipTo(long target) {
      this.skipNBytes(Math.max(var1 - this.counter.count, 0L));
   }

   private static class Counter extends InputStream {
      private final InputStream in;
      private long count;
      private long mark = -1L;
      private int markLimit = 0;

      public int read() {
         int var1 = this.in.read();
         if (var1 != -1) {
            this.count(1);
         }

         return var1;
      }

      public int read(@NotNull byte[] b, int off, int len) {
         int var4 = this.in.read(var1, var2, var3);
         if (var4 != -1) {
            this.count(var4);
         }

         return var4;
      }

      private void count(int i) {
         this.count = Math.addExact(this.count, (long)var1);
         if (this.mark != -1L) {
            this.markLimit -= var1;
            if (this.markLimit <= 0) {
               this.mark = -1L;
            }

         }
      }

      public boolean markSupported() {
         return this.in.markSupported();
      }

      public synchronized void mark(int readlimit) {
         if (this.in.markSupported()) {
            this.in.mark(var1);
            if (var1 <= 0) {
               this.mark = -1L;
               this.markLimit = 0;
            } else {
               this.mark = this.count;
               this.markLimit = var1;
            }
         }
      }

      public synchronized void reset() {
         this.in.reset();
         this.count = this.mark;
      }

      public void close() {
         this.in.close();
      }

      @Generated
      public Counter(final InputStream in) {
         this.in = var1;
      }
   }
}
