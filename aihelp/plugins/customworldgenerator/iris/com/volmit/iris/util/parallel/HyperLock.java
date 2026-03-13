package com.volmit.iris.util.parallel;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap.Builder;
import com.volmit.iris.Iris;
import com.volmit.iris.engine.data.cache.Cache;
import com.volmit.iris.util.function.NastyRunnable;
import com.volmit.iris.util.function.NastySupplier;
import com.volmit.iris.util.io.IORunnable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class HyperLock {
   private final ConcurrentLinkedHashMap<Long, ReentrantLock> locks;
   private boolean enabled;
   private boolean fair;

   public HyperLock() {
      this(1024, false);
   }

   public HyperLock(int capacity) {
      this(var1, false);
   }

   public HyperLock(int capacity, boolean fair) {
      this.enabled = true;
      this.fair = false;
      this.fair = var2;
      this.locks = (new Builder()).initialCapacity(var1).maximumWeightedCapacity((long)var1).listener((var0, var1x) -> {
         if (var1x.isLocked() || var1x.isHeldByCurrentThread()) {
            Iris.warn("InfiniLock Eviction of " + var0 + " still has locks on it!");
         }

      }).concurrencyLevel(32).build();
   }

   public void with(int x, int z, Runnable r) {
      this.lock(var1, var2);

      try {
         var3.run();
      } finally {
         this.unlock(var1, var2);
      }

   }

   public void withLong(long k, Runnable r) {
      int var4 = Cache.keyX(var1);
      int var5 = Cache.keyZ(var1);
      this.lock(var4, var5);

      try {
         var3.run();
      } finally {
         this.unlock(var4, var5);
      }

   }

   public void withNasty(int x, int z, NastyRunnable r) {
      this.lock(var1, var2);
      Throwable var4 = null;

      try {
         var3.run();
      } catch (Throwable var9) {
         var4 = var9;
      } finally {
         this.unlock(var1, var2);
         if (var4 != null) {
            throw var4;
         }

      }

   }

   public void withIO(int x, int z, IORunnable r) {
      this.lock(var1, var2);
      IOException var4 = null;

      try {
         var3.run();
      } catch (IOException var9) {
         var4 = var9;
      } finally {
         this.unlock(var1, var2);
         if (var4 != null) {
            throw var4;
         }

      }

   }

   public <T> T withResult(int x, int z, Supplier<T> r) {
      this.lock(var1, var2);

      Object var4;
      try {
         var4 = var3.get();
      } finally {
         this.unlock(var1, var2);
      }

      return var4;
   }

   public <T> T withNastyResult(int x, int z, NastySupplier<T> r) {
      this.lock(var1, var2);

      Object var4;
      try {
         var4 = var3.get();
      } finally {
         this.unlock(var1, var2);
      }

      return var4;
   }

   public boolean tryLock(int x, int z) {
      return this.getLock(var1, var2).tryLock();
   }

   public boolean tryLock(int x, int z, long timeout) {
      try {
         return this.getLock(var1, var2).tryLock(var3, TimeUnit.MILLISECONDS);
      } catch (InterruptedException var6) {
         Iris.reportError(var6);
         return false;
      }
   }

   private ReentrantLock getLock(int x, int z) {
      return (ReentrantLock)this.locks.computeIfAbsent(Cache.key(var1, var2), (var1x) -> {
         return new ReentrantLock(this.fair);
      });
   }

   public void lock(int x, int z) {
      if (this.enabled) {
         this.getLock(var1, var2).lock();
      }
   }

   public void unlock(int x, int z) {
      if (this.enabled) {
         this.getLock(var1, var2).unlock();
      }
   }

   public void disable() {
      this.enabled = false;
      this.locks.values().forEach(ReentrantLock::lock);
   }
}
