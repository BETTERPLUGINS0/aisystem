package com.volmit.iris.util.hunk.bits;

import com.volmit.iris.util.function.Consumer2;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class LinearPalette<T> implements Palette<T> {
   private volatile AtomicReferenceArray<T> palette;
   private final AtomicInteger size = new AtomicInteger(1);

   public LinearPalette(int initialSize) {
      this.palette = new AtomicReferenceArray(var1);
      this.palette.set(0, (Object)null);
   }

   public T get(int id) {
      return var1 >= 0 && var1 < this.size.get() ? this.palette.get(var1) : null;
   }

   public int add(T t) {
      int var2 = this.size.getAndIncrement();
      if (this.palette.length() <= var2) {
         this.grow(var2);
      }

      this.palette.set(var2, var1);
      return var2;
   }

   private synchronized void grow(int lastIndex) {
      if (this.palette.length() <= var1) {
         AtomicReferenceArray var2 = new AtomicReferenceArray(var1 + 1);

         for(int var3 = 0; var3 < this.palette.length(); ++var3) {
            var2.set(var3, this.palette.get(var3));
         }

         this.palette = var2;
      }
   }

   public int id(T t) {
      if (var1 == null) {
         return 0;
      } else {
         for(int var2 = 1; var2 < this.size.get(); ++var2) {
            if (var1.equals(this.palette.get(var2))) {
               return var2;
            }
         }

         return -1;
      }
   }

   public int size() {
      return this.size.get() - 1;
   }

   public void iterate(Consumer2<T, Integer> c) {
      for(int var2 = 1; var2 <= this.size(); ++var2) {
         var1.accept(this.palette.get(var2), var2);
      }

   }
}
