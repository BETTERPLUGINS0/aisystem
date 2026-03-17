package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Greedy;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ITrainProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class DisplayNameProperty implements ITrainProperty<String> {
   @CommandTargetTrain
   @PropertyCheckPermission("displayname")
   @Command("train displayname <name>")
   @CommandDescription("Sets the display name of the train")
   private void setProperty(CommandSender sender, TrainProperties properties, @Argument("name") @Greedy String name) {
      properties.set(this, name);
      this.getProperty(sender, properties);
   }

   @Command("train displayname")
   @CommandDescription("Displays whether the current display name of the train")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Display name of the train: " + ChatColor.WHITE + (String)properties.get(this));
   }

   @PropertyParser("dname|displayname|setdisplayname|setdname")
   public String parseName(String input) {
      return input;
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_DISPLAYNAME.has(sender);
   }

   public String getDefault() {
      return "";
   }

   public Optional<String> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "displayName", String.class);
   }

   public void writeToConfig(ConfigurationNode config, Optional<String> value) {
      Util.setConfigOptional(config, "displayName", value);
   }
}
