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

public final class InvincibleProperty implements ICartProperty<Boolean> {
   @CommandTargetTrain
   @PropertyCheckPermission("invincible")
   @Command("train invincible|godmode <invincible>")
   @CommandDescription("Sets whether the train is invincible to damage")
   private void setProperty(CommandSender sender, TrainProperties properties, @Argument("invincible") boolean invincible) {
      properties.set(this, invincible);
      this.getProperty(sender, properties);
   }

   @Command("train invincible|godmode")
   @CommandDescription("Displays whether the train is invincible to damage")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Train is invincible to damage: " + Localization.boolStr((Boolean)properties.get(this)));
   }

   @CommandTargetTrain
   @PropertyCheckPermission("invincible")
   @Command("cart invincible|godmode <invincible>")
   @CommandDescription("Sets whether the cart is invincible to damage")
   private void setProperty(CommandSender sender, CartProperties properties, @Argument("invincible") boolean invincible) {
      properties.set(this, invincible);
      this.getProperty(sender, properties);
   }

   @Command("cart invincible|godmode")
   @CommandDescription("Displays whether the cart is invincible to damage")
   private void getProperty(CommandSender sender, CartProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Cart is invincible to damage: " + Localization.boolStr((Boolean)properties.get(this)));
   }

   @PropertyParser("invincible|godmode")
   public boolean parseInvincible(PropertyParseContext<Boolean> context) {
      return context.inputBoolean();
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_INVINCIBLE.has(sender);
   }

   public Boolean getDefault() {
      return Boolean.FALSE;
   }

   public Optional<Boolean> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "invincible", Boolean.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Boolean> value) {
      Util.setConfigOptional(config, "invincible", value);
   }
}
