package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.HashCommon;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleCollection;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleCollections;
import ac.grim.grimac.shaded.fastutil.doubles.DoubleSets;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

public final class Object2DoubleMaps {
   public static final Object2DoubleMaps.EmptyMap EMPTY_MAP = new Object2DoubleMaps.EmptyMap();

   private Object2DoubleMaps() {
   }

   public static <K> ObjectIterator<Object2DoubleMap.Entry<K>> fastIterator(Object2DoubleMap<K> map) {
      ObjectSet<Object2DoubleMap.Entry<K>> entries = map.object2DoubleEntrySet();
      return entries instanceof Object2DoubleMap.FastEntrySet ? ((Object2DoubleMap.FastEntrySet)entries).fastIterator() : entries.iterator();
   }

   public static <K> void fastForEach(Object2DoubleMap<K> map, Consumer<? super Object2DoubleMap.Entry<K>> consumer) {
      ObjectSet<Object2DoubleMap.Entry<K>> entries = map.object2DoubleEntrySet();
      if (entries instanceof Object2DoubleMap.FastEntrySet) {
         ((Object2DoubleMap.FastEntrySet)entries).fastForEach(consumer);
      } else {
         entries.forEach(consumer);
      }

   }

   public static <K> ObjectIterable<Object2DoubleMap.Entry<K>> fastIterable(Object2DoubleMap<K> map) {
      final ObjectSet<Object2DoubleMap.Entry<K>> entries = map.object2DoubleEntrySet();
      return (ObjectIterable)(entries instanceof Object2DoubleMap.FastEntrySet ? new ObjectIterable<Object2DoubleMap.Entry<K>>() {
         public ObjectIterator<Object2DoubleMap.Entry<K>> iterator() {
            return ((Object2DoubleMap.FastEntrySet)entries).fastIterator();
         }

         public ObjectSpliterator<Object2DoubleMap.Entry<K>> spliterator() {
            return entries.spliterator();
         }

         public void forEach(Consumer<? super Object2DoubleMap.Entry<K>> consumer) {
            ((Object2DoubleMap.FastEntrySet)entries).fastForEach(consumer);
         }
      } : entries);
   }

   public static <K> Object2DoubleMap<K> emptyMap() {
      return EMPTY_MAP;
   }

   public static <K> Object2DoubleMap<K> singleton(K key, double value) {
      return new Object2DoubleMaps.Singleton(key, value);
   }

   public static <K> Object2DoubleMap<K> singleton(K key, Double value) {
      return new Object2DoubleMaps.Singleton(key, value);
   }

   public static <K> Object2DoubleMap<K> synchronize(Object2DoubleMap<K> m) {
      return new Object2DoubleMaps.SynchronizedMap(m);
   }

   public static <K> Object2DoubleMap<K> synchronize(Object2DoubleMap<K> m, Object sync) {
      return new Object2DoubleMaps.SynchronizedMap(m, sync);
   }

   public static <K> Object2DoubleMap<K> unmodifiable(Object2DoubleMap<? extends K> m) {
      return new Object2DoubleMaps.UnmodifiableMap(m);
   }

   public static class EmptyMap<K> extends Object2DoubleFunctions.EmptyFunction<K> implements Object2DoubleMap<K>, Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;

      protected EmptyMap() {
      }

      public boolean containsValue(double v) {
         return false;
      }

      /** @deprecated */
      @Deprecated
      public Double getOrDefault(Object key, Double defaultValue) {
         return defaultValue;
      }

      public double getOrDefault(Object key, double defaultValue) {
         return defaultValue;
      }

      /** @deprecated */
      @Deprecated
      public boolean containsValue(Object ov) {
         return false;
      }

      public void putAll(Map<? extends K, ? extends Double> m) {
         throw new UnsupportedOperationException();
      }

      public ObjectSet<Object2DoubleMap.Entry<K>> object2DoubleEntrySet() {
         return ObjectSets.EMPTY_SET;
      }

      public ObjectSet<K> keySet() {
         return ObjectSets.EMPTY_SET;
      }

