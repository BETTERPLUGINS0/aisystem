package me.casperge.realisticseasons.blockscanner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.paperlib.PaperLib;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.block.data.type.Campfire;
import org.bukkit.block.data.type.Furnace;

public class ChunkSystem {
   private static final int[] UNEVEN = new int[]{1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23, 25, 27, 29, 31, 33, 35, 37, 39, 41};
   private ChunkSnapshot[] sections;
   private String worldName;
   private int middleX;
   private int middleZ;
   private int range;
   public AtomicBoolean hasProcessed = new AtomicBoolean(false);
   private AtomicInteger count = new AtomicInteger(0);
   private AtomicInteger required = new AtomicInteger(0);
   private AtomicBoolean completedScan = new AtomicBoolean(false);
   private int minY;
   private int maxY;
   private boolean isNether;

   public ChunkSystem(Chunk var1, int var2, int var3, int var4) {
      int var5 = UNEVEN[var2] * UNEVEN[var2];
      this.minY = var3;
      this.maxY = var4;
      this.range = var2;
      this.sections = new ChunkSnapshot[var5];
      this.worldName = var1.getWorld().getName();
      this.middleX = var1.getX();
      this.middleZ = var1.getZ();
      World var6 = var1.getWorld();
      this.isNether = var6.getEnvironment() == Environment.NETHER;
      int var7 = 0;

      for(int var8 = 0; var8 < UNEVEN[var2]; ++var8) {
         for(int var9 = 0; var9 < UNEVEN[var2]; ++var9) {
            int var10 = this.middleX + var8 - var2;
            int var11 = this.middleZ + var9 - var2;
            if (var10 < 1875000 && var11 < 1875000 && var10 > -1875000 && var11 > -1875000) {
               if (var6.isChunkLoaded(var10, var11)) {
                  CompletableFuture var12 = PaperLib.getChunkAtAsync(var6, this.middleX + var8 - var2, this.middleZ + var9 - var2);
                  this.required.getAndIncrement();
                  long var14 = System.currentTimeMillis();
                  var12.thenAccept((var4x) -> {
                     if (System.currentTimeMillis() - var14 < 2000L) {
                        Runnable var5 = new Runnable() {
                           public void run() {
                              synchronized(ChunkSystem.this.sections) {
                                 ChunkSystem.this.sections[var3] = var4.getChunkSnapshot(false, false, false);
                              }

                              ChunkSystem.this.count.getAndIncrement();
                              ChunkSystem.this.checkForCompletion();
                           }
                        };
                        if (RealisticSeasons.getInstance().isPaper()) {
                           Bukkit.getScheduler().runTaskAsynchronously(RealisticSeasons.getInstance(), var5);
                        } else {
                           Bukkit.getScheduler().runTask(RealisticSeasons.getInstance(), var5);
                        }
                     }

                  });
               } else {
                  this.sections[var7] = null;
               }
            } else {
               this.sections[var7] = null;
            }

            ++var7;
         }
      }

      this.completedScan.set(true);
      this.checkForCompletion();
   }

   public String getWorld() {
      return this.worldName;
   }

   private void checkForCompletion() {
      if (this.completedScan.get() && this.required.get() == this.count.get()) {
         this.hasProcessed.set(true);
      }

   }

   public Material getBlockType(int var1, int var2, int var3) {
      int var4 = var1 & 15;
      int var5 = var3 & 15;
      if (var2 >= this.minY && var2 <= this.maxY) {
         int var6 = var1 - var4 >> 4;
         int var7 = var3 - var5 >> 4;
         ChunkSnapshot var8 = this.getSnapshotAtWorldLocation(var6, var7);
         if (var8 == null) {
            return Material.AIR;
         } else {
            Material var9;
            try {
               var9 = var8.getBlockType(var4, var2, var5);
            } catch (ArrayIndexOutOfBoundsException var11) {
               return Material.AIR;
            }

            if (!var9.equals(Material.CAMPFIRE) && !var9.equals(Material.SOUL_CAMPFIRE)) {
               if (var9.equals(Material.FURNACE) || var9.equals(Material.BLAST_FURNACE)) {
                  Furnace var12 = (Furnace)var8.getBlockData(var4, var2, var5);
                  if (!var12.isLit()) {
                     return Material.AIR;
                  }
               }
            } else {
               Campfire var10 = (Campfire)var8.getBlockData(var4, var2, var5);
               if (!var10.isLit()) {
                  return Material.AIR;
               }
            }

            return var8.getBlockType(var4, var2, var5);
         }
      } else {
         return Material.AIR;
      }
   }

   private ChunkSnapshot getSnapshotAtWorldLocation(int var1, int var2) {
      int var3 = var1 - this.middleX;
      int var4 = var2 - this.middleZ;
      int var5 = (var3 + this.range) * UNEVEN[this.range] + var4 + this.range;
      return var5 >= 0 && var5 <= this.sections.length - 1 ? this.sections[var5] : null;
   }

   public boolean isNether() {
      return this.isNether;
   }
}
