package ac.grim.grimac.shaded.fastutil.floats;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.SafeMath;
import ac.grim.grimac.shaded.fastutil.booleans.Boolean2ByteFunction;
import ac.grim.grimac.shaded.fastutil.booleans.Boolean2CharFunction;
import ac.grim.grimac.shaded.fastutil.booleans.Boolean2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.booleans.Boolean2FloatFunction;
import ac.grim.grimac.shaded.fastutil.booleans.Boolean2IntFunction;
import ac.grim.grimac.shaded.fastutil.booleans.Boolean2LongFunction;
import ac.grim.grimac.shaded.fastutil.booleans.Boolean2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.booleans.Boolean2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.booleans.Boolean2ShortFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2FloatFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2FloatFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2FloatFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2FloatFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2FloatFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2FloatFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2FloatFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2FloatFunction;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface Float2BooleanFunction extends Function<Float, Boolean>, DoublePredicate {
   /** @deprecated */
   @Deprecated
   default boolean test(double operand) {
      return this.get(SafeMath.safeDoubleToFloat(operand));
   }

   default boolean put(float key, boolean value) {
      throw new UnsupportedOperationException();
   }

   boolean get(float var1);

   default boolean getOrDefault(float key, boolean defaultValue) {
      boolean v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default boolean remove(float key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Boolean put(Float key, Boolean value) {
      float k = key;
      boolean containsKey = this.containsKey(k);
      boolean v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Boolean get(Object key) {
      if (key == null) {
         return null;
      } else {
         float k = (Float)key;
         boolean v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Boolean getOrDefault(Object key, Boolean defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         float k = (Float)key;
         boolean v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Boolean remove(Object key) {
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

   default void defaultReturnValue(boolean rv) {
      throw new UnsupportedOperationException();
   }

   default boolean defaultReturnValue() {
      return false;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Boolean> compose(java.util.function.Function<? super T, ? extends Float> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Float, T> andThen(java.util.function.Function<? super Boolean, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Float2ByteFunction andThenByte(Boolean2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2BooleanFunction composeByte(Byte2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2ShortFunction andThenShort(Boolean2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2BooleanFunction composeShort(Short2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2IntFunction andThenInt(Boolean2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2BooleanFunction composeInt(Int2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2LongFunction andThenLong(Boolean2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2BooleanFunction composeLong(Long2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2CharFunction andThenChar(Boolean2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2BooleanFunction composeChar(Char2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2FloatFunction andThenFloat(Boolean2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2BooleanFunction composeFloat(Float2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Float2DoubleFunction andThenDouble(Boolean2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2BooleanFunction composeDouble(Double2FloatFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Float2ObjectFunction<T> andThenObject(Boolean2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2BooleanFunction<T> composeObject(Object2FloatFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getFloat(k));
      };
   }

   default <T> Float2ReferenceFunction<T> andThenReference(Boolean2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2BooleanFunction<T> composeReference(Reference2FloatFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getFloat(k));
      };
   }
}
