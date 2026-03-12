package com.nisovin.shopkeepers.config.migration;

import com.nisovin.shopkeepers.util.data.container.DataContainer;

public interface ConfigMigration {
   void apply(DataContainer var1);
}
