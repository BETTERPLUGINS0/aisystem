package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ShortFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ShortFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ShortFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ShortFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ShortFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ShortFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ByteFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2CharFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2FloatFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2IntFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2LongFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ShortFunction;
import java.util.function.ToIntFunction;

@FunctionalInterface
public interface Reference2ShortFunction<K> extends Function<K, Short>, ToIntFunction<K> {
   default int applyAsInt(K operand) {
      return this.getShort(operand);
   }

   default short put(K key, short value) {
      throw new UnsupportedOperationException();
   }

   short getShort(Object var1);

   default short getOrDefault(Object key, short defaultValue) {
      short v;
      return (v = this.getShort(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default short removeShort(Object key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Short put(K key, Short value) {
      boolean containsKey = this.containsKey(key);
      short v = this.put(key, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Short get(Object key) {
      short v;
      return (v = this.getShort(key)) == this.defaultReturnValue() && !this.containsKey(key) ? null : v;
   }

   /** @deprecated */
   @Deprecated
   default Short getOrDefault(Object key, Short defaultValue) {
      short v = this.getShort(key);
      return v == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   /** @deprecated */
   @Deprecated
   default Short remove(Object key) {
      return this.containsKey(key) ? this.removeShort(key) : null;
   }

   default void defaultReturnValue(short rv) {
      throw new UnsupportedOperationException();
   }

   default short defaultReturnValue() {
      return 0;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Short, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Reference2ByteFunction<K> andThenByte(Short2ByteFunction after) {
      return (k) -> {
         return after.get(this.getShort(k));
      };
   }

   default Byte2ShortFunction composeByte(Byte2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getShort(before.get(k));
      };
   }

   default Reference2ShortFunction<K> andThenShort(Short2ShortFunction after) {
      return (k) -> {
         return after.get(this.getShort(k));
      };
   }

   default Short2ShortFunction composeShort(Short2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getShort(before.get(k));
      };
   }

   default Reference2IntFunction<K> andThenInt(Short2IntFunction after) {
      return (k) -> {
         return after.get(this.getShort(k));
      };
   }

   default Int2ShortFunction composeInt(Int2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getShort(before.get(k));
      };
   }

   default Reference2LongFunction<K> andThenLong(Short2LongFunction after) {
      return (k) -> {
         return after.get(this.getShort(k));
      };
   }

   default Long2ShortFunction composeLong(Long2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getShort(before.get(k));
      };
   }

   default Reference2CharFunction<K> andThenChar(Short2CharFunction after) {
      return (k) -> {
         return after.get(this.getShort(k));
      };
   }

   default Char2ShortFunction composeChar(Char2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getShort(before.get(k));
      };
   }

   default Reference2FloatFunction<K> andThenFloat(Short2FloatFunction after) {
      return (k) -> {
         return after.get(this.getShort(k));
      };
   }

   default Float2ShortFunction composeFloat(Float2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getShort(before.get(k));
      };
   }

   default Reference2DoubleFunction<K> andThenDouble(Short2DoubleFunction after) {
      return (k) -> {
         return after.get(this.getShort(k));
      };
   }

   default Double2ShortFunction composeDouble(Double2ReferenceFunction<K> before) {
      return (k) -> {
         return this.getShort(before.get(k));
      };
   }

   default <T> Reference2ObjectFunction<K, T> andThenObject(Short2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getShort(k));
      };
   }

   default <T> Object2ShortFunction<T> composeObject(Object2ReferenceFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getShort(before.get(k));
      };
   }

   default <T> Reference2ReferenceFunction<K, T> andThenReference(Short2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getShort(k));
      };
   }

   default <T> Reference2ShortFunction<T> composeReference(Reference2ReferenceFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getShort(before.get(k));
      };
   }
}
