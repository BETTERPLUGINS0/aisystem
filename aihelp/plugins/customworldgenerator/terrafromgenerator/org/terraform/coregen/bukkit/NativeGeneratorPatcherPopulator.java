package org.terraform.coregen.bukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.terraform.data.SimpleChunkLocation;
import org.terraform.main.TLogger;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;

public class NativeGeneratorPatcherPopulator extends BlockPopulator implements Listener {
   @NotNull
   private static final Map<SimpleChunkLocation, Collection<Object[]>> cache = new ConcurrentHashMap();
   private static boolean flushIsQueued = false;

   public NativeGeneratorPatcherPopulator() {
      Bukkit.getPluginManager().registerEvents(this, TerraformGeneratorPlugin.get());
   }

   public static void pushChange(String world, int x, int y, int z, BlockData data) {
      if (!flushIsQueued && cache.size() > TConfig.c.DEVSTUFF_FLUSH_PATCHER_CACHE_FREQUENCY) {
         flushIsQueued = true;
         (new BukkitRunnable() {
            public void run() {
               NativeGeneratorPatcherPopulator.flushChanges();
               NativeGeneratorPatcherPopulator.flushIsQueued = false;
            }
         }).runTask(TerraformGeneratorPlugin.get());
      }

      SimpleChunkLocation scl = new SimpleChunkLocation(world, x, y, z);
      Collection<Object[]> cached = (Collection)cache.getOrDefault(scl, new ArrayList());
      cached.add(new Object[]{new int[]{x, y, z}, data});
      cache.put(scl, cached);
   }

   public static void flushChanges() {
      if (!cache.isEmpty()) {
         TerraformGeneratorPlugin.logger.info("[NativeGeneratorPatcher] Flushing repairs (" + cache.size() + " chunks), pushed by cache size");
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
               Object[] entry = (Object[])var5.next();
               int[] loc = (int[])entry[0];
               BlockData data = (BlockData)entry[1];
               w.getBlockAt(loc[0], loc[1], loc[2]).setBlockData(data, false);
            }
         }
      }
   }

   public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk chunk) {
      SimpleChunkLocation scl = new SimpleChunkLocation(chunk);
      Collection<Object[]> changes = (Collection)cache.remove(scl);
      if (changes != null) {
         Iterator var6 = changes.iterator();

         while(var6.hasNext()) {
            Object[] entry = (Object[])var6.next();
            int[] loc = (int[])entry[0];
            BlockData data = (BlockData)entry[1];
            world.getBlockAt(loc[0], loc[1], loc[2]).setBlockData(data, false);
         }
      }

   }

   @EventHandler
   public void onChunkLoad(@NotNull ChunkLoadEvent event) {
      SimpleChunkLocation scl = new SimpleChunkLocation(event.getChunk());
      Collection<Object[]> changes = (Collection)cache.remove(scl);
      if (changes != null) {
         Iterator var4 = changes.iterator();

         while(var4.hasNext()) {
            Object[] entry = (Object[])var4.next();
            int[] loc = (int[])entry[0];
            BlockData data = (BlockData)entry[1];
            event.getChunk().getWorld().getBlockAt(loc[0], loc[1], loc[2]).setBlockData(data, false);
         }
      }

   }

   @EventHandler
   public void onWorldUnload(@NotNull WorldUnloadEvent event) {
      TLogger var10000 = TerraformGeneratorPlugin.logger;
      String var10001 = event.getWorld().getName();
      var10000.info("[NativeGeneratorPatcher] Flushing repairs for " + var10001 + " (" + cache.size() + " chunks in cache), triggered by world unload");
      int processed = 0;
      Iterator var3 = cache.keySet().iterator();

      while(true) {
         SimpleChunkLocation scl;
         do {
            if (!var3.hasNext()) {
               return;
            }

            scl = (SimpleChunkLocation)var3.next();
         } while(!scl.getWorld().equals(event.getWorld().getName()));

         Collection<Object[]> changes = (Collection)cache.get(scl);
         if (changes != null) {
            Iterator var6 = changes.iterator();

            while(var6.hasNext()) {
               Object[] entry = (Object[])var6.next();
               int[] loc = (int[])entry[0];
               BlockData data = (BlockData)entry[1];
               event.getWorld().getBlockAt(loc[0], loc[1], loc[2]).setBlockData(data, false);
            }
         }

         ++processed;
         if (processed % 20 == 0) {
            TerraformGeneratorPlugin.logger.info("[NativeGeneratorPatcher] Processed " + processed + "/" + cache.size() + " chunks");
         }
      }
   }
}
