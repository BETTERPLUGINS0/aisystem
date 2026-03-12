package fr.xephi.authme.libs.com.google.common.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

@ElementTypesAreNonnullByDefault
abstract class ForwardingLock implements Lock {
   abstract Lock delegate();

   public void lock() {
      this.delegate().lock();
   }

   public void lockInterruptibly() throws InterruptedException {
      this.delegate().lockInterruptibly();
   }

   public boolean tryLock() {
      return this.delegate().tryLock();
   }

   public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
      return this.delegate().tryLock(time, unit);
   }

   public void unlock() {
      this.delegate().unlock();
   }

   public Condition newCondition() {
      return this.delegate().newCondition();
   }
}
