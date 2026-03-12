package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.util.data.container.DataContainer;

public class ConfigMigration6 implements ConfigMigration {
   public void apply(DataContainer configData) {
      ConfigMigrationHelper.addSetting(configData, "add-shop-creation-item-tag", false);
      ConfigMigrationHelper.addSetting(configData, "identify-shop-creation-item-by-tag", false);
   }
}
