package com.nisovin.shopkeepers.util.data.serialization.java;

import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class NumberSerializers {
   public static final DataSerializer<Integer> INTEGER = new NumberSerializers.NumberSerializer<Integer>(Integer.class) {
      @Nullable
      public Integer deserializeNumber(Object data) throws InvalidDataException {
         return ConversionUtils.toInteger(data);
      }
   };
   public static final DataSerializer<Float> FLOAT = new NumberSerializers.NumberSerializer<Float>(Float.class) {
      @Nullable
      public Float deserializeNumber(Object data) throws InvalidDataException {
         return ConversionUtils.toFloat(data);
      }
   };
   public static final DataSerializer<Double> DOUBLE = new NumberSerializers.NumberSerializer<Double>(Double.class) {
      @Nullable
      public Double deserializeNumber(Object data) throws InvalidDataException {
         return ConversionUtils.toDouble(data);
      }
   };

   private NumberSerializers() {
   }

   private abstract static class NumberSerializer<N extends Number> implements DataSerializer<N> {
      private final String numberTypeName;

      public NumberSerializer(Class<N> numberType) {
         Validate.notNull(numberType, (String)"numberType is null");
         this.numberTypeName = numberType.getSimpleName();
      }

      @Nullable
      public Object serialize(N value) {
         Validate.notNull(value, (String)"value is null");
         return value;
      }

      public N deserialize(Object data) throws InvalidDataException {
         Validate.notNull(data, "data is null");
         N value = this.deserializeNumber(data);
         if (value == null) {
            String var10002 = this.numberTypeName;
            throw new InvalidDataException("Failed to parse " + var10002 + " from '" + String.valueOf(data) + "'!");
         } else {
            return value;
         }
      }

      @Nullable
      protected abstract N deserializeNumber(Object var1) throws InvalidDataException;
   }
}
