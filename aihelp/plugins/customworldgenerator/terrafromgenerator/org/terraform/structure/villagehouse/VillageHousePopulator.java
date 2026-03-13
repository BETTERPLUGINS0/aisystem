package org.terraform.structure.villagehouse;

import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.MegaChunk;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.structure.SingleMegaChunkStructurePopulator;
import org.terraform.structure.villagehouse.animalfarm.AnimalFarmPopulator;
import org.terraform.structure.villagehouse.farmhouse.FarmhousePopulator;
import org.terraform.structure.villagehouse.mountainhouse.MountainhousePopulator;
import org.terraform.utils.GenUtils;

public class VillageHousePopulator extends SingleMegaChunkStructurePopulator {
   @NotNull
   public Random getHashedRandom(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return tw.getHashedRand(2291282L, chunkX, chunkZ);
   }

   private boolean rollSpawnRatio(@NotNull TerraformWorld tw, int chunkX, int chunkZ) {
      return GenUtils.chance(tw.getHashedRand((long)chunkX, chunkZ, 12422), (int)(TConfig.c.STRUCTURES_VILLAGEHOUSE_SPAWNRATIO * 10000.0D), 10000);
   }

   public boolean canSpawn(@NotNull TerraformWorld tw, int chunkX, int chunkZ, @NotNull BiomeBank biome) {
      if (!this.isEnabled()) {
         return false;
      } else {
         MegaChunk mc = new MegaChunk(chunkX, chunkZ);
         int[] coords = mc.getCenterBiomeSectionBlockCoords();
         if (coords[0] >> 4 == chunkX && coords[1] >> 4 == chunkZ) {
            if (!biome.isDry()) {
               return false;
            }

            if (HeightMap.getBlockHeight(tw, coords[0], coords[1]) > TerraformGenerator.seaLevel) {
               if (biome == BiomeBank.DESERT || biome == BiomeBank.BADLANDS || biome == BiomeBank.ICE_SPIKES) {
                  return TConfig.c.STRUCTURES_ANIMALFARM_ENABLED && this.rollSpawnRatio(tw, chunkX, chunkZ);
               }

               if (biome == BiomeBank.SNOWY_TAIGA || biome == BiomeBank.SNOWY_WASTELAND || biome == BiomeBank.JUNGLE) {
                  return TConfig.c.STRUCTURES_FARMHOUSE_ENABLED && this.rollSpawnRatio(tw, chunkX, chunkZ);
               }

               if (biome == BiomeBank.ROCKY_MOUNTAINS) {
                  return TConfig.c.STRUCTURES_MOUNTAINHOUSE_ENABLED && this.rollSpawnRatio(tw, chunkX, chunkZ);
               }
            }
         }

         return false;
      }
   }

   public void populate(@NotNull TerraformWorld tw, @NotNull PopulatorDataAbstract data) {
      if (this.isEnabled()) {
         MegaChunk mc = new MegaChunk(data.getChunkX(), data.getChunkZ());
         BiomeBank biome = mc.getCenterBiomeSection(tw).getBiomeBank();
         if (biome != BiomeBank.DESERT && biome != BiomeBank.BADLANDS && biome != BiomeBank.ICE_SPIKES) {
            if (biome != BiomeBank.SNOWY_TAIGA && biome != BiomeBank.SNOWY_WASTELAND && biome != BiomeBank.JUNGLE) {
               if (biome == BiomeBank.ROCKY_MOUNTAINS) {
                  if (!TConfig.c.STRUCTURES_MOUNTAINHOUSE_ENABLED) {
                     return;
                  }

                  (new MountainhousePopulator()).populate(tw, data);
               }
            } else {
               if (!TConfig.c.STRUCTURES_FARMHOUSE_ENABLED) {
                  return;
               }

               (new FarmhousePopulator()).populate(tw, data);
            }
         } else {
            if (!TConfig.c.STRUCTURES_ANIMALFARM_ENABLED) {
               return;
            }

            (new AnimalFarmPopulator()).populate(tw, data);
         }

      }
   }

   public boolean isEnabled() {
      return TConfig.areStructuresEnabled() && (BiomeBank.isBiomeEnabled(BiomeBank.DESERT) || BiomeBank.isBiomeEnabled(BiomeBank.BADLANDS) || BiomeBank.isBiomeEnabled(BiomeBank.ICE_SPIKES) || BiomeBank.isBiomeEnabled(BiomeBank.SNOWY_TAIGA) || BiomeBank.isBiomeEnabled(BiomeBank.SNOWY_WASTELAND) || BiomeBank.isBiomeEnabled(BiomeBank.JUNGLE) || BiomeBank.isBiomeEnabled(BiomeBank.ROCKY_MOUNTAINS)) && (TConfig.c.STRUCTURES_ANIMALFARM_ENABLED || TConfig.c.STRUCTURES_FARMHOUSE_ENABLED || TConfig.c.STRUCTURES_MOUNTAINHOUSE_ENABLED);
   }
}
