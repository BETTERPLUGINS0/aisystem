package ac.grim.grimac.shaded.maps;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedMap;
import java.util.Map.Entry;

public class Fluent {
   private Fluent() {
   }

   public static class ConcurrentHashMap<K, V> extends java.util.concurrent.ConcurrentHashMap<K, V> implements Fluent.Map<K, V> {
      public ConcurrentHashMap() {
      }

      public ConcurrentHashMap(int initialCapacity) {
         super(initialCapacity);
      }

      public ConcurrentHashMap(java.util.Map<? extends K, ? extends V> m) {
         super(m);
      }

      public ConcurrentHashMap(int initialCapacity, float loadFactor) {
         super(initialCapacity, loadFactor);
      }

      public ConcurrentHashMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
         super(initialCapacity, loadFactor, concurrencyLevel);
      }
   }

   public static class ConcurrentSkipListMap<K, V> extends java.util.concurrent.ConcurrentSkipListMap<K, V> implements Fluent.Map<K, V> {
      public ConcurrentSkipListMap() {
      }

      public ConcurrentSkipListMap(Comparator<? super K> comparator) {
         super(comparator);
      }

      public ConcurrentSkipListMap(java.util.Map<? extends K, ? extends V> m) {
         super(m);
      }

      public ConcurrentSkipListMap(SortedMap<K, ? extends V> m) {
         super(m);
      }
   }

   public static class WeakHashMap<K, V> extends java.util.WeakHashMap<K, V> implements Fluent.Map<K, V> {
      public WeakHashMap(int initialCapacity, float loadFactor) {
         super(initialCapacity, loadFactor);
      }

      public WeakHashMap(int initialCapacity) {
         super(initialCapacity);
      }

      public WeakHashMap() {
      }

      public WeakHashMap(java.util.Map<? extends K, ? extends V> m) {
         super(m);
      }
   }

   public static class EnumMap<K extends Enum<K>, V> extends java.util.EnumMap<K, V> implements Fluent.Map<K, V> {
      public EnumMap(Class<K> keyType) {
         super(keyType);
      }

      public EnumMap(java.util.EnumMap<K, ? extends V> m) {
         super(m);
      }

      public EnumMap(java.util.Map<K, ? extends V> m) {
         super(m);
      }
   }

   public static class IdentityHashMap<K, V> extends java.util.IdentityHashMap<K, V> implements Fluent.Map<K, V> {
      public IdentityHashMap() {
      }

      public IdentityHashMap(int expectedMaxSize) {
         super(expectedMaxSize);
      }

      public IdentityHashMap(java.util.Map<? extends K, ? extends V> m) {
         super(m);
      }
   }

   public static class LinkedHashMap<K, V> extends java.util.LinkedHashMap<K, V> implements Fluent.Map<K, V> {
      public LinkedHashMap(int initialCapacity, float loadFactor) {
         super(initialCapacity, loadFactor);
      }

      public LinkedHashMap(int initialCapacity) {
         super(initialCapacity);
      }

      public LinkedHashMap(java.util.Map m) {
         super(m);
      }

      public LinkedHashMap() {
      }
   }

   public static class HashMap<K, V> extends java.util.HashMap<K, V> implements Fluent.Map<K, V> {
      public HashMap(int initialCapacity, float loadFactor) {
         super(initialCapacity, loadFactor);
      }

      public HashMap(int initialCapacity) {
         super(initialCapacity);
      }

      public HashMap(java.util.Map m) {
         super(m);
      }

      public HashMap() {
      }
   }

   public interface Map<K, V> extends java.util.Map<K, V> {
      default Fluent.Map<K, V> append(K key, V val) {
         this.put(key, val);
         return this;
      }

      default Fluent.Map<K, V> appendAll(java.util.Map<? extends K, ? extends V> map) {
         this.putAll(map);
         return this;
      }

      default Fluent.Map<K, V> append(Entry<? extends K, ? extends V> entry) {
         return this.append(entry.getKey(), entry.getValue());
      }

      default java.util.Map<K, V> unmodifiable() {
         return Collections.unmodifiableMap(this);
      }
   }
}
