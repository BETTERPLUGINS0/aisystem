package com.bergerkiller.bukkit.tc.properties.api;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import java.util.Optional;

public interface ISyntheticProperty<T> extends IProperty<T> {
   default Optional<T> readFromConfig(ConfigurationNode config) {
      return Optional.empty();
   }

   default void writeToConfig(ConfigurationNode config, Optional<T> value) {
   }

   default boolean isAppliedAsDefault() {
      return false;
   }
}
