package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.PropertySelectorCondition;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardTrainProperty;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class GravityProperty extends FieldBackedStandardTrainProperty.StandardDouble {
   @CommandTargetTrain
   @PropertyCheckPermission("gravity")
   @Command("train gravity <multiplier>")
   @CommandDescription("Sets a gravity effect multiplier for the train")
   private void trainSetProperty(CommandSender sender, TrainProperties properties, @Argument("multiplier") double multiplier) {
      properties.setGravity(multiplier);
      this.trainGetProperty(sender, properties);
   }

   @Command("train gravity")
   @CommandDescription("Displays the gravity multiplier currently set for the train")
   private void trainGetProperty(CommandSender sender, TrainProperties properties) {
      if (properties.getGravity() == 1.0D) {
         sender.sendMessage(ChatColor.YELLOW + "Gravity multiplier: " + ChatColor.WHITE + "1 X (default)");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Gravity multiplier: " + ChatColor.WHITE + properties.getGravity() + " X");
      }

   }

   @PropertyParser("gravity")
   public double parseGravity(PropertyParseContext<Double> context) {
      return context.inputDouble();
   }

   @PropertySelectorCondition("gravity")
   public double selectorgetValue(TrainProperties properties) {
      return this.getDouble(properties);
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_GRAVITY.has(sender);
   }

   public double getDoubleDefault() {
      return 1.0D;
   }

   public double getDoubleData(FieldBackedProperty.TrainInternalData data) {
      return data.gravity;
   }

   public void setDoubleData(FieldBackedProperty.TrainInternalData data, double value) {
      data.gravity = value;
   }

   public Optional<Double> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "gravity", Double.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Double> value) {
      Util.setConfigOptional(config, "gravity", value);
   }
}
