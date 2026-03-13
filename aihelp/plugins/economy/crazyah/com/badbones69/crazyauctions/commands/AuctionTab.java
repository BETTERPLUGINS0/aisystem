package com.badbones69.crazyauctions.commands;

import com.badbones69.crazyauctions.CrazyAuctions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class AuctionTab implements TabCompleter {
   private final CrazyAuctions plugin = CrazyAuctions.get();

   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String commandLabel, String[] args) {
      List<String> completions = new ArrayList();
      if (args.length == 1) {
         if (this.hasPermission(sender, "access")) {
            completions.add("help");
            completions.add("collect");
            completions.add("expired");
            completions.add("listed");
         }

         if (this.hasPermission(sender, "test")) {
            completions.add("test");
         }

         if (this.hasPermission(sender, "admin")) {
            completions.add("reload");
         }

         if (this.hasPermission(sender, "force-end-all")) {
            completions.add("force_end_all");
         }

         if (this.hasPermission(sender, "view")) {
            completions.add("view");
         }

         if (this.hasPermission(sender, "sell")) {
            completions.add("sell");
         }

         if (this.hasPermission(sender, "bid")) {
            completions.add("bid");
         }

         return (List)StringUtil.copyPartialMatches(args[0], completions, new ArrayList());
      } else {
         String var6;
         byte var7;
         if (args.length == 2) {
            var6 = args[0].toLowerCase();
            var7 = -1;
            switch(var6.hashCode()) {
            case 97533:
               if (var6.equals("bid")) {
                  var7 = 0;
               }
               break;
            case 3526482:
               if (var6.equals("sell")) {
                  var7 = 1;
               }
               break;
            case 3619493:
               if (var6.equals("view")) {
                  var7 = 2;
               }
            }

            switch(var7) {
            case 0:
            case 1:
               completions.addAll(Arrays.asList("50", "100", "250", "500", "1000", "2500", "5000", "10000"));
               break;
            case 2:
               completions.addAll(this.plugin.getServer().getOnlinePlayers().stream().map(Player::getName).toList());
            }

            return (List)StringUtil.copyPartialMatches(args[1], completions, new ArrayList());
         } else if (args.length == 3) {
            var6 = args[0].toLowerCase();
            var7 = -1;
            switch(var6.hashCode()) {
            case 97533:
               if (var6.equals("bid")) {
                  var7 = 0;
               }
               break;
            case 3526482:
               if (var6.equals("sell")) {
                  var7 = 1;
               }
            }

            switch(var7) {
            case 0:
            case 1:
               completions.addAll(Arrays.asList("1", "2", "4", "8", "10", "20", "40", "64"));
            default:
               return (List)StringUtil.copyPartialMatches(args[2], completions, new ArrayList());
            }
         } else {
            return new ArrayList();
         }
      }
   }

   private boolean hasPermission(CommandSender sender, String node) {
      return sender.hasPermission("crazyauctions." + node) || sender.hasPermission("crazyauctions.admin.*");
   }
}
