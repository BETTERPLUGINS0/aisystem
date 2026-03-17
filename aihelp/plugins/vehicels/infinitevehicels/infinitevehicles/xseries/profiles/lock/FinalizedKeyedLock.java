package me.PM2.infinitevehicles.xseries.profiles.lock;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
final class FinalizedKeyedLock<K, V> implements KeyedLock<K, V> {
   private final V value;

   protected FinalizedKeyedLock(V var1) {
      this.value = var1;
   }

   public String toString() {
      return this.getClass().getSimpleName() + '(' + this.value + ')';
   }

   public V getOrRetryValue() {
      return this.value;
   }

   public void lock() {
   }

   public void unlock() {
   }

   public void close() {
   }
}
