package me.PM2.infinitevehicles.xseries.profiles.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class NulledKeyedLock<K, V> implements KeyedLock<K, V> {
   protected final Lock lock = new ReentrantLock();
   private final KeyedLockMap<K> map;
   protected final K key;
   protected int pendingTasks;

   protected NulledKeyedLock(KeyedLockMap<K> var1, K var2) {
      this.map = var1;
      this.key = var2;
   }

   public V getOrRetryValue() {
      return null;
   }

   public void lock() {
      this.lock.lock();
   }

   protected boolean tryLock() {
      return this.lock.tryLock();
   }

   public void unlock() {
      this.map.unlock(this);
   }

   public void close() {
      this.unlock();
   }

   public String toString() {
      return this.getClass().getSimpleName() + "(key=" + this.key + ", pendingTasks=" + this.pendingTasks + ')';
   }
}
