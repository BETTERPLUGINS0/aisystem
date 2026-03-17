package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ICartProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class PlayerExitProperty implements ICartProperty<Boolean> {
   @CommandTargetTrain
   @PropertyCheckPermission("playerexit")
   @Command("train playerexit|allowplayerexit|playerleave <allow>")
   @CommandDescription("Sets whether players can exit from carts of this train")
   private void commandSetProperty(CommandSender sender, TrainProperties properties, @Argument("allow") boolean allow) {
      properties.setPlayersExit(allow);
      this.commandGetProperty(sender, properties);
   }

   @Command("train playerexit|allowplayerexit|playerleave")
   @CommandDescription("Gets whether players can exit from carts of this train")
   private void commandGetProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Players can exit the train: " + Localization.boolStr(properties.getPlayersExit()));
   }

   @CommandTargetTrain
   @PropertyCheckPermission("playerexit")
   @Command("cart playerexit|allowplayerexit|playerleave <allow>")
   @CommandDescription("Sets whether players can exit the cart")
   private void commandSetProperty(CommandSender sender, CartProperties properties, @Argument("allow") boolean allow) {
      properties.setPlayersExit(allow);
      this.commandGetProperty(sender, properties);
   }

   @Command("cart playerexit|allowplayerexit|playerleave")
   @CommandDescription("Gets whether players can exit the cart")
   private void commandGetProperty(CommandSender sender, CartProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Players can exit the cart: " + Localization.boolStr(properties.getPlayersExit()));
   }

   @PropertyParser("allowplayerexit|playerexit")
   public boolean parsePlayerExit(PropertyParseContext<Boolean> context) {
      return context.inputBoolean();
   }

   public String getListedName() {
      return "playerexit";
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_PLAYEREXIT.has(sender);
   }

   public Boolean getDefault() {
      return Boolean.TRUE;
   }

   public Optional<Boolean> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "allowPlayerExit", Boolean.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Boolean> value) {
      Util.setConfigOptional(config, "allowPlayerExit", value);
   }
}
