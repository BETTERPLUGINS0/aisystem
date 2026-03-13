package org.terraform.structure;

import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.HeightMap;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;

public class StructureBufferDistanceHandler {
   public static boolean[] canDecorateChunk(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      if (Math.pow((double)(chunkX * 16), 2.0D) + Math.pow((double)(chunkZ * 16), 2.0D) < (double)HeightMap.spawnFlatRadiusSquared) {
         return new boolean[]{false, true};
      } else {
         boolean[] canDecorate = new boolean[]{true, true};
         MegaChunk mc = new MegaChunk(chunkX, chunkZ);
         BiomeBank biome = mc.getCenterBiomeSection(tw).getBiomeBank();
         SingleMegaChunkStructurePopulator[] var6 = StructureRegistry.getLargeStructureForMegaChunk(tw, mc);
         int var7 = var6.length;

         for(int var8 = 0; var8 < var7; ++var8) {
            StructurePopulator structPop = var6[var8];
            if (structPop != null && structPop instanceof SingleMegaChunkStructurePopulator) {
               SingleMegaChunkStructurePopulator spop = (SingleMegaChunkStructurePopulator)structPop;
               int chunkBufferRadius = spop.getChunkBufferDistance();
               if (chunkBufferRadius > 0 || spop.getCaveClusterBufferDistance() > 0) {
                  int[] chunkCoords = mc.getCenterBiomeSectionChunkCoords();
                  if (TConfig.areStructuresEnabled() && spop.canSpawn(tw, chunkCoords[0], chunkCoords[1], biome)) {
                     int dist = (int)(Math.pow((double)(chunkCoords[0] - chunkX), 2.0D) + Math.pow((double)(chunkCoords[1] - chunkZ), 2.0D));
                     double rootedDist = Math.max(Math.sqrt((double)dist), 0.002D);
                     canDecorate[0] &= rootedDist > (double)chunkBufferRadius;
                     canDecorate[1] &= rootedDist > (double)spop.getCaveClusterBufferDistance();
                     if (!canDecorate[0] && !canDecorate[1]) {
                        return canDecorate;
                     }
                  }
               }
            }
         }

         return canDecorate;
      }
   }
}
