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

public final class SoundEnabledProperty extends FieldBackedStandardTrainProperty<Boolean> {
   @CommandTargetTrain
   @PropertyCheckPermission("soundenabled")
   @Command("train soundenabled|sound <enabled>")
   @CommandDescription("Sets whether the train makes sound while moving")
   private void setProperty(CommandSender sender, TrainProperties properties, @Argument("enabled") boolean enabled) {
      properties.set(this, enabled);
      this.getProperty(sender, properties);
   }

   @Command("train soundenabled|sound")
   @CommandDescription("Displays whether the train makes sound while moving")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      sender.sendMessage(ChatColor.YELLOW + "Train makes sound while moving: " + Localization.boolStr((Boolean)properties.get(this)));
   }

   @PropertyParser("sound|soundenabled|minecartsound")
   public boolean parseSoundEnabled(PropertyParseContext<Boolean> context) {
      return context.inputBoolean();
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_SOUNDENABLED.has(sender);
   }

   public Boolean getDefault() {
      return Boolean.TRUE;
   }

   public Boolean getData(FieldBackedProperty.TrainInternalData data) {
      return data.soundEnabled;
   }

   public void setData(FieldBackedProperty.TrainInternalData data, Boolean value) {
      data.soundEnabled = value;
   }

   public Optional<Boolean> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "soundEnabled", Boolean.TYPE);
   }

   public void writeToConfig(ConfigurationNode config, Optional<Boolean> value) {
      Util.setConfigOptional(config, "soundEnabled", value);
   }
}
