package com.nisovin.shopkeepers.tradenotifications;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class NotificationUserPreferences implements Listener {
   private final Plugin plugin;
   private final Map<UUID, NotificationUserPreferences.UserPreferences> userPreferences = new HashMap();

   public NotificationUserPreferences(Plugin plugin) {
      this.plugin = plugin;
   }

   public void onEnable() {
      Bukkit.getPluginManager().registerEvents(this, this.plugin);
   }

   public void onDisable() {
      HandlerList.unregisterAll(this);
      this.userPreferences.clear();
   }

   private NotificationUserPreferences.UserPreferences getOrCreateUserPreferences(Player player) {
      Validate.notNull(player, (String)"player is null");
      NotificationUserPreferences.UserPreferences preferences = (NotificationUserPreferences.UserPreferences)this.userPreferences.computeIfAbsent(player.getUniqueId(), (playerId) -> {
         return new NotificationUserPreferences.UserPreferences();
      });

      assert preferences != null;

      return preferences;
   }

   public boolean hasReceivedDisableTradeNotificationsHint(Player player) {
      return this.getOrCreateUserPreferences(player).receivedDisableTradeNotificationsHint;
   }

   public void setReceivedDisableTradeNotificationsHint(Player player, boolean received) {
      this.getOrCreateUserPreferences(player).receivedDisableTradeNotificationsHint = received;
   }

   public boolean isNotifyOnTrades(Player player) {
      return this.getOrCreateUserPreferences(player).notifyOnTrades;
   }

   public void setNotifyOnTrades(Player player, boolean notify) {
      this.getOrCreateUserPreferences(player).notifyOnTrades = notify;
   }

   private void clearUserPreferences(Player player) {
      Validate.notNull(player, (String)"player is null");
      this.userPreferences.remove(player.getUniqueId());
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   void onPlayerQuit(PlayerQuitEvent event) {
      this.clearUserPreferences(event.getPlayer());
   }

   private static class UserPreferences {
      public boolean notifyOnTrades = true;
      public boolean receivedDisableTradeNotificationsHint = false;
   }
}
