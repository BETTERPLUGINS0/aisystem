package fr.xephi.authme.util.expiring;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ExpiringMap<K, V> {
   private final Map<K, ExpiringMap.ExpiringEntry<V>> entries = new ConcurrentHashMap();
   private long expirationMillis;

   public ExpiringMap(long duration, TimeUnit unit) {
      this.setExpiration(duration, unit);
   }

   public V get(K key) {
      ExpiringMap.ExpiringEntry<V> value = (ExpiringMap.ExpiringEntry)this.entries.get(key);
      if (value == null) {
         return null;
      } else if (System.currentTimeMillis() > value.getExpiration()) {
         this.entries.remove(key);
         return null;
      } else {
         return value.getValue();
      }
   }

   public void put(K key, V value) {
      long expiration = System.currentTimeMillis() + this.expirationMillis;
      this.entries.put(key, new ExpiringMap.ExpiringEntry(value, expiration));
   }

   public void remove(K key) {
      this.entries.remove(key);
   }

   public void removeExpiredEntries() {
      this.entries.entrySet().removeIf((entry) -> {
         return System.currentTimeMillis() > ((ExpiringMap.ExpiringEntry)entry.getValue()).getExpiration();
      });
   }

   public void setExpiration(long duration, TimeUnit unit) {
      this.expirationMillis = unit.toMillis(duration);
   }

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }

   protected Map<K, ExpiringMap.ExpiringEntry<V>> getEntries() {
      return this.entries;
   }

   protected static final class ExpiringEntry<V> {
      private final V value;
      private final long expiration;

      ExpiringEntry(V value, long expiration) {
         this.value = value;
         this.expiration = expiration;
      }

      V getValue() {
         return this.value;
      }

      long getExpiration() {
         return this.expiration;
      }
   }
}
