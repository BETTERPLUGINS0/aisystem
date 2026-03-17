package com.bergerkiller.bukkit.tc.controller.functions.ui;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunction;
import com.bergerkiller.bukkit.tc.controller.functions.TransferFunctionHost;
import java.util.function.BooleanSupplier;

public abstract class MapWidgetTransferFunctionSingleConfigItem extends MapWidgetTransferFunctionSingleItem {
   private final ConfigurationNode config;
   private final String configKey;

   public MapWidgetTransferFunctionSingleConfigItem(TransferFunctionHost host, ConfigurationNode config, String configKey, BooleanSupplier isBooleanInput) {
      super(host, config.getNodeIfExists(configKey), isBooleanInput);
      this.config = config;
      this.configKey = configKey;
   }

   public void onChanged(TransferFunction.Holder<TransferFunction> function) {
      if (function.isDefault()) {
         this.config.remove(this.configKey);
      } else {
         this.config.set(this.configKey, this.host.saveFunction(function.getFunction()));
      }

   }
}
