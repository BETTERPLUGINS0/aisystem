package com.nisovin.shopkeepers.util.data.serialization.java;

import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class BooleanSerializers {
   public static final DataSerializer<Boolean> STRICT = new BooleanSerializers.BooleanSerializer() {
      public Boolean deserialize(Object data) throws InvalidDataException {
         Validate.notNull(data, "data is null");
         if (!(data instanceof Boolean)) {
            throw new InvalidDataException("Data is not of type Boolean, but " + data.getClass().getName() + "!");
         } else {
            return (Boolean)data;
         }
      }
   };
   public static final DataSerializer<Boolean> LENIENT = new BooleanSerializers.BooleanSerializer() {
      public Boolean deserialize(Object data) throws InvalidDataException {
         Validate.notNull(data, "data is null");
         Boolean value = ConversionUtils.toBoolean(data);
         if (value == null) {
            throw new InvalidDataException("Failed to parse Boolean from '" + String.valueOf(data) + "'!");
         } else {
            return value;
         }
      }
   };

   private BooleanSerializers() {
   }

   private abstract static class BooleanSerializer implements DataSerializer<Boolean> {
      @Nullable
      public Object serialize(Boolean value) {
         Validate.notNull(value, (String)"value is null");
         return value;
      }
   }
}
