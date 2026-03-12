package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.tradelog.TradeLogStorageType;
import com.nisovin.shopkeepers.util.data.container.DataContainer;

public class ConfigMigration8 implements ConfigMigration {
   public void apply(DataContainer configData) {
      boolean logTradesToCsv = configData.getBoolean("log-trades-to-csv");
      ConfigMigrationHelper.removeSetting(configData, "log-trades-to-csv");
      ConfigMigrationHelper.addSetting(configData, "trade-log-storage", logTradesToCsv ? TradeLogStorageType.CSV.name() : TradeLogStorageType.DISABLED.name());
   }
}
