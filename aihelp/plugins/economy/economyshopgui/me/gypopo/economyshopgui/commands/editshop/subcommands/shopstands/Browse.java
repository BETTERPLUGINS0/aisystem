package me.gypopo.economyshopgui.commands.editshop.subcommands.shopstands;

import java.util.List;
import me.gypopo.economyshopgui.EconomyShopGUI;
import me.gypopo.economyshopgui.commands.editshop.Methods;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.files.Lang;
import me.gypopo.economyshopgui.objects.inventorys.StandBrowser;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Browse extends SubCommad {
   private final Methods methods;
   private final EconomyShopGUI plugin;

   public Browse(EconomyShopGUI plugin, Methods methods) {
      this.plugin = plugin;
      this.methods = methods;
   }

   public String getName() {
      return "browse";
   }

   public String getDescription() {
      return Lang.EDITSHOP_SHOPSTANDS_BROWSE_SUBCOMMAND_DESC.get().getLegacy();
   }

   public String getSyntax() {
      return Lang.EDITSHOP_SHOPSTANDS_BROWSE_SUBCOMMAND_SYNTAX.get().getLegacy();
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop.shopstands." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (logger instanceof Player) {
         new StandBrowser(this.plugin, (Player)logger);
      }
   }

   public List<String> getTabCompletion(String[] args) {
      return null;
   }
}
