package com.volmit.iris.engine.data.cache;

import com.volmit.iris.Iris;
import com.volmit.iris.util.function.NastySupplier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class AtomicCache<T> {
   private final transient AtomicReference<T> t;
   private final transient AtomicBoolean set;
   private final transient ReentrantLock lock;
   private final transient boolean nullSupport;

   public AtomicCache() {
      this(false);
   }

   public AtomicCache(boolean nullSupport) {
      this.set = var1 ? new AtomicBoolean() : null;
      this.t = new AtomicReference();
      this.lock = new ReentrantLock();
      this.nullSupport = var1;
   }

   public void reset() {
      this.t.set((Object)null);
      if (this.nullSupport) {
         this.set.set(false);
      }

   }

   public T aquireNasty(NastySupplier<T> t) {
      return this.aquire(() -> {
         try {
            return var1.get();
         } catch (Throwable var2) {
            return null;
         }
      });
   }

   public T aquireNastyPrint(NastySupplier<T> t) {
      return this.aquire(() -> {
         try {
            return var1.get();
         } catch (Throwable var2) {
            var2.printStackTrace();
            return null;
         }
      });
   }

   public T aquire(Supplier<T> t) {
      if (this.t.get() != null) {
         return this.t.get();
      } else if (this.nullSupport && this.set.get()) {
         return null;
      } else {
         this.lock.lock();
         if (this.t.get() != null) {
            this.lock.unlock();
            return this.t.get();
         } else if (this.nullSupport && this.set.get()) {
            this.lock.unlock();
            return null;
         } else {
            try {
               this.t.set(var1.get());
               if (this.nullSupport) {
                  this.set.set(true);
               }
            } catch (Throwable var3) {
               Iris.error("Atomic cache failure!");
               var3.printStackTrace();
            }

            this.lock.unlock();
            return this.t.get();
         }
      }
   }
}
