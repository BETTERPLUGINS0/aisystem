package com.volmit.iris.util.parallel;

import com.volmit.iris.util.function.NastyRunnable;
import com.volmit.iris.util.io.IORunnable;
import java.util.function.Supplier;

public class NOOPGridLock extends GridLock {
   public NOOPGridLock(int x, int z) {
      super(var1, var2);
   }

   public void with(int x, int z, Runnable r) {
      var3.run();
   }

   public void withNasty(int x, int z, NastyRunnable r) {
      var3.run();
   }

   public void withIO(int x, int z, IORunnable r) {
      var3.run();
   }

   public <T> T withResult(int x, int z, Supplier<T> r) {
      return var3.get();
   }

   public void withAll(Runnable r) {
      var1.run();
   }

   public <T> T withAllResult(Supplier<T> r) {
      return var1.get();
   }

   public boolean tryLock(int x, int z) {
      return true;
   }

   public boolean tryLock(int x, int z, long timeout) {
      return true;
   }

   public void lock(int x, int z) {
   }

   public void unlock(int x, int z) {
   }
}
