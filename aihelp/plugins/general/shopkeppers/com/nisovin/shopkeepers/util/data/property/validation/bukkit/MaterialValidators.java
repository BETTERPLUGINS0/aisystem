package com.nisovin.shopkeepers.util.data.property.validation.bukkit;

import com.nisovin.shopkeepers.util.data.property.validation.PropertyValidator;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Material;

public final class MaterialValidators {
   public static final PropertyValidator<Material> NOT_AIR = (value) -> {
      Validate.isTrue(value != Material.AIR, "Material cannot be AIR!");
   };
   public static final PropertyValidator<Material> IS_ITEM = (value) -> {
      Validate.isTrue(value.isItem(), "Material is not an item: " + String.valueOf(value));
   };
   public static final PropertyValidator<Material> NON_LEGACY = (value) -> {
      Validate.isTrue(!value.isLegacy(), "Unsupported legacy material: " + String.valueOf(value));
   };

   private MaterialValidators() {
   }
}
