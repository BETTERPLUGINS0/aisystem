package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.util.data.container.DataContainer;

public class ConfigMigration5 implements ConfigMigration {
   public void apply(DataContainer configData) {
      ConfigMigrationHelper.removeSetting(configData, "zero-currency-item");
      ConfigMigrationHelper.removeSetting(configData, "zero-high-currency-item");
   }
}
