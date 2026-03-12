package ac.grim.grimac.shaded.fastutil.shorts;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.SafeMath;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2LongFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ShortFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2LongFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ShortFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2LongFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ShortFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2LongFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ShortFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2LongFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ShortFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ByteFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2CharFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2FloatFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2IntFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2LongFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ShortFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2LongFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2ShortFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2LongFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2ShortFunction;
import java.util.function.IntToLongFunction;

@FunctionalInterface
public interface Short2LongFunction extends Function<Short, Long>, IntToLongFunction {
   /** @deprecated */
   @Deprecated
   default long applyAsLong(int operand) {
      return this.get(SafeMath.safeIntToShort(operand));
   }

   default long put(short key, long value) {
      throw new UnsupportedOperationException();
   }

   long get(short var1);

   default long getOrDefault(short key, long defaultValue) {
      long v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default long remove(short key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Long put(Short key, Long value) {
      short k = key;
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
         short k = (Short)key;
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
         short k = (Short)key;
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
         short k = (Short)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(short key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Short)key);
   }

   default void defaultReturnValue(long rv) {
      throw new UnsupportedOperationException();
   }

   default long defaultReturnValue() {
      return 0L;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Long> compose(java.util.function.Function<? super T, ? extends Short> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Short, T> andThen(java.util.function.Function<? super Long, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Short2ByteFunction andThenByte(Long2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2LongFunction composeByte(Byte2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2ShortFunction andThenShort(Long2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2LongFunction composeShort(Short2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2IntFunction andThenInt(Long2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2LongFunction composeInt(Int2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2LongFunction andThenLong(Long2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2LongFunction composeLong(Long2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2CharFunction andThenChar(Long2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2LongFunction composeChar(Char2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2FloatFunction andThenFloat(Long2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2LongFunction composeFloat(Float2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Short2DoubleFunction andThenDouble(Long2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2LongFunction composeDouble(Double2ShortFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Short2ObjectFunction<T> andThenObject(Long2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2LongFunction<T> composeObject(Object2ShortFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getShort(k));
      };
   }

   default <T> Short2ReferenceFunction<T> andThenReference(Long2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2LongFunction<T> composeReference(Reference2ShortFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getShort(k));
      };
   }
}
