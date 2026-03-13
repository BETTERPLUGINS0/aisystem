package com.nisovin.shopkeepers.util.data.serialization.java;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class DataContainerSerializers {
   public static final DataSerializer<DataContainer> DEFAULT = new DataContainerSerializers.DataContainerSerializer() {
      public DataContainer deserialize(Object data) throws InvalidDataException {
         Validate.notNull(data, "data is null");
         DataContainer value = DataContainer.of(data);
         if (value == null) {
            throw new InvalidDataException("Data is not a DataContainer, but of type " + data.getClass().getName() + "!");
         } else {
            return value;
         }
      }
   };
   public static final DataSerializer<DataContainer> DEFAULT_NON_EMPTY = new DataContainerSerializers.DataContainerSerializer() {
      public DataContainer deserialize(Object data) throws InvalidDataException {
         DataContainer value = (DataContainer)DataContainerSerializers.DEFAULT.deserialize(data);
         if (value.isEmpty()) {
            throw new InvalidDataException("Data is empty!");
         } else {
            return value;
         }
      }
   };

   private DataContainerSerializers() {
   }

   private abstract static class DataContainerSerializer implements DataSerializer<DataContainer> {
      @Nullable
      public Object serialize(DataContainer value) {
         Validate.notNull(value, (String)"value is null");
         return value.serialize();
      }
   }
}
