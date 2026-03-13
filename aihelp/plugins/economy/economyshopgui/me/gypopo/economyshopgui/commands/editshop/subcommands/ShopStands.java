package me.gypopo.economyshopgui.commands.editshop.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.Methods;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.commands.editshop.subcommands.shopstands.Browse;
import me.gypopo.economyshopgui.commands.editshop.subcommands.shopstands.Destroy;
import me.gypopo.economyshopgui.commands.editshop.subcommands.shopstands.Give;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class ShopStands extends SubCommad {
   private final EconomyShopGUI plugin;
   private final ArrayList<SubCommad> subCommads = new ArrayList();

   public ShopStands(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.subCommads.add(new Give(plugin, methods));
      this.subCommads.add(new Destroy(plugin, methods));
      this.subCommads.add(new Browse(plugin, methods));
   }

   public void perform(Object logger, String[] args) {
      if (!this.plugin.shopStands) {
         SendMessage.warnMessage(logger, Lang.SHOP_STANDS_MODULE_NOT_ENABLED.get());
      } else {
         if (logger instanceof Player) {
            Player player = (Player)logger;
            if (args.length > 1) {
               Iterator var7 = this.subCommads.iterator();

               while(var7.hasNext()) {
                  SubCommad subCommad = (SubCommad)var7.next();
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
               if (args[1].equalsIgnoreCase("browse") || args[1].equalsIgnoreCase("give")) {
                  SendMessage.warnMessage(logger, Lang.REAL_PLAYER.get());
                  return;
               }

               Iterator var3 = this.subCommads.iterator();

               while(var3.hasNext()) {
                  SubCommad subCommad = (SubCommad)var3.next();
                  if (args[1].equalsIgnoreCase(subCommad.getName())) {
                     subCommad.perform(logger, args);
                     return;
                  }
               }
            }

            this.sendAllSyntaxes(logger);
         }

      }
   }

   public String getName() {
      return "shopstands";
   }

   public String getDescription() {
      return Lang.EDITSHOP_SHOPSTANDS_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_SHOPSTANDS_SUBCOMMAND_SYNTAX.get().getLegacy();
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
         if (subCommad.hasPermission((CommandSender)logger) && (!(logger instanceof ConsoleCommandSender) || !(subCommad instanceof Give) && !(subCommad instanceof Browse))) {
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
