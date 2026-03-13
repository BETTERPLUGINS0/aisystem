package com.volmit.iris.util.data;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class Recycler<T> {
   private final List<Recycler.RecycledObject<T>> pool = new CopyOnWriteArrayList();
   private final Supplier<T> factory;

   public Recycler(Supplier<T> factory) {
      this.factory = var1;
   }

   public int getFreeObjects() {
      int var1 = 0;
      Iterator var3 = this.pool.iterator();

      while(var3.hasNext()) {
         Recycler.RecycledObject var4 = (Recycler.RecycledObject)var3.next();
         if (!var4.isUsed()) {
            ++var1;
         }
      }

      return var1;
   }

   public int getUsedOjects() {
      int var1 = 0;
      Iterator var3 = this.pool.iterator();

      while(var3.hasNext()) {
         Recycler.RecycledObject var4 = (Recycler.RecycledObject)var3.next();
         if (var4.isUsed()) {
            ++var1;
         }
      }

      return var1;
   }

   public void dealloc(T t) {
      Iterator var3 = this.pool.iterator();

      Recycler.RecycledObject var4;
      do {
         if (!var3.hasNext()) {
            return;
         }

         var4 = (Recycler.RecycledObject)var3.next();
      } while(!var4.isUsed() || System.identityHashCode(var1) != System.identityHashCode(var4.getObject()));

      var4.dealloc();
   }

   public T alloc() {
      Iterator var2 = this.pool.iterator();

      Recycler.RecycledObject var3;
      do {
         if (!var2.hasNext()) {
            this.expand();
            return this.alloc();
         }

         var3 = (Recycler.RecycledObject)var2.next();
      } while(!var3.alloc());

      return var3.getObject();
   }

   public boolean contract() {
      return this.contract(Math.max(this.getFreeObjects() / 2, Runtime.getRuntime().availableProcessors()));
   }

   public boolean contract(int maxFree) {
      int var2 = this.getFreeObjects() - var1;
      if (var2 > 0) {
         for(int var4 = this.pool.size() - 1; var4 > 0; --var4) {
            Recycler.RecycledObject var3 = (Recycler.RecycledObject)this.pool.get(var4);
            if (!var3.isUsed()) {
               this.pool.remove(var4);
               --var2;
               if (var2 <= 0) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public void expand() {
      if (this.pool.isEmpty()) {
         this.expand(Runtime.getRuntime().availableProcessors());
      } else {
         this.expand(this.getUsedOjects() + Runtime.getRuntime().availableProcessors());
      }
   }

   public void expand(int by) {
      for(int var2 = 0; var2 < var1; ++var2) {
         this.pool.add(new Recycler.RecycledObject(this.factory.get()));
      }

   }

   public int size() {
      return this.pool.size();
   }

   public void deallocAll() {
      this.pool.clear();
   }

   public static class RecycledObject<T> {
      private final T object;
      private final AtomicBoolean used;

      public RecycledObject(T object) {
         this.object = var1;
         this.used = new AtomicBoolean(false);
      }

      public T getObject() {
         return this.object;
      }

      public boolean isUsed() {
         return this.used.get();
      }

      public void dealloc() {
         this.used.set(false);
      }

      public boolean alloc() {
         if (this.used.get()) {
            return false;
         } else {
            this.used.set(true);
            return true;
         }
      }
   }
}
