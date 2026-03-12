package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ByteFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2CharFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2FloatFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2IntFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2LongFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ShortFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ByteFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ByteFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ByteFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ByteFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2ByteFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2ByteFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ByteFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2DoubleFunction;
import java.util.function.DoubleToIntFunction;

@FunctionalInterface
public interface Double2ByteFunction extends Function<Double, Byte>, DoubleToIntFunction {
   default int applyAsInt(double operand) {
      return this.get(operand);
   }

   default byte put(double key, byte value) {
      throw new UnsupportedOperationException();
   }

   byte get(double var1);

   default byte getOrDefault(double key, byte defaultValue) {
      byte v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default byte remove(double key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Byte put(Double key, Byte value) {
      double k = key;
      boolean containsKey = this.containsKey(k);
      byte v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Byte get(Object key) {
      if (key == null) {
         return null;
      } else {
         double k = (Double)key;
         byte v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Byte getOrDefault(Object key, Byte defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         double k = (Double)key;
         byte v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Byte remove(Object key) {
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

   default void defaultReturnValue(byte rv) {
      throw new UnsupportedOperationException();
   }

   default byte defaultReturnValue() {
      return 0;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Byte> compose(java.util.function.Function<? super T, ? extends Double> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Double, T> andThen(java.util.function.Function<? super Byte, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Double2ByteFunction andThenByte(Byte2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2ByteFunction composeByte(Byte2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2ShortFunction andThenShort(Byte2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2ByteFunction composeShort(Short2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2IntFunction andThenInt(Byte2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2ByteFunction composeInt(Int2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2LongFunction andThenLong(Byte2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2ByteFunction composeLong(Long2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2CharFunction andThenChar(Byte2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2ByteFunction composeChar(Char2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2FloatFunction andThenFloat(Byte2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2ByteFunction composeFloat(Float2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2DoubleFunction andThenDouble(Byte2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2ByteFunction composeDouble(Double2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Double2ObjectFunction<T> andThenObject(Byte2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2ByteFunction<T> composeObject(Object2DoubleFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getDouble(k));
      };
   }

   default <T> Double2ReferenceFunction<T> andThenReference(Byte2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2ByteFunction<T> composeReference(Reference2DoubleFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getDouble(k));
      };
   }
}
