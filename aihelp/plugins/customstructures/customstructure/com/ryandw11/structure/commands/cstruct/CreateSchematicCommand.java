package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.commands.SubCommand;
import com.ryandw11.structure.schematic.SchematicHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateSchematicCommand implements SubCommand {
   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (args.length == 0) {
         if (!sender.hasPermission("customstructures.createschematic")) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
            return true;
         }

         sender.sendMessage(String.valueOf(ChatColor.RED) + "You must specify the name in order to create a schematic.");
      } else {
         String var10001;
         Player p;
         String name;
         if (args.length == 1) {
            if (!sender.hasPermission("customstructures.createschematic")) {
               sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
               return true;
            }

            if (!(sender instanceof Player)) {
               sender.sendMessage(String.valueOf(ChatColor.RED) + "This command is for players only!");
               return true;
            }

            p = (Player)sender;
            name = args[0].replace(".schem", "");
            if (SchematicHandler.createSchematic(name, p, p.getWorld(), false)) {
               var10001 = String.valueOf(ChatColor.GREEN);
               p.sendMessage(var10001 + "Successfully created a schematic with the name of " + String.valueOf(ChatColor.GOLD) + name + String.valueOf(ChatColor.GREEN) + "!");
               var10001 = String.valueOf(ChatColor.GREEN);
               p.sendMessage(var10001 + "You can now use " + String.valueOf(ChatColor.GOLD) + name + ".schem" + String.valueOf(ChatColor.GREEN) + " in a structure.");
            } else {
               p.sendMessage(String.valueOf(ChatColor.RED) + "The world edit region seems to be incomplete! Try making a selection first!");
            }
         } else if (args.length == 2) {
            if (!sender.hasPermission("customstructures.createschematic.options")) {
               sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
               return true;
            }

            if (!(sender instanceof Player)) {
               sender.sendMessage(String.valueOf(ChatColor.RED) + "This command is for players only!");
               return true;
            }

            if (args[1].equalsIgnoreCase("-c") || args[1].equalsIgnoreCase("-compile")) {
               p = (Player)sender;
               name = args[0].replace(".schem", "");
               if (SchematicHandler.createSchematic(name, p, p.getWorld(), true)) {
                  var10001 = String.valueOf(ChatColor.GREEN);
                  p.sendMessage(var10001 + "Successfully created a schematic with the name of " + String.valueOf(ChatColor.GOLD) + name + String.valueOf(ChatColor.GREEN) + "!");
                  p.sendMessage(String.valueOf(ChatColor.GREEN) + "Successfully compiled the schematic!");
                  var10001 = String.valueOf(ChatColor.GREEN);
                  p.sendMessage(var10001 + "You can now use " + String.valueOf(ChatColor.GOLD) + name + ".schem" + String.valueOf(ChatColor.GREEN) + " and " + String.valueOf(ChatColor.GOLD) + name + ".cschem" + String.valueOf(ChatColor.GREEN) + " in a structure.");
               } else {
                  p.sendMessage(String.valueOf(ChatColor.RED) + "The world edit region seems to be incomplete! Try making a selection first!");
               }
            }

            if (args[1].equalsIgnoreCase("-cOnly") || args[1].equalsIgnoreCase("-compileOnly")) {
               p = (Player)sender;
               name = args[0].replace(".schem", "").replace(".cschem", "");
               if (SchematicHandler.compileOnly(name, p, p.getWorld())) {
                  var10001 = String.valueOf(ChatColor.GREEN);
                  p.sendMessage(var10001 + "Successfully compiled the schematic with the name of " + String.valueOf(ChatColor.GOLD) + name + String.valueOf(ChatColor.GREEN) + "!");
                  p.sendMessage(String.valueOf(ChatColor.RED) + "The option is for advanced users only. Please be sure the selection is valid.");
               } else {
                  p.sendMessage(String.valueOf(ChatColor.RED) + "The world edit region seems to be incomplete! Try making a selection first!");
               }
            }
         }
      }

      return false;
   }
}
