package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddItemCommand implements SubCommand {
   private final CustomStructures plugin;

   public AddItemCommand(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (args.length == 0) {
         if (!sender.hasPermission("customstructures.additem")) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
            return true;
         }

         sender.sendMessage(String.valueOf(ChatColor.RED) + "You must specify a unique key to call the item by.");
      } else if (args.length == 1) {
         if (!sender.hasPermission("customstructures.additem")) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
            return true;
         }

         if (!(sender instanceof Player)) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "This command is for players only!");
            return true;
         }

         Player p = (Player)sender;
         String key = args[0];
         ItemStack item = p.getInventory().getItemInMainHand();
         item.setAmount(1);
         if (item.getType() == Material.AIR) {
            p.sendMessage(String.valueOf(ChatColor.RED) + "You must be holding an item to use this command!");
            return true;
         }

         if (!this.plugin.getCustomItemManager().addItem(key, item)) {
            p.sendMessage(String.valueOf(ChatColor.RED) + "That key already exists!");
         } else {
            p.sendMessage(String.valueOf(ChatColor.GREEN) + "Successfully added the custom item to the list.");
         }
      }

      return false;
   }
}
