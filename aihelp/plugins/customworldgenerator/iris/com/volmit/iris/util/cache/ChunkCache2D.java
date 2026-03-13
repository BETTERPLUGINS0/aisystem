package com.volmit.iris.util.cache;

import com.volmit.iris.util.function.Function2;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class ChunkCache2D<T> {
   private static final boolean FAST = Boolean.getBoolean("iris.cache.fast");
   private static final boolean DYNAMIC = Boolean.getBoolean("iris.cache.dynamic");
   private static final VarHandle AA = MethodHandles.arrayElementVarHandle(ChunkCache2D.Entry[].class);
   private final ChunkCache2D.Entry<T>[] cache = new ChunkCache2D.Entry[256];

   public ChunkCache2D() {
      if (!DYNAMIC) {
         for(int var1 = 0; var1 < this.cache.length; ++var1) {
            this.cache[var1] = (ChunkCache2D.Entry)(FAST ? new ChunkCache2D.FastEntry() : new ChunkCache2D.Entry());
         }

      }
   }

   public T get(int x, int z, Function2<Integer, Integer, T> resolver) {
      int var4 = (var2 & 15) * 16 + (var1 & 15);
      Object var5 = this.cache[var4];
      if (var5 == null) {
         var5 = FAST ? new ChunkCache2D.FastEntry() : new ChunkCache2D.Entry();
         if (!AA.compareAndSet(this.cache, var4, (Void)null, (ChunkCache2D.Entry)var5)) {
            var5 = AA.getVolatile(this.cache, var4);
         }
      }

      return ((ChunkCache2D.Entry)var5).compute(var1, var2, var3);
   }

   private static class Entry<T> {
      protected volatile T t;

      protected T compute(int x, int z, Function2<Integer, Integer, T> resolver) {
         if (this.t != null) {
            return this.t;
         } else {
            synchronized(this) {
               if (this.t == null) {
                  this.t = var3.apply(var1, var2);
               }

               return this.t;
            }
         }
      }
   }

   private static class FastEntry<T> extends ChunkCache2D.Entry<T> {
      protected T compute(int x, int z, Function2<Integer, Integer, T> resolver) {
         return this.t != null ? this.t : (this.t = var3.apply(var1, var2));
      }
   }
}
