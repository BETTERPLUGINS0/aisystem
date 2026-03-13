package com.nisovin.shopkeepers.util.data.serialization.bukkit;

import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.serialization.DataSerializer;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.java.StringSerializers;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ColoredStringSerializers {
   public static final DataSerializer<String> STRICT;
   public static final DataSerializer<String> STRICT_NON_EMPTY;
   public static final DataSerializer<String> SCALAR;
   public static final DataSerializer<String> SCALAR_NON_EMPTY;
   public static final DataSerializer<String> LENIENT;
   public static final DataSerializer<String> LENIENT_NON_EMPTY;

   private ColoredStringSerializers() {
   }

   static {
      STRICT = new ColoredStringSerializers.ColoredStringSerializer(StringSerializers.STRICT);
      STRICT_NON_EMPTY = new ColoredStringSerializers.ColoredStringSerializer(StringSerializers.STRICT_NON_EMPTY);
      SCALAR = new ColoredStringSerializers.ColoredStringSerializer(StringSerializers.SCALAR);
      SCALAR_NON_EMPTY = new ColoredStringSerializers.ColoredStringSerializer(StringSerializers.SCALAR_NON_EMPTY);
      LENIENT = new ColoredStringSerializers.ColoredStringSerializer(StringSerializers.LENIENT);
      LENIENT_NON_EMPTY = new ColoredStringSerializers.ColoredStringSerializer(StringSerializers.LENIENT_NON_EMPTY);
   }

   private static final class ColoredStringSerializer implements DataSerializer<String> {
      private final DataSerializer<String> stringSerializer;

      private ColoredStringSerializer(DataSerializer<String> stringSerializer) {
         Validate.notNull(stringSerializer, (String)"stringSerializer is null");
         this.stringSerializer = stringSerializer;
      }

      @Nullable
      public Object serialize(String value) {
         Validate.notNull(value, (String)"value is null");
         String decolored = TextUtils.decolorize(value);
         return this.stringSerializer.serialize(decolored);
      }

      public String deserialize(Object data) throws InvalidDataException {
         String value = (String)this.stringSerializer.deserialize(data);
         return TextUtils.colorize(value);
      }
   }
}
