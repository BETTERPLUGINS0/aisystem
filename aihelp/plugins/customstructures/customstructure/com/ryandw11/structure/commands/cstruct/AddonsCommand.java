package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.api.structaddon.CustomStructureAddon;
import com.ryandw11.structure.commands.SubCommand;
import java.util.Iterator;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class AddonsCommand implements SubCommand {
   private final CustomStructures plugin;
   private static final List<String> officialAddons = List.of("CSCustomBiomes", "CSFastFill");

   public AddonsCommand(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (!sender.hasPermission("customstructures.addon")) {
         sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
         return true;
      } else {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&3============[&2Enabled Addons&3]============"));

         CustomStructureAddon addon;
         String official;
         for(Iterator var5 = this.plugin.getAddonHandler().getCustomStructureAddons().iterator(); var5.hasNext(); sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&a- &6%s &aby: &6%s   &9%s", addon.getName(), String.join("&a,&6 ", addon.getAuthors()), official)))) {
            addon = (CustomStructureAddon)var5.next();
            official = "";
            if (addon.getAuthors().size() == 1 && ((String)addon.getAuthors().get(0)).equalsIgnoreCase("Ryandw11") && officialAddons.contains(addon.getName())) {
               official = "(Official)";
            }
         }

         return false;
      }
   }
}
