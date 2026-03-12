package com.nisovin.shopkeepers.config.lib;

import com.nisovin.shopkeepers.config.lib.bukkit.BukkitConfigData;
import com.nisovin.shopkeepers.util.data.container.ConfigBasedDataContainer;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.java.Validate;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface ConfigData extends DataContainer {
   static ConfigData create() {
      return of(DataContainer.create());
   }

   static ConfigData of(DataContainer dataContainer) {
      Validate.notNull(dataContainer, (String)"dataContainer is null");
      return (ConfigData)(dataContainer instanceof ConfigBasedDataContainer ? new BukkitConfigData((ConfigBasedDataContainer)dataContainer) : new SimpleConfigData(dataContainer));
   }

   static ConfigData of(Object dataSource) {
      return of(DataContainer.ofNonNull(dataSource));
   }

   @Nullable
   DataContainer getDefaults();

   void setDefaults(@Nullable DataContainer var1);
}
