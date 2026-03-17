package com.bergerkiller.bukkit.tc.properties.standard.category;

import com.bergerkiller.bukkit.common.config.ConfigurationNode;
import com.bergerkiller.bukkit.common.dep.cloud.annotation.specifier.Quoted;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Argument;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.Util;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.properties.CartProperties;
import com.bergerkiller.bukkit.tc.properties.TrainProperties;
import com.bergerkiller.bukkit.tc.properties.api.ICartProperty;
import com.bergerkiller.bukkit.tc.properties.api.PropertyCheckPermission;
import com.bergerkiller.bukkit.tc.properties.api.PropertyParser;
import com.bergerkiller.bukkit.tc.properties.api.PropertySelectorCondition;
import com.bergerkiller.bukkit.tc.properties.standard.StandardProperties;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class DestinationProperty implements ICartProperty<String> {
   @CommandTargetTrain
   @Command("train destination|dest none")
   @CommandDescription("Clears the destination set for a train")
   private void commandClearProperty(CommandSender sender, TrainProperties properties) {
      this.commandSetProperty(sender, properties, "");
   }

   @CommandTargetTrain
   @PropertyCheckPermission("destination")
   @Command("train destination|dest <destination>")
   @CommandDescription("Sets a new destination for the train to go to")
   private void commandSetProperty(CommandSender sender, TrainProperties properties, @Quoted @Argument(value = "destination",suggestions = "destinations") String destination) {
      properties.setDestination(destination);
      this.commandGetProperty(sender, properties);
   }

   @Command("train destination|dest")
   @CommandDescription("Displays the current destination set for the train")
   private void commandGetProperty(CommandSender sender, TrainProperties properties) {
      if (properties.hasDestination()) {
         sender.sendMessage(ChatColor.YELLOW + "Train destination is set to: " + ChatColor.WHITE + properties.getDestination());
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Train destination is set to: " + ChatColor.RED + "None");
      }

   }

   @CommandTargetTrain
   @Command("cart destination|dest none")
   @CommandDescription("Clears the destination set for a cart")
   private void commandClearProperty(CommandSender sender, CartProperties properties) {
      this.commandSetProperty(sender, properties, "");
   }

   @CommandTargetTrain
   @PropertyCheckPermission("destination")
   @Command("cart destination|dest <destination>")
   @CommandDescription("Sets a new destination for the cart to go to")
   private void commandSetProperty(CommandSender sender, CartProperties properties, @Quoted @Argument(value = "destination",suggestions = "destinations") String destination) {
      properties.setDestination(destination);
      this.commandGetProperty(sender, properties);
   }

   @Command("cart destination|dest")
   @CommandDescription("Displays the current destination set for the cart")
   private void commandGetProperty(CommandSender sender, CartProperties properties) {
      if (properties.hasDestination()) {
         sender.sendMessage(ChatColor.YELLOW + "Cart destination is set to: " + ChatColor.WHITE + properties.getDestination());
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Cart destination is set to: " + ChatColor.RED + "None");
      }

   }

   @PropertyParser("destination")
   public String parseDestination(String input) {
      return input;
   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_DESTINATION.has(sender);
   }

   public String getDefault() {
      return "";
   }

   public Optional<String> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "destination", String.class);
   }

   public void writeToConfig(ConfigurationNode config, Optional<String> value) {
      Util.setConfigOptional(config, "destination", value);
   }

   public void set(CartProperties properties, String value) {
      int prior_route_index = properties.getCurrentRouteDestinationIndex();
      ICartProperty.super.set((CartProperties)properties, value);
      if (!value.isEmpty() && prior_route_index != -1) {
         List<String> route = (List)StandardProperties.DESTINATION_ROUTE.get((CartProperties)properties);
         int nextIndex = (prior_route_index + 1) % route.size();
         if (value.equals(route.get(nextIndex))) {
            StandardProperties.DESTINATION_ROUTE_INDEX.set(properties, nextIndex);
         }
      }

   }

   @PropertySelectorCondition("destination")
   public String get(TrainProperties properties) {
      Iterator var2 = properties.iterator();

      String destination;
      do {
         if (!var2.hasNext()) {
            return "";
         }

         CartProperties cprop = (CartProperties)var2.next();
         destination = (String)this.get((CartProperties)cprop);
      } while(destination.isEmpty());

      return destination;
   }
}
