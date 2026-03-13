package com.nisovin.shopkeepers.playershops.inactivity;

import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.config.Settings;
import com.nisovin.shopkeepers.util.java.Validate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerInactivity {
   private final SKShopkeepersPlugin plugin;
   private final PlayerInactivity.DeleteInactivePlayerShopsTask task;

   public PlayerInactivity(SKShopkeepersPlugin plugin) {
      Validate.notNull(plugin, (String)"plugin is null");
      this.plugin = plugin;
      this.task = new PlayerInactivity.DeleteInactivePlayerShopsTask(plugin);
   }

   public void onEnable() {
      if (Settings.playerShopkeeperInactiveDays > 0) {
         this.task.start();
      }
   }

   public void onDisable() {
      this.task.stop();
   }

   public void deleteShopsOfInactivePlayers() {
      if (Settings.playerShopkeeperInactiveDays > 0) {
         (new DeleteShopsOfInactivePlayersProcedure(this.plugin)).start();
      }
   }

   private final class DeleteInactivePlayerShopsTask implements Runnable {
      private static final long INTERVAL_TICKS = 288000L;
      private final Plugin plugin;
      @Nullable
      private BukkitTask task = null;

      public DeleteInactivePlayerShopsTask(Plugin param2) {
         Validate.notNull(plugin, (String)"plugin is null");
         this.plugin = plugin;
      }

      public void start() {
         this.stop();
         this.task = Bukkit.getScheduler().runTaskTimer(this.plugin, this, 5L, 288000L);
      }

      public void stop() {
         if (this.task != null) {
            this.task.cancel();
            this.task = null;
         }

      }

      public void run() {
         PlayerInactivity.this.deleteShopsOfInactivePlayers();
      }
   }
}
