package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Default;
import com.bergerkiller.bukkit.tc.Localization;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyInvalidInputException;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardTrainProperty;
import com.bergerkiller.bukkit.tc.properties.standard.type.WaitOptions;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class WaitOptionsProperty extends FieldBackedStandardTrainProperty<WaitOptions> {
   @CommandTargetTrain
   @PropertyCheckPermission("waitdistance")
   @Command("train wait distance <blocks>")
   @CommandDescription("Sets the distance to keep to other trains")
   private void setDistanceProperty(CommandSender sender, TrainProperties properties, @Argument(value = "blocks",description = "Number of blocks distance") double distance) {
      properties.update(this, (options) -> {
         return WaitOptions.create(distance, options.delay(), options.acceleration(), options.deceleration(), options.predict());
      });
      this.getDistanceProperty(sender, properties);
   }

   @Command("train wait distance")
   @CommandDescription("Displays the distance to keep to other trains")
   private void getDistanceProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Distance to keep to other trains: " + ChatColor.GREEN + properties.getWaitDistance() + " blocks");
   }

   @CommandTargetTrain
   @PropertyCheckPermission("waitdelay")
   @Command("train wait delay <time>")
   @CommandDescription("Sets the time a train waits when fully stopped to wait")
   private void setDelayProperty(CommandSender sender, TrainProperties properties, @Argument(value = "time",description = "Time to wait in seconds") double delay) {
      properties.update(this, (options) -> {
         return WaitOptions.create(options.distance(), delay, options.acceleration(), options.deceleration(), options.predict());
      });
      this.getDelayProperty(sender, properties);
   }

   @Command("train wait delay")
   @CommandDescription("Displays the time a train waits when fully stopped to wait")
   private void getDelayProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Wait delay when stopped: " + ChatColor.GREEN + properties.getWaitDelay() + " seconds");
   }

   @CommandTargetTrain
   @PropertyCheckPermission("waitacceleration")
   @Command("train wait acceleration <acceleration> [deceleration]")
   @CommandDescription("Sets the rate of acceleration (and deceleration) of the train")
   private void setAccelerationProperty(CommandSender sender, TrainProperties properties, @Argument(value = "acceleration",parserName = "acceleration",description = "Acceleration in blocks/tick²") double acceleration, @Argument(value = "deceleration",parserName = "acceleration",description = "De-acceleration in blocks/tick²") @Default("NaN") double deceleration) {
      properties.update(this, (options) -> {
         return WaitOptions.create(options.distance(), options.delay(), acceleration, Double.isNaN(deceleration) ? acceleration : deceleration, options.predict());
      });
      this.getAccelerationProperty(sender, properties);
   }

   @Command("train wait acceleration")
   @CommandDescription("Displays the rate of acceleration (and deceleration) of the train")
   private void getAccelerationProperty(CommandSender sender, TrainProperties properties) {
      if (properties.getWaitAcceleration() == properties.getWaitDeceleration()) {
         sender.sendMessage(ChatColor.YELLOW + "Speeds up and slows down to wait at: " + ChatColor.GREEN + properties.getWaitAcceleration() + " blocks/tick²");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Slows down to wait at: " + ChatColor.GREEN + properties.getWaitDeceleration() + " blocks/tick²");
         sender.sendMessage(ChatColor.YELLOW + "Speeds up after waiting at: " + ChatColor.GREEN + properties.getWaitAcceleration() + " blocks/tick²");
      }

   }

   @CommandTargetTrain
   @PropertyCheckPermission("waitprediction")
   @Command("train wait predict <predict>")
   @CommandDescription("Sets whether the train will predict routing up ahead")
   private void setPredictProperty(CommandSender sender, TrainProperties properties, @Argument(value = "predict",description = "Whether to predict") boolean predict) {
      properties.update(this, (options) -> {
         return WaitOptions.create(options.distance(), options.delay(), options.acceleration(), options.deceleration(), predict);
      });
      this.getPredictProperty(sender, properties);
   }

   @Command("train wait predict")
   @CommandDescription("Displays whether the train will predict routing up ahead")
   private void getPredictProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Predict path up ahead: " + Localization.boolStr(properties.isWaitPredicted()));
   }

   @PropertyParser("waitdistance|wait distance")
   public WaitOptions parseWaitDistance(PropertyParseContext<WaitOptions> context) {
      return WaitOptions.create(context.inputDouble(), ((WaitOptions)context.current()).delay(), ((WaitOptions)context.current()).acceleration(), ((WaitOptions)context.current()).deceleration(), ((WaitOptions)context.current()).predict());
   }

   @PropertyParser("waitdelay|wait delay")
   public WaitOptions parseWaitDelay(PropertyParseContext<WaitOptions> context) {
      return WaitOptions.create(((WaitOptions)context.current()).distance(), context.inputDouble(), ((WaitOptions)context.current()).acceleration(), ((WaitOptions)context.current()).deceleration(), ((WaitOptions)context.current()).predict());
   }

   @PropertyParser("waitacceleration|wait acceleration")
   public WaitOptions parseWaitAcceleration(PropertyParseContext<WaitOptions> context) {
      String[] args = context.input().trim().split(" ");
      double newAcceleration;
      double newDeceleration;
      if (args.length >= 2) {
         newAcceleration = Util.parseAcceleration(args[0], Double.NaN);
         newDeceleration = Util.parseAcceleration(args[1], Double.NaN);
      } else {
         newAcceleration = newDeceleration = Util.parseAcceleration(context.input(), Double.NaN);
      }

      if (Double.isNaN(newAcceleration)) {
         throw new PropertyInvalidInputException("Acceleration is not a number or acceleration expression");
      } else if (Double.isNaN(newDeceleration)) {
         throw new PropertyInvalidInputException("Deceleration is not a number or acceleration expression");
      } else {
         return WaitOptions.create(((WaitOptions)context.current()).distance(), ((WaitOptions)context.current()).delay(), newAcceleration, newDeceleration, ((WaitOptions)context.current()).predict());
      }
   }

   @PropertyParser("waitpredicted|waitprediction|wait predicted|wait predict")
   public WaitOptions parseWaitPrediction(PropertyParseContext<WaitOptions> context) {
      return WaitOptions.create(((WaitOptions)context.current()).distance(), ((WaitOptions)context.current()).delay(), ((WaitOptions)context.current()).acceleration(), ((WaitOptions)context.current()).deceleration(), context.inputBoolean());
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_WAIT.has(sender);
   }

   public WaitOptions getDefault() {
      return WaitOptions.DEFAULT;
   }

   public WaitOptions getData(FieldBackedProperty.TrainInternalData data) {
      return data.waitOptionsData;
   }

   public void setData(FieldBackedProperty.TrainInternalData data, WaitOptions value) {
      data.waitOptionsData = value;
   }

   public Optional<WaitOptions> readFromConfig(ConfigurationNode config) {
      if (!config.isNode("wait")) {
         return config.contains("waitDistance") ? Optional.of(WaitOptions.create((Double)config.get("waitDistance", 0.0D))) : Optional.empty();
      } else {
         ConfigurationNode waitConfig = config.getNode("wait");
         double distance = (Double)waitConfig.get("distance", 0.0D);
         double delay = (Double)waitConfig.get("delay", 0.0D);
         double accel = (Double)waitConfig.get("acceleration", 0.0D);
         double decel = (Double)waitConfig.get("deceleration", 0.0D);
         boolean predict = (Boolean)waitConfig.get("predict", true);
         return Optional.of(WaitOptions.create(distance, delay, accel, decel, predict));
      }
   }

   public void writeToConfig(ConfigurationNode config, Optional<WaitOptions> value) {
      config.remove("waitDistance");
      if (value.isPresent()) {
         WaitOptions data = (WaitOptions)value.get();
         ConfigurationNode node = config.getNode("wait");
         node.set("distance", data.distance());
         node.set("delay", data.delay());
         node.set("acceleration", data.acceleration());
         node.set("deceleration", data.deceleration());
         node.set("predict", data.predict());
      } else {
         config.remove("wait");
      }

   }
}
