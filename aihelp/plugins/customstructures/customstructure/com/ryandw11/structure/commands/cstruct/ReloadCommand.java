package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements SubCommand {
   private final CustomStructures plugin;

   public ReloadCommand(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (sender.hasPermission("customstructures.reload")) {
         if (this.plugin.getStructureHandler() == null) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "Unable to reload CustomStructures. The plugin has not been properly initialized.");
            sender.sendMessage(String.valueOf(ChatColor.RED) + "Please check the console for errors during startup.");
            return false;
         }

         this.plugin.reloadConfig();
         sender.sendMessage(String.valueOf(ChatColor.GREEN) + "The plugin has been reloaded!");
         this.plugin.getLogger().info("Plugin reloaded!");
         this.plugin.reloadHandlers();
      } else {
         sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command.");
      }

      return false;
   }
}
