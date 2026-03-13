package me.gypopo.economyshopgui.commands.editshop.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.Methods;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.files.config.Config;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

public class AddItem extends SubCommad {
   private final Methods methods;
   private final EconomyShopGUI plugin;
   String section;
   String material;
   String displayname;
   Double buyPrice;
   Double sellPrice;

   public AddItem(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.methods = methods;
   }

   public String getName() {
      return "additem";
   }

   public String getDescription() {
      return Lang.EDITSHOP_ADD_ITEM_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_ADD_ITEM_SUBCOMMAND_SYNTAX.get().getLegacy();
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (args.length > 1) {
         this.section = this.methods.getSection(logger, args[1]);
         if (this.section != null) {
            if (args.length > 2) {
               this.material = this.methods.getMaterial(logger, args[2]);
               if (this.material != null) {
                  if (args.length > 3) {
                     this.buyPrice = this.methods.getPrice(logger, args[3]);
                     if (this.buyPrice != null) {
                        if (args.length > 4) {
                           this.sellPrice = this.methods.getPrice(logger, args[4]);
                           if (this.sellPrice != null) {
                              if (args.length > 5) {
                                 StringBuilder value2 = new StringBuilder();

                                 for(int i = 5; i < args.length; ++i) {
                                    value2.append(args[i]).append(" ");
                                 }

                                 this.displayname = value2.substring(0, value2.length() - 1);
                              }

                              this.addItemToShopsConfig(logger);
                           }
                        } else {
                           SendMessage.sendMessage(logger, this.getSyntax());
                        }
                     }
                  } else {
                     SendMessage.sendMessage(logger, this.getSyntax());
                  }
               }
            } else {
               SendMessage.sendMessage(logger, this.getSyntax());
            }
         }
      } else {
         SendMessage.sendMessage(logger, this.getSyntax());
      }
   }

   public List<String> getTabCompletion(String[] args) {
      ArrayList completions;
      ArrayList completions;
      switch(args.length) {
      case 2:
         if (!args[1].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[1], this.plugin.getShopSections(), completions);
            Collections.sort(completions);
            return completions;
         }

         return this.plugin.getShopSections();
      case 3:
         if (!args[2].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[2], this.plugin.getSupportedMatNames(), completions);
            Collections.sort(completions);
            return completions;
         }

         return this.plugin.getSupportedMatNames();
      case 4:
      case 5:
         String currentArg = args[args.length - 1];
         if (!currentArg.isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(currentArg, this.plugin.getExamplePrices(), completions);
            Collections.sort(completions);
            return completions;
         }

         return this.plugin.getExamplePrices();
      case 6:
         if (!args[5].isEmpty()) {
            completions = new ArrayList();
            StringUtil.copyPartialMatches(args[0], this.methods.getExampleItemNames(), completions);
            Collections.sort(completions);
            return completions;
         }

         return this.methods.getExampleItemNames();
      default:
         return null;
      }
   }

   private void addItemToShopsConfig(Object logger) {
      SendMessage.sendMessage(logger, Lang.EDITSHOP_ADDING_ITEM.get().replace("%shop%", this.section));
      String locateItem = "pages." + this.getNextAvailablePosition();
      ConfigManager.getShop(this.section).set(locateItem + ".material", this.material);
      if (this.displayname != null) {
         ConfigManager.getShop(this.section).set(locateItem + ".displayname", this.displayname);
      }

      ConfigManager.getShop(this.section).set(locateItem + ".buy", this.buyPrice);
      ConfigManager.getShop(this.section).set(locateItem + ".sell", this.sellPrice);
      ConfigManager.saveShop(this.section);
      SendMessage.sendMessage(logger, Lang.EDITSHOP_ADD_ITEM_SUCCESSFUL.get().replace("%shop%", this.section).replace("%itemPath%", locateItem));
      SendMessage.sendMessage(logger, Lang.EDITSHOP_RELOAD_SHOP_TO_SEE_CHANGES.get());
   }

   private String getNextAvailablePosition() {
      Config config = ConfigManager.getShop(this.section);
      int page = config.getConfigurationSection("pages").getKeys(false).size();
      if (page == 0) {
         return "page1.items.1";
      } else {
         LinkedList<String> pages = new LinkedList(config.getConfigurationSection("pages").getKeys(false));
         String p = (String)pages.get(page - 1);
         int size = config.getInt("pages." + p + ".gui-rows", 6) * 9 - (this.plugin.navBar.isEnabled(this.section) ? 9 : 0);
         if (config.getConfigurationSection("pages." + p + ".items").getKeys(false).size() >= size) {
            return "page" + (pages.size() + 1) + ".items.1";
         } else {
            Set<String> keys = config.getConfigurationSection("pages." + p + ".items").getKeys(false);

            int pos;
            for(pos = keys.size() + 1; keys.contains(String.valueOf(pos)); ++pos) {
            }

            return p + ".items." + pos;
         }
      }
   }
}
