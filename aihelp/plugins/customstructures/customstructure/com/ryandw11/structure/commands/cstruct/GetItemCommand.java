package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetItemCommand implements SubCommand {
   private final CustomStructures plugin;

   public GetItemCommand(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (args.length == 0) {
         if (!sender.hasPermission("customstructures.getitem")) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
            return true;
         }

         sender.sendMessage(String.valueOf(ChatColor.RED) + "You must specify the key in order to get an item from the list.");
      } else if (args.length == 1) {
         if (!sender.hasPermission("customstructures.getitem")) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
            return true;
         }

         if (!(sender instanceof Player)) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "This command is for players only!");
            return true;
         }

         Player p = (Player)sender;
         String key = args[0];
         ItemStack item = this.plugin.getCustomItemManager().getItem(key);
         if (item == null) {
            p.sendMessage(String.valueOf(ChatColor.RED) + "An item with that key was not found!");
            return true;
         }

         p.getInventory().addItem(new ItemStack[]{item});
         p.sendMessage(String.valueOf(ChatColor.GREEN) + "Successfully retrieved that item!");
      }

      return false;
   }
}
