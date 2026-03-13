package github.nighter.smartspawner.spawner.gui.synchronization.utils;

import github.nighter.smartspawner.Scheduler;
import github.nighter.smartspawner.SmartSpawner;
import github.nighter.smartspawner.spawner.properties.SpawnerData;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public final class LootPreGenerationHelper {
   private static final long PRE_GENERATION_THRESHOLD = 2000L;
   private static final long EARLY_SPAWN_THRESHOLD = 1000L;
   private final SmartSpawner plugin;

   public LootPreGenerationHelper(SmartSpawner plugin) {
      this.plugin = plugin;
   }

   public boolean shouldPreGenerateLoot(long timeUntilNextSpawn) {
      return timeUntilNextSpawn > 0L && timeUntilNextSpawn <= 2000L;
   }

   public boolean shouldAddLootEarly(long timeUntilNextSpawn) {
      return timeUntilNextSpawn > 0L && timeUntilNextSpawn <= 1000L;
   }

   public void preGenerateLoot(SpawnerData spawner) {
      if (!spawner.isPreGenerating() && !spawner.hasPreGeneratedLoot()) {
         if (spawner.getSpawnerActive() && !spawner.getSpawnerStop().get()) {
            spawner.setPreGenerating(true);
            Location spawnerLocation = spawner.getSpawnerLocation();
            if (spawnerLocation != null) {
               Scheduler.runLocationTask(spawnerLocation, () -> {
                  if (spawner.getSpawnerActive() && !spawner.getSpawnerStop().get()) {
                     this.plugin.getSpawnerLootGenerator().preGenerateLoot(spawner, (items, experience) -> {
                        spawner.storePreGeneratedLoot(items, experience);
                        spawner.setPreGenerating(false);
                     });
                  } else {
                     spawner.setPreGenerating(false);
                  }
               });
            }

         }
      }
   }

   public void addPreGeneratedLootEarly(SpawnerData spawner, long cachedDelay) {
      if (spawner.hasPreGeneratedLoot()) {
         try {
            if (spawner.getDataLock().tryLock(100L, TimeUnit.MILLISECONDS)) {
               try {
                  long currentTime = System.currentTimeMillis();
                  long lastSpawnTime = spawner.getLastSpawnTime();
                  long timeElapsed = currentTime - lastSpawnTime;
                  long remainingTime = cachedDelay - timeElapsed;
                  if (remainingTime <= 0L || remainingTime > 1000L) {
                     return;
                  }

                  if (spawner.getSpawnerActive() && !spawner.getSpawnerStop().get()) {
                     Location spawnerLocation = spawner.getSpawnerLocation();
                     if (spawnerLocation != null) {
                        long scheduledSpawnTime = lastSpawnTime + cachedDelay;
                        Scheduler.runLocationTask(spawnerLocation, () -> {
                           if (spawner.getSpawnerActive() && !spawner.getSpawnerStop().get()) {
                              if (spawner.hasPreGeneratedLoot()) {
                                 List<ItemStack> items = spawner.getAndClearPreGeneratedItems();
                                 int exp = spawner.getAndClearPreGeneratedExperience();
                                 this.plugin.getSpawnerLootGenerator().addPreGeneratedLoot(spawner, items, exp, scheduledSpawnTime);
                              }

                           } else {
                              spawner.clearPreGeneratedLoot();
                           }
                        });
                     }

                     return;
                  }

                  spawner.clearPreGeneratedLoot();
               } finally {
                  spawner.getDataLock().unlock();
               }

               return;
            }
         } catch (InterruptedException var19) {
            Thread.currentThread().interrupt();
         }

      }
   }
}
