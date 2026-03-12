package fr.xephi.authme.util.expiring;

import java.util.concurrent.TimeUnit;

public class TimedCounter<K> extends ExpiringMap<K, Integer> {
   public TimedCounter(long duration, TimeUnit unit) {
      super(duration, unit);
   }

   public Integer get(K key) {
      Integer value = (Integer)super.get(key);
      return value == null ? 0 : value;
   }

   public void increment(K key) {
      this.put(key, this.get(key) + 1);
   }

   public void decrement(K key) {
      ExpiringMap.ExpiringEntry<Integer> e = (ExpiringMap.ExpiringEntry)this.getEntries().get(key);
      if (e != null) {
         if ((Integer)e.getValue() <= 0) {
            this.remove(key);
         } else {
            this.getEntries().put(key, new ExpiringMap.ExpiringEntry((Integer)e.getValue() - 1, e.getExpiration()));
         }
      }

   }

   public int total() {
      long currentTime = System.currentTimeMillis();
      return (Integer)this.getEntries().values().stream().filter((entry) -> {
         return currentTime <= entry.getExpiration();
      }).map(ExpiringMap.ExpiringEntry::getValue).reduce(0, Integer::sum);
   }
}
