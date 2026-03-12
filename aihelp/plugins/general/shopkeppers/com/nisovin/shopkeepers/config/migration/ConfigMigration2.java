package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.util.bukkit.DataUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.inventory.ItemData;
import com.nisovin.shopkeepers.util.java.ConversionUtils;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.List;
import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.Nullable;

public class ConfigMigration2 implements ConfigMigration {
   public void apply(DataContainer configData) {
      migrateItem(configData, "shop-creation-item", "shop-creation-item", "shop-creation-item-name", "shop-creation-item-lore");
      migrateItem(configData, "name-item", "name-item", (String)null, "name-item-lore");
      migrateItem(configData, "hire-item", "hire-item", "hire-item-name", "hire-item-lore");
      migrateItem(configData, "currency-item", "currency-item", "currency-item-name", "currency-item-lore");
      migrateItem(configData, "zero-currency-item", "zero-currency-item", "zero-currency-item-name", "zero-currency-item-lore");
      migrateItem(configData, "high-currency-item", "high-currency-item", "high-currency-item-name", "high-currency-item-lore");
      migrateItem(configData, "zero-high-currency-item", "high-zero-currency-item", "high-zero-currency-item-name", "high-zero-currency-item-lore");
   }

   private static void migrateItem(DataContainer configData, String newItemKey, String itemTypeKey, @Nullable String displayNameKey, @Nullable String loreKey) {
      assert configData != null && newItemKey != null && itemTypeKey != null;

      StringBuilder msgBuilder = new StringBuilder();
      msgBuilder.append("  Migrating item data for '").append(itemTypeKey).append("' (").append(configData.get(itemTypeKey)).append(")");
      if (displayNameKey != null) {
         msgBuilder.append(" and '").append(displayNameKey).append("' (").append(configData.get(displayNameKey)).append(")");
      }

      if (loreKey != null) {
         msgBuilder.append(" and '").append(loreKey).append("' (").append(configData.get(loreKey)).append(")");
      }

      msgBuilder.append(" to new format at '").append(newItemKey).append("'.");
      Log.info(msgBuilder.toString());
      Material itemType = DataUtils.loadMaterial(configData, itemTypeKey);
      if (itemType == null) {
         Log.warning("    Skipping migration for item '" + itemTypeKey + "'! Unknown material: " + String.valueOf(configData.get(itemTypeKey)));
      } else {
         String displayName = null;
         if (displayNameKey != null) {
            displayName = configData.getString(displayNameKey);
            if (displayName != null && !displayName.isEmpty()) {
               displayName = TextUtils.colorize(displayName);
            } else {
               displayName = null;
            }
         }

         List<String> lore = null;
         if (loreKey != null) {
            lore = ConversionUtils.toStringList(configData.getList(loreKey));
            if (lore != null && !lore.isEmpty()) {
               lore = TextUtils.colorize(lore);
            } else {
               lore = null;
            }
         }

         ItemData itemData = new ItemData(itemType, displayName, lore);
         if (!itemTypeKey.equals(newItemKey)) {
            configData.remove(itemTypeKey);
         }

         if (displayNameKey != null) {
            configData.remove(displayNameKey);
         }

         if (loreKey != null) {
            configData.remove(loreKey);
         }

         configData.set(newItemKey, itemData.serialize());
      }
   }
}
