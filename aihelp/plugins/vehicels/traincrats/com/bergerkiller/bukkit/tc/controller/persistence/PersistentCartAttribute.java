package com.bergerkiller.bukkit.tc.controller.persistence;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.entity.CommonEntity;

public interface PersistentCartAttribute<E extends CommonEntity<?>> {
   void save(E var1, ConfigurationNode var2);

   void load(E var1, ConfigurationNode var2);
}
