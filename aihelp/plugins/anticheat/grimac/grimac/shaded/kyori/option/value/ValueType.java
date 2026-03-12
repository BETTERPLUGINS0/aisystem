package ac.grim.grimac.shaded.kyori.option.value;

import java.util.Objects;

public interface ValueType<T> {
   static ValueType<String> stringType() {
      return ValueTypeImpl.Types.STRING;
   }

   static ValueType<Boolean> booleanType() {
      return ValueTypeImpl.Types.BOOLEAN;
   }

   static ValueType<Integer> integerType() {
      return ValueTypeImpl.Types.INT;
   }

   static ValueType<Double> doubleType() {
      return ValueTypeImpl.Types.DOUBLE;
   }

   static <E extends Enum<E>> ValueType<E> enumType(final Class<E> enumClazz) {
      return new ValueTypeImpl.EnumType((Class)Objects.requireNonNull(enumClazz, "enumClazz"));
   }

   Class<T> type();

   T parse(final String plainValue) throws IllegalArgumentException;
}
