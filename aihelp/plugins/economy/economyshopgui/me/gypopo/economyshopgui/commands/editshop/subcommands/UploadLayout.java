package me.gypopo.economyshopgui.commands.editshop.subcommands;

import java.util.List;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.methodes.MarketplaceIntegration;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UploadLayout extends SubCommad {
   public String getName() {
      return "uploadLayout";
   }

   public String getDescription() {
      return "§aUpload your current shop layout to https://layouts.gpplugins.com/ for other people to use!";
   }

   public String getSyntax() {
      return "§a/editshop uploadLayout";
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (logger instanceof Player) {
         (new MarketplaceIntegration()).startUpload((Player)logger);
      }
   }

   public List<String> getTabCompletion(String[] args) {
      return null;
   }
}
