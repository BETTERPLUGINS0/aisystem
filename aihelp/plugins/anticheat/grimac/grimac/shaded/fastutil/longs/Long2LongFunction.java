package ac.grim.grimac.shaded.fastutil.longs;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2LongFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2LongFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2LongFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2LongFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2LongFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2LongFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2LongFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2LongFunction;

@FunctionalInterface
public interface Long2LongFunction extends Function<Long, Long>, java.util.function.LongUnaryOperator {
   default long applyAsLong(long operand) {
      return this.get(operand);
   }

   default long put(long key, long value) {
      throw new UnsupportedOperationException();
   }

   long get(long var1);

   default long getOrDefault(long key, long defaultValue) {
      long v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default long remove(long key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Long put(Long key, Long value) {
      long k = key;
      boolean containsKey = this.containsKey(k);
      long v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Long get(Object key) {
      if (key == null) {
         return null;
      } else {
         long k = (Long)key;
         long v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Long getOrDefault(Object key, Long defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         long k = (Long)key;
         long v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Long remove(Object key) {
      if (key == null) {
         return null;
      } else {
         long k = (Long)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(long key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Long)key);
   }

   default void defaultReturnValue(long rv) {
      throw new UnsupportedOperationException();
   }

   default long defaultReturnValue() {
      return 0L;
   }

   static Long2LongFunction identity() {
      return (k) -> {
         return k;
      };
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Long> compose(java.util.function.Function<? super T, ? extends Long> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Long, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Long2ByteFunction andThenByte(Long2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2LongFunction composeByte(Byte2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2ShortFunction andThenShort(Long2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2LongFunction composeShort(Short2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2IntFunction andThenInt(Long2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2LongFunction composeInt(Int2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2LongFunction andThenLong(Long2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2LongFunction composeLong(Long2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2CharFunction andThenChar(Long2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2LongFunction composeChar(Char2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2FloatFunction andThenFloat(Long2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2LongFunction composeFloat(Float2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Long2DoubleFunction andThenDouble(Long2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2LongFunction composeDouble(Double2LongFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Long2ObjectFunction<T> andThenObject(Long2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2LongFunction<T> composeObject(Object2LongFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getLong(k));
      };
   }

   default <T> Long2ReferenceFunction<T> andThenReference(Long2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2LongFunction<T> composeReference(Reference2LongFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getLong(k));
      };
   }
}
