package github.nighter.smartspawner.spawner.lootgen;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.data.SpawnerManager;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SpawnerRangeChecker {
   private static final long CHECK_INTERVAL = 20L;
   private final SmartSpawner plugin;
   private final SpawnerManager spawnerManager;
   private final ExecutorService executor;

   public SpawnerRangeChecker(SmartSpawner plugin) {
      this.plugin = plugin;
      this.spawnerManager = plugin.getSpawnerManager();
      this.executor = Executors.newSingleThreadExecutor((r) -> {
         return new Thread(r, "SmartSpawner-RangeCheck");
      });
      this.initializeRangeCheckTask();
   }

   private void initializeRangeCheckTask() {
      Scheduler.runTaskTimer(this::scheduleRegionSpecificCheck, 20L, 20L);
   }

   private void scheduleRegionSpecificCheck() {
      PlayerRangeWrapper[] rangePlayers = this.getRangePlayers();
      this.executor.execute(() -> {
         List<SpawnerData> allSpawners = this.spawnerManager.getAllSpawners();
         RangeMath rangeCheck = new RangeMath(rangePlayers, allSpawners);
         boolean[] spawnersPlayerFound = rangeCheck.getActiveSpawners();

         for(int i = 0; i < spawnersPlayerFound.length; ++i) {
            boolean expectedStop = !spawnersPlayerFound[i];
            SpawnerData sd = (SpawnerData)allSpawners.get(i);
            String spawnerId = sd.getSpawnerId();
            if (sd.getSpawnerStop().compareAndSet(!expectedStop, expectedStop)) {
               Scheduler.runLocationTask(sd.getSpawnerLocation(), () -> {
                  if (!this.isSpawnerValid(sd)) {
                     this.cleanupRemovedSpawner(spawnerId);
                  } else {
                     if (sd.getSpawnerStop().get() == expectedStop) {
                        this.handleSpawnerStateChange(sd, expectedStop);
                     }

                  }
               });
            } else if (sd.getSpawnerActive() && !sd.getSpawnerStop().get()) {
               this.checkAndSpawnLoot(sd);
            }
         }

      });
   }

   private PlayerRangeWrapper[] getRangePlayers() {
      Player[] onlinePlayers = (Player[])Bukkit.getOnlinePlayers().toArray(new Player[0]);
      PlayerRangeWrapper[] rangePlayers = new PlayerRangeWrapper[onlinePlayers.length];
      int i = 0;
      Player[] var4 = onlinePlayers;
      int var5 = onlinePlayers.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         Player p = var4[var6];
         boolean conditions = p.isConnected() && !p.isDead() && p.getGameMode() != GameMode.SPECTATOR;
         rangePlayers[i++] = new PlayerRangeWrapper(p.getWorld().getUID(), p.getX(), p.getY(), p.getZ(), conditions);
      }

      return rangePlayers;
   }

   private boolean isSpawnerValid(SpawnerData spawner) {
      SpawnerData current = this.spawnerManager.getSpawnerById(spawner.getSpawnerId());
      if (current == null) {
         return false;
      } else if (current != spawner) {
         return false;
      } else {
         Location loc = spawner.getSpawnerLocation();
         return loc != null && loc.getWorld() != null;
      }
   }

   private void cleanupRemovedSpawner(String spawnerId) {
      SpawnerData spawner = this.spawnerManager.getSpawnerById(spawnerId);
      if (spawner != null) {
         spawner.clearPreGeneratedLoot();
      }

   }

   private void handleSpawnerStateChange(SpawnerData spawner, boolean shouldStop) {
      if (!shouldStop) {
         this.activateSpawner(spawner);
      } else {
         this.deactivateSpawner(spawner);
      }

      if (this.plugin.getSpawnerGuiViewManager().hasViewers(spawner)) {
         this.plugin.getSpawnerGuiViewManager().forceStateChangeUpdate(spawner);
      }

   }

   public void activateSpawner(SpawnerData spawner) {
      this.deactivateSpawner(spawner);
      if (spawner.getSpawnerActive()) {
         long currentTime = System.currentTimeMillis();
         spawner.setLastSpawnTime(currentTime);
         if (this.plugin.getSpawnerGuiViewManager().hasViewers(spawner)) {
            this.plugin.getSpawnerGuiViewManager().updateSpawnerMenuViewers(spawner);
         }

      }
   }

   public void deactivateSpawner(SpawnerData spawner) {
      spawner.clearPreGeneratedLoot();
   }

   private void checkAndSpawnLoot(SpawnerData spawner) {
      long cachedDelay = spawner.getCachedSpawnDelay();
      if (cachedDelay == 0L) {
         cachedDelay = (spawner.getSpawnDelay() + 20L) * 50L;
         spawner.setCachedSpawnDelay(cachedDelay);
      }

      long finalCachedDelay = cachedDelay;
      long currentTime = System.currentTimeMillis();
      long lastSpawnTime = spawner.getLastSpawnTime();
      long timeElapsed = currentTime - lastSpawnTime;
      if (timeElapsed >= cachedDelay) {
         try {
            if (spawner.getDataLock().tryLock(50L, TimeUnit.MILLISECONDS)) {
               try {
                  currentTime = System.currentTimeMillis();
                  lastSpawnTime = spawner.getLastSpawnTime();
                  timeElapsed = currentTime - lastSpawnTime;
                  if (timeElapsed >= cachedDelay && spawner.getSpawnerActive() && !spawner.getSpawnerStop().get()) {
                     Location spawnerLocation = spawner.getSpawnerLocation();
                     if (spawnerLocation != null) {
                        Scheduler.runLocationTask(spawnerLocation, () -> {
                           if (spawner.getSpawnerActive() && !spawner.getSpawnerStop().get()) {
                              long timeSinceLastSpawn = System.currentTimeMillis() - spawner.getLastSpawnTime();
                              if (timeSinceLastSpawn < finalCachedDelay - 100L) {
                                 if (this.plugin.getSpawnerGuiViewManager().hasViewers(spawner)) {
                                    this.plugin.getSpawnerGuiViewManager().updateSpawnerMenuViewers(spawner);
                                 }

                              } else {
                                 if (spawner.hasPreGeneratedLoot()) {
                                    List<ItemStack> items = spawner.getAndClearPreGeneratedItems();
                                    int exp = spawner.getAndClearPreGeneratedExperience();
                                    this.plugin.getSpawnerLootGenerator().addPreGeneratedLoot(spawner, items, exp);
                                 } else {
                                    this.plugin.getSpawnerLootGenerator().spawnLootToSpawner(spawner);
                                 }

                                 if (this.plugin.getSpawnerGuiViewManager().hasViewers(spawner)) {
                                    this.plugin.getSpawnerGuiViewManager().updateSpawnerMenuViewers(spawner);
                                 }

                              }
                           } else {
                              spawner.clearPreGeneratedLoot();
                           }
                        });
                     }
                  }
               } finally {
                  spawner.getDataLock().unlock();
               }
            }
         } catch (InterruptedException var17) {
            Thread.currentThread().interrupt();
         }
      }

   }

   public void cleanup() {
      this.executor.shutdown();

      try {
         if (!this.executor.awaitTermination(5L, TimeUnit.SECONDS)) {
            this.executor.shutdownNow();
         }
      } catch (InterruptedException var2) {
         this.executor.shutdownNow();
         Thread.currentThread().interrupt();
      }

   }
}
