package com.volmit.iris.util.data;

import com.volmit.iris.util.function.Function2;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class ChunkCache<T> {
   private final AtomicReferenceArray<T> cache = new AtomicReferenceArray(256);

   public T compute(int x, int z, Function2<Integer, Integer, T> function) {
      Object var4 = this.get(var1 & 15, var2 & 15);
      if (var4 == null) {
         var4 = var3.apply(var1, var2);
         this.set(var1 & 15, var2 & 15, var4);
      }

      return var4;
   }

   private void set(int x, int z, T t) {
      this.cache.set(var1 * 16 + var2, var3);
   }

   private T get(int x, int z) {
      return this.cache.get(var1 * 16 + var2);
   }
}
