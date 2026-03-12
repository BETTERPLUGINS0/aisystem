package fr.xephi.authme.util.expiring;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ExpiringSet<E> {
   private Map<E, Long> entries = new ConcurrentHashMap();
   private long expirationMillis;

   public ExpiringSet(long duration, TimeUnit unit) {
      this.setExpiration(duration, unit);
   }

   public void add(E entry) {
      this.entries.put(entry, System.currentTimeMillis() + this.expirationMillis);
   }

   public boolean contains(E entry) {
      Long expiration = (Long)this.entries.get(entry);
      if (expiration == null) {
         return false;
      } else if (expiration > System.currentTimeMillis()) {
         return true;
      } else {
         this.entries.remove(entry);
         return false;
      }
   }

   public void remove(E entry) {
      this.entries.remove(entry);
   }

   public void clear() {
      this.entries.clear();
   }

   public void removeExpiredEntries() {
      this.entries.entrySet().removeIf((entry) -> {
         return System.currentTimeMillis() > (Long)entry.getValue();
      });
   }

   public Duration getExpiration(E entry) {
      Long expiration = (Long)this.entries.get(entry);
      if (expiration == null) {
         return new Duration(-1L, TimeUnit.SECONDS);
      } else {
         long stillPresentMillis = expiration - System.currentTimeMillis();
         if (stillPresentMillis < 0L) {
            this.entries.remove(entry);
            return new Duration(-1L, TimeUnit.SECONDS);
         } else {
            return Duration.createWithSuitableUnit(stillPresentMillis, TimeUnit.MILLISECONDS);
         }
      }
   }

   public void setExpiration(long duration, TimeUnit unit) {
      this.expirationMillis = unit.toMillis(duration);
   }

   public boolean isEmpty() {
      return this.entries.isEmpty();
   }
}
