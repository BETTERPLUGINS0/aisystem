package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2LongFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2LongFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2LongFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2LongFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2LongFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ByteFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2CharFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2FloatFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2IntFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2LongFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ShortFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2LongFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ObjectFunction;
import java.util.function.ToLongFunction;

@FunctionalInterface
public interface Object2LongFunction<K> extends Function<K, Long>, ToLongFunction<K> {
   default long applyAsLong(K operand) {
      return this.getLong(operand);
   }

   default long put(K key, long value) {
      throw new UnsupportedOperationException();
   }

   long getLong(Object var1);

   default long getOrDefault(Object key, long defaultValue) {
      long v;
      return (v = this.getLong(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default long removeLong(Object key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Long put(K key, Long value) {
      boolean containsKey = this.containsKey(key);
      long v = this.put(key, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Long get(Object key) {
      long v;
      return (v = this.getLong(key)) == this.defaultReturnValue() && !this.containsKey(key) ? null : v;
   }

   /** @deprecated */
   @Deprecated
   default Long getOrDefault(Object key, Long defaultValue) {
      long v = this.getLong(key);
      return v == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   /** @deprecated */
   @Deprecated
   default Long remove(Object key) {
      return this.containsKey(key) ? this.removeLong(key) : null;
   }

   default void defaultReturnValue(long rv) {
      throw new UnsupportedOperationException();
   }

   default long defaultReturnValue() {
      return 0L;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Object2ByteFunction<K> andThenByte(Long2ByteFunction after) {
      return (k) -> {
         return after.get(this.getLong(k));
      };
   }

   default Byte2LongFunction composeByte(Byte2ObjectFunction<K> before) {
      return (k) -> {
         return this.getLong(before.get(k));
      };
   }

   default Object2ShortFunction<K> andThenShort(Long2ShortFunction after) {
      return (k) -> {
         return after.get(this.getLong(k));
      };
   }

   default Short2LongFunction composeShort(Short2ObjectFunction<K> before) {
      return (k) -> {
         return this.getLong(before.get(k));
      };
   }

   default Object2IntFunction<K> andThenInt(Long2IntFunction after) {
      return (k) -> {
         return after.get(this.getLong(k));
      };
   }

   default Int2LongFunction composeInt(Int2ObjectFunction<K> before) {
      return (k) -> {
         return this.getLong(before.get(k));
      };
   }

   default Object2LongFunction<K> andThenLong(Long2LongFunction after) {
      return (k) -> {
         return after.get(this.getLong(k));
      };
   }

   default Long2LongFunction composeLong(Long2ObjectFunction<K> before) {
      return (k) -> {
         return this.getLong(before.get(k));
      };
   }

   default Object2CharFunction<K> andThenChar(Long2CharFunction after) {
      return (k) -> {
         return after.get(this.getLong(k));
      };
   }

   default Char2LongFunction composeChar(Char2ObjectFunction<K> before) {
      return (k) -> {
         return this.getLong(before.get(k));
      };
   }

   default Object2FloatFunction<K> andThenFloat(Long2FloatFunction after) {
      return (k) -> {
         return after.get(this.getLong(k));
      };
   }

   default Float2LongFunction composeFloat(Float2ObjectFunction<K> before) {
      return (k) -> {
         return this.getLong(before.get(k));
      };
   }

   default Object2DoubleFunction<K> andThenDouble(Long2DoubleFunction after) {
      return (k) -> {
         return after.get(this.getLong(k));
      };
   }

   default Double2LongFunction composeDouble(Double2ObjectFunction<K> before) {
      return (k) -> {
         return this.getLong(before.get(k));
      };
   }

   default <T> Object2ObjectFunction<K, T> andThenObject(Long2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getLong(k));
      };
   }

   default <T> Object2LongFunction<T> composeObject(Object2ObjectFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getLong(before.get(k));
      };
   }

   default <T> Object2ReferenceFunction<K, T> andThenReference(Long2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getLong(k));
      };
   }

   default <T> Reference2LongFunction<T> composeReference(Reference2ObjectFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getLong(before.get(k));
      };
   }
}
