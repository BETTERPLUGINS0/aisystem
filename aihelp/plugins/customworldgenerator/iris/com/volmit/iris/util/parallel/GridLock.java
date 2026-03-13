package com.volmit.iris.util.parallel;

import com.volmit.iris.Iris;
import com.volmit.iris.util.function.NastyRunnable;
import com.volmit.iris.util.hunk.Hunk;
import com.volmit.iris.util.io.IORunnable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

public class GridLock {
   private final Hunk<ReentrantLock> locks;

   public GridLock(int x, int z) {
      this.locks = Hunk.newAtomicHunk(var1, 1, var2);
      this.locks.iterateSync((var1x, var2x, var3) -> {
         this.locks.set(var1x, var2x, var3, new ReentrantLock());
      });
   }

   public void with(int x, int z, Runnable r) {
      this.lock(var1, var2);
      var3.run();
      this.unlock(var1, var2);
   }

   public void withNasty(int x, int z, NastyRunnable r) {
      this.lock(var1, var2);
      var3.run();
      this.unlock(var1, var2);
   }

   public void withIO(int x, int z, IORunnable r) {
      this.lock(var1, var2);
      var3.run();
      this.unlock(var1, var2);
   }

   public <T> T withResult(int x, int z, Supplier<T> r) {
      this.lock(var1, var2);
      Object var4 = var3.get();
      this.unlock(var1, var2);
      return var4;
   }

   public void withAll(Runnable r) {
      this.locks.iterateSync((var0, var1x, var2, var3) -> {
         var3.lock();
      });
      var1.run();
      this.locks.iterateSync((var0, var1x, var2, var3) -> {
         var3.unlock();
      });
   }

   public <T> T withAllResult(Supplier<T> r) {
      this.locks.iterateSync((var0, var1x, var2x, var3) -> {
         var3.lock();
      });
      Object var2 = var1.get();
      this.locks.iterateSync((var0, var1x, var2x, var3) -> {
         var3.unlock();
      });
      return var2;
   }

   public boolean tryLock(int x, int z) {
      return ((ReentrantLock)this.locks.get(var1, 0, var2)).tryLock();
   }

   public boolean tryLock(int x, int z, long timeout) {
      try {
         return ((ReentrantLock)this.locks.get(var1, 0, var2)).tryLock(var3, TimeUnit.MILLISECONDS);
      } catch (InterruptedException var6) {
         Iris.reportError(var6);
         return false;
      }
   }

   public void lock(int x, int z) {
      ((ReentrantLock)this.locks.get(var1, 0, var2)).lock();
   }

   public void unlock(int x, int z) {
      ((ReentrantLock)this.locks.get(var1, 0, var2)).unlock();
   }
}
