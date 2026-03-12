package me.gypopo.economyshopgui.commands.editshop.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

public class AddHandItem extends SubCommad {
   private final Methods methods;
   private final EconomyShopGUI plugin;
   String section;
   Double buyPrice;
   Double sellPrice;

   public AddHandItem(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.methods = methods;
   }

   public String getName() {
      return "addhanditem";
   }

   public String getDescription() {
      return Lang.EDITSHOP_ADD_HAND_ITEM_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_ADD_HAND_ITEM_SUBCOMMAND_SYNTAX.get().getLegacy();
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (logger instanceof Player) {
         Player player = (Player)logger;
         if (args.length > 1) {
            this.section = this.methods.getSection(player, args[1]);
            if (this.section != null) {
               if (args.length > 2) {
                  this.buyPrice = this.methods.getPrice(player, args[2]);
                  if (this.buyPrice != null) {
                     if (args.length > 3) {
                        this.sellPrice = this.methods.getPrice(player, args[3]);
                        if (this.sellPrice != null) {
                           this.addItemToShopsConfig(player, this.plugin.versionHandler.getItemInHand(player));
                        }
                     } else {
                        SendMessage.chatToPlayer(player, this.getSyntax());
                     }
                  }
               } else {
                  SendMessage.chatToPlayer(player, this.getSyntax());
               }
            }
         } else {
            SendMessage.chatToPlayer(player, this.getSyntax());
         }
      }
   }

   public List<String> getTabCompletion(String[] args) {
      switch(args.length) {
      case 2:
         if (!args[1].isEmpty()) {
            List<String> completions = new ArrayList();
            StringUtil.copyPartialMatches(args[1], this.plugin.getShopSections(), completions);
            Collections.sort(completions);
            return completions;
         }

         return this.plugin.getShopSections();
      case 3:
      case 4:
         String currentArg = args[args.length - 1];
         if (!currentArg.isEmpty()) {
            List<String> completions = new ArrayList();
            StringUtil.copyPartialMatches(currentArg, this.plugin.getExamplePrices(), completions);
            Collections.sort(completions);
            return completions;
         }

         return this.plugin.getExamplePrices();
      default:
         return null;
      }
   }

   private void addItemToShopsConfig(Player player, ItemStack itemToAdd) {
      SendMessage.chatToPlayer(player, Lang.EDITSHOP_ADDING_ITEM.get().replace("%shop%", this.section));
      String locateItem = "pages." + this.getNextAvailablePosition();
      HashMap<String, Object> values = this.plugin.createItem.getShopItem(itemToAdd, this.buyPrice, this.sellPrice);
      Iterator var5 = values.keySet().iterator();

      while(var5.hasNext()) {
         String value = (String)var5.next();
         ConfigManager.getShop(this.section).set(locateItem + "." + value, values.get(value));
      }

      ConfigManager.saveShop(this.section);
      SendMessage.chatToPlayer(player, Lang.EDITSHOP_ADD_ITEM_SUCCESSFUL.get().replace("%shop%", this.section).replace("%itemPath%", locateItem));
      SendMessage.chatToPlayer(player, Lang.EDITSHOP_RELOAD_SHOP_TO_SEE_CHANGES.get());
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
