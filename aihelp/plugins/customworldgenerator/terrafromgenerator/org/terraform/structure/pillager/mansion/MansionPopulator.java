package org.terraform.structure.pillager.mansion;

import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.utils.GenUtils;

public class MansionPopulator extends SingleMegaChunkStructurePopulator {
   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return tw.getHashedRand(717281012L, chunkX, chunkZ);
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 99572), (int)(TConfig.c.STRUCTURES_MANSION_SPAWNRATIO * 10000.0D), 10000);
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else if (Math.pow((double)(chunkX * 16), 2.0D) + Math.pow((double)(chunkZ * 16), 2.0D) < Math.pow((double)TConfig.c.STRUCTURES_MANSION_MINDISTANCE, 2.0D)) {
         return false;
      } else {
         return biome == BiomeBank.DARK_FOREST ? this.rollSpawnRatio(tw, chunkX, chunkZ) : false;
      }
   }

   public void populate(TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
         int[] coords = mc.getCenterBiomeSectionBlockCoords();
         int y = GenUtils.getHighestGround(data, coords[0], coords[1]);
         if (y < TerraformGenerator.seaLevel) {
            y = TerraformGenerator.seaLevel;
         }

         MansionJigsawBuilder builder = new MansionJigsawBuilder(TConfig.c.STRUCTURES_MANSION_SIZE, TConfig.c.STRUCTURES_MANSION_SIZE, data, coords[0], y, coords[1]);
         builder.generate(new Random());
         builder.build(new Random());
      }
   }

   public int getChunkBufferDistance() {
      return TConfig.c.STRUCTURES_MANSION_CHUNK_EXCLUSION_ZONE;
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && BiomeBank.isBiomeEnabled(BiomeBank.DARK_FOREST) && TConfig.c.STRUCTURES_MANSION_ENABLED;
   }
}
