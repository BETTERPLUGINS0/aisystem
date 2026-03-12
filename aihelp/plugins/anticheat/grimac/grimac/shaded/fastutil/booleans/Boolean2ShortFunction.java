package ac.grim.grimac.shaded.fastutil.booleans;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ShortFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ShortFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ShortFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ShortFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ShortFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ShortFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2ShortFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2ShortFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ByteFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2CharFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2FloatFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2IntFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2LongFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ShortFunction;

@FunctionalInterface
public interface Boolean2ShortFunction extends Function<Boolean, Short> {
   default short put(boolean key, short value) {
      throw new UnsupportedOperationException();
   }

   short get(boolean var1);

   default short getOrDefault(boolean key, short defaultValue) {
      short v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default short remove(boolean key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Short put(Boolean key, Short value) {
      boolean k = key;
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
         boolean k = (Boolean)key;
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
         boolean k = (Boolean)key;
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

   default void defaultReturnValue(short rv) {
      throw new UnsupportedOperationException();
   }

   default short defaultReturnValue() {
      return 0;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Short> compose(java.util.function.Function<? super T, ? extends Boolean> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Boolean, T> andThen(java.util.function.Function<? super Short, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Boolean2ByteFunction andThenByte(Short2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2ShortFunction composeByte(Byte2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2ShortFunction andThenShort(Short2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2ShortFunction composeShort(Short2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2IntFunction andThenInt(Short2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2ShortFunction composeInt(Int2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2LongFunction andThenLong(Short2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2ShortFunction composeLong(Long2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2CharFunction andThenChar(Short2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2ShortFunction composeChar(Char2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2FloatFunction andThenFloat(Short2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2ShortFunction composeFloat(Float2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Boolean2DoubleFunction andThenDouble(Short2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2ShortFunction composeDouble(Double2BooleanFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Boolean2ObjectFunction<T> andThenObject(Short2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2ShortFunction<T> composeObject(Object2BooleanFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getBoolean(k));
      };
   }

   default <T> Boolean2ReferenceFunction<T> andThenReference(Short2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2ShortFunction<T> composeReference(Reference2BooleanFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getBoolean(k));
      };
   }
}
