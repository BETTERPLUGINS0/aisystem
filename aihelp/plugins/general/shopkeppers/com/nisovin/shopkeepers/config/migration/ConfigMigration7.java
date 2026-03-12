package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.util.data.container.DataContainer;

public class ConfigMigration7 implements ConfigMigration {
   public void apply(DataContainer configData) {
      ConfigMigrationHelper.removeSetting(configData, "file-encoding");
   }
}
