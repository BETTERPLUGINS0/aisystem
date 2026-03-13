package com.nisovin.shopkeepers.playershops.inactivity;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.events.PlayerInactiveEvent;
import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;
import com.nisovin.shopkeepers.util.bukkit.SchedulerUtils;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import com.nisovin.shopkeepers.util.java.CollectionUtils;
import com.nisovin.shopkeepers.util.java.Validate;
import com.nisovin.shopkeepers.util.logging.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.Nullable;

class DeleteShopsOfInactivePlayersProcedure {
   private final SKShopkeepersPlugin plugin;
   private final SKShopkeeperRegistry shopkeeperRegistry;
   private final int playerInactivityDays;
   private boolean started = false;
   private final long currentTimeMillis = System.currentTimeMillis();
   private final Map<User, DeleteShopsOfInactivePlayersProcedure.InactivePlayerData> inactivePlayers = new HashMap();

   public DeleteShopsOfInactivePlayersProcedure(SKShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
      this.shopkeeperRegistry = plugin.getShopkeeperRegistry();
      this.playerInactivityDays = Settings.playerShopkeeperInactiveDays;
   }

   public void start() {
      Validate.State.isTrue(!this.started, "Already started!");
      this.started = true;
      if (this.playerInactivityDays > 0) {
         Log.info("Checking for shopkeepers of inactive players.");
         this.collectShopOwners();
         if (!this.inactivePlayers.isEmpty()) {
            this.asyncCheckInactivityOfAllShopOwnersAndContinue();
         }
      }
   }

   private void collectShopOwners() {
      this.shopkeeperRegistry.getAllPlayerShopkeepers().forEach((playerShop) -> {
         this.inactivePlayers.put(playerShop.getOwnerUser(), (Object)null);
      });
   }

   private void asyncCheckInactivityOfAllShopOwnersAndContinue() {
      (new BukkitRunnable() {
         public void run() {
            DeleteShopsOfInactivePlayersProcedure.this.setUpInactiveShopOwners();
            if (!DeleteShopsOfInactivePlayersProcedure.this.inactivePlayers.isEmpty()) {
               if (!this.isCancelled()) {
                  SchedulerUtils.runTaskOrOmit(DeleteShopsOfInactivePlayersProcedure.this.plugin, () -> {
                     DeleteShopsOfInactivePlayersProcedure.this.continueWithInactiveShopOwners();
                  });
               }
            }
         }
      }).runTaskAsynchronously(this.plugin);
   }

   private void setUpInactiveShopOwners() {
      Iterator iterator = this.inactivePlayers.entrySet().iterator();

      while(iterator.hasNext()) {
         Entry<User, DeleteShopsOfInactivePlayersProcedure.InactivePlayerData> entry = (Entry)iterator.next();
         User user = (User)entry.getKey();
         DeleteShopsOfInactivePlayersProcedure.InactivePlayerData data = this.setUpIfInactive(user);
         if (data == null) {
            iterator.remove();
         } else {
            entry.setValue(data);
         }
      }

      assert !CollectionUtils.containsNull(this.inactivePlayers.values());

   }

   @Nullable
   private DeleteShopsOfInactivePlayersProcedure.InactivePlayerData setUpIfInactive(User user) {
      assert user != null;

      OfflinePlayer offlinePlayer = user.getOfflinePlayer();
      if (!offlinePlayer.hasPlayedBefore()) {
         return null;
      } else {
         long lastPlayedMillis = offlinePlayer.getLastPlayed();
         if (lastPlayedMillis == 0L) {
            return null;
         } else {
            long millisSinceLastPlayed = this.currentTimeMillis - lastPlayedMillis;
            int daysSinceLastPlayed = (int)TimeUnit.MILLISECONDS.toDays(millisSinceLastPlayed);
            return daysSinceLastPlayed < this.playerInactivityDays ? null : new DeleteShopsOfInactivePlayersProcedure.InactivePlayerData(daysSinceLastPlayed);
         }
      }
   }

   private void continueWithInactiveShopOwners() {
      assert Bukkit.isPrimaryThread();

      assert !this.inactivePlayers.isEmpty();

      assert !CollectionUtils.containsNull(this.inactivePlayers.values());

      this.collectShopsOfInactivePlayers();
      this.deleteShopsOfInactivePlayers();
   }

   private void collectShopsOfInactivePlayers() {
      this.shopkeeperRegistry.getAllPlayerShopkeepers().forEach((playerShop) -> {
         User shopOwner = playerShop.getOwnerUser();
         DeleteShopsOfInactivePlayersProcedure.InactivePlayerData inactivePlayerData = (DeleteShopsOfInactivePlayersProcedure.InactivePlayerData)this.inactivePlayers.get(shopOwner);
         if (inactivePlayerData != null) {
            inactivePlayerData.getShopkeepers().add(playerShop);
         }

      });
   }

   private void deleteShopsOfInactivePlayers() {
      this.inactivePlayers.forEach((user, nullableInactivePlayerData) -> {
         DeleteShopsOfInactivePlayersProcedure.InactivePlayerData inactivePlayerData = (DeleteShopsOfInactivePlayersProcedure.InactivePlayerData)Unsafe.assertNonNull(nullableInactivePlayerData);
         List<? extends PlayerShopkeeper> shopkeepers = inactivePlayerData.getShopkeepers();
         if (!shopkeepers.isEmpty()) {
            int originalShopkeepersCount = shopkeepers.size();
            PlayerInactiveEvent event = new PlayerInactiveEvent(user, shopkeepers);
            Bukkit.getPluginManager().callEvent(event);
            if (!event.isCancelled() && !shopkeepers.isEmpty()) {
               shopkeepers.forEach((playerShop) -> {
                  if (!playerShop.isValid()) {
                     Log.debug(() -> {
                        String var10000 = playerShop.getUniqueIdLogPrefix();
                        return var10000 + "Deletion due to inactivity of owner " + playerShop.getOwnerString() + " (last seen " + inactivePlayerData.getLastSeenDaysAgo() + " days ago) skipped: The shopkeeper has already been removed.";
                     });
                  } else {
                     String var10000 = playerShop.getUniqueIdLogPrefix();
                     Log.info(var10000 + "Deletion due to inactivity of owner " + playerShop.getOwnerString() + " (last seen " + inactivePlayerData.getLastSeenDaysAgo() + " days ago).");
                     playerShop.delete();
                  }
               });
            } else {
               Log.debug(() -> {
                  String var10000 = TextUtils.getPlayerString(user);
                  return "Ignoring inactive player " + var10000 + " (last seen " + inactivePlayerData.getLastSeenDaysAgo() + " days ago) and their " + originalShopkeepersCount + " shopkeepers" + (shopkeepers.size() != originalShopkeepersCount ? " (reduced to " + shopkeepers.size() + ")" : "") + ": Cancelled by a plugin.";
               });
            }
         }
      });
      this.plugin.getShopkeeperStorage().saveIfDirty();
   }

   private static class InactivePlayerData {
      private final int lastSeenDaysAgo;
      private final List<PlayerShopkeeper> shopkeepers = new ArrayList();

      InactivePlayerData(int lastSeenDaysAgo) {
         this.lastSeenDaysAgo = lastSeenDaysAgo;
      }

      int getLastSeenDaysAgo() {
         return this.lastSeenDaysAgo;
      }

      List<PlayerShopkeeper> getShopkeepers() {
         return this.shopkeepers;
      }
   }
}
