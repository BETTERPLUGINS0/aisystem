package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.InvalidEntityTypeException;
import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import org.bukkit.entity.EntityType;

public class EntityTypeValue extends MinecraftEnumValue<EntityType> {
   public static final EntityTypeValue INSTANCE = new EntityTypeValue();

   public EntityTypeValue() {
      super(EntityType.class);
   }

   protected ValueLoadException newInvalidEnumValueException(String valueName, ValueParseException parseException) {
      return new InvalidEntityTypeException(parseException.getMessage(), parseException);
   }
}
