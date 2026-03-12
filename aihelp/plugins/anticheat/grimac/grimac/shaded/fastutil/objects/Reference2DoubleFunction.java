package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ByteFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2CharFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2FloatFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2IntFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2LongFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ShortFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface Reference2DoubleFunction<K> extends Function<K, Double>, ToDoubleFunction<K> {
   default double applyAsDouble(K operand) {
      return this.getDouble(operand);
   }

   default double put(K key, double value) {
      throw new UnsupportedOperationException();
   }

   double getDouble(Object var1);

   default double getOrDefault(Object key, double defaultValue) {
      double v;
      return (v = this.getDouble(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default double removeDouble(Object key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Double put(K key, Double value) {
      boolean containsKey = this.containsKey(key);
      double v = this.put(key, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Double get(Object key) {
      double v;
      return (v = this.getDouble(key)) == this.defaultReturnValue() && !this.containsKey(key) ? null : v;
   }

   /** @deprecated */
   @Deprecated
   default Double getOrDefault(Object key, Double defaultValue) {
      double v = this.getDouble(key);
      return v == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   /** @deprecated */
   @Deprecated
   default Double remove(Object key) {
      return this.containsKey(key) ? this.removeDouble(key) : null;
   }

   default void defaultReturnValue(double rv) {
      throw new UnsupportedOperationException();
   }

   default double defaultReturnValue() {
      return 0.0D;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Double, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Reference2ByteFunction<K> andThenByte(Double2ByteFunction after) {
      return (k) -> {
         return after.get(this.getDouble(k));
      };
   }

   default Byte2DoubleFunction composeByte(Byte2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getDouble(before.get(k));
      };
   }

   default Reference2ShortFunction<K> andThenShort(Double2ShortFunction after) {
      return (k) -> {
         return after.get(this.getDouble(k));
      };
   }

   default Short2DoubleFunction composeShort(Short2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getDouble(before.get(k));
      };
   }

   default Reference2IntFunction<K> andThenInt(Double2IntFunction after) {
      return (k) -> {
         return after.get(this.getDouble(k));
      };
   }

   default Int2DoubleFunction composeInt(Int2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getDouble(before.get(k));
      };
   }

   default Reference2LongFunction<K> andThenLong(Double2LongFunction after) {
      return (k) -> {
         return after.get(this.getDouble(k));
      };
   }

   default Long2DoubleFunction composeLong(Long2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getDouble(before.get(k));
      };
   }

   default Reference2CharFunction<K> andThenChar(Double2CharFunction after) {
      return (k) -> {
         return after.get(this.getDouble(k));
      };
   }

   default Char2DoubleFunction composeChar(Char2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getDouble(before.get(k));
      };
   }

   default Reference2FloatFunction<K> andThenFloat(Double2FloatFunction after) {
      return (k) -> {
         return after.get(this.getDouble(k));
      };
   }

   default Float2DoubleFunction composeFloat(Float2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getDouble(before.get(k));
      };
   }

   default Reference2DoubleFunction<K> andThenDouble(Double2DoubleFunction after) {
      return (k) -> {
         return after.get(this.getDouble(k));
      };
   }

   default Double2DoubleFunction composeDouble(Double2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getDouble(before.get(k));
      };
   }

   default <T> Reference2ObjectFunction<K, T> andThenObject(Double2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getDouble(k));
      };
   }

   default <T> Object2DoubleFunction<T> composeObject(Object2ReferenceFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getDouble(before.get(k));
      };
   }

   default <T> Reference2ReferenceFunction<K, T> andThenReference(Double2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getDouble(k));
      };
   }

   default <T> Reference2DoubleFunction<T> composeReference(Reference2ReferenceFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getDouble(before.get(k));
      };
   }
}
