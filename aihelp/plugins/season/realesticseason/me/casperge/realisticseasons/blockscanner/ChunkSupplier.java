package me.casperge.realisticseasons.blockscanner;

import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.temperature.TemperatureSettings;
import me.casperge.realisticseasons.utils.ChunkUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ChunkSupplier extends BukkitRunnable {
   private RealisticSeasons main;
   private TemperatureSettings tempsettings;
   private HashMap<UUID, ChunkSystem> chunkSystems = new HashMap();
   private HashMap<UUID, SimpleLocation> playerLocations = new HashMap();
   private BlockProcessor processor;
   private HashMap<UUID, Integer> emptyHashmap = new HashMap();

   public ChunkSupplier(RealisticSeasons var1) {
      this.main = var1;
      this.tempsettings = var1.getTemperatureManager().getTempData().getTempSettings();
      this.processor = new BlockProcessor(var1);
   }

   public void run() {
      if (!this.processor.isProcessing()) {
         if (this.main.getNMSUtils().getTPS() < this.main.getSettings().minTPS && this.main.getSettings().prioritiseTPS) {
            this.main.getTemperatureManager().getTempData().updateBlockEffects(this.emptyHashmap);
         } else {
            synchronized(this.chunkSystems) {
               this.chunkSystems.clear();
               this.playerLocations.clear();
               if (!this.main.getTemperatureManager().getTempData().isEnabled() && !this.main.getSettings().fallingLeavesEnabled && !this.main.getSettings().smallFallingLeavesEnabled) {
                  return;
               }

               if (!this.main.getSettings().fallingLeavesEnabled && !this.main.getSettings().smallFallingLeavesEnabled) {
                  if (!this.main.getTemperatureManager().getTempData().isEnabled()) {
                     return;
                  }

                  if (!this.tempsettings.blockEffectsEnabled()) {
                     return;
                  }
               }

               Iterator var2 = Bukkit.getOnlinePlayers().iterator();

               while(true) {
                  if (!var2.hasNext()) {
                     break;
                  }

                  Player var3 = (Player)var2.next();
                  if (ChunkUtils.isChunkLoaded(var3.getLocation()) && (this.main.getTemperatureManager().hasTemperature(var3) || this.main.getSeasonManager().worldData.containsKey(var3.getWorld())) && (this.main.getTemperatureManager().getTempData().getEnabledWorlds().contains(var3.getWorld()) || this.main.getSettings().fallingLeavesEnabled || this.main.getSettings().smallFallingLeavesEnabled)) {
                     int var4 = this.main.getNMSUtils().getMinHeight(var3.getWorld());
                     int var5 = this.main.getNMSUtils().getMaxHeight(var3.getWorld());
                     ChunkSystem var6 = new ChunkSystem(var3.getLocation().getChunk(), 1, var4, var5);
                     this.chunkSystems.put(var3.getUniqueId(), var6);
                     this.playerLocations.put(var3.getUniqueId(), new SimpleLocation(var3.getLocation(), var5, var4));
                  }
               }
            }

            final Long var1 = System.currentTimeMillis();
            Bukkit.getScheduler().runTaskAsynchronously(this.main, new Runnable() {
               public void run() {
                  while(true) {
                     if (System.currentTimeMillis() - var1 <= 2000L) {
                        boolean var1x = true;
                        Iterator var2 = ChunkSupplier.this.chunkSystems.keySet().iterator();

                        while(var2.hasNext()) {
                           UUID var3 = (UUID)var2.next();
                           ChunkSystem var4 = (ChunkSystem)ChunkSupplier.this.chunkSystems.get(var3);
                           if (!var4.hasProcessed.get()) {
                              var1x = false;
                           }
                        }

                        if (var1x) {
                           if (RealisticSeasons.isEnabled.get()) {
                              ChunkSupplier.this.processor.process(ChunkSupplier.this.chunkSystems, ChunkSupplier.this.playerLocations);
                           }
                        } else {
                           try {
                              Thread.sleep(100L);
                              continue;
                           } catch (InterruptedException var5) {
                              var5.printStackTrace();
                           }
                        }
                     }

                     return;
                  }
               }
            });
         }
      }
   }
}
