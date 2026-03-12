package ac.grim.grimac.shaded.fastutil.ints;

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
import ac.grim.grimac.shaded.fastutil.bytes.Byte2IntFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2IntFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2IntFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2IntFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2IntFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2IntFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2IntFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2IntFunction;

@FunctionalInterface
public interface Int2BooleanFunction extends Function<Integer, Boolean>, java.util.function.IntPredicate {
   default boolean test(int operand) {
      return this.get(operand);
   }

   default boolean put(int key, boolean value) {
      throw new UnsupportedOperationException();
   }

   boolean get(int var1);

   default boolean getOrDefault(int key, boolean defaultValue) {
      boolean v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default boolean remove(int key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Boolean put(Integer key, Boolean value) {
      int k = key;
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
         int k = (Integer)key;
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
         int k = (Integer)key;
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
         int k = (Integer)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(int key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Integer)key);
   }

   default void defaultReturnValue(boolean rv) {
      throw new UnsupportedOperationException();
   }

   default boolean defaultReturnValue() {
      return false;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Boolean> compose(java.util.function.Function<? super T, ? extends Integer> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Integer, T> andThen(java.util.function.Function<? super Boolean, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Int2ByteFunction andThenByte(Boolean2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2BooleanFunction composeByte(Byte2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2ShortFunction andThenShort(Boolean2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2BooleanFunction composeShort(Short2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2IntFunction andThenInt(Boolean2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2BooleanFunction composeInt(Int2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2LongFunction andThenLong(Boolean2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2BooleanFunction composeLong(Long2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2CharFunction andThenChar(Boolean2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2BooleanFunction composeChar(Char2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2FloatFunction andThenFloat(Boolean2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2BooleanFunction composeFloat(Float2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Int2DoubleFunction andThenDouble(Boolean2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2BooleanFunction composeDouble(Double2IntFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Int2ObjectFunction<T> andThenObject(Boolean2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2BooleanFunction<T> composeObject(Object2IntFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getInt(k));
      };
   }

   default <T> Int2ReferenceFunction<T> andThenReference(Boolean2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2BooleanFunction<T> composeReference(Reference2IntFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getInt(k));
      };
   }
}
