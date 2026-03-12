package ac.grim.grimac.shaded.fastutil.objects;

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
import ac.grim.grimac.shaded.fastutil.chars.Char2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ByteFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ByteFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ByteFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ByteFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ByteFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ObjectFunction;
import java.util.function.ToIntFunction;

@FunctionalInterface
public interface Object2ByteFunction<K> extends Function<K, Byte>, ToIntFunction<K> {
   default int applyAsInt(K operand) {
      return this.getByte(operand);
   }

   default byte put(K key, byte value) {
      throw new UnsupportedOperationException();
   }

   byte getByte(Object var1);

   default byte getOrDefault(Object key, byte defaultValue) {
      byte v;
      return (v = this.getByte(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default byte removeByte(Object key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Byte put(K key, Byte value) {
      boolean containsKey = this.containsKey(key);
      byte v = this.put(key, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Byte get(Object key) {
      byte v;
      return (v = this.getByte(key)) == this.defaultReturnValue() && !this.containsKey(key) ? null : v;
   }

   /** @deprecated */
   @Deprecated
   default Byte getOrDefault(Object key, Byte defaultValue) {
      byte v = this.getByte(key);
      return v == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   /** @deprecated */
   @Deprecated
   default Byte remove(Object key) {
      return this.containsKey(key) ? this.removeByte(key) : null;
   }

   default void defaultReturnValue(byte rv) {
      throw new UnsupportedOperationException();
   }

   default byte defaultReturnValue() {
      return 0;
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Byte, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Object2ByteFunction<K> andThenByte(Byte2ByteFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Byte2ByteFunction composeByte(Byte2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2ShortFunction<K> andThenShort(Byte2ShortFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Short2ByteFunction composeShort(Short2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2IntFunction<K> andThenInt(Byte2IntFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Int2ByteFunction composeInt(Int2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2LongFunction<K> andThenLong(Byte2LongFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Long2ByteFunction composeLong(Long2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2CharFunction<K> andThenChar(Byte2CharFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Char2ByteFunction composeChar(Char2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2FloatFunction<K> andThenFloat(Byte2FloatFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Float2ByteFunction composeFloat(Float2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default Object2DoubleFunction<K> andThenDouble(Byte2DoubleFunction after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default Double2ByteFunction composeDouble(Double2ObjectFunction<K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default <T> Object2ObjectFunction<K, T> andThenObject(Byte2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default <T> Object2ByteFunction<T> composeObject(Object2ObjectFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }

   default <T> Object2ReferenceFunction<K, T> andThenReference(Byte2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getByte(k));
      };
   }

   default <T> Reference2ByteFunction<T> composeReference(Reference2ObjectFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getByte(before.get(k));
      };
   }
}
