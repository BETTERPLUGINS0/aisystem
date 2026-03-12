package me.gypopo.economyshopgui.util;

import java.util.Locale;
import me.gypopo.economyshopgui.files.ConfigManager;

public enum SortOrder {
   SHOP_LOAD_ORDER,
   ASCENDING_SELL_PRICE,
   DESCENDING_SELL_PRICE;

   public static SortOrder fromConfig() {
      String order = ConfigManager.getConfig().getString("sort-items-by", "DESCENDING_PRICE");

      try {
         return valueOf(order.toUpperCase(Locale.ROOT));
      } catch (IllegalArgumentException var2) {
         return DESCENDING_SELL_PRICE;
      }
   }

   // $FF: synthetic method
   private static SortOrder[] $values() {
      return new SortOrder[]{SHOP_LOAD_ORDER, ASCENDING_SELL_PRICE, DESCENDING_SELL_PRICE};
   }
}
