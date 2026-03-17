package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.TrainPropertiesStore;
import com.bergerkiller.bukkit.tc.properties.api.ISyntheticProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyInvalidInputException;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.defaults.DefaultProperties;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class DefaultConfigSyntheticProperty implements ISyntheticProperty<DefaultProperties> {
   @CommandTargetTrain
   @PropertyCheckPermission("setdefault")
   @Command("train defaults apply <defaultname>")
   @CommandDescription("Applies defaults from DefaultTrainProperties to a train")
   private void commandApplyDefaults(CommandSender sender, TrainProperties properties, @Argument("defaultname") String defaultName) {
      DefaultProperties defaults = TrainPropertiesStore.getDefaultsByName(defaultName);
      if (defaults == null) {
         sender.sendMessage(ChatColor.RED + "Train Property Defaults by key " + ChatColor.BLUE + "'" + defaultName + "' " + ChatColor.RED + " does not exist!");
      } else {
         properties.apply(defaults);
         sender.sendMessage(ChatColor.GREEN + "Default properties '" + defaultName + "' applied!");
      }
   }

   @PropertyParser("applydefault|setdefault|default")
   public DefaultProperties parseDefaultConfig(String defaultName) {
      DefaultProperties defaults = TrainPropertiesStore.getDefaultsByName(defaultName);
      if (defaults == null) {
         throw new PropertyInvalidInputException("Train Property Defaults by key '" + defaultName + "' does not exist");
      } else {
         return defaults;
      }
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_APPLYDEFAULTS.has(sender);
   }

   public DefaultProperties getDefault() {
      return TrainPropertiesStore.getDefaultsByName("default");
   }

   public DefaultProperties get(CartProperties properties) {
      return this.getDefault();
   }

   public DefaultProperties get(TrainProperties properties) {
      return this.getDefault();
   }

   public void set(CartProperties properties, DefaultProperties config) {
      if (config != null) {
         config.applyTo(properties);
      }

   }

   public void set(TrainProperties properties, DefaultProperties config) {
      if (config != null) {
         config.applyTo(properties);
      }

   }
}
