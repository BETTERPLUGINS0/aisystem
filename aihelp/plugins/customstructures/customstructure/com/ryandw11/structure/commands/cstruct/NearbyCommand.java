package com.ryandw11.structure.commands.cstruct;

import com.ryandw11.structure.CustomStructures;
import com.ryandw11.structure.commands.SubCommand;
import com.ryandw11.structure.io.NearbyStructuresRequest;
import com.ryandw11.structure.io.NearbyStructuresResponse;
import com.ryandw11.structure.io.StructureDatabaseHandler;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NearbyCommand implements SubCommand {
   private final CustomStructures plugin;

   public NearbyCommand(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public boolean subCommand(CommandSender sender, Command cmd, String s, String[] args) {
      if (!sender.hasPermission("customstructures.findnearby")) {
         sender.sendMessage(String.valueOf(ChatColor.RED) + "You do not have permission for this command!");
         return true;
      } else if (sender instanceof Player) {
         Player p = (Player)sender;
         if (this.plugin.getStructureHandler().getStructureDatabaseHandler().isEmpty()) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "Structure logging is not enabled! If you are an admin check the console for more information!");
            this.plugin.getLogger().info("Structure logging is currently disabled! Enable it in the config.yml file in order ot enable features like /cstruct nearby.");
            return true;
         } else {
            p.sendMessage(String.valueOf(ChatColor.GREEN) + "Fetching closest structures. This may take awhile...");
            NearbyStructuresRequest nearbyStructuresRequest;
            if (args.length == 0) {
               nearbyStructuresRequest = new NearbyStructuresRequest(p.getLocation());
            } else {
               int limit;
               if (args.length == 1) {
                  try {
                     limit = Integer.parseInt(args[0]);
                     limit = Math.max(1, Math.min(limit, 20));
                     nearbyStructuresRequest = new NearbyStructuresRequest(p.getLocation(), limit);
                  } catch (NumberFormatException var9) {
                     nearbyStructuresRequest = new NearbyStructuresRequest(p.getLocation(), args[0]);
                  }
               } else {
                  if (args.length != 2) {
                     p.sendMessage(String.valueOf(ChatColor.RED) + "Error: Invalid number of arguments. (/cstruct nearby [structName] [limit])");
                     return true;
                  }

                  try {
                     limit = Integer.parseInt(args[1]);
                     limit = Math.max(1, Math.min(limit, 20));
                     nearbyStructuresRequest = new NearbyStructuresRequest(p.getLocation(), args[0], limit);
                  } catch (NumberFormatException var8) {
                     p.sendMessage(String.valueOf(ChatColor.RED) + "Error: Invalid limit specified.");
                     return true;
                  }
               }
            }

            ((StructureDatabaseHandler)this.plugin.getStructureHandler().getStructureDatabaseHandler().get()).findNearby(nearbyStructuresRequest).thenAccept((response) -> {
               if (!response.hasEntries()) {
                  p.sendMessage(String.valueOf(ChatColor.RED) + "Could not find any nearby structures!");
               } else {
                  p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&aNearby Structures (&c%s&a, Limit &c%d&a):", nearbyStructuresRequest.hasName() ? nearbyStructuresRequest.getName() : "All Structures", nearbyStructuresRequest.getLimit())));
                  Iterator var3 = response.getResponse().iterator();

                  while(var3.hasNext()) {
                     NearbyStructuresResponse.NearbyStructureContainer nearbyStructure = (NearbyStructuresResponse.NearbyStructureContainer)var3.next();
                     p.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&aFound structure &6%s &aat &6%s&a, &6%s&a, &6%s&a (&6%.2f &ablocks away)!", nearbyStructure.getStructure().getName(), nearbyStructure.getLocation().getBlockX(), nearbyStructure.getLocation().getBlockY(), nearbyStructure.getLocation().getBlockZ(), nearbyStructure.getDistance())));
                  }

               }
            }).exceptionally((ex) -> {
               p.sendMessage(String.valueOf(ChatColor.RED) + "Too many requests have been sent at this time. Try again later.");
               return null;
            });
            return false;
         }
      } else {
         sender.sendMessage(String.valueOf(ChatColor.RED) + "The command is for players only!");
         return true;
      }
   }
}
