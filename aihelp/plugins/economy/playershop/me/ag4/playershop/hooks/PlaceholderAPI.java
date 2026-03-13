package me.ag4.playershop.hooks;

import me.ag4.playershop.api.PlayerShopAPI;
import me.ag4.playershop.files.Lang;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlaceholderAPI extends PlaceholderExpansion {
   public String getIdentifier() {
      return "playershop";
   }

   public String getAuthor() {
      return "AG4";
   }

   public String getVersion() {
      return "1.16.5";
   }

   public boolean canRegister() {
      return true;
   }

   public boolean persist() {
      return true;
   }

   public String onRequest(OfflinePlayer player, String params) {
      if (player == null) {
         return "";
      } else if (params.equals("shop_max")) {
         int i = Integer.parseInt(Integer.toString(PlayerShopAPI.getPlayerShopMax((Player)player)));
         return Integer.MAX_VALUE == i ? Lang.Placeholder_Shop_Unlimited.toString() : Lang.Placeholder_Shop_Max.toString().replace("{count}", Integer.toString(PlayerShopAPI.getPlayerShopMax((Player)player)));
      } else {
         return params.equals("shop_count") ? Lang.Placeholder_Shop_Count.toString().replace("{count}", Integer.toString(PlayerShopAPI.getPlayerShopCount(player.getUniqueId()))) : null;
      }
   }
}
