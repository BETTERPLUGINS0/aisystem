package org.terraform.structure.small.dungeon;

import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.structure.MultiMegaChunkStructurePopulator;
import org.terraform.utils.GenUtils;

public class SmallDungeonPopulator extends MultiMegaChunkStructurePopulator {
   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         int totalHeight = 0;

         for(int x = data.getChunkX() * 16; x < data.getChunkX() * 16 + 16; ++x) {
            for(int z = data.getChunkZ() * 16; z < data.getChunkZ() * 16 + 16; ++z) {
               totalHeight += HeightMap.getBlockHeight(tw, x, z);
            }
         }

         if (totalHeight / 256 <= TConfig.c.STRUCTURES_DROWNEDDUNGEON_MIN_DEPTH && GenUtils.chance(tw.getHashedRand(1223L, data.getChunkX(), data.getChunkZ()), TConfig.c.STRUCTURES_DROWNEDDUNGEON_CHANCE, 1000)) {
            if (!TConfig.c.STRUCTURES_DROWNEDDUNGEON_ENABLED) {
               return;
            }

            (new DrownedDungeonPopulator()).populate(tw, data);
         } else {
            if (!TConfig.c.STRUCTURES_UNDERGROUNDDUNGEON_ENABLED) {
               return;
            }

            (new UndergroundDungeonPopulator()).populate(tw, data);
         }

      }
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 12222), (int)(TConfig.c.STRUCTURES_DUNGEONS_SPAWNRATIO * 10000.0D), 10000);
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      if (!this.isEnabled()) {
         return false;
      } else {
         MegaChunk mc = new MegaChunk(chunkX, chunkZ);
         int[][] allCoords = this.getCoordsFromMegaChunk(tw, mc);
         int[][] var6 = allCoords;
         int var7 = allCoords.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            int[] coords = var6[var8];
            if (coords[0] >> 4 == chunkX && coords[1] >> 4 == chunkZ) {
               return this.rollSpawnRatio(tw, chunkX, chunkZ);
            }
         }

         return false;
      }
   }

   public int[][] getCoordsFromMegaChunk(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      int num = TConfig.c.STRUCTURES_DUNGEONS_COUNT_PER_MEGACHUNK;
      int[][] coords = new int[num][2];

      for(int i = 0; i < num; ++i) {
         coords[i] = mc.getRandomCoords(tw.getHashedRand((long)mc.getX(), mc.getZ(), 1317324 * (1 + i)));
      }

      return coords;
   }

   public int[] getNearestFeature(@NotNull TerraformWorld tw, int rawX, int rawZ) {
      MegaChunk mc = new MegaChunk(rawX, 0, rawZ);
      double minDistanceSquared = 2.147483647E9D;
      int[] min = null;

      for(int nx = -1; nx <= 1; ++nx) {
         for(int nz = -1; nz <= 1; ++nz) {
            int[][] var10 = this.getCoordsFromMegaChunk(tw, mc.getRelative(nx, nz));
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               int[] loc = var10[var12];
               double distSqr = Math.pow((double)(loc[0] - rawX), 2.0D) + Math.pow((double)(loc[1] - rawZ), 2.0D);
               if (distSqr < minDistanceSquared) {
                  minDistanceSquared = distSqr;
                  min = loc;
               }
            }
         }
      }

      return min;
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(48772719L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && (TConfig.c.STRUCTURES_DROWNEDDUNGEON_ENABLED || TConfig.c.STRUCTURES_UNDERGROUNDDUNGEON_ENABLED);
   }
}
