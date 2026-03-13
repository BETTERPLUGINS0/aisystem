package github.nighter.smartspawner.spawner.gui.synchronization;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.gui.storage.StoragePageHolder;
import github.nighter.smartspawner.spawner.gui.storage.filter.FilterConfigHolder;
import github.nighter.smartspawner.spawner.gui.synchronization.listeners.InventoryEventListener;
import github.nighter.smartspawner.spawner.gui.synchronization.listeners.PlayerEventListener;
import github.nighter.smartspawner.spawner.gui.synchronization.managers.SlotCacheManager;
import github.nighter.smartspawner.spawner.gui.synchronization.managers.UpdateTaskManager;
import github.nighter.smartspawner.spawner.gui.synchronization.managers.ViewerTrackingManager;
import github.nighter.smartspawner.spawner.gui.synchronization.services.GuiUpdateService;
import github.nighter.smartspawner.spawner.gui.synchronization.services.StorageUpdateService;
import github.nighter.smartspawner.spawner.gui.synchronization.services.TimerUpdateService;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class SpawnerGuiViewManager {
   private final SmartSpawner plugin;
   private final ViewerTrackingManager viewerTrackingManager;
   private final SlotCacheManager slotCacheManager;
   private final UpdateTaskManager updateTaskManager;
   private final TimerUpdateService timerUpdateService;
   private final GuiUpdateService guiUpdateService;
   private final StorageUpdateService storageUpdateService;
   private final InventoryEventListener inventoryEventListener;
   private final PlayerEventListener playerEventListener;

   public SpawnerGuiViewManager(SmartSpawner plugin) {
      this.plugin = plugin;
      this.viewerTrackingManager = new ViewerTrackingManager();
      this.slotCacheManager = new SlotCacheManager(plugin);
      this.updateTaskManager = new UpdateTaskManager();
      this.timerUpdateService = new TimerUpdateService(plugin, this.viewerTrackingManager, this.slotCacheManager);
      this.guiUpdateService = new GuiUpdateService(plugin, this.slotCacheManager);
      this.storageUpdateService = new StorageUpdateService(plugin);
      this.inventoryEventListener = new InventoryEventListener(this.viewerTrackingManager, this::onViewerAdded);
      this.playerEventListener = new PlayerEventListener(this.viewerTrackingManager);
      Bukkit.getPluginManager().registerEvents(this.inventoryEventListener, plugin);
      Bukkit.getPluginManager().registerEvents(this.playerEventListener, plugin);
   }

   private void onViewerAdded() {
      if (!this.updateTaskManager.isRunning() && this.viewerTrackingManager.hasAnyViewers()) {
         this.updateTaskManager.startTask(this::processPeriodicUpdates);
      }

   }

   private void processPeriodicUpdates() {
      GuiUpdateService var10000 = this.guiUpdateService;
      ViewerTrackingManager var10001 = this.viewerTrackingManager;
      Objects.requireNonNull(var10001);
      var10000.processPendingUpdates(var10001::getViewerInfo, this::cleanupViewer);
      if (this.timerUpdateService.shouldProcessTimerUpdates()) {
         this.timerUpdateService.processTimerUpdates();
      }

      if (!this.viewerTrackingManager.hasAnyViewers()) {
         this.updateTaskManager.stopTask();
      }

   }

   private void cleanupViewer(UUID playerId) {
      this.viewerTrackingManager.untrackViewer(playerId);
      this.guiUpdateService.clearPlayerUpdates(playerId);
      this.timerUpdateService.clearPlayerTracking(playerId);
   }

   public boolean isTimerPlaceholdersEnabled() {
      return this.timerUpdateService.isTimerPlaceholdersEnabled();
   }

   public String calculateTimerDisplay(SpawnerData spawner) {
      return this.timerUpdateService.calculateTimerDisplay(spawner, (Player)null);
   }

   public String calculateTimerDisplay(SpawnerData spawner, Player player) {
      return this.timerUpdateService.calculateTimerDisplay(spawner, player);
   }

   public void recheckTimerPlaceholders() {
      this.timerUpdateService.recheckTimerPlaceholders();
   }

   public void clearSlotCache() {
      this.slotCacheManager.clearAndReinitialize();
   }

   public Set<Player> getViewers(String spawnerId) {
      return this.viewerTrackingManager.getViewers(spawnerId);
   }

   public boolean hasViewers(SpawnerData spawner) {
      return this.viewerTrackingManager.hasViewers(spawner);
   }

   public void clearAllTrackedGuis() {
      this.viewerTrackingManager.clearAll();
      this.guiUpdateService.clearAllPendingUpdates();
      this.timerUpdateService.clearAllTracking();
   }

   public void forceStateChangeUpdate(SpawnerData spawner) {
      this.timerUpdateService.forceStateChangeUpdate(spawner);
   }

   public void forceTimerUpdateInactive(Player player, SpawnerData spawner) {
      this.timerUpdateService.forceTimerUpdateInactive(player, spawner);
   }

   public void updateSpawnerMenuViewers(SpawnerData spawner) {
      Set<UUID> viewers = this.viewerTrackingManager.getViewerIds(spawner.getSpawnerId());
      if (viewers != null && !viewers.isEmpty()) {
         if (this.plugin.getSpawnerMenuUI() != null) {
            this.plugin.getSpawnerMenuUI().invalidateSpawnerCache(spawner.getSpawnerId());
         }

         if (this.plugin.getSpawnerMenuFormUI() != null) {
            this.plugin.getSpawnerMenuFormUI().invalidateSpawnerCache(spawner.getSpawnerId());
         }

         int viewerCount = viewers.size();
         if (viewerCount > 10) {
            this.plugin.debug(viewerCount + " spawner menu viewers to update for " + spawner.getSpawnerId() + " (batch update)");
         }

         Iterator var4 = viewers.iterator();

         while(true) {
            while(var4.hasNext()) {
               UUID viewerId = (UUID)var4.next();
               Player viewer = Bukkit.getPlayer(viewerId);
               if (viewer != null && viewer.isOnline()) {
                  this.guiUpdateService.scheduleUpdate(viewerId, 7);
                  Inventory openInv = viewer.getOpenInventory().getTopInventory();
                  InventoryHolder var9 = openInv.getHolder(false);
                  if (var9 instanceof StoragePageHolder) {
                     StoragePageHolder holder = (StoragePageHolder)var9;
                     Location loc = viewer.getLocation();
                     if (holder.getSpawnerData().getSpawnerId().equals(spawner.getSpawnerId())) {
                        Scheduler.runLocationTask(loc, () -> {
                           if (viewer.isOnline()) {
                              Inventory inv = viewer.getOpenInventory().getTopInventory();
                              InventoryHolder patt0$temp = inv.getHolder(false);
                              if (patt0$temp instanceof StoragePageHolder) {
                                 StoragePageHolder spHolder = (StoragePageHolder)patt0$temp;
                                 if (!spHolder.getSpawnerData().getSpawnerId().equals(spawner.getSpawnerId())) {
                                    return;
                                 }

                                 int oldPages = this.storageUpdateService.calculateTotalPages(holder.getOldUsedSlots());
                                 int newPages = this.storageUpdateService.calculateTotalPages(spawner.getVirtualInventory().getUsedSlots());
                                 this.storageUpdateService.processStorageUpdateDirect(viewer, inv, spawner, spHolder, oldPages, newPages);
                              }

                           }
                        });
                     }
                  }
               } else {
                  this.cleanupViewer(viewerId);
               }
            }

            return;
         }
      }
   }

   public void updateSpawnerMenuGui(Player player, SpawnerData spawner, boolean forceUpdate) {
      this.guiUpdateService.scheduleUpdate(player.getUniqueId(), 7);
   }

   public void closeAllViewersInventory(SpawnerData spawner) {
      String spawnerId = spawner.getSpawnerId();
      Set<Player> viewers = this.getViewers(spawnerId);
      if (!viewers.isEmpty()) {
         Iterator var4 = viewers.iterator();

         while(var4.hasNext()) {
            Player viewer = (Player)var4.next();
            if (viewer != null && viewer.isOnline()) {
               viewer.closeInventory();
            }
         }
      }

      Set<UUID> filterViewers = this.viewerTrackingManager.getFilterViewersForSpawner(spawnerId);
      if (filterViewers != null && !filterViewers.isEmpty()) {
         Set<UUID> filterViewersCopy = new HashSet(filterViewers);
         Iterator var6 = filterViewersCopy.iterator();

         while(var6.hasNext()) {
            UUID viewerId = (UUID)var6.next();
            Player viewer = Bukkit.getPlayer(viewerId);
            if (viewer != null && viewer.isOnline()) {
               Inventory openInventory = viewer.getOpenInventory().getTopInventory();
               if (openInventory != null) {
                  InventoryHolder var11 = openInventory.getHolder(false);
                  if (var11 instanceof FilterConfigHolder) {
                     FilterConfigHolder filterHolder = (FilterConfigHolder)var11;
                     if (filterHolder.getSpawnerData().getSpawnerId().equals(spawnerId)) {
                        viewer.closeInventory();
                     }
                  }
               }
            }
         }
      }

      if (this.plugin.getSpawnerStackerHandler() != null) {
         this.plugin.getSpawnerStackerHandler().closeAllViewersInventory(spawnerId);
      }

   }

   public void cleanup() {
      this.updateTaskManager.stopTask();
      this.clearAllTrackedGuis();
      HandlerList.unregisterAll(this.inventoryEventListener);
      HandlerList.unregisterAll(this.playerEventListener);
   }
}
