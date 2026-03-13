package com.ryandw11.structure.commands;

import com.ryandw11.structure.CustomStructures;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

public class SCommandTab implements TabCompleter {
   private final CustomStructures plugin;

   public SCommandTab(CustomStructures plugin) {
      this.plugin = plugin;
   }

   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
      List<String> completions = new ArrayList();
      ArrayList completions;
      if (args.length == 2 && (args[0].equalsIgnoreCase("test") || args[0].equalsIgnoreCase("testspawn") || args[0].equalsIgnoreCase("nearby"))) {
         completions = new ArrayList(this.plugin.getStructureHandler().getStructureNames());
         completions = this.getApplicableTabCompleter(args[1], completions);
      } else if (args.length == 2 && (args[0].equalsIgnoreCase("setLootTable") || args[0].equalsIgnoreCase("setLoot") || args[0].equalsIgnoreCase("setlt") || args[0].equalsIgnoreCase("testLootTable") || args[0].equalsIgnoreCase("testlt") || args[0].equalsIgnoreCase("testLoot"))) {
         List<String> completions = this.plugin.getLootTableHandler().getLootTablesNames();
         completions = this.getApplicableTabCompleter(args[1], completions);
      } else if (args.length <= 1) {
         completions = new ArrayList(Arrays.asList("reload", "test", "list", "addItem", "checkKey", "getItem", "createSchem", "create", "nearby", "testspawn", "addons", "setLootTable", "testLootTable"));
         completions = this.getApplicableTabCompleter(args.length == 1 ? args[0] : "", completions);
      }

      Collections.sort((List)completions);
      return (List)completions;
   }

   private List<String> getApplicableTabCompleter(String arg, List<String> completions) {
      if (arg != null && !arg.equalsIgnoreCase("")) {
         List<String> valid = new ArrayList();
         Iterator var4 = completions.iterator();

         while(var4.hasNext()) {
            String posib = (String)var4.next();
            if (posib.startsWith(arg)) {
               valid.add(posib);
            }
         }

         return valid;
      } else {
         return completions;
      }
   }
}
