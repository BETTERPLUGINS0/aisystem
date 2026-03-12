package ac.grim.grimac.shaded.fastutil.objects;

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
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2BooleanFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ObjectFunction;
import java.util.function.Predicate;

@FunctionalInterface
public interface Object2BooleanFunction<K> extends Function<K, Boolean>, Predicate<K> {
   default boolean test(K operand) {
      return this.getBoolean(operand);
   }

   default boolean put(K key, boolean value) {
      throw new UnsupportedOperationException();
   }

   boolean getBoolean(Object var1);

   default boolean getOrDefault(Object key, boolean defaultValue) {
      boolean v;
      return (v = this.getBoolean(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default boolean removeBoolean(Object key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Boolean put(K key, Boolean value) {
      boolean containsKey = this.containsKey(key);
      boolean v = this.put(key, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Boolean get(Object key) {
      boolean v;
      return (v = this.getBoolean(key)) == this.defaultReturnValue() && !this.containsKey(key) ? null : v;
   }

   /** @deprecated */
   @Deprecated
   default Boolean getOrDefault(Object key, Boolean defaultValue) {
      boolean v = this.getBoolean(key);
      return v == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   /** @deprecated */
   @Deprecated
   default Boolean remove(Object key) {
      return this.containsKey(key) ? this.removeBoolean(key) : null;
   }

   default void defaultReturnValue(boolean rv) {
      throw new UnsupportedOperationException();
   }

   default boolean defaultReturnValue() {
      return false;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Boolean, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Object2ByteFunction<K> andThenByte(Boolean2ByteFunction after) {
      return (k) -> {
         return after.get(this.getBoolean(k));
      };
   }

   default Byte2BooleanFunction composeByte(Byte2ObjectFunction<K> before) {
      return (k) -> {
         return this.getBoolean(before.get(k));
      };
   }

   default Object2ShortFunction<K> andThenShort(Boolean2ShortFunction after) {
      return (k) -> {
         return after.get(this.getBoolean(k));
      };
   }

   default Short2BooleanFunction composeShort(Short2ObjectFunction<K> before) {
      return (k) -> {
         return this.getBoolean(before.get(k));
      };
   }

   default Object2IntFunction<K> andThenInt(Boolean2IntFunction after) {
      return (k) -> {
         return after.get(this.getBoolean(k));
      };
   }

   default Int2BooleanFunction composeInt(Int2ObjectFunction<K> before) {
      return (k) -> {
         return this.getBoolean(before.get(k));
      };
   }

   default Object2LongFunction<K> andThenLong(Boolean2LongFunction after) {
      return (k) -> {
         return after.get(this.getBoolean(k));
      };
   }

   default Long2BooleanFunction composeLong(Long2ObjectFunction<K> before) {
      return (k) -> {
         return this.getBoolean(before.get(k));
      };
   }

   default Object2CharFunction<K> andThenChar(Boolean2CharFunction after) {
      return (k) -> {
         return after.get(this.getBoolean(k));
      };
   }

   default Char2BooleanFunction composeChar(Char2ObjectFunction<K> before) {
      return (k) -> {
         return this.getBoolean(before.get(k));
      };
   }

   default Object2FloatFunction<K> andThenFloat(Boolean2FloatFunction after) {
      return (k) -> {
         return after.get(this.getBoolean(k));
      };
   }

   default Float2BooleanFunction composeFloat(Float2ObjectFunction<K> before) {
      return (k) -> {
         return this.getBoolean(before.get(k));
      };
   }

   default Object2DoubleFunction<K> andThenDouble(Boolean2DoubleFunction after) {
      return (k) -> {
         return after.get(this.getBoolean(k));
      };
   }

   default Double2BooleanFunction composeDouble(Double2ObjectFunction<K> before) {
      return (k) -> {
         return this.getBoolean(before.get(k));
      };
   }

   default <T> Object2ObjectFunction<K, T> andThenObject(Boolean2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getBoolean(k));
      };
   }

   default <T> Object2BooleanFunction<T> composeObject(Object2ObjectFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getBoolean(before.get(k));
      };
   }

   default <T> Object2ReferenceFunction<K, T> andThenReference(Boolean2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getBoolean(k));
      };
   }

   default <T> Reference2BooleanFunction<T> composeReference(Reference2ObjectFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getBoolean(before.get(k));
      };
   }
}
