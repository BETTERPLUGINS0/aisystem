package github.nighter.smartspawner.spawner.data;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.data.storage.SpawnerStorage;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.bukkit.Location;
import org.bukkit.Material;

public class SpawnerManager {
   private final SmartSpawner plugin;
   private final Map<String, SpawnerData> spawners = new ConcurrentHashMap();
   private final Map<SpawnerManager.LocationKey, SpawnerData> locationIndex = new HashMap();
   private final Map<String, Set<SpawnerData>> worldIndex = new HashMap();
   private final SpawnerStorage spawnerStorage;
   private final Set<String> confirmedGhostSpawners = ConcurrentHashMap.newKeySet();

   public SpawnerManager(SmartSpawner plugin) {
      this.plugin = plugin;
      this.spawnerStorage = plugin.getSpawnerStorage();
      this.initializeWithoutLoading();
   }

   public void reloadSpawnerDrops() {
      List<SpawnerData> allSpawners = this.getAllSpawners();
      Iterator var2 = allSpawners.iterator();

      while(var2.hasNext()) {
         SpawnerData spawner = (SpawnerData)var2.next();

         try {
            spawner.setLootConfig();
         } catch (Exception var5) {
            Logger var10000 = this.plugin.getLogger();
            String var10001 = spawner.getSpawnerId();
            var10000.warning("Failed to reload drops config for spawner " + var10001 + ": " + var5.getMessage());
         }
      }

   }

   public void reloadSpawnerDropsAndConfigs() {
      List<SpawnerData> allSpawners = this.getAllSpawners();
      Iterator var2 = allSpawners.iterator();

      while(var2.hasNext()) {
         SpawnerData spawner = (SpawnerData)var2.next();

         try {
            spawner.loadConfigurationValues();
            spawner.recalculateAfterConfigReload();
         } catch (Exception var5) {
            Logger var10000 = this.plugin.getLogger();
            String var10001 = spawner.getSpawnerId();
            var10000.warning("Failed to reload config for spawner " + var10001 + ": " + var5.getMessage());
         }
      }

   }

   public void addSpawner(String id, SpawnerData spawner) {
      this.spawners.put(id, spawner);
      this.locationIndex.put(new SpawnerManager.LocationKey(spawner.getSpawnerLocation()), spawner);
      String worldName = spawner.getSpawnerLocation().getWorld().getName();
      ((Set)this.worldIndex.computeIfAbsent(worldName, (k) -> {
         return new HashSet();
      })).add(spawner);
      this.spawnerStorage.queueSpawnerForSaving(id);
   }

   public void removeSpawner(String id) {
      SpawnerData spawner = (SpawnerData)this.spawners.get(id);
      if (spawner != null) {
         Location loc = spawner.getSpawnerLocation();
         Objects.requireNonNull(spawner);
         Scheduler.runLocationTask(loc, spawner::removeHologram);
         this.locationIndex.remove(new SpawnerManager.LocationKey(spawner.getSpawnerLocation()));
         String worldName = spawner.getSpawnerLocation().getWorld().getName();
         Set<SpawnerData> worldSpawners = (Set)this.worldIndex.get(worldName);
         if (worldSpawners != null) {
            worldSpawners.remove(spawner);
            if (worldSpawners.isEmpty()) {
               this.worldIndex.remove(worldName);
            }
         }

         this.spawners.remove(id);
      }

   }

   public int countSpawnersInWorld(String worldName) {
      Set<SpawnerData> worldSpawners = (Set)this.worldIndex.get(worldName);
      return worldSpawners != null ? worldSpawners.size() : 0;
   }

   public int countTotalSpawnersWithStacks(String worldName) {
      Set<SpawnerData> worldSpawners = (Set)this.worldIndex.get(worldName);
      return worldSpawners == null ? 0 : worldSpawners.stream().mapToInt(SpawnerData::getStackSize).sum();
   }

   public SpawnerData getSpawnerByLocation(Location location) {
      return (SpawnerData)this.locationIndex.get(new SpawnerManager.LocationKey(location));
   }

   public SpawnerData getSpawnerById(String id) {
      return (SpawnerData)this.spawners.get(id);
   }

   public List<SpawnerData> getAllSpawners() {
      return new ArrayList(this.spawners.values());
   }

