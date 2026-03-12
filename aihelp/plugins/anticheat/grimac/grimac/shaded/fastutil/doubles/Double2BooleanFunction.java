package ac.grim.grimac.shaded.fastutil.doubles;

import ac.grim.grimac.shaded.fastutil.Function;
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
import ac.grim.grimac.shaded.fastutil.bytes.Byte2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2DoubleFunction;

@FunctionalInterface
public interface Double2BooleanFunction extends Function<Double, Boolean>, java.util.function.DoublePredicate {
   default boolean test(double operand) {
      return this.get(operand);
   }

   default boolean put(double key, boolean value) {
      throw new UnsupportedOperationException();
   }

   boolean get(double var1);

   default boolean getOrDefault(double key, boolean defaultValue) {
      boolean v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default boolean remove(double key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Boolean put(Double key, Boolean value) {
      double k = key;
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
         double k = (Double)key;
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
         double k = (Double)key;
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

   default void defaultReturnValue(boolean rv) {
      throw new UnsupportedOperationException();
   }

   default boolean defaultReturnValue() {
      return false;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Boolean> compose(java.util.function.Function<? super T, ? extends Double> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Double, T> andThen(java.util.function.Function<? super Boolean, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Double2ByteFunction andThenByte(Boolean2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2BooleanFunction composeByte(Byte2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2ShortFunction andThenShort(Boolean2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2BooleanFunction composeShort(Short2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2IntFunction andThenInt(Boolean2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2BooleanFunction composeInt(Int2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2LongFunction andThenLong(Boolean2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2BooleanFunction composeLong(Long2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2CharFunction andThenChar(Boolean2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2BooleanFunction composeChar(Char2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2FloatFunction andThenFloat(Boolean2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2BooleanFunction composeFloat(Float2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Double2DoubleFunction andThenDouble(Boolean2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2BooleanFunction composeDouble(Double2DoubleFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Double2ObjectFunction<T> andThenObject(Boolean2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2BooleanFunction<T> composeObject(Object2DoubleFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getDouble(k));
      };
   }

   default <T> Double2ReferenceFunction<T> andThenReference(Boolean2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2BooleanFunction<T> composeReference(Reference2DoubleFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getDouble(k));
      };
   }
}
