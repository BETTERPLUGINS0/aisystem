package fr.xephi.authme.libs.waffle.util.cache;

import fr.xephi.authme.libs.com.github.benmanes.caffeine.cache.Caffeine;
import java.time.Duration;
import org.checkerframework.checker.index.qual.NonNegative;

public class CaffeineCache<K, V> implements Cache<K, V> {
   private final fr.xephi.authme.libs.com.github.benmanes.caffeine.cache.Cache<K, V> cache;

   public CaffeineCache(@NonNegative long timeout) {
      this.cache = Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(timeout)).build();
   }

   public V get(K key) {
      return this.cache.asMap().get(key);
   }

   public void put(K key, V value) {
      this.cache.put(key, value);
   }

   public void remove(K key) {
      this.cache.asMap().remove(key);
   }

   public int size() {
      return this.cache.asMap().size();
   }
}
