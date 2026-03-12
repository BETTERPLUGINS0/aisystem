package com.nisovin.shopkeepers.util.data.serialization.java;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.StringUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Collection;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class StringSerializers {
   public static final DataSerializer<String> STRICT = new StringSerializers.StringSerializer() {
      public String deserialize(Object data) throws InvalidDataException {
         Validate.notNull(data, "data is null");
         if (!(data instanceof String)) {
            throw invalidDataTypeException(data);
         } else {
            return (String)data;
         }
      }
   };
   public static final DataSerializer<String> STRICT_NON_EMPTY = new StringSerializers.StringSerializer() {
      public String deserialize(Object data) throws InvalidDataException {
         String value = (String)StringSerializers.STRICT.deserialize(data);
         if (value.isEmpty()) {
            throw emptyDataException();
         } else {
            return value;
         }
      }
   };
   public static final DataSerializer<String> SCALAR = new StringSerializers.StringSerializer() {
      public String deserialize(Object data) throws InvalidDataException {
         if (!(data instanceof Collection) && !DataContainer.isDataContainer(data)) {
            return (String)StringSerializers.LENIENT.deserialize(data);
         } else {
            throw invalidDataTypeException(data);
         }
      }
   };
   public static final DataSerializer<String> SCALAR_NON_EMPTY = new StringSerializers.StringSerializer() {
      public String deserialize(Object data) throws InvalidDataException {
         String value = (String)StringSerializers.SCALAR.deserialize(data);
         if (value.isEmpty()) {
            throw emptyDataException();
         } else {
            return value;
         }
      }
   };
   public static final DataSerializer<String> LENIENT = new StringSerializers.StringSerializer() {
      public String deserialize(Object data) throws InvalidDataException {
         Validate.notNull(data, "data is null");
         String value = StringUtils.toStringOrNull(data);
         if (value == null) {
            throw new InvalidDataException("Failed to convert data of type " + data.getClass().getName() + " to String!");
         } else {
            return value;
         }
      }
   };
   public static final DataSerializer<String> LENIENT_NON_EMPTY = new StringSerializers.StringSerializer() {
      public String deserialize(Object data) throws InvalidDataException {
         String value = (String)StringSerializers.LENIENT.deserialize(data);
         if (value.isEmpty()) {
            throw emptyDataException();
         } else {
            return value;
         }
      }
   };

   private StringSerializers() {
   }

   private abstract static class StringSerializer implements DataSerializer<String> {
      @Nullable
      public Object serialize(String value) {
         Validate.notNull(value, (String)"value is null");
         return value;
      }

      protected static InvalidDataException invalidDataTypeException(Object data) {
         return new InvalidDataException("Data is not of type String, but " + data.getClass().getName() + "!");
      }

      protected static InvalidDataException emptyDataException() {
         return new InvalidDataException("String data is empty!");
      }
   }
}
