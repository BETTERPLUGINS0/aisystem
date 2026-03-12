package ac.grim.grimac.shaded.fastutil.booleans;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2FloatFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2FloatFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2FloatFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ByteFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2CharFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2FloatFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2IntFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2LongFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ShortFunction;
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

@FunctionalInterface
public interface Boolean2FloatFunction extends Function<Boolean, Float> {
   default float put(boolean key, float value) {
      throw new UnsupportedOperationException();
   }

   float get(boolean var1);

   default float getOrDefault(boolean key, float defaultValue) {
      float v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default float remove(boolean key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Float put(Boolean key, Float value) {
      boolean k = key;
      boolean containsKey = this.containsKey(k);
      float v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Float get(Object key) {
      if (key == null) {
         return null;
      } else {
         boolean k = (Boolean)key;
         float v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Float getOrDefault(Object key, Float defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         boolean k = (Boolean)key;
         float v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Float remove(Object key) {
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

   default void defaultReturnValue(float rv) {
      throw new UnsupportedOperationException();
   }

   default float defaultReturnValue() {
      return 0.0F;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Float> compose(java.util.function.Function<? super T, ? extends Boolean> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Boolean, T> andThen(java.util.function.Function<? super Float, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Boolean2ByteFunction andThenByte(Float2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2FloatFunction composeByte(Byte2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2ShortFunction andThenShort(Float2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2FloatFunction composeShort(Short2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2IntFunction andThenInt(Float2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2FloatFunction composeInt(Int2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2LongFunction andThenLong(Float2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2FloatFunction composeLong(Long2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2CharFunction andThenChar(Float2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2FloatFunction composeChar(Char2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2FloatFunction andThenFloat(Float2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2FloatFunction composeFloat(Float2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2DoubleFunction andThenDouble(Float2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2FloatFunction composeDouble(Double2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Boolean2ObjectFunction<T> andThenObject(Float2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2FloatFunction<T> composeObject(Object2BooleanFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getBoolean(k));
      };
   }

   default <T> Boolean2ReferenceFunction<T> andThenReference(Float2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2FloatFunction<T> composeReference(Reference2BooleanFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getBoolean(k));
      };
   }
}
