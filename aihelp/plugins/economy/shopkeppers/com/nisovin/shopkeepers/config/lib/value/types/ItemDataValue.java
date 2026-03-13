package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.InvalidMaterialException;
import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.config.lib.value.ValueType;
import com.nisovin.shopkeepers.util.data.serialization.InvalidDataException;
import com.nisovin.shopkeepers.util.data.serialization.bukkit.UnknownMaterialException;
import com.nisovin.shopkeepers.util.inventory.ItemData;
import com.nisovin.shopkeepers.util.java.ThrowableUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ItemDataValue extends ValueType<ItemData> {
   public static final ItemDataValue INSTANCE = new ItemDataValue();

   @Nullable
   public ItemData load(@Nullable Object configValue) throws ValueLoadException {
      if (configValue == null) {
         return null;
      } else {
         try {
            return (ItemData)ItemData.SERIALIZER.deserialize(configValue);
         } catch (InvalidDataException var3) {
            if (ThrowableUtils.getRootCause(var3) instanceof UnknownMaterialException) {
               throw new InvalidMaterialException(var3.getMessage(), var3);
            } else {
               throw new ValueLoadException(var3.getMessage(), var3);
            }
         }
      }
   }

   @Nullable
   public Object save(@Nullable ItemData value) {
      return value == null ? null : value.serialize();
   }

   public String format(@Nullable ItemData value) {
      if (value == null) {
         return "null";
      } else {
         StringBuilder builder = new StringBuilder(value.getType().name());
         if (value.hasItemMeta()) {
            builder.append(" (+NBT)");
         }

         return builder.toString();
      }
   }

   public ItemData parse(String input) throws ValueParseException {
      Validate.notNull(input, (String)"input is null");

      try {
         return (ItemData)ItemData.SERIALIZER.deserialize(input);
      } catch (InvalidDataException var3) {
         throw new ValueParseException(var3.getMessage(), var3);
      }
   }
}
