package com.nisovin.shopkeepers.shopcreation;

import com.nisovin.shopkeepers.lang.Messages;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.bukkit.TextUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.checkerframework.checker.nullness.qual.Nullable;

class ShopCreationItemSelectionTask implements Runnable {
   private static final long DELAY_TICKS = 5L;
   private static final Map<UUID, ShopCreationItemSelectionTask> activeTasks = new HashMap();
   private final Plugin plugin;
   private final Player player;
   @Nullable
   private BukkitTask bukkitTask = null;

   static void start(Plugin plugin, Player player) {
      assert plugin != null && player != null;

      ShopCreationItemSelectionTask task = (ShopCreationItemSelectionTask)activeTasks.computeIfAbsent(player.getUniqueId(), (uuid) -> {
         return new ShopCreationItemSelectionTask(plugin, player);
      });

      assert task != null;

      task.start();
   }

   static void cleanupAndCancel(Player player) {
      assert player != null;

      ShopCreationItemSelectionTask task = (ShopCreationItemSelectionTask)activeTasks.remove(player.getUniqueId());
      if (task != null) {
         task.cancel();
      }

   }

   static void onDisable() {
      activeTasks.clear();
   }

   private static void cleanup(Player player) {
      activeTasks.remove(player.getUniqueId());
   }

   private ShopCreationItemSelectionTask(Plugin plugin, Player player) {
      assert plugin != null && player != null;

      this.plugin = plugin;
      this.player = player;
   }

   private void start() {
      this.cancel();
      this.bukkitTask = Bukkit.getScheduler().runTaskLater(this.plugin, this, 5L);
   }

   private void cancel() {
      if (this.bukkitTask != null) {
         this.bukkitTask.cancel();
         this.bukkitTask = null;
      }

   }

   public void run() {
      cleanup(this.player);
      if (this.player.isOnline()) {
         ItemStack itemInHand = this.player.getInventory().getItemInMainHand();
         ShopCreationItem shopCreationItem = new ShopCreationItem(itemInHand);
         if (shopCreationItem.isShopCreationItem()) {
            boolean hasShopType = shopCreationItem.hasShopType();
            boolean hasObjectType = shopCreationItem.hasObjectType();
            TextUtils.sendMessage(this.player, (Text)Messages.creationItemSelected, (Object[])("shopTypeSelection", hasShopType ? "" : Messages.creationItemShopTypeSelection.copy(), "objectTypeSelection", hasObjectType ? "" : Messages.creationItemObjectTypeSelection.copy()));
         }
      }
   }
}
