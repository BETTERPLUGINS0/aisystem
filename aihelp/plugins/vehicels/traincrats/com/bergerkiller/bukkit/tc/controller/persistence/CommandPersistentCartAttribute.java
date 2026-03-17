package com.bergerkiller.bukkit.tc.controller.persistence;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.entity.type.CommonMinecartCommandBlock;

public class CommandPersistentCartAttribute implements PersistentCartAttribute<CommonMinecartCommandBlock> {
   public void save(CommonMinecartCommandBlock entity, ConfigurationNode data) {
      data.set("command", entity.metaCommand.get());
   }

   public void load(CommonMinecartCommandBlock entity, ConfigurationNode data) {
      if (data.contains("command")) {
         entity.metaCommand.set((String)data.get("command", ""));
      }

   }
}
