package me.gypopo.economyshopgui.commands.editshop.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.Methods;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.files.ConfigManager;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

public class DeleteItem extends SubCommad {
   private final Methods methods;
   private final EconomyShopGUI plugin;
   String section;
   String itemLoc;

   public DeleteItem(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.methods = methods;
   }

   public String getName() {
      return "deleteitem";
   }

   public String getDescription() {
      return Lang.EDITSHOP_REMOVE_ITEM_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_REMOVE_ITEM_SUBCOMMAND_SYNTAX.get().getLegacy();
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (args.length > 1) {
         this.section = this.methods.getSection(logger, args[1]);
         if (this.section != null) {
            if (args.length > 2) {
               this.itemLoc = this.methods.getItemLoc(logger, this.section, args[2]);
               if (this.itemLoc != null) {
                  this.removeItem(logger);
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
         if (this.isSection(args[1])) {
            if (!args[2].isEmpty()) {
               completions = new ArrayList();
               StringUtil.copyPartialMatches(args[2], this.plugin.getSection(args[1]).getItemLocs(), completions);
               Collections.sort(completions);
               return completions;
            }

            return this.plugin.getSection(args[1]).getItemLocs();
         }

         return null;
      default:
         return null;
      }
   }

   private boolean isSection(String section) {
      return this.plugin.getShopSections().contains(section);
   }

   private List<String> getClosest(String current, Set<String> collection) {
      List<String> completions = new ArrayList();
      StringUtil.copyPartialMatches(current, collection, completions);
      Collections.sort(completions);
      return completions;
   }

   private void removeItem(Object logger) {
      SendMessage.sendMessage(logger, Lang.EDITSHOP_REMOVING_ITEM.get());
      ConfigManager.getShop(this.section).set("pages." + this.itemLoc, (Object)null);
      ConfigManager.saveShop(this.section);
      SendMessage.sendMessage(logger, Lang.EDITSHOP_REMOVE_ITEM_SUCCESSFUL.get().replace("%itemPath%", this.section + "." + this.itemLoc));
      SendMessage.sendMessage(logger, Lang.EDITSHOP_RELOAD_SHOP_TO_SEE_CHANGES.get());
   }
}
