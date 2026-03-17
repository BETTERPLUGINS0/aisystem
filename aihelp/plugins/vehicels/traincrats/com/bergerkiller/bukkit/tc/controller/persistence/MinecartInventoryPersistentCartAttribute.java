package com.bergerkiller.bukkit.tc.controller.persistence;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartInventory;
import com.bergerkiller.bukkit.tc.Util;

public class MinecartInventoryPersistentCartAttribute implements PersistentCartAttribute<CommonMinecartInventory<?>> {
   public void save(CommonMinecartInventory<?> entity, ConfigurationNode data) {
      Util.saveInventoryToConfig(entity.getInventory(), data);
   }

   public void load(CommonMinecartInventory<?> entity, ConfigurationNode data) {
      Util.loadInventoryFromConfig(entity.getInventory(), data);
   }
}
