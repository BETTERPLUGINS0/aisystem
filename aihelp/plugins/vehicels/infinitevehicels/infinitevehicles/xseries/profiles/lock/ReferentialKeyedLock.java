package me.PM2.infinitevehicles.xseries.profiles.lock;

import java.util.function.Function;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class ReferentialKeyedLock<K, V> implements KeyedLock<K, V> {
   protected final Function<K, V> fetcher;
   protected final NulledKeyedLock<K, V> lock;
   private V value;

   protected ReferentialKeyedLock(NulledKeyedLock<K, V> var1, Function<K, V> var2) {
      this.lock = var1;
      this.fetcher = var2;
   }

   public V getOrRetryValue() {
      if (this.value == null) {
         this.value = this.fetcher.apply(this.lock.key);
      }

      return this.value;
   }

   public void lock() {
      this.lock.lock();
   }

   public void unlock() {
      this.lock.unlock();
   }

   public void close() {
      this.unlock();
   }

   public String toString() {
      return this.getClass().getSimpleName() + "(lock=" + this.lock + ", value=" + this.value + ", fetcher=" + (this.fetcher == null ? "null" : this.fetcher.getClass().getSimpleName()) + ')';
   }
}
