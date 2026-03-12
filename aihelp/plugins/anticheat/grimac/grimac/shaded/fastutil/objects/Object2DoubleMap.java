package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.doubles.DoubleCollection;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.ToDoubleFunction;

public interface Object2DoubleMap<K> extends Object2DoubleFunction<K>, Map<K, Double> {
   int size();

   default void clear() {
      throw new UnsupportedOperationException();
   }

   void defaultReturnValue(double var1);

   double defaultReturnValue();

   ObjectSet<Object2DoubleMap.Entry<K>> object2DoubleEntrySet();

   /** @deprecated */
   @Deprecated
   default ObjectSet<java.util.Map.Entry<K, Double>> entrySet() {
      return this.object2DoubleEntrySet();
   }

   /** @deprecated */
   @Deprecated
   default Double put(K key, Double value) {
      return Object2DoubleFunction.super.put(key, value);
   }

   /** @deprecated */
   @Deprecated
   default Double get(Object key) {
      return Object2DoubleFunction.super.get(key);
   }

   /** @deprecated */
   @Deprecated
   default Double remove(Object key) {
      return Object2DoubleFunction.super.remove(key);
   }

   ObjectSet<K> keySet();

   DoubleCollection values();

   boolean containsKey(Object var1);

   boolean containsValue(double var1);

   /** @deprecated */
   @Deprecated
   default boolean containsValue(Object value) {
      return value == null ? false : this.containsValue((Double)value);
   }

   default void forEach(BiConsumer<? super K, ? super Double> consumer) {
      ObjectSet<Object2DoubleMap.Entry<K>> entrySet = this.object2DoubleEntrySet();
      Consumer<Object2DoubleMap.Entry<K>> wrappingConsumer = (entry) -> {
         consumer.accept(entry.getKey(), entry.getDoubleValue());
      };
      if (entrySet instanceof Object2DoubleMap.FastEntrySet) {
         ((Object2DoubleMap.FastEntrySet)entrySet).fastForEach(wrappingConsumer);
      } else {
         entrySet.forEach(wrappingConsumer);
      }

   }

