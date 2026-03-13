package org.terraform.structure.village;

import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.structure.village.plains.PlainsVillagePopulator;
import org.terraform.utils.GenUtils;

public class VillagePopulator extends SingleMegaChunkStructurePopulator {
   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return tw.getHashedRand(11111199L, chunkX, chunkZ);
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 12422), (int)(TConfig.c.STRUCTURES_VILLAGE_SPAWNRATIO * 10000.0D), 10000);
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else {
         return biome != BiomeBank.PLAINS && biome != BiomeBank.FOREST && biome != BiomeBank.SAVANNA && biome != BiomeBank.TAIGA && biome != BiomeBank.SCARLET_FOREST && biome != BiomeBank.CHERRY_GROVE ? false : this.rollSpawnRatio(tw, chunkX, chunkZ);
      }
   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         (new PlainsVillagePopulator()).populate(tw, data);
      }
   }

   public int getChunkBufferDistance() {
      return TConfig.c.STRUCTURES_VILLAGE_CHUNK_EXCLUSION_ZONE;
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && (BiomeBank.isBiomeEnabled(BiomeBank.PLAINS) || BiomeBank.isBiomeEnabled(BiomeBank.FOREST) || BiomeBank.isBiomeEnabled(BiomeBank.SAVANNA) || BiomeBank.isBiomeEnabled(BiomeBank.TAIGA) || BiomeBank.isBiomeEnabled(BiomeBank.SCARLET_FOREST) || BiomeBank.isBiomeEnabled(BiomeBank.CHERRY_GROVE)) && TConfig.c.STRUCTURES_PLAINSVILLAGE_ENABLED;
   }
}
