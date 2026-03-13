package com.volmit.iris.util.hunk.bits;

import com.volmit.iris.util.collection.KMap;
import com.volmit.iris.util.function.Consumer2;
import java.io.DataInputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class HashPalette<T> implements Palette<T> {
   private final Object lock = new Object();
   private final KMap<T, Integer> palette = new KMap();
   private final KMap<Integer, T> lookup = new KMap();
   private final AtomicInteger size = new AtomicInteger(1);

   public T get(int id) {
      return var1 > 0 && var1 < this.size.get() ? this.lookup.get(var1) : null;
   }

   public int add(T t) {
      return var1 == null ? 0 : (Integer)this.palette.computeIfAbsent(var1, (var2) -> {
         synchronized(this.lock) {
            int var4 = this.size.getAndIncrement();
            this.lookup.put(var4, var1);
            return var4;
         }
      });
   }

   public int id(T t) {
      if (var1 == null) {
         return 0;
      } else {
         Integer var2 = (Integer)this.palette.get(var1);
         return var2 != null ? var2 : -1;
      }
   }

   public int size() {
      return this.size.get() - 1;
   }

   public void iterate(Consumer2<T, Integer> c) {
      synchronized(this.lock) {
         for(int var3 = 1; var3 < this.size.get(); ++var3) {
            var1.accept(this.lookup.get(var3), var3);
         }

      }
   }

   public Palette<T> from(Palette<T> oldPalette) {
      var1.iterate((var1x, var2) -> {
         if (var1x == null) {
            throw new NullPointerException("Null palette entries are not allowed!");
         } else {
            this.lookup.put(var2, var1x);
            this.palette.put(var1x, var2);
         }
      });
      this.size.set(var1.size() + 1);
      return this;
   }

   public Palette<T> from(int size, Writable<T> writable, DataInputStream in) {
      for(int var4 = 1; var4 <= var1; ++var4) {
         Object var5 = var2.readNodeData(var3);
         if (var5 == null) {
            throw new NullPointerException("Null palette entries are not allowed!");
         }

         this.lookup.put(var4, var5);
         this.palette.put(var5, var4);
      }

      this.size.set(var1 + 1);
      return this;
   }
}