   default double getOrDefault(Object key, double defaultValue) {
      double v;
      return (v = this.getDouble(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   /** @deprecated */
   @Deprecated
   default Double getOrDefault(Object key, Double defaultValue) {
      return (Double)super.getOrDefault(key, defaultValue);
   }

   default double putIfAbsent(K key, double value) {
      double v = this.getDouble(key);
      double drv = this.defaultReturnValue();
      if (v == drv && !this.containsKey(key)) {
         this.put(key, value);
         return drv;
      } else {
         return v;
      }
   }

   default boolean remove(Object key, double value) {
      double curValue = this.getDouble(key);
      if (Double.doubleToLongBits(curValue) == Double.doubleToLongBits(value) && (curValue != this.defaultReturnValue() || this.containsKey(key))) {
         this.removeDouble(key);
         return true;
      } else {
         return false;
      }
   }

   default boolean replace(K key, double oldValue, double newValue) {
      double curValue = this.getDouble(key);
      if (Double.doubleToLongBits(curValue) == Double.doubleToLongBits(oldValue) && (curValue != this.defaultReturnValue() || this.containsKey(key))) {
         this.put(key, newValue);
         return true;
      } else {
         return false;
      }
   }

   default double replace(K key, double value) {
      return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
   }

   default double computeIfAbsent(K key, ToDoubleFunction<? super K> mappingFunction) {
      Objects.requireNonNull(mappingFunction);
      double v = this.getDouble(key);
      if (v == this.defaultReturnValue() && !this.containsKey(key)) {
         double newValue = mappingFunction.applyAsDouble(key);
         this.put(key, newValue);
         return newValue;
      } else {
         return v;
      }
   }

   /** @deprecated */
   @Deprecated
   default double computeDoubleIfAbsent(K key, ToDoubleFunction<? super K> mappingFunction) {
      return this.computeIfAbsent(key, mappingFunction);
   }

   default double computeIfAbsent(K key, Object2DoubleFunction<? super K> mappingFunction) {
      Objects.requireNonNull(mappingFunction);
      double v = this.getDouble(key);
      double drv = this.defaultReturnValue();
      if (v == drv && !this.containsKey(key)) {
         if (!mappingFunction.containsKey(key)) {
            return drv;
         } else {
            double newValue = mappingFunction.getDouble(key);
            this.put(key, newValue);
            return newValue;
         }
      } else {
         return v;
      }
   }

   /** @deprecated */
   @Deprecated
   default double computeDoubleIfAbsentPartial(K key, Object2DoubleFunction<? super K> mappingFunction) {
      return this.computeIfAbsent(key, mappingFunction);
   }

   default double computeDoubleIfPresent(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      double oldValue = this.getDouble(key);
      double drv = this.defaultReturnValue();
      if (oldValue == drv && !this.containsKey(key)) {
         return drv;
      } else {
         Double newValue = (Double)remappingFunction.apply(key, oldValue);
         if (newValue == null) {
            this.removeDouble(key);
            return drv;
         } else {
            double newVal = newValue;
            this.put(key, newVal);
            return newVal;
         }
      }
   }

   default double computeDouble(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      double oldValue = this.getDouble(key);
      double drv = this.defaultReturnValue();
      boolean contained = oldValue != drv || this.containsKey(key);
      Double newValue = (Double)remappingFunction.apply(key, contained ? oldValue : null);
      if (newValue == null) {
         if (contained) {
            this.removeDouble(key);
         }

         return drv;
      } else {
         double newVal = newValue;
         this.put(key, newVal);
         return newVal;
      }
   }

   default double merge(K key, double value, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      double oldValue = this.getDouble(key);
      double drv = this.defaultReturnValue();
      double newValue;
      if (oldValue == drv && !this.containsKey(key)) {
         newValue = value;
      } else {
         Double mergedValue = (Double)remappingFunction.apply(oldValue, value);
         if (mergedValue == null) {
            this.removeDouble(key);
            return drv;
         }

         newValue = mergedValue;
      }

      this.put(key, newValue);
      return newValue;
   }

   default double mergeDouble(K key, double value, DoubleBinaryOperator remappingFunction) {
      Objects.requireNonNull(remappingFunction);
      double oldValue = this.getDouble(key);
      double drv = this.defaultReturnValue();
      double newValue = oldValue == drv && !this.containsKey(key) ? value : remappingFunction.applyAsDouble(oldValue, value);
      this.put(key, newValue);
      return newValue;
   }

   default double mergeDouble(K key, double value, ac.grim.grimac.shaded.fastutil.doubles.DoubleBinaryOperator remappingFunction) {
      return this.mergeDouble(key, value, (DoubleBinaryOperator)remappingFunction);
   }

   /** @deprecated */
   @Deprecated
   default double mergeDouble(K key, double value, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
      return this.merge(key, value, remappingFunction);
   }

   /** @deprecated */
   @Deprecated
   default Double putIfAbsent(K key, Double value) {
      return (Double)super.putIfAbsent(key, value);
   }

   /** @deprecated */
   @Deprecated
   default boolean remove(Object key, Object value) {
      return super.remove(key, value);
   }

   /** @deprecated */
   @Deprecated
   default boolean replace(K key, Double oldValue, Double newValue) {
      return super.replace(key, oldValue, newValue);
   }

   /** @deprecated */
   @Deprecated
   default Double replace(K key, Double value) {
      return (Double)super.replace(key, value);
   }

   /** @deprecated */
   @Deprecated
   default Double merge(K key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
      return (Double)super.merge(key, value, remappingFunction);
   }

   public interface FastEntrySet<K> extends ObjectSet<Object2DoubleMap.Entry<K>> {
      ObjectIterator<Object2DoubleMap.Entry<K>> fastIterator();

      default void fastForEach(Consumer<? super Object2DoubleMap.Entry<K>> consumer) {
         this.forEach(consumer);
      }
   }

   public interface Entry<K> extends java.util.Map.Entry<K, Double> {
      double getDoubleValue();

      double setValue(double var1);

      /** @deprecated */
      @Deprecated
      default Double getValue() {
         return this.getDoubleValue();
      }

      /** @deprecated */
      @Deprecated
      default Double setValue(Double value) {
         return this.setValue(value);
      }
   }
}
