package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TCConfig;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyInvalidInputException;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.PropertySelectorCondition;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardTrainProperty;
import com.bergerkiller.bukkit.tc.utils.FormattedSpeed;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class SpeedLimitProperty extends FieldBackedStandardTrainProperty.StandardDouble {
   @CommandTargetTrain
   @PropertyCheckPermission("maxspeed")
   @Command("train maxspeed|speedlimit <speed>")
   @CommandDescription("Sets a new  speed limit for the train")
   private void trainSetSpeedLimit(CommandSender sender, TrainProperties properties, @Argument("speed") FormattedSpeed speed) {
      properties.setSpeedLimit(speed.getValue());
      this.trainGetSpeedLimit(sender, properties);
   }

   @Command("train maxspeed|speedlimit")
   @CommandDescription("Reads the current speed limit set for the train")
   private void trainGetSpeedLimit(CommandSender sender, TrainProperties properties) {
      double currSpeed = properties.hasHolder() ? properties.getHolder().head().getRealSpeedLimited() : 0.0D;
      sender.sendMessage(ChatColor.YELLOW + "Maximum speed: " + formatSpeed(properties.getSpeedLimit(), ChatColor.WHITE));
      sender.sendMessage(ChatColor.YELLOW + "Current speed: " + formatSpeed(currSpeed, currSpeed == properties.getSpeedLimit() ? ChatColor.RED : ChatColor.WHITE));
   }

   private static String formatSpeed(double speed, ChatColor baseColor) {
      double speedKMH = MathUtil.round(speed * 72000.0D / 1000.0D, 2);
      double speedMPH = MathUtil.round(speed * 72000.0D / 1609.344D, 2);
      return baseColor.toString() + MathUtil.round(speed, 4) + " blocks/tick (" + ChatColor.BLUE + speedKMH + " km/h" + baseColor + " / " + ChatColor.BLUE + speedMPH + " mph" + baseColor + ")";
   }

   @PropertyParser("maxspeed|speedlimit")
   public double parse(String input) {
      double result = Util.parseVelocity(input, Double.NaN);
      if (Double.isNaN(result)) {
         throw new PropertyInvalidInputException("Not a valid number or speed expression");
      } else {
         return result;
      }
   }

   @PropertySelectorCondition("speedlimit")
   public double getSelectorDoubleValue(TrainProperties properties) {
      return this.getDouble(properties);
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_MAXSPEED.has(sender);
   }

   public double getDoubleDefault() {
      return 0.4D;
   }

   public double getDoubleData(FieldBackedProperty.TrainInternalData data) {
      return data.speedLimit;
   }

   public void setDoubleData(FieldBackedProperty.TrainInternalData data, double value) {
      data.speedLimit = value;
   }

   public Optional<Double> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "speedLimit", Double.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Double> value) {
      Util.setConfigOptional(config, "speedLimit", value);
   }

   public void set(TrainProperties properties, Double value) {
      double valuePrim = value;
      if (valuePrim < 0.0D) {
         value = 0.0D;
      } else if (valuePrim > TCConfig.maxVelocity) {
         value = TCConfig.maxVelocity;
      }

      super.set(properties, value);
   }
}
