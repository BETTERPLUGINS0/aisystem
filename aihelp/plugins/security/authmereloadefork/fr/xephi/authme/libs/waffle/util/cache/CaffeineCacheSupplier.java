package fr.xephi.authme.libs.waffle.util.cache;

public class CaffeineCacheSupplier implements CacheSupplier {
   public <K, V> Cache<K, V> newCache(long timeout) {
      return new CaffeineCache(timeout);
   }
}
