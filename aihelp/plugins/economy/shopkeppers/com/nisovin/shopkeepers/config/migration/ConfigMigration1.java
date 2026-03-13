package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.util.bukkit.DataUtils;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.Material;

public class ConfigMigration1 implements ConfigMigration {
   public void apply(DataContainer configData) {
      if (configData.contains("shop-creation-item-spawn-egg-entity-type")) {
         Log.info("  Migration of 'shop-creation-item-spawn-egg-entity-type' is no longer supported.");
         ConfigMigrationHelper.removeSetting(configData, "shop-creation-item-spawn-egg-entity-type");
      }

      migrateLegacyItemData(configData, "shop-creation-item", "shop-creation-item-data", Material.VILLAGER_SPAWN_EGG);
      migrateLegacyItemData(configData, "name-item", "name-item-data", Material.NAME_TAG);
      migrateLegacyItemData(configData, "chest-item", "chest-item-data", Material.CHEST);
      migrateLegacyItemData(configData, "delete-item", "delete-item-data", Material.BONE);
      migrateLegacyItemData(configData, "hire-item", "hire-item-data", Material.EMERALD);
      migrateLegacyItemData(configData, "currency-item", "currency-item-data", Material.EMERALD);
      migrateLegacyItemData(configData, "zero-currency-item", "zero-currency-item-data", Material.BARRIER);
      migrateLegacyItemData(configData, "high-currency-item", "high-currency-item-data", Material.EMERALD_BLOCK);
      migrateLegacyItemData(configData, "high-zero-currency-item", "high-zero-currency-item-data", Material.BARRIER);
   }

   private static boolean migrateLegacyItemData(DataContainer configData, String itemTypeKey, String itemDataKey, Material defaultType) {
      boolean migrated = false;
      String itemTypeName = configData.getString(itemTypeKey);
      if (itemTypeName != null) {
         int itemData = configData.getInt(itemDataKey);
         Material itemType = DataUtils.loadMaterial(configData, itemTypeKey);
         if (itemType == null) {
            Log.info("  Migration of '" + itemTypeKey + "' from type '" + itemTypeName + "' and data value '" + itemData + "' is no longer supported. Falling back to default type '" + defaultType.name() + "'.");
            configData.set((String)itemTypeKey, defaultType.name());
            migrated = true;
         }
      }

      if (ConfigMigrationHelper.removeSetting(configData, itemDataKey)) {
         migrated = true;
      }

      return migrated;
   }
}
