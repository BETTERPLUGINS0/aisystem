package com.nisovin.shopkeepers.util.data.serialization.java;

import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class InstantSerializers {
   public static final DataSerializer<Instant> ISO = new DataSerializer<Instant>() {
      @Nullable
      public Object serialize(Instant value) {
         Validate.notNull(value, (String)"value is null");
         return value.toString();
      }

      public Instant deserialize(Object data) throws InvalidDataException {
         String instantString = (String)StringSerializers.STRICT_NON_EMPTY.deserialize(data);

         try {
            return Instant.parse(instantString);
         } catch (DateTimeParseException var4) {
            throw new InvalidDataException("Failed to parse timestamp from '" + instantString + "'!", var4);
         }
      }
   };

   private InstantSerializers() {
   }
}
