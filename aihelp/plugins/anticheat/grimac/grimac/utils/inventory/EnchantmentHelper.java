package ac.grim.grimac.utils.inventory;

import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.ItemStack;
import ac.grim.grimac.shaded.com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import ac.grim.grimac.utils.latency.CompensatedInventory;

public class EnchantmentHelper {
   public static int getMaximumEnchantLevel(CompensatedInventory inventory, EnchantmentType enchantmentType) {
      int maxEnchantLevel = 0;
      ItemStack helmet = inventory.getHelmet();
      if (helmet != ItemStack.EMPTY) {
         maxEnchantLevel = Math.max(maxEnchantLevel, helmet.getEnchantmentLevel(enchantmentType));
      }

      ItemStack chestplate = inventory.getChestplate();
      if (chestplate != ItemStack.EMPTY) {
         maxEnchantLevel = Math.max(maxEnchantLevel, chestplate.getEnchantmentLevel(enchantmentType));
      }

      ItemStack leggings = inventory.getLeggings();
      if (leggings != ItemStack.EMPTY) {
         maxEnchantLevel = Math.max(maxEnchantLevel, leggings.getEnchantmentLevel(enchantmentType));
      }

      ItemStack boots = inventory.getBoots();
      if (boots != ItemStack.EMPTY) {
         maxEnchantLevel = Math.max(maxEnchantLevel, boots.getEnchantmentLevel(enchantmentType));
      }

      return maxEnchantLevel;
   }
}
