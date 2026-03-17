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

public final class AllowManualMobMovementProperty extends FieldBackedStandardTrainProperty<Boolean> {
   @CommandTargetTrain
   @PropertyCheckPermission("allowmobmanual")
   @Command("train manualmovement mob <enabled>")
   @CommandDescription("Sets whether mobs seated in the train can cause the train to move")
   private void getProperty(CommandSender sender, TrainProperties properties, @Argument("enabled") boolean enabled) {
      properties.set(this, enabled);
      this.getProperty(sender, properties);
   }

   @Command("train manualmovement mob")
   @CommandDescription("Displays whether mobs seated in the train can cause the train to move")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Mobs in the train can set the train in motion: " + Localization.boolStr((Boolean)properties.get(this)));
   }

   @PropertyParser("allowmobmanual|mobmanualmove|mobmanual|manualmovement mob")
   public boolean parseAllowMovement(PropertyParseContext<Boolean> context) {
      return context.inputBoolean();
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_ALLOWMOBMANUALMOVEMENT.has(sender);
   }

   public Boolean getDefault() {
      return Boolean.FALSE;
   }

   public Boolean getData(FieldBackedProperty.TrainInternalData data) {
      return data.allowMobManualMovement;
   }

   public void setData(FieldBackedProperty.TrainInternalData data, Boolean value) {
      data.allowMobManualMovement = value;
   }

   public Optional<Boolean> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "allowMobManualMovement", Boolean.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Boolean> value) {
      Util.setConfigOptional(config, "allowMobManualMovement", value);
   }
}