   public void addSpawnerToIndexes(String spawnerId, SpawnerData spawner) {
      this.spawners.put(spawnerId, spawner);
      this.locationIndex.put(new SpawnerManager.LocationKey(spawner.getSpawnerLocation()), spawner);
      String worldName = spawner.getSpawnerLocation().getWorld().getName();
      ((Set)this.worldIndex.computeIfAbsent(worldName, (k) -> {
         return new HashSet();
      })).add(spawner);
   }

   public Set<SpawnerData> getSpawnersInWorld(String worldName) {
      return (Set)this.worldIndex.get(worldName);
   }

   public void initializeWithoutLoading() {
      this.spawners.clear();
      this.locationIndex.clear();
      this.worldIndex.clear();
      this.confirmedGhostSpawners.clear();
   }

   public boolean isGhostSpawner(SpawnerData spawner) {
      if (spawner == null) {
         return false;
      } else if (this.confirmedGhostSpawners.contains(spawner.getSpawnerId())) {
         return true;
      } else {
         Location loc = spawner.getSpawnerLocation();
         if (loc != null && loc.getWorld() != null) {
            int chunkX = loc.getBlockX() >> 4;
            int chunkZ = loc.getBlockZ() >> 4;
            if (!loc.getWorld().isChunkLoaded(chunkX, chunkZ)) {
               return false;
            } else {
               return loc.getBlock().getType() != Material.SPAWNER;
            }
         } else {
            return true;
         }
      }
   }

   public void removeGhostSpawner(String spawnerId) {
      SpawnerData spawner = (SpawnerData)this.spawners.get(spawnerId);
      if (spawner != null) {
         Location loc = spawner.getSpawnerLocation();
         this.confirmedGhostSpawners.add(spawnerId);
         Scheduler.runLocationTask(loc, () -> {
            spawner.removeHologram();
            Scheduler.runTask(() -> {
               this.removeSpawner(spawnerId);
               this.spawnerStorage.markSpawnerDeleted(spawnerId);
               this.plugin.debug("Removed ghost spawner " + spawnerId);
            });
         });
      }

   }

   public void markSpawnerModified(String spawnerId) {
      this.spawnerStorage.markSpawnerModified(spawnerId);
   }

   public void markSpawnerDeleted(String spawnerId) {
      this.spawnerStorage.markSpawnerDeleted(spawnerId);
   }

   public void queueSpawnerForSaving(String spawnerId) {
      this.spawnerStorage.queueSpawnerForSaving(spawnerId);
   }

   public void refreshAllHolograms() {
      Iterator var1 = this.spawners.values().iterator();

      while(var1.hasNext()) {
         SpawnerData spawner = (SpawnerData)var1.next();
         Location loc = spawner.getSpawnerLocation();
         Objects.requireNonNull(spawner);
         Scheduler.runLocationTask(loc, spawner::refreshHologram);
      }

   }

   public void reloadAllHolograms() {
      if (this.plugin.getConfig().getBoolean("hologram.enabled", false)) {
         Iterator var1 = this.spawners.values().iterator();

         while(var1.hasNext()) {
            SpawnerData spawner = (SpawnerData)var1.next();
            Location loc = spawner.getSpawnerLocation();
            Objects.requireNonNull(spawner);
            Scheduler.runLocationTask(loc, spawner::reloadHologramData);
         }
      }

   }

   public void cleanupAllSpawners() {
      this.spawners.clear();
      this.locationIndex.clear();
      this.worldIndex.clear();
      this.confirmedGhostSpawners.clear();
   }

   public int getTotalSpawners() {
      return this.spawners.size();
   }

   private static class LocationKey {
      private final String world;
      private final int x;
      private final int y;
      private final int z;

      public LocationKey(Location location) {
         this.world = location.getWorld().getName();
         this.x = location.getBlockX();
         this.y = location.getBlockY();
         this.z = location.getBlockZ();
      }

      public boolean equals(Object o) {
         if (this == o) {
            return true;
         } else if (!(o instanceof SpawnerManager.LocationKey)) {
            return false;
         } else {
            SpawnerManager.LocationKey that = (SpawnerManager.LocationKey)o;
            return this.x == that.x && this.y == that.y && this.z == that.z && this.world.equals(that.world);
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.world, this.x, this.y, this.z});
      }
   }
}
