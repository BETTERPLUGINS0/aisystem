package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.data.type.Bamboo;
import org.bukkit.block.data.type.Bamboo.Leaves;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class BambooForestHandler extends BiomeHandler {
   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.BAMBOO_JUNGLE;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRASS_BLOCK, Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      FastNoise pathNoise = NoiseCacheHandler.getNoise(world, NoiseCacheHandler.NoiseCacheEntry.BIOME_BAMBOOFOREST_PATHNOISE, (tw) -> {
         FastNoise n = new FastNoise((int)(tw.getSeed() * 13L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.07F);
         return n;
      });
      if ((double)pathNoise.GetNoise((float)rawX, (float)rawZ) > 0.27D && GenUtils.chance(random, 99, 100) && data.getBiome(rawX, rawZ) == this.getBiome() && BlockUtils.isDirtLike(data.getType(rawX, surfaceY, rawZ))) {
         data.setType(rawX, surfaceY, rawZ, Material.PODZOL);
      }

      if ((data.getType(rawX, surfaceY, rawZ) == Material.GRASS_BLOCK || data.getType(rawX, surfaceY, rawZ) == Material.PODZOL) && GenUtils.chance(random, 1, 3)) {
         if (GenUtils.chance(random, 6, 10)) {
            PlantBuilder.GRASS.build(data, rawX, surfaceY + 1, rawZ);
            if (random.nextBoolean()) {
               PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
            }
         } else if (GenUtils.chance(random, 7, 10)) {
            PlantBuilder.FERN.build(data, rawX, surfaceY + 1, rawZ);
         } else {
            PlantBuilder.LARGE_FERN.build(data, rawX, surfaceY + 1, rawZ);
         }
      }

   }

   public void populateLargeItems(TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      for(int x = data.getChunkX() * 16; x < data.getChunkX() * 16 + 16; ++x) {
         for(int z = data.getChunkZ() * 16; z < data.getChunkZ() * 16 + 16; ++z) {
            int y = GenUtils.getTrueHighestBlock(data, x, z);
            if (data.getBiome(x, z) == this.getBiome() && (data.getType(x, y, z) == Material.GRASS_BLOCK || data.getType(x, y, z) == Material.PODZOL)) {
               if (TConfig.arePlantsEnabled() && GenUtils.chance(random, 1, 50)) {
                  BlockUtils.replaceSphere(random.nextInt(424444), 2.0F, 3.0F, 2.0F, new SimpleBlock(data, x, y + 1, z), false, Material.JUNGLE_LEAVES);
               }

               if (TConfig.arePlantsEnabled() && GenUtils.chance(random, 1, 3) && BlockUtils.isDirtLike(data.getType(x, y, z))) {
                  int h = BlockUtils.spawnPillar(random, data, x, y + 1, z, Material.BAMBOO, 12, 16);
                  Bamboo bambooHead = (Bamboo)Bukkit.createBlockData(Material.BAMBOO);
                  bambooHead.setLeaves(Leaves.LARGE);
                  data.setBlockData(x, y + h, z, bambooHead);
                  bambooHead = (Bamboo)Bukkit.createBlockData(Material.BAMBOO);
                  bambooHead.setLeaves(Leaves.LARGE);
                  data.setBlockData(x, y + h - 1, z, bambooHead);
                  bambooHead = (Bamboo)Bukkit.createBlockData(Material.BAMBOO);
                  bambooHead.setLeaves(Leaves.SMALL);
                  data.setBlockData(x, y + h - 2, z, bambooHead);
               }
            }
         }
      }

   }
}
