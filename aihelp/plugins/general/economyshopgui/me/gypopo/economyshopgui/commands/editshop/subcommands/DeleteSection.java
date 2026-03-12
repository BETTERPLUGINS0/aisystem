package me.gypopo.economyshopgui.commands.editshop.subcommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.Methods;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

public class DeleteSection extends SubCommad {
   private final Methods methods;
   private final EconomyShopGUI plugin;
   private String section;

   public DeleteSection(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.methods = methods;
   }

   public String getName() {
      return "deletesection";
   }

   public String getDescription() {
      return Lang.EDITSHOP_REMOVE_SECTION_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_REMOVE_SECTION_SUBCOMMAND_SYNTAX.get().getLegacy();
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (args.length > 1) {
         this.section = this.methods.getSection(logger, args[1]);
         if (this.section != null) {
            this.deleteSection(logger);
         }
      } else {
         SendMessage.infoMessage(logger, this.getSyntax());
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
      default:
         return null;
      }
   }

   private void deleteSection(Object logger) {
      SendMessage.sendMessage(logger, Lang.EDITSHOP_REMOVING_SECTION.get());
      this.plugin.getConfigManager().deleteSection(this.section);
      this.plugin.getConfigManager().deleteShopsConfig(this.section);
      SendMessage.sendMessage(logger, Lang.EDITSHOP_REMOVED_SECTION.get().replace("%itemPath%", "ShopSections." + this.section));
      SendMessage.sendMessage(logger, Lang.EDITSHOP_RELOAD_SHOP_TO_SEE_CHANGES.get());
   }
}
