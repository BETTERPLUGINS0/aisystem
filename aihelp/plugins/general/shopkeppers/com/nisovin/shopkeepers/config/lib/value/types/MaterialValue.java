package com.nisovin.shopkeepers.config.lib.value.types;

import com.nisovin.shopkeepers.config.lib.value.InvalidMaterialException;
import com.nisovin.shopkeepers.config.lib.value.ValueLoadException;
import com.nisovin.shopkeepers.config.lib.value.ValueParseException;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Material;

public class MaterialValue extends MinecraftEnumValue<Material> {
   public static final MaterialValue INSTANCE = new MaterialValue();

   public MaterialValue() {
      super(Material.class);
   }

   protected ValueLoadException newInvalidEnumValueException(String valueName, ValueParseException parseException) {
      return new InvalidMaterialException(parseException.getMessage(), parseException);
   }

   public Material parse(String input) throws ValueParseException {
      Validate.notNull(input, (String)"input is null");
      Material material = ItemUtils.parseMaterial(input);
      if (material == null) {
         throw new ValueParseException("Unknown Material: " + input);
      } else if (material.isLegacy()) {
         throw new ValueParseException("Unsupported legacy Material: " + input);
      } else {
         return material;
      }
   }
}
