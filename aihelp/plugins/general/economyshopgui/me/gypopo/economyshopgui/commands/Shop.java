package me.gypopo.economyshopgui.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.base.PluginCommands;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.Translatable;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.MainMenu;
import me.gypopo.economyshopgui.objects.shops.ShopSection;
import me.gypopo.economyshopgui.util.BackButton;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class Shop extends PluginCommands {
   public Shop(EconomyShopGUI plugin, List<String> aliases, List<String> disabledWorlds) {
      super(plugin, (String)aliases.get(0), "Opens the server shop", "/" + (String)aliases.remove(0) + " <section/search>", "EconomyShopGUI.shop", aliases, disabledWorlds);
   }

   public boolean execute(CommandSender sender, String label, String[] args) {
      if (this.plugin.badYMLParse != null) {
         SendMessage.warnMessage(sender, (String)"This command cannot be executed now, please fix the configuration formatting first!");
         return true;
      } else {
         Player player;
         if (sender instanceof Player) {
            player = (Player)sender;
            if (this.canUse(player)) {
               if (args.length == 0) {
                  new MainMenu(player);
               } else {
                  if (args.length > 2) {
                     return false;
                  }

                  if (this.plugin.getShopSections().contains(args[0]) && !this.plugin.getSection(args[0]).isSubSection()) {
                     if (PermissionsCache.hasPermission(player, "EconomyShopGUI.shop." + args[0])) {
                        if (args.length == 2) {
                           try {
                              int page = Integer.parseInt(args[1]);
                              ShopSection section = this.plugin.getSection(args[0]);
                              if (section.getShopPageItems(page) == null) {
                                 throw new NumberFormatException();
                              }

                              section.openShopSection(player, page, BackButton.DIRECT);
                           } catch (NumberFormatException var7) {
                              SendMessage.warnMessage(player, (Translatable)Lang.NO_SHOP_PAGE_FOUND.get().replace("%page%", args[1]));
                           }
                        } else {
                           this.plugin.getSection(args[0]).openShopSection(player, BackButton.DIRECT);
                        }
                     } else {
                        SendMessage.chatToPlayer(player, Lang.NO_PERMISSIONS_TO_OPEN_SHOP.get());
                     }
                  } else {
                     SendMessage.chatToPlayer(player, Lang.NO_SHOP_FOUND.get());
                  }
               }
            }
         } else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender || sender instanceof RemoteConsoleCommandSender) {
            if (args.length == 0) {
               SendMessage.warnMessage("Not enough params: /shop <section> <player> [page]");
            } else if (args.length == 1) {
               player = this.plugin.getServer().getPlayer(args[0]);
               if (player != null) {
                  new MainMenu(player);
               } else {
                  SendMessage.warnMessage(sender, (Translatable)Lang.PLAYER_NOT_ONLINE.get());
               }
            } else {
               if (args.length != 2) {
                  return false;
               }

               player = this.plugin.getServer().getPlayer(args[1]);
               if (player != null) {
                  if (this.plugin.getShopSections().contains(args[0])) {
                     this.plugin.getSection(args[0]).openShopSection(player, BackButton.DIRECT_CONSOLE);
                  } else {
                     SendMessage.warnMessage(sender, (Translatable)Lang.NO_SHOP_FOUND.get());
                  }
               } else {
                  SendMessage.warnMessage(sender, (Translatable)Lang.PLAYER_NOT_ONLINE.get());
               }
            }
         }

         return true;
      }
   }

   public List<String> tabComplete(CommandSender commandSender, String s, String[] args) {
      if (args.length == 1) {
         List<String> tabCompletions = (List)this.plugin.getSections().values().stream().filter((section) -> {
            return !section.isSubSection() && PermissionsCache.hasPermission(commandSender, "EconomyShopGUI.shop." + section.getSection());
         }).map(ShopSection::getSection).collect(Collectors.toList());
         if (!args[0].isEmpty()) {
            List<String> completions = new ArrayList();
            StringUtil.copyPartialMatches(args[0], tabCompletions, completions);
            Collections.sort(completions);
            return completions;
         } else {
            return tabCompletions;
         }
      } else {
         return Collections.emptyList();
      }
   }
}
