package com.nisovin.shopkeepers.shopkeeper.player;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.inventory.EnchantmentUtils;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import com.nisovin.shopkeepers.util.inventory.PotionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.PolyNull;

public final class PlaceholderItems {
   public static ItemStack createPlaceholderItem(@Nullable String displayName) {
      ItemStack item = Settings.DerivedSettings.placeholderItemData.createItemStack();
      ItemUtils.setDisplayName(item, displayName);
      return item;
   }

   public static boolean isPlaceholderItem(@Nullable @ReadOnly ItemStack itemStack) {
      return ItemUtils.isEmpty(itemStack) ? false : Settings.DerivedSettings.placeholderItemData.matches(itemStack);
   }

   public static ItemStack getSubstitutedItem(@ReadOnly ItemStack placeholderItem) {
      if (!isPlaceholderItem(placeholderItem)) {
         return null;
      } else {
         assert placeholderItem != null;

         ItemMeta meta = placeholderItem.getItemMeta();
         if (meta == null) {
            return null;
         } else {
            String displayName = meta.getDisplayName();
            if (displayName.isEmpty()) {
               return null;
            } else {
               String normalizedDisplayName = normalizeDisplayName(displayName);
               Material material = ItemUtils.parseMaterial(normalizedDisplayName);
               if (material != null) {
                  if (!material.isLegacy() && !material.isAir() && material.isItem()) {
                     ItemStack substitutedItem = new ItemStack(material, placeholderItem.getAmount());
                     applyItemMeta(substitutedItem, meta);
                     return substitutedItem;
                  } else {
                     return null;
                  }
               } else {
                  EnchantmentUtils.EnchantmentWithLevel enchantmentWithLevel = EnchantmentUtils.parseEnchantmentWithLevel(normalizedDisplayName);
                  if (enchantmentWithLevel != null) {
                     Enchantment enchantment = enchantmentWithLevel.getEnchantment();
                     int level = enchantmentWithLevel.getLevel();
                     return EnchantmentUtils.createEnchantedBook(enchantment, level);
                  } else {
                     ItemStack potionItem = PotionUtils.parsePotionItem(normalizedDisplayName);
                     return potionItem != null ? potionItem : null;
                  }
               }
            }
         }
      }
   }

   private static void applyItemMeta(@ReadWrite ItemStack substitutedItem, ItemMeta placeholderMeta) {
      assert substitutedItem != null && placeholderMeta != null;

      if (substitutedItem.getType().isBlock()) {
         if (placeholderMeta instanceof BlockDataMeta) {
            BlockDataMeta blockStateMeta = (BlockDataMeta)placeholderMeta;
            if (blockStateMeta.hasBlockData()) {
               ItemMeta substitutedMeta = substitutedItem.getItemMeta();
               if (substitutedMeta instanceof BlockDataMeta) {
                  BlockDataMeta substitutedBlockStateMeta = (BlockDataMeta)substitutedMeta;
                  substitutedBlockStateMeta.setBlockData(blockStateMeta.getBlockData(substitutedItem.getType()));
                  substitutedItem.setItemMeta(substitutedMeta);
               }
            }
         }
      }
   }

   @Nullable
   public static Material getSubstitutedMaterial(@Nullable @ReadOnly ItemStack placeholderItem) {
      if (!isPlaceholderItem(placeholderItem)) {
         return null;
      } else {
         assert placeholderItem != null;

         ItemMeta meta = (ItemMeta)Unsafe.assertNonNull(placeholderItem.getItemMeta());
         String displayName = meta.getDisplayName();
         if (displayName.isEmpty()) {
            return null;
         } else {
            String normalizedDisplayName = normalizeDisplayName(displayName);
            Material material = ItemUtils.parseMaterial(normalizedDisplayName);
            if (material != null) {
               return !material.isLegacy() && !material.isAir() ? material : null;
            } else {
               return null;
            }
         }
      }
   }

   private static String normalizeDisplayName(String displayName) {
      String normalizedDisplayName = displayName.trim();
      normalizedDisplayName = (String)Unsafe.assertNonNull(ChatColor.stripColor(normalizedDisplayName));
      return normalizedDisplayName;
   }

   public static boolean isValidPlaceholderItem(@Nullable @ReadOnly ItemStack itemStack) {
      return getSubstitutedItem(itemStack) != null;
   }

   @PolyNull
   public static ItemStack replace(@PolyNull @ReadOnly ItemStack itemStack) {
      ItemStack substitutedItem = getSubstitutedItem(itemStack);
      return substitutedItem != null ? substitutedItem : itemStack;
   }

   public static ItemStack replaceNonNull(@ReadOnly ItemStack itemStack) {
      return (ItemStack)Unsafe.assertNonNull(replace(itemStack));
   }

   @PolyNull
   public static UnmodifiableItemStack replace(@PolyNull UnmodifiableItemStack itemStack) {
      return UnmodifiableItemStack.of(replace(ItemUtils.asItemStackOrNull(itemStack)));
   }

   public static UnmodifiableItemStack replaceNonNull(UnmodifiableItemStack itemStack) {
      return (UnmodifiableItemStack)Unsafe.assertNonNull(replace(itemStack));
   }

   private PlaceholderItems() {
   }
}
