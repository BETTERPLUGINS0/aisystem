package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.bukkit.SoundEffect;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SoundEffectValue extends ValueType<SoundEffect> {
   public static final SoundEffectValue INSTANCE = new SoundEffectValue();

   @Nullable
   public SoundEffect load(@Nullable Object configValue) throws ValueLoadException {
      if (configValue == null) {
         return null;
      } else {
         try {
            return (SoundEffect)SoundEffect.SERIALIZER.deserialize(configValue);
         } catch (InvalidDataException var3) {
            throw new ValueLoadException(var3.getMessage(), var3);
         }
      }
   }

   @Nullable
   public Object save(@Nullable SoundEffect value) {
      return value == null ? null : value.serialize();
   }

   public SoundEffect parse(String input) throws ValueParseException {
      Validate.notNull(input, (String)"input is null");

      try {
         return (SoundEffect)SoundEffect.SERIALIZER.deserialize(input);
      } catch (InvalidDataException var3) {
         throw new ValueParseException(var3.getMessage(), var3);
      }
   }
}
