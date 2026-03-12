package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.util.data.container.DataContainer;

public class ConfigMigration4 implements ConfigMigration {
   public void apply(DataContainer configData) {
      ConfigMigrationHelper.migrateValue(configData, "language", "en", "en-default");
      ConfigMigrationHelper.migrateValue(configData, "max-shops-per-player", 0, -1);
      ConfigMigrationHelper.removeSetting(configData, "enable-spawn-verifier");
      ConfigMigrationHelper.migrateSetting(configData, "enable-purchase-logging", "log-trades-to-csv");
   }
}
