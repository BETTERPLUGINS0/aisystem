package github.nighter.smartspawner.spawner.gui.synchronization.listeners;

import github.nighter.smartspawner.spawner.gui.main.SpawnerMenuHolder;
import github.nighter.smartspawner.spawner.gui.storage.StoragePageHolder;
import github.nighter.smartspawner.spawner.gui.storage.filter.FilterConfigHolder;
import github.nighter.smartspawner.spawner.gui.synchronization.managers.ViewerTrackingManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.Set;
import java.util.UUID;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryEventListener implements Listener {
   private final ViewerTrackingManager viewerTrackingManager;
   private final Runnable onViewerAdded;
   private final Set<Class<? extends InventoryHolder>> validHolderTypes;

   public InventoryEventListener(ViewerTrackingManager viewerTrackingManager, Runnable onViewerAdded) {
      this.viewerTrackingManager = viewerTrackingManager;
      this.onViewerAdded = onViewerAdded;
      this.validHolderTypes = Set.of(SpawnerMenuHolder.class, StoragePageHolder.class, FilterConfigHolder.class);
   }

   @EventHandler(
      priority = EventPriority.MONITOR,
      ignoreCancelled = true
   )
   public void onInventoryOpen(InventoryOpenEvent event) {
      HumanEntity var3 = event.getPlayer();
      if (var3 instanceof Player) {
         Player player = (Player)var3;
         InventoryHolder holder = event.getInventory().getHolder(false);
         if (this.isValidHolder(holder)) {
            UUID playerId = player.getUniqueId();
            SpawnerData spawnerData = null;
            ViewerTrackingManager.ViewerType viewerType = null;
            if (holder instanceof SpawnerMenuHolder) {
               SpawnerMenuHolder spawnerHolder = (SpawnerMenuHolder)holder;
               spawnerData = spawnerHolder.getSpawnerData();
               viewerType = ViewerTrackingManager.ViewerType.MAIN_MENU;
            } else if (holder instanceof StoragePageHolder) {
               StoragePageHolder storageHolder = (StoragePageHolder)holder;
               spawnerData = storageHolder.getSpawnerData();
               viewerType = ViewerTrackingManager.ViewerType.STORAGE;
            } else if (holder instanceof FilterConfigHolder) {
               FilterConfigHolder filterHolder = (FilterConfigHolder)holder;
               spawnerData = filterHolder.getSpawnerData();
               viewerType = ViewerTrackingManager.ViewerType.FILTER;
            }

            if (spawnerData != null && viewerType != null) {
               this.viewerTrackingManager.trackViewer(playerId, spawnerData, viewerType);
               this.onViewerAdded.run();
            }

         }
      }
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onInventoryClose(InventoryCloseEvent event) {
      HumanEntity var3 = event.getPlayer();
      if (var3 instanceof Player) {
         Player player = (Player)var3;
         ViewerTrackingManager.ViewerInfo info = this.viewerTrackingManager.getViewerInfo(player.getUniqueId());
         if (info != null && info.getSpawnerData() != null) {
            info.getSpawnerData().updateLastInteractedPlayer(player.getName());
         }

         this.viewerTrackingManager.untrackViewer(player.getUniqueId());
      }
   }

   private boolean isValidHolder(InventoryHolder holder) {
      return holder != null && this.validHolderTypes.contains(holder.getClass());
   }
}
