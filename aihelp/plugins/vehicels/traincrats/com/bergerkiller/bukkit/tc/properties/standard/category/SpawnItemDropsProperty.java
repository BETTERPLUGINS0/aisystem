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

public final class SpawnItemDropsProperty implements ICartProperty<Boolean> {
   @CommandTargetTrain
   @PropertyCheckPermission("spawnitemdrops")
   @Command("train spawnitemdrops <spawn>")
   @CommandDescription("Sets whether the train drops items when destroyed")
   private void setProperty(CommandSender sender, TrainProperties properties, @Argument("spawn") boolean spawn) {
      properties.set(this, spawn);
      this.getProperty(sender, properties);
   }

   @Command("train spawnitemdrops")
   @CommandDescription("Displays whether the train drops items when destroyed")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Train drops items when destroyed: " + Localization.boolStr((Boolean)properties.get(this)));
   }

   @CommandTargetTrain
   @PropertyCheckPermission("spawnitemdrops")
   @Command("cart spawnitemdrops <spawn>")
   @CommandDescription("Sets whether the cart drops items when destroyed")
   private void setProperty(CommandSender sender, CartProperties properties, @Argument("spawn") boolean spawn) {
      properties.set(this, spawn);
      this.getProperty(sender, properties);
   }

   @Command("cart spawnitemdrops")
   @CommandDescription("Displays whether the cart drops items when destroyed")
   private void getProperty(CommandSender sender, CartProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Cart drops items when destroyed: " + Localization.boolStr((Boolean)properties.get(this)));
   }

   @PropertyParser("spawnitemdrops|spawndrops|killdrops")
   public boolean parseSpawnItemDrops(PropertyParseContext<Boolean> context) {
      return context.inputBoolean();
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_SPAWNITEMDROPS.has(sender);
   }

   public Boolean getDefault() {
      return Boolean.TRUE;
   }

   public Optional<Boolean> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "spawnItemDrops", Boolean.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Boolean> value) {
      Util.setConfigOptional(config, "spawnItemDrops", value);
   }
}
