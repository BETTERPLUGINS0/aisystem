package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.lang.ref.ReferenceQueue;

class SI<K, V> extends BoundedLocalCache<K, V> {
   final ReferenceQueue<V> valueReferenceQueue = new ReferenceQueue();

   SI(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
      super(builder, cacheLoader, async);
   }

   protected final ReferenceQueue<V> valueReferenceQueue() {
      return this.valueReferenceQueue;
   }

   protected final boolean collectValues() {
      return true;
   }
}
