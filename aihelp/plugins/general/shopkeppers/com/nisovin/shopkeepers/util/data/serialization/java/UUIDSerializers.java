package com.nisovin.shopkeepers.util.data.serialization.java;

import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class UUIDSerializers {
   public static final DataSerializer<UUID> LENIENT = new UUIDSerializers.UUIDSerializer() {
      public UUID deserialize(Object data) throws InvalidDataException {
         String uuidString = (String)StringSerializers.STRICT_NON_EMPTY.deserialize(data);
         UUID value = ConversionUtils.parseUUID(uuidString);
         if (value == null) {
            throw new InvalidDataException("Failed to parse UUID from '" + uuidString + "'!");
         } else {
            return value;
         }
      }
   };

   private UUIDSerializers() {
   }

   private abstract static class UUIDSerializer implements DataSerializer<UUID> {
      @Nullable
      public Object serialize(UUID value) {
         Validate.notNull(value, (String)"value is null");
         return value.toString();
      }
   }
}
