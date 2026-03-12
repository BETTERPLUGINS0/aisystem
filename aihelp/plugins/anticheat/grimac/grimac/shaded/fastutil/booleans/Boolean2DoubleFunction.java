package ac.grim.grimac.shaded.fastutil.booleans;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ByteFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2CharFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2FloatFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2IntFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2LongFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ShortFunction;
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
public interface Boolean2DoubleFunction extends Function<Boolean, Double> {
   default double put(boolean key, double value) {
      throw new UnsupportedOperationException();
   }

   double get(boolean var1);

   default double getOrDefault(boolean key, double defaultValue) {
      double v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default double remove(boolean key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Double put(Boolean key, Double value) {
      boolean k = key;
      boolean containsKey = this.containsKey(k);
      double v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Double get(Object key) {
      if (key == null) {
         return null;
      } else {
         boolean k = (Boolean)key;
         double v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Double getOrDefault(Object key, Double defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         boolean k = (Boolean)key;
         double v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Double remove(Object key) {
      if (key == null) {
         return null;
      } else {
         boolean k = (Boolean)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(boolean key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Boolean)key);
   }

   default void defaultReturnValue(double rv) {
      throw new UnsupportedOperationException();
   }

   default double defaultReturnValue() {
      return 0.0D;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Double> compose(java.util.function.Function<? super T, ? extends Boolean> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Boolean, T> andThen(java.util.function.Function<? super Double, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Boolean2ByteFunction andThenByte(Double2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2DoubleFunction composeByte(Byte2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2ShortFunction andThenShort(Double2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2DoubleFunction composeShort(Short2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2IntFunction andThenInt(Double2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2DoubleFunction composeInt(Int2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2LongFunction andThenLong(Double2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2DoubleFunction composeLong(Long2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2CharFunction andThenChar(Double2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2DoubleFunction composeChar(Char2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2FloatFunction andThenFloat(Double2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2DoubleFunction composeFloat(Float2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2DoubleFunction andThenDouble(Double2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2DoubleFunction composeDouble(Double2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Boolean2ObjectFunction<T> andThenObject(Double2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2DoubleFunction<T> composeObject(Object2BooleanFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getBoolean(k));
      };
   }

   default <T> Boolean2ReferenceFunction<T> andThenReference(Double2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2DoubleFunction<T> composeReference(Reference2BooleanFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getBoolean(k));
      };
   }
}
