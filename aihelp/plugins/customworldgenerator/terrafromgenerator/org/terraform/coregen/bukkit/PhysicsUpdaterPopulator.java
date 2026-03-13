package org.terraform.coregen.bukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleChunkLocation;
import org.terraform.data.SimpleLocation;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;

public class PhysicsUpdaterPopulator extends BlockPopulator implements Listener {
   @NotNull
   public static final Map<SimpleChunkLocation, Collection<SimpleLocation>> cache = new ConcurrentHashMap();
   private static boolean flushIsQueued = false;

   public PhysicsUpdaterPopulator() {
      Bukkit.getPluginManager().registerEvents(this, TerraformGeneratorPlugin.get());
   }

   public static void pushChange(String world, @NotNull SimpleLocation loc) {
      if (!flushIsQueued && cache.size() > TConfig.c.DEVSTUFF_FLUSH_PATCHER_CACHE_FREQUENCY) {
         flushIsQueued = true;
         (new BukkitRunnable() {
            public void run() {
               PhysicsUpdaterPopulator.flushChanges();
               PhysicsUpdaterPopulator.flushIsQueued = false;
            }
         }).runTask(TerraformGeneratorPlugin.get());
      }

      SimpleChunkLocation scl = new SimpleChunkLocation(world, loc.getX(), loc.getY(), loc.getZ());
      if (!cache.containsKey(scl)) {
         cache.put(scl, new ArrayList());
      }

      ((Collection)cache.get(scl)).add(loc);
   }

   public static void flushChanges() {
      if (!cache.isEmpty()) {
         TerraformGeneratorPlugin.logger.info("[PhysicsUpdaterPopulator] Flushing repairs (" + cache.size() + " chunks)");
         ArrayList<SimpleChunkLocation> locs = new ArrayList(cache.keySet());
         Iterator var1 = locs.iterator();

         while(true) {
            World w;
            Collection changes;
            do {
               while(true) {
                  SimpleChunkLocation scl;
                  do {
                     if (!var1.hasNext()) {
                        return;
                     }

                     scl = (SimpleChunkLocation)var1.next();
                     w = Bukkit.getWorld(scl.getWorld());
                  } while(w == null);

                  if (w.isChunkLoaded(scl.getX(), scl.getZ())) {
                     changes = (Collection)cache.remove(scl);
                     break;
                  }

                  w.loadChunk(scl.getX(), scl.getZ());
               }
            } while(changes == null);

            Iterator var5 = changes.iterator();

            while(var5.hasNext()) {
               SimpleLocation entry = (SimpleLocation)var5.next();
               Block target = w.getBlockAt(entry.getX(), entry.getY(), entry.getZ());
               BlockData old = target.getBlockData();
               TerraformGeneratorPlugin.logger.info("[PhysicsUpdaterPopulator] " + String.valueOf(target.getLocation()));
               target.setType(Material.AIR);
               target.setBlockData(old, true);
            }
         }
      }
   }

   public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
      SimpleChunkLocation scl = new SimpleChunkLocation(chunk);
      Collection<SimpleLocation> changes = (Collection)cache.remove(scl);
      if (changes != null) {
         Iterator var6 = changes.iterator();

         while(var6.hasNext()) {
            SimpleLocation entry = (SimpleLocation)var6.next();
            Block target = world.getBlockAt(entry.getX(), entry.getY(), entry.getZ());
            BlockData old = target.getBlockData();
            target.setType(Material.AIR);
            target.setBlockData(old, true);
         }
      }

   }

   @EventHandler
   public void onWorldUnload(@NotNull WorldUnloadEvent event) {
      TLogger var10000 = TerraformGeneratorPlugin.logger;
      String var10001 = event.getWorld().getName();
      var10000.info("[PhysicsUpdaterPopulator] Flushing repairs for " + var10001 + " (" + cache.size() + " chunks in cache)");
      int processed = 0;
      Iterator var3 = Set.copyOf(cache.keySet()).iterator();

      while(true) {
         SimpleChunkLocation scl;
         do {
            if (!var3.hasNext()) {
               return;
            }

            scl = (SimpleChunkLocation)var3.next();
         } while(!scl.getWorld().equals(event.getWorld().getName()));

         Collection<SimpleLocation> changes = (Collection)cache.remove(scl);
         if (changes != null) {
            Iterator var6 = changes.iterator();

            while(var6.hasNext()) {
               SimpleLocation entry = (SimpleLocation)var6.next();
               Block target = event.getWorld().getBlockAt(entry.getX(), entry.getY(), entry.getZ());
               BlockData old = target.getBlockData();
               target.setType(Material.AIR);
               target.setBlockData(old, true);
            }
         }

         ++processed;
         if (processed % 20 == 0) {
            TerraformGeneratorPlugin.logger.info("[PhysicsUpdaterPopulator] Processed " + processed + " more chunks");
         }
      }
   }
}
