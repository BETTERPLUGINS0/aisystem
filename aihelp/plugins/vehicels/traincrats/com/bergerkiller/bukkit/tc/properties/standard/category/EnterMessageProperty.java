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
import java.util.Optional;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class EnterMessageProperty implements ICartProperty<String> {
   @CommandTargetTrain
   @PropertyCheckPermission("entermessage")
   @Command("train entermessage <message>")
   @CommandDescription("Sets the message displayed to players when they enter the train")
   private void setProperty(CommandSender sender, TrainProperties properties, @Quoted @Argument("message") String message) {
      properties.set(this, message);
      this.getProperty(sender, properties);
   }

   @Command("train entermessage")
   @CommandDescription("Displays the message that will be displayed to players when they enter the train")
   private void getProperty(CommandSender sender, TrainProperties properties) {
      String message = (String)properties.get(this);
      if (message.isEmpty()) {
         sender.sendMessage(ChatColor.YELLOW + "Message displayed: " + ChatColor.RED + "NONE");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Message displayed: " + ChatColor.WHITE + message);
      }

   }

   @CommandTargetTrain
   @PropertyCheckPermission("entermessage")
   @Command("cart entermessage <message>")
   @CommandDescription("Sets the message displayed to players when they enter the cart")
   private void setProperty(CommandSender sender, CartProperties properties, @Quoted @Argument("message") String message) {
      properties.set(this, message);
      this.getProperty(sender, properties);
   }

   @Command("cart entermessage")
   @CommandDescription("Displays the message that will be displayed to players when they enter the cart")
   private void getProperty(CommandSender sender, CartProperties properties) {
      String message = (String)properties.get(this);
      if (message.isEmpty()) {
         sender.sendMessage(ChatColor.YELLOW + "Message displayed: " + ChatColor.RED + "NONE");
      } else {
         sender.sendMessage(ChatColor.YELLOW + "Message displayed: " + ChatColor.WHITE + message);
      }

   }

   public boolean hasPermission(CommandSender sender, String name) {
      return Permission.PROPERTY_ENTER_MESSAGE.has(sender);
   }

   @PropertyParser("entermessage|entermsg")
   public String parseMessage(String input) {
      return input;
   }

   public String getDefault() {
      return "";
   }

   public Optional<String> readFromConfig(ConfigurationNode config) {
      return Util.getConfigOptional(config, "enterMessage", String.class);
   }

   public void writeToConfig(ConfigurationNode config, Optional<String> value) {
      Util.setConfigOptional(config, "enterMessage", value);
   }
}
