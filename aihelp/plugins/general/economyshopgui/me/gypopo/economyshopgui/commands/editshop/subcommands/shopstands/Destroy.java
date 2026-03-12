package me.gypopo.economyshopgui.commands.editshop.subcommands.shopstands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.Methods;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.objects.stands.Stand;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

public class Destroy extends SubCommad {
   private final Methods methods;
   private final EconomyShopGUI plugin;

   public Destroy(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.methods = methods;
   }

   public String getName() {
      return "destroy";
   }

   public String getDescription() {
      return Lang.EDITSHOP_SHOPSTANDS_DESTROY_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_SHOPSTANDS_DESTROY_SUBCOMMAND_SYNTAX.get().getLegacy();
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop.shopstands." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (args.length > 2) {
         int id;
         try {
            id = Integer.parseInt(args[2]);
         } catch (NumberFormatException var5) {
            SendMessage.warnMessage(logger, Lang.NO_VALID_AMOUNT.get());
            return;
         }

         Stand stand = this.plugin.getStandProvider().getStand(id);
         if (stand == null) {
            SendMessage.warnMessage(logger, Lang.UNKNOWN_SHOP_STAND_ID.get().replace("%id%", String.valueOf(id)));
         } else {
            this.plugin.getStandProvider().destroy(stand);
            SendMessage.infoMessage(logger, Lang.REMOVED_SHOP_STAND.get().replace("%location%", stand.getLoc().toString()));
         }
      } else {
         SendMessage.sendMessage(logger, this.getSyntax());
      }
   }

   public List<String> getTabCompletion(String[] args) {
      switch(args.length) {
      case 3:
         if (!args[2].isEmpty()) {
            List<String> completions = new ArrayList();
            StringUtil.copyPartialMatches(args[2], (Iterable)this.plugin.getStandProvider().getStands().stream().map((s) -> {
               return String.valueOf(s.getId());
            }).collect(Collectors.toList()), completions);
            Collections.sort(completions);
            return completions;
         }

         return (List)this.plugin.getStandProvider().getStands().stream().map((s) -> {
            return String.valueOf(s.getId());
         }).collect(Collectors.toList());
      default:
         return null;
      }
   }
}
