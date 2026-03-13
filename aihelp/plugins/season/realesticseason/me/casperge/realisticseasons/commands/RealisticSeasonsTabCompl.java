package me.casperge.realisticseasons.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.api.CustomWorldGenerator;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class RealisticSeasonsTabCompl implements TabCompleter {
   List<String> results = new ArrayList();
   private RealisticSeasons main;

   public RealisticSeasonsTabCompl(RealisticSeasons var1) {
      this.main = var1;
   }

   public List<String> onTabComplete(CommandSender var1, Command var2, String var3, String[] var4) {
      if (var1 instanceof Player && !((Player)var1).hasPermission("realisticseasons.admin")) {
         this.results.clear();
         return this.results;
      } else {
         if (var4.length == 1) {
            if (var2.getLabel().equalsIgnoreCase("rs") || var2.getLabel().equalsIgnoreCase("realisticseasons")) {
               this.results.clear();
               this.results.add("set");
               this.results.add("setdate");
               this.results.add("temperature");
               this.results.add("disable");
               this.results.add("getinfo");
               this.results.add("help");
               this.results.add("nextseason");
               this.results.add("restoreworld");
               this.results.add("install");
               this.results.add("pausetime");
               this.results.add("reload");
               return this.sortedResults(var4[0]);
            }
         } else if (var4.length == 2) {
            if (var2.getLabel().equalsIgnoreCase("rs") || var2.getLabel().equalsIgnoreCase("realisticseasons")) {
               if (var4[0].equalsIgnoreCase("set")) {
                  this.results.clear();
                  this.results.add("summer");
                  this.results.add("winter");
                  this.results.add("spring");
                  this.results.add("fall");
                  return this.sortedResults(var4[1]);
               }

               if (var4[0].equalsIgnoreCase("setdate")) {
                  if (!this.main.getSettings().americandateformat) {
                     this.results.clear();
                     this.results.add("dd/mm/yyyy");
                     return this.sortedResults(var4[1]);
                  }

                  this.results.clear();
                  this.results.add("mm/dd/yyyy");
                  return this.sortedResults(var4[1]);
               }

               if (var4[0].equalsIgnoreCase("temperature")) {
                  this.results.clear();
                  this.results.add("modify");
                  this.results.add("toggle");
                  this.results.add("clear");
                  return this.sortedResults(var4[1]);
               }

               if (var4[0].equalsIgnoreCase("install")) {
                  this.results.clear();
                  this.results.addAll(CustomWorldGenerator.getAllGenerators());
                  return this.sortedResults(var4[1]);
               }
            }
         } else if (var4.length == 3) {
            if (var4[0].equalsIgnoreCase("temperature") && (var4[1].equals("modify") || var4[1].equals("clear"))) {
               this.results.clear();
               Iterator var5 = Bukkit.getOnlinePlayers().iterator();

               while(var5.hasNext()) {
                  Player var6 = (Player)var5.next();
                  this.results.add(var6.getName());
               }

               return this.sortedResults(var4[2]);
            }
         } else if (var4.length == 4) {
            if (var4[0].equalsIgnoreCase("temperature") && var4[1].equals("modify")) {
               this.results.clear();
               this.results.add("<temperature-change>");
               return this.sortedResults(var4[3]);
            }
         } else if (var4.length == 5 && var4[0].equalsIgnoreCase("temperature") && var4[1].equals("modify")) {
            this.results.clear();
            this.results.add("<duration(s)>");
            return this.sortedResults(var4[4]);
         }

         this.results.clear();
         return this.sortedResults(var4[0]);
      }
   }

   private List<String> sortedResults(String var1) {
      ArrayList var2 = new ArrayList();
      StringUtil.copyPartialMatches(var1, this.results, var2);
      Collections.sort(var2);
      this.results.clear();
      Iterator var3 = var2.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         this.results.add(var4);
      }

      return this.results;
   }
}
