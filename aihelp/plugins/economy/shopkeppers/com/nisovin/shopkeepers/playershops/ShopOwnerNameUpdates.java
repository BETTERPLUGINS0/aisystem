package com.nisovin.shopkeepers.playershops;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.ShopkeeperRegistry;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.debug.DebugOptions;
import com.nisovin.shopkeepers.dependencies.citizens.CitizensUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ShopOwnerNameUpdates implements Listener {
   private final SKShopkeepersPlugin plugin;

   public ShopOwnerNameUpdates(SKShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
   }

   public void onEnable() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
      Iterator var1 = Bukkit.getOnlinePlayers().iterator();

      while(var1.hasNext()) {
         Player player = (Player)var1.next();

         assert player != null;

         if (!CitizensUtils.isNPC(player)) {
            UUID playerId = player.getUniqueId();
            String playerName = (String)Unsafe.assertNonNull(player.getName());
            this.updateShopkeepersForPlayer(playerId, playerName);
         }
      }

   }

   public void onDisable() {
      HandlerList.unregisterAll(this);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      if (player.isOnline()) {
         String playerName = (String)Unsafe.assertNonNull(player.getName());
         this.updateShopkeepersForPlayer(player.getUniqueId(), playerName);
      }
   }

   private void updateShopkeepersForPlayer(UUID playerId, String playerName) {
      Log.debug(DebugOptions.ownerNameUpdates, () -> {
         String var10000 = TextUtils.getPlayerString(playerName, playerId);
         return "Updating shopkeeper owner names for: " + var10000;
      });
      boolean dirty = false;
      ShopkeeperRegistry shopkeeperRegistry = this.plugin.getShopkeeperRegistry();
      Iterator var5 = shopkeeperRegistry.getPlayerShopkeepersByOwner(playerId).iterator();

      while(var5.hasNext()) {
         PlayerShopkeeper playerShop = (PlayerShopkeeper)var5.next();
         String ownerName = playerShop.getOwnerName();
         if (!ownerName.equals(playerName)) {
            Log.debug(DebugOptions.ownerNameUpdates, () -> {
               return playerShop.getLogPrefix() + "Updating owner name '" + ownerName + "' to '" + playerName + "'.";
            });
            playerShop.setOwner(playerId, playerName);
            dirty = true;
         } else if (!dirty) {
            Log.debug(DebugOptions.ownerNameUpdates, () -> {
               String var10000 = playerShop.getLogPrefix();
               return var10000 + "Owner name '" + ownerName + "' is up-to-date. Assuming the owner names of all shopkeepers are consistent, we skip checking all other shopkeepers.";
            });
            return;
         }
      }

      if (dirty) {
         this.plugin.getShopkeeperStorage().save();
      }

   }
}
