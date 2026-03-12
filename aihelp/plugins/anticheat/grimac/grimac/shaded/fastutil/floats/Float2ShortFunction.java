package ac.grim.grimac.shaded.fastutil.floats;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.SafeMath;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2FloatFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ShortFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2FloatFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ShortFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2FloatFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ShortFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2FloatFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ShortFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2FloatFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ShortFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2FloatFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2ShortFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2FloatFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2ShortFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ByteFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2CharFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2FloatFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2IntFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2LongFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ShortFunction;
import java.util.function.DoubleToIntFunction;

@FunctionalInterface
public interface Float2ShortFunction extends Function<Float, Short>, DoubleToIntFunction {
   /** @deprecated */
   @Deprecated
   default int applyAsInt(double operand) {
      return this.get(SafeMath.safeDoubleToFloat(operand));
   }

   default short put(float key, short value) {
      throw new UnsupportedOperationException();
   }

   short get(float var1);

   default short getOrDefault(float key, short defaultValue) {
      short v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default short remove(float key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Short put(Float key, Short value) {
      float k = key;
      boolean containsKey = this.containsKey(k);
      short v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Short get(Object key) {
      if (key == null) {
         return null;
      } else {
         float k = (Float)key;
         short v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Short getOrDefault(Object key, Short defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         float k = (Float)key;
         short v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Short remove(Object key) {
      if (key == null) {
         return null;
      } else {
         float k = (Float)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(float key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Float)key);
   }

   default void defaultReturnValue(short rv) {
      throw new UnsupportedOperationException();
   }

   default short defaultReturnValue() {
      return 0;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Short> compose(java.util.function.Function<? super T, ? extends Float> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Float, T> andThen(java.util.function.Function<? super Short, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Float2ByteFunction andThenByte(Short2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2ShortFunction composeByte(Byte2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2ShortFunction andThenShort(Short2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2ShortFunction composeShort(Short2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2IntFunction andThenInt(Short2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2ShortFunction composeInt(Int2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2LongFunction andThenLong(Short2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2ShortFunction composeLong(Long2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2CharFunction andThenChar(Short2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2ShortFunction composeChar(Char2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2FloatFunction andThenFloat(Short2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2ShortFunction composeFloat(Float2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2DoubleFunction andThenDouble(Short2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2ShortFunction composeDouble(Double2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Float2ObjectFunction<T> andThenObject(Short2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2ShortFunction<T> composeObject(Object2FloatFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getFloat(k));
      };
   }

   default <T> Float2ReferenceFunction<T> andThenReference(Short2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2ShortFunction<T> composeReference(Reference2FloatFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getFloat(k));
      };
   }
}
