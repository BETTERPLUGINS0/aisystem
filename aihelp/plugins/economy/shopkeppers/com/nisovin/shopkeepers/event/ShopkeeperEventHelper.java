package com.nisovin.shopkeepers.event;

import com.nisovin.shopkeepers.api.events.PlayerDeleteShopkeeperEvent;
import com.nisovin.shopkeepers.api.shopkeeper.Shopkeeper;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ShopkeeperEventHelper {
   private ShopkeeperEventHelper() {
   }

   public static PlayerDeleteShopkeeperEvent callPlayerDeleteShopkeeperEvent(Shopkeeper shopkeeper, Player player) {
      Validate.notNull(shopkeeper, (String)"shopkeeper is null");
      Validate.notNull(player, (String)"player is null");
      PlayerDeleteShopkeeperEvent event = new PlayerDeleteShopkeeperEvent(shopkeeper, player);
      Bukkit.getPluginManager().callEvent(event);
      if (event.isCancelled()) {
         Log.debug(() -> {
            String var10000 = shopkeeper.getLogPrefix();
            return var10000 + "PlayerDeleteShopkeeperEvent for player '" + player.getName() + "' was cancelled.";
         });
      }

      return event;
   }
}