      public DoubleCollection values() {
         return DoubleSets.EMPTY_SET;
      }

      public void forEach(BiConsumer<? super K, ? super Double> consumer) {
      }

      public Object clone() {
         return Object2DoubleMaps.EMPTY_MAP;
      }

      public boolean isEmpty() {
         return true;
      }

      public int hashCode() {
         return 0;
      }

      public boolean equals(Object o) {
         return !(o instanceof Map) ? false : ((Map)o).isEmpty();
      }

      public String toString() {
         return "{}";
      }
   }

   public static class Singleton<K> extends Object2DoubleFunctions.Singleton<K> implements Object2DoubleMap<K>, Serializable, Cloneable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected transient ObjectSet<Object2DoubleMap.Entry<K>> entries;
      protected transient ObjectSet<K> keys;
      protected transient DoubleCollection values;

      protected Singleton(K key, double value) {
         super(key, value);
      }

      public boolean containsValue(double v) {
         return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(v);
      }

      /** @deprecated */
      @Deprecated
      public boolean containsValue(Object ov) {
         return Double.doubleToLongBits((Double)ov) == Double.doubleToLongBits(this.value);
      }

      public void putAll(Map<? extends K, ? extends Double> m) {
         throw new UnsupportedOperationException();
      }

      public ObjectSet<Object2DoubleMap.Entry<K>> object2DoubleEntrySet() {
         if (this.entries == null) {
            this.entries = ObjectSets.singleton(new AbstractObject2DoubleMap.BasicEntry(this.key, this.value));
         }

         return this.entries;
      }

      /** @deprecated */
      @Deprecated
      public ObjectSet<Entry<K, Double>> entrySet() {
         return this.object2DoubleEntrySet();
      }

      public ObjectSet<K> keySet() {
         if (this.keys == null) {
            this.keys = ObjectSets.singleton(this.key);
         }

         return this.keys;
      }

      public DoubleCollection values() {
         if (this.values == null) {
            this.values = DoubleSets.singleton(this.value);
         }

         return this.values;
      }

      public boolean isEmpty() {
         return false;
      }

      public int hashCode() {
         return (this.key == null ? 0 : this.key.hashCode()) ^ HashCommon.double2int(this.value);
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else if (!(o instanceof Map)) {
            return false;
         } else {
            Map<?, ?> m = (Map)o;
            return m.size() != 1 ? false : ((Entry)m.entrySet().iterator().next()).equals(this.entrySet().iterator().next());
         }
      }

      public String toString() {
         return "{" + this.key + "=>" + this.value + "}";
      }
   }

   public static class SynchronizedMap<K> extends Object2DoubleFunctions.SynchronizedFunction<K> implements Object2DoubleMap<K>, Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Object2DoubleMap<K> map;
      protected transient ObjectSet<Object2DoubleMap.Entry<K>> entries;
      protected transient ObjectSet<K> keys;
      protected transient DoubleCollection values;

      protected SynchronizedMap(Object2DoubleMap<K> m, Object sync) {
         super(m, sync);
         this.map = m;
      }

      protected SynchronizedMap(Object2DoubleMap<K> m) {
         super(m);
         this.map = m;
      }

      public boolean containsValue(double v) {
         synchronized(this.sync) {
            return this.map.containsValue(v);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean containsValue(Object ov) {
         synchronized(this.sync) {
            return this.map.containsValue(ov);
         }
      }

      public void putAll(Map<? extends K, ? extends Double> m) {
         synchronized(this.sync) {
            this.map.putAll(m);
         }
      }

      public ObjectSet<Object2DoubleMap.Entry<K>> object2DoubleEntrySet() {
         synchronized(this.sync) {
            if (this.entries == null) {
               this.entries = ObjectSets.synchronize(this.map.object2DoubleEntrySet(), this.sync);
            }

            return this.entries;
         }
      }

      /** @deprecated */
      @Deprecated
      public ObjectSet<Entry<K, Double>> entrySet() {
         return this.object2DoubleEntrySet();
      }

      public ObjectSet<K> keySet() {
         synchronized(this.sync) {
            if (this.keys == null) {
               this.keys = ObjectSets.synchronize(this.map.keySet(), this.sync);
            }

            return this.keys;
         }
      }

      public DoubleCollection values() {
         synchronized(this.sync) {
            if (this.values == null) {
               this.values = DoubleCollections.synchronize(this.map.values(), this.sync);
            }

            return this.values;
         }
      }

      public boolean isEmpty() {
         synchronized(this.sync) {
            return this.map.isEmpty();
         }
      }

      public int hashCode() {
         synchronized(this.sync) {
            return this.map.hashCode();
         }
      }

      public boolean equals(Object o) {
         if (o == this) {
            return true;
         } else {
            synchronized(this.sync) {
               return this.map.equals(o);
            }
         }
      }

      private void writeObject(ObjectOutputStream s) throws IOException {
         synchronized(this.sync) {
            s.defaultWriteObject();
         }
      }

      public double getOrDefault(Object key, double defaultValue) {
         synchronized(this.sync) {
            return this.map.getOrDefault(key, defaultValue);
         }
      }

      public void forEach(BiConsumer<? super K, ? super Double> action) {
         synchronized(this.sync) {
            this.map.forEach(action);
         }
      }

      public void replaceAll(BiFunction<? super K, ? super Double, ? extends Double> function) {
         synchronized(this.sync) {
            this.map.replaceAll(function);
         }
      }

      public double putIfAbsent(K key, double value) {
         synchronized(this.sync) {
            return this.map.putIfAbsent(key, value);
         }
      }

      public boolean remove(Object key, double value) {
         synchronized(this.sync) {
            return this.map.remove(key, value);
         }
      }

      public double replace(K key, double value) {
         synchronized(this.sync) {
            return this.map.replace(key, value);
         }
      }

      public boolean replace(K key, double oldValue, double newValue) {
         synchronized(this.sync) {
            return this.map.replace(key, oldValue, newValue);
         }
      }

      public double computeIfAbsent(K key, ToDoubleFunction<? super K> mappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfAbsent(key, mappingFunction);
         }
      }

      public double computeIfAbsent(K key, Object2DoubleFunction<? super K> mappingFunction) {
         synchronized(this.sync) {
            return this.map.computeIfAbsent(key, mappingFunction);
         }
      }

      public double computeDoubleIfPresent(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
         synchronized(this.sync) {
            return this.map.computeDoubleIfPresent(key, remappingFunction);
         }
      }

      public double computeDouble(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
         synchronized(this.sync) {
            return this.map.computeDouble(key, remappingFunction);
         }
      }

      public double merge(K key, double value, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
         synchronized(this.sync) {
            return this.map.merge(key, value, remappingFunction);
         }
      }

      /** @deprecated */
      @Deprecated
      public Double getOrDefault(Object key, Double defaultValue) {
         synchronized(this.sync) {
            return this.map.getOrDefault(key, defaultValue);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean remove(Object key, Object value) {
         synchronized(this.sync) {
            return this.map.remove(key, value);
         }
      }

      /** @deprecated */
      @Deprecated
      public Double replace(K key, Double value) {
         synchronized(this.sync) {
            return this.map.replace(key, value);
         }
      }

      /** @deprecated */
      @Deprecated
      public boolean replace(K key, Double oldValue, Double newValue) {
         synchronized(this.sync) {
            return this.map.replace(key, oldValue, newValue);
         }
      }

      /** @deprecated */
      @Deprecated
      public Double putIfAbsent(K key, Double value) {
         synchronized(this.sync) {
            return this.map.putIfAbsent(key, value);
         }
      }

      public Double computeIfAbsent(K key, Function<? super K, ? extends Double> mappingFunction) {
         synchronized(this.sync) {
            return (Double)this.map.computeIfAbsent(key, (Function)mappingFunction);
         }
      }

      public Double computeIfPresent(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
         synchronized(this.sync) {
            return (Double)this.map.computeIfPresent(key, remappingFunction);
         }
      }

      public Double compute(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
         synchronized(this.sync) {
            return (Double)this.map.compute(key, remappingFunction);
         }
      }

      /** @deprecated */
      @Deprecated
      public Double merge(K key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
         synchronized(this.sync) {
            return this.map.merge(key, value, remappingFunction);
         }
      }
   }

   public static class UnmodifiableMap<K> extends Object2DoubleFunctions.UnmodifiableFunction<K> implements Object2DoubleMap<K>, Serializable {
      private static final long serialVersionUID = -7046029254386353129L;
      protected final Object2DoubleMap<? extends K> map;
      protected transient ObjectSet<Object2DoubleMap.Entry<K>> entries;
      protected transient ObjectSet<K> keys;
      protected transient DoubleCollection values;

      protected UnmodifiableMap(Object2DoubleMap<? extends K> m) {
         super(m);
         this.map = m;
      }

      public boolean containsValue(double v) {
         return this.map.containsValue(v);
      }

      /** @deprecated */
      @Deprecated
      public boolean containsValue(Object ov) {
         return this.map.containsValue(ov);
      }

      public void putAll(Map<? extends K, ? extends Double> m) {
         throw new UnsupportedOperationException();
      }

      public ObjectSet<Object2DoubleMap.Entry<K>> object2DoubleEntrySet() {
         if (this.entries == null) {
            this.entries = ObjectSets.unmodifiable(this.map.object2DoubleEntrySet());
         }

         return this.entries;
      }

      /** @deprecated */
      @Deprecated
      public ObjectSet<Entry<K, Double>> entrySet() {
         return this.object2DoubleEntrySet();
      }

      public ObjectSet<K> keySet() {
         if (this.keys == null) {
            this.keys = ObjectSets.unmodifiable(this.map.keySet());
         }

         return this.keys;
      }

      public DoubleCollection values() {
         if (this.values == null) {
            this.values = DoubleCollections.unmodifiable(this.map.values());
         }

         return this.values;
      }

      public boolean isEmpty() {
         return this.map.isEmpty();
      }

      public int hashCode() {
         return this.map.hashCode();
      }

      public boolean equals(Object o) {
         return o == this ? true : this.map.equals(o);
      }

      public double getOrDefault(Object key, double defaultValue) {
         return this.map.getOrDefault(key, defaultValue);
      }

      public void forEach(BiConsumer<? super K, ? super Double> action) {
         this.map.forEach(action);
      }

      public void replaceAll(BiFunction<? super K, ? super Double, ? extends Double> function) {
         throw new UnsupportedOperationException();
      }

      public double putIfAbsent(K key, double value) {
         throw new UnsupportedOperationException();
      }

      public boolean remove(Object key, double value) {
         throw new UnsupportedOperationException();
      }

      public double replace(K key, double value) {
         throw new UnsupportedOperationException();
      }

      public boolean replace(K key, double oldValue, double newValue) {
         throw new UnsupportedOperationException();
      }

      public double computeIfAbsent(K key, ToDoubleFunction<? super K> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      public double computeIfAbsent(K key, Object2DoubleFunction<? super K> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      public double computeDoubleIfPresent(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      public double computeDouble(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      public double merge(K key, double value, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Double getOrDefault(Object key, Double defaultValue) {
         return this.map.getOrDefault(key, defaultValue);
      }

      /** @deprecated */
      @Deprecated
      public boolean remove(Object key, Object value) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Double replace(K key, Double value) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public boolean replace(K key, Double oldValue, Double newValue) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Double putIfAbsent(K key, Double value) {
         throw new UnsupportedOperationException();
      }

      public Double computeIfAbsent(K key, Function<? super K, ? extends Double> mappingFunction) {
         throw new UnsupportedOperationException();
      }

      public Double computeIfPresent(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      public Double compute(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
         throw new UnsupportedOperationException();
      }

      /** @deprecated */
      @Deprecated
      public Double merge(K key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
         throw new UnsupportedOperationException();
      }
   }
}
