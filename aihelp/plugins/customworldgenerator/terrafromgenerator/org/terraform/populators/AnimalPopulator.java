package org.terraform.populators;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.utils.GenUtils;

public class AnimalPopulator {
   private final EntityType animalType;
   private final int chance;
   private final int minNum;
   private final int maxNum;
   private BiomeBank[] whitelistedBiomes;
   private BiomeBank[] blacklistedBiomes;
   private boolean isAquatic = false;

   public AnimalPopulator(EntityType animalType, int minNum, int maxNum, int chance, boolean useWhitelist, BiomeBank... biomes) {
      this.animalType = animalType;
      this.chance = chance;
      if (useWhitelist) {
         this.whitelistedBiomes = biomes;
      } else {
         this.blacklistedBiomes = biomes;
      }

      this.minNum = minNum;
      this.maxNum = maxNum;
   }

   public boolean canSpawn(@NotNull Random rand) {
      return TConfig.areAnimalsEnabled() && !GenUtils.chance(rand, 100 - this.chance, 100);
   }

   private boolean canSpawnInBiome(BiomeBank b) {
      if (!TConfig.areAnimalsEnabled()) {
         return false;
      } else {
         BiomeBank[] var2;
         int var3;
         int var4;
         BiomeBank entr;
         if (this.whitelistedBiomes != null) {
            var2 = this.whitelistedBiomes;
            var3 = var2.length;

            for(var4 = 0; var4 < var3; ++var4) {
               entr = var2[var4];
               if (entr == b) {
                  return true;
               }
            }

            return false;
         } else if (this.blacklistedBiomes != null) {
            var2 = this.blacklistedBiomes;
            var3 = var2.length;

            for(var4 = 0; var4 < var3; ++var4) {
               entr = var2[var4];
               if (entr == b) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public void populate(@NotNull TerraformWorld world, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      if (TConfig.areAnimalsEnabled()) {
         for(int i = 0; i < GenUtils.randInt(random, this.minNum, this.maxNum); ++i) {
            int x = (data.getChunkX() << 4) + GenUtils.randInt(random, 5, 7);
            int z = (data.getChunkZ() << 4) + GenUtils.randInt(random, 5, 7);
            int height = GenUtils.getTransformedHeight(world, x, z) + 1;
            if (data.getType(x, height, z).isSolid()) {
               height = GenUtils.getHighestGround(data, x, z) + 1;
            }

            if (this.canSpawnInBiome(world.getBiomeBank(x, z))) {
               if (!this.isAquatic && height > TerraformGenerator.seaLevel) {
                  if (!data.getType(x, height, z).isSolid()) {
                     data.addEntity(x, height, z, this.animalType);
                  }
               } else if (this.isAquatic && height <= TerraformGenerator.seaLevel && data.getType(x, height, z) == Material.WATER) {
                  data.addEntity(x, height, z, this.animalType);
               }
            }
         }

      }
   }

   @NotNull
   public AnimalPopulator setAquatic(boolean aquatic) {
      this.isAquatic = aquatic;
      return this;
   }
}
