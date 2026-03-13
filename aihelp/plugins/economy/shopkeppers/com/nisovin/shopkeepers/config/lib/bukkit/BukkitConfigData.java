package com.nisovin.shopkeepers.config.lib.bukkit;

import com.nisovin.shopkeepers.config.lib.SimpleConfigData;
import com.nisovin.shopkeepers.util.data.container.ConfigBasedDataContainer;
import com.nisovin.shopkeepers.util.data.container.DataContainer;
import org.bukkit.configuration.ConfigurationSection;
import org.checkerframework.checker.nullness.qual.Nullable;

public class BukkitConfigData extends SimpleConfigData {
   private boolean defaultsSetup = false;

   public BukkitConfigData(ConfigBasedDataContainer dataContainer) {
      super(dataContainer);
   }

   @Nullable
   private ConfigurationSection getDefaultConfig() {
      return ((ConfigBasedDataContainer)this.dataContainer).getConfig().getDefaultSection();
   }

   @Nullable
   public DataContainer getDefaults() {
      if (!this.defaultsSetup) {
         this.setDefaults(DataContainer.of(this.getDefaultConfig()));
      }

      return super.getDefaults();
   }

   public void setDefaults(@Nullable DataContainer defaults) {
      super.setDefaults(defaults);
      this.defaultsSetup = true;
   }
}
