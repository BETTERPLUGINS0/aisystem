package github.nighter.smartspawner.spawner.data;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldEventHandler implements Listener {
   private final SmartSpawner plugin;
   private final Logger logger;
   private final Set<String> processedWorlds = ConcurrentHashMap.newKeySet();
   private final Map<String, WorldEventHandler.PendingSpawnerData> pendingSpawners = new ConcurrentHashMap();
   private volatile boolean initialLoadAttempted = false;

   public WorldEventHandler(SmartSpawner plugin) {
      this.plugin = plugin;
      this.logger = plugin.getLogger();
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onWorldInit(WorldInitEvent event) {
      World world = event.getWorld();
      this.plugin.debug("World initialized: " + world.getName());
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onWorldLoad(WorldLoadEvent event) {
      World world = event.getWorld();
      String worldName = world.getName();
      this.plugin.debug("World loaded: " + worldName);
      this.processedWorlds.add(worldName);
      this.loadPendingSpawnersForWorld(worldName);
      if (!this.initialLoadAttempted) {
         Scheduler.runTaskLater(() -> {
            this.attemptInitialSpawnerLoad();
         }, 20L);
      }

   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onWorldSave(WorldSaveEvent event) {
      World world = event.getWorld();
      this.plugin.debug("World saving: " + world.getName());
      this.plugin.getSpawnerStorage().flushChanges();
   }

   @EventHandler(
      priority = EventPriority.MONITOR
   )
   public void onWorldUnload(WorldUnloadEvent event) {
      World world = event.getWorld();
      String worldName = world.getName();
      this.plugin.debug("World unloading: " + worldName);
      this.processedWorlds.remove(worldName);
      this.unloadSpawnersFromWorld(worldName);
      this.plugin.getSpawnerStorage().flushChanges();
   }

   public void attemptInitialSpawnerLoad() {
      if (!this.initialLoadAttempted) {
         this.initialLoadAttempted = true;
         this.plugin.debug("Attempting initial spawner load...");
         Map<String, SpawnerData> allSpawnerData = this.plugin.getSpawnerStorage().loadAllSpawnersRaw();
         int loadedCount = 0;
         int pendingCount = 0;
         Iterator var4 = allSpawnerData.entrySet().iterator();

         while(var4.hasNext()) {
            Entry<String, SpawnerData> entry = (Entry)var4.next();
            String spawnerId = (String)entry.getKey();
            SpawnerData spawner = (SpawnerData)entry.getValue();
            if (spawner != null) {
               this.plugin.getSpawnerManager().addSpawnerToIndexes(spawnerId, spawner);
               ++loadedCount;
            } else {
               WorldEventHandler.PendingSpawnerData pending = this.loadPendingSpawnerFromFile(spawnerId);
               if (pending != null) {
                  this.pendingSpawners.put(spawnerId, pending);
                  ++pendingCount;
               }
            }
         }

         this.logger.info("Initial spawner load complete. Loaded: " + loadedCount + ", Pending (missing worlds): " + pendingCount);
         if (pendingCount > 0) {
            this.logger.info("Pending spawners will be loaded when their worlds become available.");
         }

      }
   }

   private void loadPendingSpawnersForWorld(String worldName) {
      if (!this.pendingSpawners.isEmpty()) {
         int loadedCount = 0;
         Set<String> spawnerIds = new HashSet(this.pendingSpawners.keySet());
         Iterator var4 = spawnerIds.iterator();

         while(var4.hasNext()) {
            String spawnerId = (String)var4.next();
            WorldEventHandler.PendingSpawnerData pending = (WorldEventHandler.PendingSpawnerData)this.pendingSpawners.get(spawnerId);
            if (pending != null && worldName.equals(pending.worldName)) {
               SpawnerData spawner = this.plugin.getSpawnerStorage().loadSpecificSpawner(spawnerId);
               if (spawner != null) {
                  this.plugin.getSpawnerManager().addSpawnerToIndexes(spawnerId, spawner);
                  this.pendingSpawners.remove(spawnerId);
                  ++loadedCount;
                  this.plugin.debug("Loaded pending spawner " + spawnerId + " for world " + worldName);
               }
            }
         }

         if (loadedCount > 0) {
            this.logger.info("Loaded " + loadedCount + " pending spawners for world: " + worldName);
         }

      }
   }

   private void unloadSpawnersFromWorld(String worldName) {
      Set<SpawnerData> worldSpawners = this.plugin.getSpawnerManager().getSpawnersInWorld(worldName);
      if (worldSpawners != null && !worldSpawners.isEmpty()) {
         int unloadedCount = 0;

         for(Iterator var4 = (new HashSet(worldSpawners)).iterator(); var4.hasNext(); ++unloadedCount) {
            SpawnerData spawner = (SpawnerData)var4.next();
            spawner.removeHologram();
         }

         this.logger.info("Unloaded " + unloadedCount + " spawners from world: " + worldName);
      }

   }

   private WorldEventHandler.PendingSpawnerData loadPendingSpawnerFromFile(String spawnerId) {
      try {
         String locationString = this.plugin.getSpawnerStorage().getRawLocationString(spawnerId);
         if (locationString != null) {
            String[] locParts = locationString.split(",");
            if (locParts.length >= 1) {
               return new WorldEventHandler.PendingSpawnerData(spawnerId, locParts[0]);
            }
         }
      } catch (Exception var4) {
         this.plugin.debug("Error loading pending spawner data for " + spawnerId + ": " + var4.getMessage());
      }

      return null;
   }

   public boolean isWorldLoaded(String worldName) {
      return this.processedWorlds.contains(worldName) && Bukkit.getWorld(worldName) != null;
   }

   public int getPendingSpawnerCount() {
      return this.pendingSpawners.size();
   }

   private static class PendingSpawnerData {
      final String spawnerId;
      final String worldName;

      PendingSpawnerData(String spawnerId, String worldName) {
         this.spawnerId = spawnerId;
         this.worldName = worldName;
      }
   }
}
