package ch.jalu.configme.properties.types;

import ch.jalu.configme.properties.convertresult.ConvertErrorRecorder;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PrimitivePropertyType<T> implements PropertyType<T> {
   public static final PropertyType<Long> LONG = fromNumber(Number::longValue);
   public static final PropertyType<Integer> INTEGER = fromNumber(Number::intValue);
   public static final PropertyType<Double> DOUBLE = fromNumber(Number::doubleValue);
   public static final PropertyType<Float> FLOAT = fromNumber(Number::floatValue);
   public static final PropertyType<Short> SHORT = fromNumber(Number::shortValue);
   public static final PropertyType<Byte> BYTE = fromNumber(Number::byteValue);
   public static final PropertyType<Boolean> BOOLEAN = new PrimitivePropertyType((object) -> {
      return object instanceof Boolean ? (Boolean)object : null;
   });
   public static final PropertyType<String> STRING = new PrimitivePropertyType((object) -> {
      return object == null ? null : object.toString();
   });
   public static final PropertyType<String> LOWERCASE_STRING = new PrimitivePropertyType((object) -> {
      return object == null ? null : object.toString().toLowerCase();
   });
   private final Function<Object, T> convertFunction;
   private final Function<T, Object> exportValueFunction;

   public PrimitivePropertyType(@NotNull Function<Object, T> convertFunction) {
      this(convertFunction, (t) -> {
         return t;
      });
   }

   public PrimitivePropertyType(@NotNull Function<Object, T> convertFunction, @NotNull Function<T, Object> exportValueFunction) {
      this.convertFunction = convertFunction;
      this.exportValueFunction = exportValueFunction;
   }

   @Nullable
   public T convert(@Nullable Object object, @NotNull ConvertErrorRecorder errorRecorder) {
      return this.convertFunction.apply(object);
   }

   @Nullable
   public Object toExportValue(@Nullable T value) {
      return this.exportValueFunction.apply(value);
   }

   @NotNull
   private static <T> PrimitivePropertyType<T> fromNumber(@NotNull Function<Number, T> function) {
      return new PrimitivePropertyType((object) -> {
         return object instanceof Number ? function.apply((Number)object) : null;
      });
   }
}
