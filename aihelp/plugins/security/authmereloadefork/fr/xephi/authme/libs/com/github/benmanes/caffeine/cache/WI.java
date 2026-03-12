package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

import java.lang.ref.ReferenceQueue;

class WI<K, V> extends BoundedLocalCache<K, V> {
   final ReferenceQueue<K> keyReferenceQueue = new ReferenceQueue();
   final ReferenceQueue<V> valueReferenceQueue = new ReferenceQueue();

   WI(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
      super(builder, cacheLoader, async);
   }

   protected final ReferenceQueue<K> keyReferenceQueue() {
      return this.keyReferenceQueue;
   }

   protected final boolean collectKeys() {
      return true;
   }

   protected final ReferenceQueue<V> valueReferenceQueue() {
      return this.valueReferenceQueue;
   }

   protected final boolean collectValues() {
      return true;
   }
}
