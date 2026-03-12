package ac.grim.grimac.shaded.fastutil.objects;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2CharFunction;
import ac.grim.grimac.shaded.fastutil.bytes.Byte2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ByteFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2CharFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2FloatFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2IntFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2LongFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ShortFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2CharFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2CharFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2CharFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2CharFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2CharFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ObjectFunction;
import java.util.function.ToIntFunction;

@FunctionalInterface
public interface Object2CharFunction<K> extends Function<K, Character>, ToIntFunction<K> {
   default int applyAsInt(K operand) {
      return this.getChar(operand);
   }

   default char put(K key, char value) {
      throw new UnsupportedOperationException();
   }

   char getChar(Object var1);

   default char getOrDefault(Object key, char defaultValue) {
      char v;
      return (v = this.getChar(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default char removeChar(Object key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Character put(K key, Character value) {
      boolean containsKey = this.containsKey(key);
      char v = this.put(key, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Character get(Object key) {
      char v;
      return (v = this.getChar(key)) == this.defaultReturnValue() && !this.containsKey(key) ? null : v;
   }

   /** @deprecated */
   @Deprecated
   default Character getOrDefault(Object key, Character defaultValue) {
      char v = this.getChar(key);
      return v == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   /** @deprecated */
   @Deprecated
   default Character remove(Object key) {
      return this.containsKey(key) ? this.removeChar(key) : null;
   }

   default void defaultReturnValue(char rv) {
      throw new UnsupportedOperationException();
   }

   default char defaultReturnValue() {
      return '\u0000';
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<K, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Object2ByteFunction<K> andThenByte(Char2ByteFunction after) {
      return (k) -> {
         return after.get(this.getChar(k));
      };
   }

   default Byte2CharFunction composeByte(Byte2ObjectFunction<K> before) {
      return (k) -> {
         return this.getChar(before.get(k));
      };
   }

   default Object2ShortFunction<K> andThenShort(Char2ShortFunction after) {
      return (k) -> {
         return after.get(this.getChar(k));
      };
   }

   default Short2CharFunction composeShort(Short2ObjectFunction<K> before) {
      return (k) -> {
         return this.getChar(before.get(k));
      };
   }

   default Object2IntFunction<K> andThenInt(Char2IntFunction after) {
      return (k) -> {
         return after.get(this.getChar(k));
      };
   }

   default Int2CharFunction composeInt(Int2ObjectFunction<K> before) {
      return (k) -> {
         return this.getChar(before.get(k));
      };
   }

   default Object2LongFunction<K> andThenLong(Char2LongFunction after) {
      return (k) -> {
         return after.get(this.getChar(k));
      };
   }

   default Long2CharFunction composeLong(Long2ObjectFunction<K> before) {
      return (k) -> {
         return this.getChar(before.get(k));
      };
   }

   default Object2CharFunction<K> andThenChar(Char2CharFunction after) {
      return (k) -> {
         return after.get(this.getChar(k));
      };
   }

   default Char2CharFunction composeChar(Char2ObjectFunction<K> before) {
      return (k) -> {
         return this.getChar(before.get(k));
      };
   }

   default Object2FloatFunction<K> andThenFloat(Char2FloatFunction after) {
      return (k) -> {
         return after.get(this.getChar(k));
      };
   }

   default Float2CharFunction composeFloat(Float2ObjectFunction<K> before) {
      return (k) -> {
         return this.getChar(before.get(k));
      };
   }

   default Object2DoubleFunction<K> andThenDouble(Char2DoubleFunction after) {
      return (k) -> {
         return after.get(this.getChar(k));
      };
   }

   default Double2CharFunction composeDouble(Double2ObjectFunction<K> before) {
      return (k) -> {
         return this.getChar(before.get(k));
      };
   }

   default <T> Object2ObjectFunction<K, T> andThenObject(Char2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getChar(k));
      };
   }

   default <T> Object2CharFunction<T> composeObject(Object2ObjectFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getChar(before.get(k));
      };
   }

   default <T> Object2ReferenceFunction<K, T> andThenReference(Char2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.getChar(k));
      };
   }

   default <T> Reference2CharFunction<T> composeReference(Reference2ObjectFunction<? super T, ? extends K> before) {
      return (k) -> {
         return this.getChar(before.get(k));
      };
   }
}
