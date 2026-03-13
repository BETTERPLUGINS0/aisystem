package github.nighter.smartspawner.spawner.gui.synchronization.managers;

import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ViewerTrackingManager {
   private final Map<UUID, ViewerTrackingManager.ViewerInfo> playerToSpawnerMap = new ConcurrentHashMap();
   private final Map<String, Set<UUID>> spawnerToPlayersMap = new ConcurrentHashMap();
   private final Map<UUID, ViewerTrackingManager.ViewerInfo> mainMenuViewers = new ConcurrentHashMap();
   private final Map<String, Set<UUID>> spawnerToMainMenuViewers = new ConcurrentHashMap();
   private final Map<String, Set<UUID>> spawnerToFilterViewersMap = new ConcurrentHashMap();

   public void trackViewer(UUID playerId, SpawnerData spawner, ViewerTrackingManager.ViewerType viewerType) {
      ViewerTrackingManager.ViewerInfo info = new ViewerTrackingManager.ViewerInfo(spawner, viewerType);
      this.playerToSpawnerMap.put(playerId, info);
      ((Set)this.spawnerToPlayersMap.computeIfAbsent(spawner.getSpawnerId(), (k) -> {
         return ConcurrentHashMap.newKeySet();
      })).add(playerId);
      if (viewerType == ViewerTrackingManager.ViewerType.MAIN_MENU) {
         this.mainMenuViewers.put(playerId, info);
         ((Set)this.spawnerToMainMenuViewers.computeIfAbsent(spawner.getSpawnerId(), (k) -> {
            return ConcurrentHashMap.newKeySet();
         })).add(playerId);
      }

      if (viewerType == ViewerTrackingManager.ViewerType.FILTER) {
         ((Set)this.spawnerToFilterViewersMap.computeIfAbsent(spawner.getSpawnerId(), (k) -> {
            return ConcurrentHashMap.newKeySet();
         })).add(playerId);
      }

   }

   public void untrackViewer(UUID playerId) {
      ViewerTrackingManager.ViewerInfo info = (ViewerTrackingManager.ViewerInfo)this.playerToSpawnerMap.remove(playerId);
      Set mainMenuViewerSet;
      if (info != null) {
         SpawnerData spawner = info.spawnerData;
         String spawnerId = spawner.getSpawnerId();
         mainMenuViewerSet = (Set)this.spawnerToPlayersMap.get(spawnerId);
         if (mainMenuViewerSet != null) {
            mainMenuViewerSet.remove(playerId);
            if (mainMenuViewerSet.isEmpty()) {
               this.spawnerToPlayersMap.remove(spawnerId);
            }
         }

         Set<UUID> filterViewers = (Set)this.spawnerToFilterViewersMap.get(spawnerId);
         if (filterViewers != null) {
            filterViewers.remove(playerId);
            if (filterViewers.isEmpty()) {
               this.spawnerToFilterViewersMap.remove(spawnerId);
            }
         }
      }

      ViewerTrackingManager.ViewerInfo mainMenuInfo = (ViewerTrackingManager.ViewerInfo)this.mainMenuViewers.remove(playerId);
      if (mainMenuInfo != null) {
         SpawnerData spawner = mainMenuInfo.spawnerData;
         mainMenuViewerSet = (Set)this.spawnerToMainMenuViewers.get(spawner.getSpawnerId());
         if (mainMenuViewerSet != null) {
            mainMenuViewerSet.remove(playerId);
            if (mainMenuViewerSet.isEmpty()) {
               this.spawnerToMainMenuViewers.remove(spawner.getSpawnerId());
            }
         }
      }

   }

   public Set<Player> getViewers(String spawnerId) {
      Set<UUID> viewerIds = (Set)this.spawnerToPlayersMap.get(spawnerId);
      if (viewerIds != null && !viewerIds.isEmpty()) {
         Set<Player> onlineViewers = new HashSet(viewerIds.size());
         Iterator var4 = viewerIds.iterator();

         while(var4.hasNext()) {
            UUID id = (UUID)var4.next();
            Player player = Bukkit.getPlayer(id);
            if (player != null && player.isOnline()) {
               onlineViewers.add(player);
            }
         }

         return onlineViewers;
      } else {
         return Collections.emptySet();
      }
   }

   public boolean hasViewers(SpawnerData spawner) {
      Set<UUID> viewers = (Set)this.spawnerToPlayersMap.get(spawner.getSpawnerId());
      return viewers != null && !viewers.isEmpty();
   }

   public ViewerTrackingManager.ViewerInfo getViewerInfo(UUID playerId) {
      return (ViewerTrackingManager.ViewerInfo)this.playerToSpawnerMap.get(playerId);
   }

   public Set<UUID> getMainMenuViewersForSpawner(String spawnerId) {
      return (Set)this.spawnerToMainMenuViewers.get(spawnerId);
   }

   public Map<UUID, ViewerTrackingManager.ViewerInfo> getAllViewers() {
      return this.playerToSpawnerMap;
   }

   public Set<UUID> getViewerIds(String spawnerId) {
      return (Set)this.spawnerToPlayersMap.get(spawnerId);
   }

   public Set<UUID> getFilterViewersForSpawner(String spawnerId) {
      return (Set)this.spawnerToFilterViewersMap.get(spawnerId);
   }

   public boolean hasAnyViewers() {
      return !this.playerToSpawnerMap.isEmpty();
   }

   public boolean hasMainMenuViewers() {
      return !this.mainMenuViewers.isEmpty();
   }

   public void clearAll() {
      this.playerToSpawnerMap.clear();
      this.spawnerToPlayersMap.clear();
      this.mainMenuViewers.clear();
      this.spawnerToMainMenuViewers.clear();
      this.spawnerToFilterViewersMap.clear();
   }

   @Generated
   public Map<UUID, ViewerTrackingManager.ViewerInfo> getMainMenuViewers() {
      return this.mainMenuViewers;
   }

   public static class ViewerInfo {
      private final SpawnerData spawnerData;
      private final long lastUpdateTime;
      private final ViewerTrackingManager.ViewerType viewerType;

      public ViewerInfo(SpawnerData spawnerData, ViewerTrackingManager.ViewerType viewerType) {
         this.spawnerData = spawnerData;
         this.lastUpdateTime = System.currentTimeMillis();
         this.viewerType = viewerType;
      }

      public SpawnerData getSpawnerData() {
         return this.spawnerData;
      }

      public long getLastUpdateTime() {
         return this.lastUpdateTime;
      }

      public ViewerTrackingManager.ViewerType getViewerType() {
         return this.viewerType;
      }
   }

   public static enum ViewerType {
      MAIN_MENU,
      STORAGE,
      FILTER;

      // $FF: synthetic method
      private static ViewerTrackingManager.ViewerType[] $values() {
         return new ViewerTrackingManager.ViewerType[]{MAIN_MENU, STORAGE, FILTER};
      }
   }
}
