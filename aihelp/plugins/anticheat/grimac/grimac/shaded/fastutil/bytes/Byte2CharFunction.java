package ac.grim.grimac.shaded.fastutil.bytes;

import ac.grim.grimac.shaded.fastutil.Function;
import ac.grim.grimac.shaded.fastutil.SafeMath;
import ac.grim.grimac.shaded.fastutil.chars.Char2ByteFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2CharFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2DoubleFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2FloatFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2IntFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2LongFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ObjectFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ReferenceFunction;
import ac.grim.grimac.shaded.fastutil.chars.Char2ShortFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2ByteFunction;
import ac.grim.grimac.shaded.fastutil.doubles.Double2CharFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2ByteFunction;
import ac.grim.grimac.shaded.fastutil.floats.Float2CharFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2ByteFunction;
import ac.grim.grimac.shaded.fastutil.ints.Int2CharFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2ByteFunction;
import ac.grim.grimac.shaded.fastutil.longs.Long2CharFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2ByteFunction;
import ac.grim.grimac.shaded.fastutil.objects.Object2CharFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2ByteFunction;
import ac.grim.grimac.shaded.fastutil.objects.Reference2CharFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2ByteFunction;
import ac.grim.grimac.shaded.fastutil.shorts.Short2CharFunction;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
public interface Byte2CharFunction extends Function<Byte, Character>, IntUnaryOperator {
   /** @deprecated */
   @Deprecated
   default int applyAsInt(int operand) {
      return this.get(SafeMath.safeIntToByte(operand));
   }

   default char put(byte key, char value) {
      throw new UnsupportedOperationException();
   }

   char get(byte var1);

   default char getOrDefault(byte key, char defaultValue) {
      char v;
      return (v = this.get(key)) == this.defaultReturnValue() && !this.containsKey(key) ? defaultValue : v;
   }

   default char remove(byte key) {
      throw new UnsupportedOperationException();
   }

   /** @deprecated */
   @Deprecated
   default Character put(Byte key, Character value) {
      byte k = key;
      boolean containsKey = this.containsKey(k);
      char v = this.put(k, value);
      return containsKey ? v : null;
   }

   /** @deprecated */
   @Deprecated
   default Character get(Object key) {
      if (key == null) {
         return null;
      } else {
         byte k = (Byte)key;
         char v;
         return (v = this.get(k)) == this.defaultReturnValue() && !this.containsKey(k) ? null : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Character getOrDefault(Object key, Character defaultValue) {
      if (key == null) {
         return defaultValue;
      } else {
         byte k = (Byte)key;
         char v = this.get(k);
         return v == this.defaultReturnValue() && !this.containsKey(k) ? defaultValue : v;
      }
   }

   /** @deprecated */
   @Deprecated
   default Character remove(Object key) {
      if (key == null) {
         return null;
      } else {
         byte k = (Byte)key;
         return this.containsKey(k) ? this.remove(k) : null;
      }
   }

   default boolean containsKey(byte key) {
      return true;
   }

   /** @deprecated */
   @Deprecated
   default boolean containsKey(Object key) {
      return key == null ? false : this.containsKey((Byte)key);
   }

   default void defaultReturnValue(char rv) {
      throw new UnsupportedOperationException();
   }

   default char defaultReturnValue() {
      return '\u0000';
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<T, Character> compose(java.util.function.Function<? super T, ? extends Byte> before) {
      return Function.super.compose(before);
   }

   /** @deprecated */
   @Deprecated
   default <T> java.util.function.Function<Byte, T> andThen(java.util.function.Function<? super Character, ? extends T> after) {
      return Function.super.andThen(after);
   }

   default Byte2ByteFunction andThenByte(Char2ByteFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Byte2CharFunction composeByte(Byte2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2ShortFunction andThenShort(Char2ShortFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Short2CharFunction composeShort(Short2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2IntFunction andThenInt(Char2IntFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Int2CharFunction composeInt(Int2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2LongFunction andThenLong(Char2LongFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Long2CharFunction composeLong(Long2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2CharFunction andThenChar(Char2CharFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Char2CharFunction composeChar(Char2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2FloatFunction andThenFloat(Char2FloatFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Float2CharFunction composeFloat(Float2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default Byte2DoubleFunction andThenDouble(Char2DoubleFunction after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default Double2CharFunction composeDouble(Double2ByteFunction before) {
      return (k) -> {
         return this.get(before.get(k));
      };
   }

   default <T> Byte2ObjectFunction<T> andThenObject(Char2ObjectFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Object2CharFunction<T> composeObject(Object2ByteFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getByte(k));
      };
   }

   default <T> Byte2ReferenceFunction<T> andThenReference(Char2ReferenceFunction<? extends T> after) {
      return (k) -> {
         return after.get(this.get(k));
      };
   }

   default <T> Reference2CharFunction<T> composeReference(Reference2ByteFunction<? super T> before) {
      return (k) -> {
         return this.get(before.getByte(k));
      };
   }
}
