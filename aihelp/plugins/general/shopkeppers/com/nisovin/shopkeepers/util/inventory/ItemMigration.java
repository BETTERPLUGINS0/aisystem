package com.nisovin.shopkeepers.util.inventory;

import com.nisovin.shopkeepers.api.util.UnmodifiableItemStack;
import com.nisovin.shopkeepers.util.annotations.ReadOnly;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ItemMigration {
   @Nullable
   private static Inventory DUMMY_INVENTORY = null;

   @Nullable
   public static ItemStack migrateItemStack(@Nullable @ReadOnly ItemStack itemStack) {
      if (itemStack == null) {
         return null;
      } else {
         Inventory inventory = DUMMY_INVENTORY;
         if (inventory == null) {
            inventory = Bukkit.createInventory((InventoryHolder)null, 9);
            DUMMY_INVENTORY = inventory;
         }

         assert inventory != null;

         inventory.setItem(0, itemStack);
         ItemStack convertedItemStack = inventory.getItem(0);
         inventory.setItem(0, (ItemStack)null);
         return convertedItemStack;
      }
   }

   @Nullable
   public static UnmodifiableItemStack migrateItemStack(@Nullable UnmodifiableItemStack itemStack) {
      return UnmodifiableItemStack.of(migrateItemStack(ItemUtils.asItemStackOrNull(itemStack)));
   }

   public static ItemStack migrateNonNullItemStack(@ReadOnly ItemStack itemStack) {
      Validate.notNull(itemStack, (String)"itemStack is null");
      ItemStack migrated = migrateItemStack(itemStack);
      return (ItemStack)Validate.State.notNull(migrated, (Supplier)(() -> {
         return "Migrated ItemStack is null! Original: " + String.valueOf(itemStack);
      }));
   }

   private ItemMigration() {
   }
}
