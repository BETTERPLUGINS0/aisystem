package org.terraform.structure.small.buriedtreasure;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.Random;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.TerraLootTable;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.structure.MultiMegaChunkStructurePopulator;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.ChestBuilder;

public class BuriedTreasurePopulator extends MultiMegaChunkStructurePopulator {
   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         Random random = this.getHashedRandom(tw, data.getChunkX(), data.getChunkZ());
         MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
         int[][] var5 = this.getCoordsFromMegaChunk(tw, mc);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int[] coords = var5[var7];
            int x = coords[0];
            int z = coords[1];
            if (x >> 4 == data.getChunkX() && z >> 4 == data.getChunkZ()) {
               int height = GenUtils.getHighestGround(data, x, z);
               height -= GenUtils.randInt(random, 3, 10);
               SimpleBlock chest = new SimpleBlock(data, x, height, z);
               (new ChestBuilder(Material.CHEST)).setFacing(BlockUtils.getDirectBlockFace(random)).setLootTable(TerraLootTable.BURIED_TREASURE).apply(chest);
            }
         }

      }
   }

   public int[][] getCoordsFromMegaChunk(@NotNull TerraformWorld tw, @NotNull MegaChunk mc) {
      int num = TConfig.c.STRUCTURES_BURIEDTREASURE_COUNT_PER_MEGACHUNK;
      int[][] coords = new int[num][2];

      for(int i = 0; i < num; ++i) {
         coords[i] = mc.getRandomCoords(tw.getHashedRand((long)mc.getX(), mc.getZ(), 87464 * (1 + i)));
      }

      return coords;
   }

   public int[] getNearestFeature(@NotNull TerraformWorld tw, int rawX, int rawZ) {
      MegaChunk mc = new MegaChunk(rawX, 0, rawZ);
      double minDistanceSquared = 2.147483647E9D;
      int[] min = null;

      for(int nx = -1; nx <= 1; ++nx) {
         for(int nz = -1; nz <= 1; ++nz) {
            int[][] var10 = this.getCoordsFromMegaChunk(tw, mc);
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

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 12422), (int)(TConfig.c.STRUCTURES_BURIEDTREASURE_SPAWNRATIO * 10000.0D), 10000);
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      if (!this.isEnabled()) {
         return false;
      } else {
         MegaChunk mc = new MegaChunk(chunkX, chunkZ);
         int[][] var5 = this.getCoordsFromMegaChunk(tw, mc);
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int[] coords = var5[var7];
            if (coords[0] >> 4 == chunkX && coords[1] >> 4 == chunkZ) {
               EnumSet<BiomeBank> biomes = GenUtils.getBiomesInChunk(tw, chunkX, chunkZ);
               double numBeach = 0.0D;
               Iterator var12 = biomes.iterator();

               while(var12.hasNext()) {
                  BiomeBank b = (BiomeBank)var12.next();
                  if (b.getType() == BiomeType.BEACH) {
                     ++numBeach;
                  }
               }

               return numBeach > 0.0D && this.rollSpawnRatio(tw, chunkX, chunkZ);
            }
         }

         return false;
      }
   }

   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld world, int chunkX, int chunkZ) {
      return world.getHashedRand(82736921L, chunkX, chunkZ);
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && TConfig.c.STRUCTURES_BURIEDTREASURE_ENABLED;
   }

   public int getChunkBufferDistance() {
      return 0;
   }
}
