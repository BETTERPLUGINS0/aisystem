package com.nisovin.shopkeepers.util.data.property.validation.bukkit;

import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.data.property.validation.PropertyValidator;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.inventory.ItemStack;

public final class ItemStackValidators {
   public static final PropertyValidator<ItemStack> NON_EMPTY = (value) -> {
      Validate.isTrue(!ItemUtils.isEmpty(value), "Item stack is empty!");
   };

   private ItemStackValidators() {
   }

   public static final class Unmodifiable {
      public static final PropertyValidator<UnmodifiableItemStack> NON_EMPTY = (value) -> {
         Validate.isTrue(!ItemUtils.isEmpty(value), "Item stack is empty!");
      };

      private Unmodifiable() {
      }
   }
}
