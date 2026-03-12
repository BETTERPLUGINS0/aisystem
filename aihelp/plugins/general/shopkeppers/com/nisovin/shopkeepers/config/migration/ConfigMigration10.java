package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.util.data.container.DataContainer;

public class ConfigMigration10 implements ConfigMigration {
   public void apply(DataContainer configData) {
      ConfigMigrationHelper.removeSetting(configData, "create-player-shop-with-command");
      ConfigMigrationHelper.migrateSetting(configData, "mob-behavior-tick-period", "entity-behavior-tick-period");
      ConfigMigrationHelper.migrateSetting(configData, "silence-living-shop-entities", "silence-shop-entities");
   }
}
