package com.bergerkiller.bukkit.tc.locator;

import com.bergerkiller.bukkit.common.MessageBuilder;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Command;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.CommandDescription;
import com.bergerkiller.bukkit.common.dep.cloud.annotations.Flag;
import com.bergerkiller.bukkit.common.utils.MathUtil;
import com.bergerkiller.bukkit.tc.Permission;
import com.bergerkiller.bukkit.tc.TrainCarts;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandRequiresPermission;
import com.bergerkiller.bukkit.tc.commands.annotations.CommandTargetTrain;
import com.bergerkiller.bukkit.tc.controller.MinecartGroup;
import com.bergerkiller.bukkit.tc.controller.MinecartMember;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TrainLocatorCommands {
   @CommandTargetTrain
   @CommandRequiresPermission(Permission.COMMAND_LOCATE)
   @Command("cart locate")
   @CommandDescription("Toggles locating a single cart of a train. Stops locating other carts.")
   private void commandLocateCart(Player sender, TrainCarts plugin, MinecartMember<?> member, @Flag(value = "timeout",description = "Timeout in seconds after which locating automatically stops") Double timeout) {
      if (plugin.getTrainLocator().isLocating(sender, member)) {
         plugin.getTrainLocator().stopAll(sender);
         sender.sendMessage(ChatColor.YELLOW + "Stopped locating the cart(s)");
      } else {
         if (plugin.getTrainLocator().stopAll(sender)) {
            sender.sendMessage(ChatColor.YELLOW + "Stopped locating the previous cart(s)");
         }

         this.commandStartLocatingCart(sender, plugin, member, timeout);
      }

   }

   @CommandTargetTrain
   @CommandRequiresPermission(Permission.COMMAND_LOCATE)
   @Command("train locate")
   @CommandDescription("Toggles locating a single train. Stops locating other trains.")
   private void commandLocateTrain(Player sender, TrainCarts plugin, MinecartGroup group, @Flag(value = "timeout",description = "Timeout in seconds after which locating automatically stops") Double timeout) {
      if (plugin.getTrainLocator().isLocating(sender, group)) {
         plugin.getTrainLocator().stopAll(sender);
         sender.sendMessage(ChatColor.YELLOW + "Stopped locating the train(s)");
      } else {
         if (plugin.getTrainLocator().stopAll(sender)) {
            sender.sendMessage(ChatColor.YELLOW + "Stopped locating the previous train(s)");
         }

         this.commandStartLocatingTrain(sender, plugin, group, timeout);
      }

   }

   @CommandTargetTrain
   @CommandRequiresPermission(Permission.COMMAND_LOCATE)
   @Command("cart locate start")
   @CommandDescription("Starts locating a single cart of a train. Keeps locating previous trains.")
   private void commandStartLocatingCart(Player sender, TrainCarts plugin, MinecartMember<?> member, @Flag(value = "timeout",description = "Timeout in seconds after which locating automatically stops") Double timeout) {
      MessageBuilder message = new MessageBuilder();
      if (timeout == null) {
         if (plugin.getTrainLocator().start(sender, member)) {
            message.green(new Object[]{"Locating the cart for indefinite time"});
            message.newLine();
            message.green(new Object[]{"To stop locating, use /cart locate stop"});
         } else {
            message.red(new Object[]{"Failed to locate this cart (different world?)"});
         }
      } else {
         int numTicks = MathUtil.ceil(timeout * 20.0D);
         if (plugin.getTrainLocator().start(sender, member, numTicks)) {
            message.green(new Object[]{"Locating the cart for ", timeout, " seconds"});
            message.newLine();
            message.green(new Object[]{"To stop locating, use /cart locate stop"});
         } else {
            message.red(new Object[]{"Failed to locate this cart (different world?)"});
         }
      }

      message.send(sender);
   }

   @CommandTargetTrain
   @CommandRequiresPermission(Permission.COMMAND_LOCATE)
   @Command("train locate start")
   @CommandDescription("Starts locating a single cart of a train. Keeps locating previous carts.")
   private void commandStartLocatingTrain(Player sender, TrainCarts plugin, MinecartGroup group, @Flag(value = "timeout",description = "Timeout in seconds after which locating automatically stops") Double timeout) {
      MessageBuilder message = new MessageBuilder();
      if (timeout == null) {
         if (plugin.getTrainLocator().start(sender, group)) {
            message.green(new Object[]{"Locating the train for indefinite time"});
            message.newLine();
            message.green(new Object[]{"To stop locating, use /train locate stop"});
         } else {
            message.red(new Object[]{"Failed to locate this train (different world?)"});
         }
      } else {
         int numTicks = MathUtil.ceil(timeout * 20.0D);
         if (plugin.getTrainLocator().start(sender, group, numTicks)) {
            message.green(new Object[]{"Locating the train for ", timeout, " seconds"});
            message.newLine();
            message.green(new Object[]{"To stop locating, use /train locate stop"});
         } else {
            message.red(new Object[]{"Failed to locate this train (different world?)"});
         }
      }

      message.send(sender);
   }

   @CommandTargetTrain
   @CommandRequiresPermission(Permission.COMMAND_LOCATE)
   @Command("cart locate stop")
   @CommandDescription("Stops locating the selected cart, keeps locating other carts")
   private void commandStopLocatingCart(Player sender, TrainCarts plugin, MinecartMember<?> member) {
      MessageBuilder message = new MessageBuilder();
      String name = member.getGroup().getProperties().getTrainName();
      if (plugin.getTrainLocator().stop(sender, member)) {
         message.yellow(new Object[]{"No longer locating cart of train "}).white(new Object[]{name});
      } else {
         message.red(new Object[]{"You were not locating this cart of train ", name});
      }

      message.send(sender);
   }

   @CommandTargetTrain
   @CommandRequiresPermission(Permission.COMMAND_LOCATE)
   @Command("train locate stop")
   @CommandDescription("Stops locating the selected train, keeps locating other train")
   private void commandStopLocatingTrain(Player sender, TrainCarts plugin, MinecartGroup group) {
      MessageBuilder message = new MessageBuilder();
      String name = group.getProperties().getTrainName();
      if (plugin.getTrainLocator().stop(sender, group)) {
         message.yellow(new Object[]{"No longer locating train "}).white(new Object[]{name});
      } else {
         message.red(new Object[]{"You were not locating the train ", name});
      }

      message.send(sender);
   }

   @CommandRequiresPermission(Permission.COMMAND_LOCATE)
   @Command("cart locate stop_all")
   @CommandDescription("Stops locating all carts")
   private void commandStopLocatingAllCartAlias(Player sender, TrainCarts plugin) {
      this.commandStopLocatingAll(sender, plugin);
   }

   @CommandRequiresPermission(Permission.COMMAND_LOCATE)
   @Command("train locate stop_all")
   @CommandDescription("Stops locating all trains")
   private void commandStopLocatingAll(Player sender, TrainCarts plugin) {
      MessageBuilder message = new MessageBuilder();
      if (plugin.getTrainLocator().stopAll(sender)) {
         message.green(new Object[]{"Stopped locating the trains"});
      } else {
         message.red(new Object[]{"You were not locating any trains"});
      }

      message.send(sender);
   }
}
