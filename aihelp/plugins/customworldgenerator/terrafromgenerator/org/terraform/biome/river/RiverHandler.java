package org.terraform.biome.river;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class RiverHandler extends BiomeHandler {
   public static void riverVegetation(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data, int rawX, int surfaceY, int rawZ) {
      boolean growsKelp = tw.getHashedRand((long)(rawX >> 4), rawZ >> 4, 97418).nextBoolean();
      if (GenUtils.chance(random, 10, 100)) {
         generateSeagrass(rawX, surfaceY + 1, rawZ, data);
         if (random.nextBoolean()) {
            generateTallSeagrass(rawX, surfaceY + 1, rawZ, data);
         }
      } else if (GenUtils.chance(random, 3, 50) && growsKelp && surfaceY + 1 < TerraformGenerator.seaLevel - 10) {
         generateKelp(rawX, surfaceY + 1, rawZ, data, random);
      }

   }

   public static void generateSeagrass(int x, int y, int z, @NotNull PopulatorDataAbstract data) {
      if (data.getType(x, y, z) == Material.WATER) {
         PlantBuilder.SEAGRASS.build(data, x, y, z);
      }
   }

   public static void generateTallSeagrass(int x, int y, int z, @NotNull PopulatorDataAbstract data) {
      if (data.getType(x, y, z) == Material.WATER && data.getType(x, y + 1, z) == Material.WATER) {
         PlantBuilder.TALL_SEAGRASS.build(data, x, y, z);
      }
   }

   private static void generateKelp(int x, int y, int z, @NotNull PopulatorDataAbstract data, Random random) {
      for(int ny = y; ny < TerraformGenerator.seaLevel - GenUtils.randInt(5, 15) && data.getType(x, ny, z) == Material.WATER; ++ny) {
         PlantBuilder.KELP_PLANT.build(data, x, ny, z);
      }

   }

   public boolean isOcean() {
      return true;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.RIVER;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE, Material.DIRT), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(@NotNull TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY < TerraformGenerator.seaLevel) {
         if (surfaceY >= TerraformGenerator.seaLevel - 2) {
            data.setType(rawX, surfaceY, rawZ, Material.SAND);
         } else if (surfaceY >= TerraformGenerator.seaLevel - 4 && random.nextBoolean()) {
            data.setType(rawX, surfaceY, rawZ, Material.SAND);
         }

         if (BlockUtils.isStoneLike(data.getType(rawX, surfaceY, rawZ))) {
            riverVegetation(world, random, data, rawX, surfaceY, rawZ);
            if (GenUtils.chance(random, TConfig.c.BIOME_CLAY_DEPOSIT_CHANCE_OUT_OF_THOUSAND, 1000)) {
               BlockUtils.generateClayDeposit(rawX, surfaceY, rawZ, data, random);
            }

         }
      }
   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
   }
}
