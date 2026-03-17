package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ITrainProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class AllowPlayerTakeProperty implements ITrainProperty<Boolean> {
   @CommandTargetTrain
   @PropertyCheckPermission("allowplayertake")
   @Command("train allowplayertake|playertake <allow>")
   @CommandDescription("Sets whether players take carts of the train with them when they leave the server")
   private void commandSetProperty(CommandSender sender, TrainProperties properties, @Argument("allow") boolean allow) {
      properties.set(this, allow);
      this.commandGetProperty(sender, properties);
   }

   @Command("train allowplayertake|playertake")
   @CommandDescription("Displays whether players take carts of the train with them when they leave the server")
   private void commandGetProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Players can take carts with them when they leave the server: " + Localization.boolStr((Boolean)properties.get(this)));
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_ALLOWPLAYERTAKE.has(sender);
   }

   public Boolean getDefault() {
      return Boolean.FALSE;
   }

   public Optional<Boolean> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "allowPlayerTake", Boolean.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Boolean> value) {
      Util.setConfigOptional(config, "allowPlayerTake", value);
   }
}
