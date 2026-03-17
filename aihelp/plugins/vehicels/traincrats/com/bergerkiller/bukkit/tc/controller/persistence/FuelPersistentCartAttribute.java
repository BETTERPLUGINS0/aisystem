package com.bergerkiller.bukkit.tc.controller.persistence;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartFurnace;

public class FuelPersistentCartAttribute implements PersistentCartAttribute<CommonMinecartFurnace> {
   public void save(CommonMinecartFurnace entity, ConfigurationNode data) {
      if (entity.getFuelTicks() > 0) {
         data.set("fuel", entity.getFuelTicks());
      } else {
         data.remove("fuel");
      }

   }

   public void load(CommonMinecartFurnace entity, ConfigurationNode data) {
      if (data.contains("fuel")) {
         entity.setFuelTicks((Integer)data.get("fuel", 0));
      } else {
         entity.setFuelTicks(0);
      }

      entity.setSmoking(entity.getFuelTicks() > 0);
   }
}
