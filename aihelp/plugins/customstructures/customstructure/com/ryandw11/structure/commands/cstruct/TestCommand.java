package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.commands.SubCommand;
import com.ryandw11.structure.structure.Structure;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestCommand implements SubCommand {
   private final CustomStructures plugin;

   public TestCommand(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (args.length != 1) {
         sender.sendMessage(String.valueOf(ChatColor.RED) + "Invalid arguments. /cstruct test {name}");
         return true;
      } else if (!(sender instanceof Player)) {
         sender.sendMessage("This command is for players only!");
         return true;
      } else {
         Player p = (Player)sender;
         if (!p.hasPermission("customstructures.test")) {
            p.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command.");
            return true;
         } else {
            Structure structure = this.plugin.getStructureHandler().getStructure(args[0]);
            if (structure == null) {
               p.sendMessage(String.valueOf(ChatColor.RED) + "That structure does not exist!");
               return true;
            } else {
               structure.spawn(p.getLocation());
               return true;
            }
         }
      }
   }
}
