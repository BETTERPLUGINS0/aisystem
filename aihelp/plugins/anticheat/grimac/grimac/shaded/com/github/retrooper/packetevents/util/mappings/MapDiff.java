package ac.grim.grimac.shaded.com.github.retrooper.packetevents.util.mappings;

import java.util.Map;

public abstract class MapDiff<K, V> implements Diff<Map<K, V>> {
   private final K key;

   public MapDiff(final K key) {
      this.key = key;
   }

   public K getKey() {
      return this.key;
   }

   public abstract void applyTo(Map<K, V> map);

   public static class Removal<K, V> extends MapDiff<K, V> {
      public Removal(final K key) {
         super(key);
      }

      public void applyTo(Map<K, V> map) {
         map.remove(this.getKey());
      }

      public String toString() {
         return "- " + this.getKey();
      }
   }

   public static class Addition<K, V> extends MapDiff<K, V> {
      private final V value;

      public Addition(final K key, final V value) {
         super(key);
         this.value = value;
      }

      public V getValue() {
         return this.value;
      }

      public void applyTo(Map<K, V> map) {
         map.put(this.getKey(), this.getValue());
      }

      public String toString() {
         return "+ " + this.getKey() + " : " + this.getValue();
      }
   }
}
