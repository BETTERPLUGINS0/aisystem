package fr.xephi.authme.libs.com.github.benmanes.caffeine.cache;

class SS<K, V> extends BoundedLocalCache<K, V> {
   SS(Caffeine<K, V> builder, CacheLoader<? super K, V> cacheLoader, boolean async) {
      super(builder, cacheLoader, async);
   }
}
