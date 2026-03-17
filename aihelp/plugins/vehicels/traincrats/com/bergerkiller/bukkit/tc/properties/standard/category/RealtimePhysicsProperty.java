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
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.context.PropertyParseContext;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedProperty;
import com.bergerkiller.bukkit.tc.properties.standard.fieldbacked.FieldBackedStandardTrainProperty;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class RealtimePhysicsProperty extends FieldBackedStandardTrainProperty<Boolean> {
   @CommandTargetTrain
   @PropertyCheckPermission("realtime")
   @Command("train realtime <enabled>")
   @CommandDescription("Sets whether the train updates in realtime, adjusting for server tick lag and jitter")
   private void getProperty(CommandSender sender, TrainProperties properties, @Argument("enabled") boolean enabled) {
      properties.set(this, enabled);
      this.getProperty(sender, properties);
   }

   @Command("train realtime")
   @CommandDescription("Displays whether the train updates in realtime, adjusting for server tick lag and jitter")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Train updates in realtime: " + Localization.boolStr((Boolean)properties.get(this)));
   }

   @PropertyParser("realtime")
   public boolean parseRealtime(PropertyParseContext<Boolean> context) {
      return context.inputBoolean();
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_REALTIME.has(sender);
   }

   public Boolean getDefault() {
      return Boolean.FALSE;
   }

   public Boolean getData(FieldBackedProperty.TrainInternalData data) {
      return data.realtimePhysics;
   }

   public void setData(FieldBackedProperty.TrainInternalData data, Boolean value) {
      data.realtimePhysics = value;
   }

   public Optional<Boolean> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "realtimePhysics", Boolean.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Boolean> value) {
      Util.setConfigOptional(config, "realtimePhysics", value);
   }
}
