package github.nighter.smartspawner.language;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LRUCache<K, V> {
   private final LinkedHashMap<K, V> cache;
   private int capacity;

   public LRUCache(int capacity) {
      this.capacity = capacity;
      this.cache = new LinkedHashMap<K, V>(capacity, 0.75F, true) {
         protected boolean removeEldestEntry(Entry<K, V> eldest) {
            return this.size() > LRUCache.this.capacity;
         }
      };
   }

   public synchronized V get(K key) {
      return this.cache.get(key);
   }

   public synchronized V put(K key, V value) {
      return this.cache.put(key, value);
   }

   public synchronized void clear() {
      this.cache.clear();
   }

   public synchronized int size() {
      return this.cache.size();
   }

   public synchronized int capacity() {
      return this.capacity;
   }

   public synchronized void resize(int newCapacity) {
      this.capacity = newCapacity;
   }
}
