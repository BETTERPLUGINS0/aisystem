package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2FloatFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2FloatFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2FloatFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ByteFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2CharFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2FloatFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2IntFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2LongFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ShortFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2FloatFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2FloatFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2FloatFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ReferenceFunction;
import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface Reference2FloatFunction<K> extends Function<K, Float>, ToDoubleFunction<K> {
   default double applyAsDouble(K operand) {
      return (double)this.getFloat(operand);
   }

   default float put(K key, float value) {
      throw new UnsupportedOperationException();
   }

   float getFloat(Object var1);

   default float getOrDefault(Object key, float defaultValue) {
      float v;
      return (v = this.getFloat(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default float removeFloat(Object key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Float put(K key, Float value) {
      boolean containsKey = this.containsKey(key);
      float v = this.put(key, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Float get(Object key) {
      float v;
      return (v = this.getFloat(key)) == this.defaultReturnValue() && !this.containsKey(key) ? null : v;
   }

   /** @deprecated */
   @Deprecated
   default Float getOrDefault(Object key, Float defaultValue) {
      float v = this.getFloat(key);
      return v == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   /** @deprecated */
   @Deprecated
   default Float remove(Object key) {
      return this.containsKey(key) ? this.removeFloat(key) : null;
   }

   default void defaultReturnValue(float rv) {
      throw new UnsupportedOperationException();
   }

   default float defaultReturnValue() {
      return 0.0F;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Float, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Reference2ByteFunction<K> andThenByte(Float2ByteFunction after) {
      return (k) -> {
         return after.get(this.getFloat(k));
      };
   }

   default Byte2FloatFunction composeByte(Byte2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getFloat(before.get(k));
      };
   }

   default Reference2ShortFunction<K> andThenShort(Float2ShortFunction after) {
      return (k) -> {
         return after.get(this.getFloat(k));
      };
   }

   default Short2FloatFunction composeShort(Short2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getFloat(before.get(k));
      };
   }

   default Reference2IntFunction<K> andThenInt(Float2IntFunction after) {
      return (k) -> {
         return after.get(this.getFloat(k));
      };
   }

   default Int2FloatFunction composeInt(Int2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getFloat(before.get(k));
      };
   }

   default Reference2LongFunction<K> andThenLong(Float2LongFunction after) {
      return (k) -> {
         return after.get(this.getFloat(k));
      };
   }

   default Long2FloatFunction composeLong(Long2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getFloat(before.get(k));
      };
   }

   default Reference2CharFunction<K> andThenChar(Float2CharFunction after) {
      return (k) -> {
         return after.get(this.getFloat(k));
      };
   }

   default Char2FloatFunction composeChar(Char2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getFloat(before.get(k));
      };
   }

   default Reference2FloatFunction<K> andThenFloat(Float2FloatFunction after) {
      return (k) -> {
         return after.get(this.getFloat(k));
      };
   }

   default Float2FloatFunction composeFloat(Float2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getFloat(before.get(k));
      };
   }

   default Reference2DoubleFunction<K> andThenDouble(Float2DoubleFunction after) {
      return (k) -> {
         return after.get(this.getFloat(k));
      };
   }

   default Double2FloatFunction composeDouble(Double2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getFloat(before.get(k));
      };
   }

   default <T> Reference2ObjectFunction<K, T> andThenObject(Float2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getFloat(k));
      };
   }

   default <T> Object2FloatFunction<T> composeObject(Object2ReferenceFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getFloat(before.get(k));
      };
   }

   default <T> Reference2ReferenceFunction<K, T> andThenReference(Float2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getFloat(k));
      };
   }

   default <T> Reference2FloatFunction<T> composeReference(Reference2ReferenceFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getFloat(before.get(k));
      };
   }
}
