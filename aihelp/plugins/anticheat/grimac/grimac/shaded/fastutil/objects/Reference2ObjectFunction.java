package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ReferenceFunction;

@FunctionalInterface
public interface Reference2ObjectFunction<K, V> extends Function<K, V> {
   default V put(K key, V value) {
      throw new UnsupportedOperationException();
   }

   V get(Object var1);

   default V getOrDefault(Object key, V defaultValue) {
      Object v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default V remove(Object key) {
      throw new UnsupportedOperationException();
   }

   default void defaultReturnValue(V rv) {
      throw new UnsupportedOperationException();
   }

   default V defaultReturnValue() {
      return null;
   }

   default Reference2ByteFunction<K> andThenByte(Object2ByteFunction<V> after) {
      return (k) -> {
         return after.getByte(this.get(k));
      };
   }

   default Byte2ObjectFunction<V> composeByte(Byte2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2ShortFunction<K> andThenShort(Object2ShortFunction<V> after) {
      return (k) -> {
         return after.getShort(this.get(k));
      };
   }

   default Short2ObjectFunction<V> composeShort(Short2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2IntFunction<K> andThenInt(Object2IntFunction<V> after) {
      return (k) -> {
         return after.getInt(this.get(k));
      };
   }

   default Int2ObjectFunction<V> composeInt(Int2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2LongFunction<K> andThenLong(Object2LongFunction<V> after) {
      return (k) -> {
         return after.getLong(this.get(k));
      };
   }

   default Long2ObjectFunction<V> composeLong(Long2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2CharFunction<K> andThenChar(Object2CharFunction<V> after) {
      return (k) -> {
         return after.getChar(this.get(k));
      };
   }

   default Char2ObjectFunction<V> composeChar(Char2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2FloatFunction<K> andThenFloat(Object2FloatFunction<V> after) {
      return (k) -> {
         return after.getFloat(this.get(k));
      };
   }

   default Float2ObjectFunction<V> composeFloat(Float2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Reference2DoubleFunction<K> andThenDouble(Object2DoubleFunction<V> after) {
      return (k) -> {
         return after.getDouble(this.get(k));
      };
   }

   default Double2ObjectFunction<V> composeDouble(Double2ReferenceFunction<K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Reference2ObjectFunction<K, T> andThenObject(Object2ObjectFunction<? super V, ? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2ObjectFunction<T, V> composeObject(Object2ReferenceFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Reference2ReferenceFunction<K, T> andThenReference(Object2ReferenceFunction<? super V, ? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2ObjectFunction<T, V> composeReference(Reference2ReferenceFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }
}
