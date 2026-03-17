package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.properties.api.ICartProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import java.util.Optional;
import org.bukkit.command.CommandSender;

public final class PlayerEnterAndExitProperty implements ICartProperty<Boolean> {
   @PropertyParser("playerenterexit|playerexitenter")
   public boolean parsePlayerExit(PropertyParseContext<Boolean> context) {
      return context.inputBoolean();
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_PLAYEREXIT.has(sender) && Permission.PROPERTY_PLAYERENTER.has(sender);
   }

   public boolean isAppliedAsDefault() {
      return false;
   }

   public boolean isListed() {
      return false;
   }

   public Boolean getDefault() {
      return Boolean.TRUE;
   }

   public Optional<Boolean> readFromConfig(ConfigurationNode config) {
      Optional<Boolean> enter = StandardProperties.ALLOW_PLAYER_ENTER.readFromConfig(config);
      Optional<Boolean> exit = StandardProperties.ALLOW_PLAYER_EXIT.readFromConfig(config);
      return !enter.isPresent() && !exit.isPresent() ? Optional.empty() : Optional.of((Boolean)enter.orElse(Boolean.TRUE) && (Boolean)exit.orElse(Boolean.TRUE));
   }

   public void writeToConfig(ConfigurationNode config, Optional<Boolean> value) {
      StandardProperties.ALLOW_PLAYER_ENTER.writeToConfig(config, value);
      StandardProperties.ALLOW_PLAYER_EXIT.writeToConfig(config, value);
   }
}
