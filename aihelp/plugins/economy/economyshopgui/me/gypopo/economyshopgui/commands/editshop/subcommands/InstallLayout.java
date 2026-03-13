package me.gypopo.economyshopgui.commands.editshop.subcommands;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import me.gypopo.economyshopgui.commands.editshop.SubCommad;
import me.gypopo.economyshopgui.methodes.MarketplaceIntegration;
import me.gypopo.economyshopgui.methodes.SendMessage;
import me.gypopo.economyshopgui.util.PermissionsCache;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InstallLayout extends SubCommad {
   public String getName() {
      return "installLayout";
   }

   public String getDescription() {
      return "§aInstall a custom premade shop layout from https://layouts.gpplugins.com/";
   }

   public String getSyntax() {
      return "§a/editshop installLayout <layoutID>";
   }

   public boolean hasPermission(CommandSender source) {
      return PermissionsCache.hasPermission(source, "EconomyShopGUI.eshop." + this.getName());
   }

   public void perform(Object logger, String[] args) {
      if (logger instanceof Player) {
         if (args.length > 1) {
            UUID uuid;
            try {
               uuid = UUID.fromString(args[1]);
            } catch (IllegalArgumentException var5) {
               SendMessage.sendMessage(logger, ChatColor.RED + "Invalid layout ID for " + args[1]);
               return;
            }

            (new MarketplaceIntegration()).downloadLayout((Player)logger, uuid.toString(), Arrays.asList(args).contains("-unsafe"));
         } else {
            SendMessage.sendMessage(logger, this.getSyntax());
         }
      }
   }

   public List<String> getTabCompletion(String[] args) {
      return null;
   }
}
