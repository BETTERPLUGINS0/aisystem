package me.gypopo.economyshopgui.commands.editshop.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.Methods;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.commands.editshop.subcommands.transactionLogs.Export;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class TransactionLogs extends SubCommad {
   private final ArrayList<SubCommad> subCommads = new ArrayList();

   public TransactionLogs(EconomyShopGUI plugin, Methods methods) {
      this.subCommads.add(new Export(plugin));
   }

   public void perform(Object logger, String[] args) {
      if (logger instanceof Player) {
         Player player = (Player)logger;
         if (args.length > 1) {
            Iterator var4 = this.subCommads.iterator();

            while(var4.hasNext()) {
               SubCommad subCommad = (SubCommad)var4.next();
               if (args[1].equalsIgnoreCase(subCommad.getName())) {
                  if (subCommad.hasPermission(player)) {
                     subCommad.perform(logger, args);
                  } else {
                     SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
                  }

                  return;
               }
            }
         }

         if (this.hasPermission(player)) {
            this.sendAllSyntaxes(logger);
         } else {
            SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS.get());
         }
      } else {
         if (args.length > 1) {
            Iterator var6 = this.subCommads.iterator();

            while(var6.hasNext()) {
               SubCommad subCommad = (SubCommad)var6.next();
               if (args[1].equalsIgnoreCase(subCommad.getName())) {
                  subCommad.perform(logger, args);
                  return;
               }
            }
         }

         this.sendAllSyntaxes(logger);
      }

   }

   public String getName() {
      return "logs";
   }

   public String getDescription() {
      return ChatColor.GREEN + "View/delete logged transactions";
   }

   public String getSyntax() {
      return ChatColor.GREEN + "/editshop logs <export> ...";
   }

   public boolean hasPermission(CommandSender source) {
      return this.subCommads.stream().anyMatch((c) -> {
         return c.hasPermission(source);
      });
   }

   private void sendAllSyntaxes(Object logger) {
      SendMessage.sendMessage(logger, Lang.SELLALL_COMMAND_USAGES.get());
      StringBuilder builder = new StringBuilder();

      for(int i = 0; i < this.subCommads.size(); ++i) {
         SubCommad subCommad = (SubCommad)this.subCommads.get(i);
         if (subCommad.hasPermission((CommandSender)logger) && !(logger instanceof ConsoleCommandSender)) {
            builder.append(ChatColor.WHITE + " - " + ChatColor.RESET);
            builder.append(subCommad.getSyntax()).append(ChatColor.DARK_GRAY).append(" - ").append(ChatColor.RESET).append(subCommad.getDescription());
            if (i != this.subCommads.size()) {
               builder.append("\n");
            }
         }
      }

      SendMessage.sendMessage(logger, builder.toString());
   }

   public List<String> getTabCompletion(String[] args) {
      if (args.length != 2) {
         Iterator var5 = this.subCommads.iterator();

         SubCommad subCommad;
         do {
            if (!var5.hasNext()) {
               return null;
            }

            subCommad = (SubCommad)var5.next();
         } while(!args[1].equalsIgnoreCase(subCommad.getName()));

         return subCommad.getTabCompletion(args);
      } else {
         List<String> tabCompletions = new ArrayList();
         Iterator var3 = this.subCommads.iterator();

         while(var3.hasNext()) {
            SubCommad subCommad = (SubCommad)var3.next();
            tabCompletions.add(subCommad.getName());
         }

         if (!args[1].isEmpty()) {
            List<String> completions = new ArrayList();
            StringUtil.copyPartialMatches(args[1], tabCompletions, completions);
            Collections.sort(completions);
            return completions;
         } else {
            return tabCompletions;
         }
      }
   }
}
