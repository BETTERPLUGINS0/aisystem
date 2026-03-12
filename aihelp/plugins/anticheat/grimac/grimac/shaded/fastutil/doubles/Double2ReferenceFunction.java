package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2ByteFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2CharFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2FloatFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2IntFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2LongFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2ShortFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.DoubleFunction;

@FunctionalInterface
public interface Double2ReferenceFunction<V> extends Function<Double, V>, DoubleFunction<V> {
   default V apply(double operand) {
      return this.get(operand);
   }

   default V put(double key, V value) {
      throw new UnsupportedOperationException();
   }

   V get(double var1);

   default V getOrDefault(double key, V defaultValue) {
      Object v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default V remove(double key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default V put(Double key, V value) {
      double k = key;
      boolean containsKey = this.containsKey(k);
      V v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default V get(Object key) {
      if (key == null) {
         return null;
      } else {
         double k = (Double)key;
         Object v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default V getOrDefault(Object key, V defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         double k = (Double)key;
         V v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default V remove(Object key) {
      if (key == null) {
         return null;
      } else {
         double k = (Double)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(double key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Double)key);
   }

   default void defaultReturnValue(V rv) {
      throw new UnsupportedOperationException();
   }

   default V defaultReturnValue() {
      return null;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, V> compose(java.util.function.Function<? super T, ? extends Double> before) {
      return Function.super.compose(before);
   }

   default Double2ByteFunction andThenByte(Reference2ByteFunction<V> after) {
      return (k) -> {
         return after.getByte(this.get(k));
      };
   }

   default Byte2ReferenceFunction<V> composeByte(Byte2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2ShortFunction andThenShort(Reference2ShortFunction<V> after) {
      return (k) -> {
         return after.getShort(this.get(k));
      };
   }

   default Short2ReferenceFunction<V> composeShort(Short2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2IntFunction andThenInt(Reference2IntFunction<V> after) {
      return (k) -> {
         return after.getInt(this.get(k));
      };
   }

   default Int2ReferenceFunction<V> composeInt(Int2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2LongFunction andThenLong(Reference2LongFunction<V> after) {
      return (k) -> {
         return after.getLong(this.get(k));
      };
   }

   default Long2ReferenceFunction<V> composeLong(Long2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2CharFunction andThenChar(Reference2CharFunction<V> after) {
      return (k) -> {
         return after.getChar(this.get(k));
      };
   }

   default Char2ReferenceFunction<V> composeChar(Char2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2FloatFunction andThenFloat(Reference2FloatFunction<V> after) {
      return (k) -> {
         return after.getFloat(this.get(k));
      };
   }

   default Float2ReferenceFunction<V> composeFloat(Float2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2DoubleFunction andThenDouble(Reference2DoubleFunction<V> after) {
      return (k) -> {
         return after.getDouble(this.get(k));
      };
   }

   default Double2ReferenceFunction<V> composeDouble(Double2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Double2ObjectFunction<T> andThenObject(Reference2ObjectFunction<? super V, ? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2ReferenceFunction<T, V> composeObject(Object2DoubleFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getDouble(k));
      };
   }

   default <T> Double2ReferenceFunction<T> andThenReference(Reference2ReferenceFunction<? super V, ? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2ReferenceFunction<T, V> composeReference(Reference2DoubleFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getDouble(k));
      };
   }
}
