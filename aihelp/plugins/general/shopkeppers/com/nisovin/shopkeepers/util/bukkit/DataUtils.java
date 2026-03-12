package com.nisovin.shopkeepers.util.bukkit;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.inventory.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class DataUtils {
   @Nullable
   public static Material loadMaterial(DataContainer dataContainer, String key) {
      String materialName = dataContainer.getString(key);
      if (materialName == null) {
         return null;
      } else {
         Material material = ItemUtils.parseMaterial(materialName);
         return material != null && material.isLegacy() ? null : material;
      }
   }

   @Nullable
   public static ItemStack processLoadedItemStack(@Nullable @ReadOnly ItemStack loadedItemStack) {
      if (loadedItemStack == null) {
         return null;
      } else {
         ItemStack processed = loadedItemStack;
         if (loadedItemStack.getType() == Material.ENCHANTED_BOOK) {
            processed = ItemUtils.ensureBukkitItemStack(loadedItemStack);
         }

         return processed;
      }
   }

   public static ItemStack processNonNullLoadedItemStack(@ReadOnly ItemStack loadedItemStack) {
      return (ItemStack)Unsafe.assertNonNull(processLoadedItemStack(loadedItemStack));
   }

   private DataUtils() {
   }
}
