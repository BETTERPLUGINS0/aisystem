package com.nisovin.shopkeepers.config.lib;

import com.nisovin.shopkeepers.util.data.container.DataContainer;
import com.nisovin.shopkeepers.util.data.container.DelegateDataContainer;
import org.checkerframework.checker.nullness.qual.Nullable;

public class SimpleConfigData extends DelegateDataContainer implements ConfigData {
   @Nullable
   private DataContainer defaults = null;

   public SimpleConfigData(DataContainer dataContainer) {
      super(dataContainer);
   }

   @Nullable
   public DataContainer getDefaults() {
      return this.defaults;
   }

   public void setDefaults(@Nullable DataContainer defaults) {
      this.defaults = defaults;
   }
}
