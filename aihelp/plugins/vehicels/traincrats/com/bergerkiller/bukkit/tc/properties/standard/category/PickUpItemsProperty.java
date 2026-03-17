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
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardCartProperty;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class PickUpItemsProperty extends FieldBackedStandardCartProperty<Boolean> {
   @CommandTargetTrain
   @PropertyCheckPermission("pickupitems")
   @Command("train pickupitems|pickup <pickup>")
   @CommandDescription("Sets whether the train picks up items off the ground")
   private void setProperty(CommandSender sender, TrainProperties properties, @Argument("pickup") boolean pickup) {
      properties.set(this, pickup);
      this.getProperty(sender, properties);
   }

   @Command("train pickupitems|pickup")
   @CommandDescription("Displays whether the train picks up items off the ground")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Train picks up items off the ground: " + Localization.boolStr((Boolean)properties.get(this)));
   }

   @CommandTargetTrain
   @PropertyCheckPermission("pickupitems")
   @Command("cart pickupitems|pickup <pickup>")
   @CommandDescription("Sets whether the cart picks up items off the ground")
   private void setProperty(CommandSender sender, CartProperties properties, @Argument("pickup") boolean pickup) {
      properties.set(this, pickup);
      this.getProperty(sender, properties);
   }

   @Command("cart pickupitems|pickup")
   @CommandDescription("Displays whether the cart picks up items off the ground")
   private void getProperty(CommandSender sender, CartProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Cart picks up items off the ground: " + Localization.boolStr((Boolean)properties.get(this)));
   }

   @PropertyParser("pickup|pickupitems")
   public boolean parsePickupItems(PropertyParseContext<Boolean> context) {
      return context.inputBoolean();
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_PICKUPITEMS.has(sender);
   }

   public Boolean getDefault() {
      return Boolean.FALSE;
   }

   public Boolean getData(FieldBackedProperty.CartInternalData data) {
      return data.pickUpItems;
   }

   public void setData(FieldBackedProperty.CartInternalData data, Boolean value) {
      data.pickUpItems = value;
   }

   public Optional<Boolean> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "pickUp", Boolean.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Boolean> value) {
      Util.setConfigOptional(config, "pickUp", value);
   }
}
