package org.terraform.biome.river;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.biome.custombiomes.CustomBiomeType;
import org.terraform.biome.flat.MuddyBogHandler;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class BogRiverHandler extends BiomeHandler {
   public boolean isOcean() {
      return true;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.SWAMP;
   }

   @NotNull
   public CustomBiomeType getCustomBiome() {
      return CustomBiomeType.MUDDY_BOG;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE, Material.DIRT), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public BiomeHandler getTransformHandler() {
      return this;
   }

   public void transformTerrain(@NotNull ChunkCache cache, @NotNull TerraformWorld tw, Random random, @NotNull ChunkData chunk, int x, int z, int chunkX, int chunkZ) {
      int rawX = chunkX * 16 + x;
      int rawZ = chunkZ * 16 + z;
      FastNoise sinkin = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_MUDDYBOG_HEIGHTMAP, (world) -> {
         FastNoise n = new FastNoise((int)tw.getSeed());
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(4);
         n.SetFrequency(0.005F);
         return n;
      });
      double noise = (double)sinkin.GetNoise((float)rawX, (float)rawZ);
      if (noise > -0.2D) {
         noise += 0.5D;
         if (noise > 1.05D) {
            noise = 1.05D;
         }

         if (cache.getTransformedHeight(x, z) < TerraformGenerator.seaLevel) {
            double maxHeight = (double)(TerraformGenerator.seaLevel - cache.getTransformedHeight(x, z)) + 2.0D;
            int height = (int)Math.round(maxHeight * noise);
            if (tw.getBiomeBank(rawX, rawZ) != BiomeBank.BOG_RIVER && tw.getBiomeBank(rawX, rawZ) != BiomeBank.MUDDY_BOG && tw.getBiomeBank(rawX, rawZ) != BiomeBank.BOG_BEACH) {
               height = 0;
            }

            for(int newHeight = 1; newHeight <= height; ++newHeight) {
               chunk.setBlock(x, cache.getTransformedHeight(x, z) + newHeight, z, Material.DIRT);
            }

            if (height >= 1) {
               cache.writeTransformedHeight(x, z, (short)(cache.getTransformedHeight(x, z) + height));
            }

            if (cache.getTransformedHeight(x, z) >= TerraformGenerator.seaLevel) {
               chunk.setBlock(x, cache.getTransformedHeight(x, z), z, Material.GRASS_BLOCK);
            }
         }
      }

   }

   public void populateSmallItems(@NotNull TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      (new MuddyBogHandler()).populateSmallItems(tw, random, rawX, surfaceY, rawZ, data);
      SimpleBlock block = new SimpleBlock(data, rawX, surfaceY, rawZ);
      if (BlockUtils.isWet(block.getUp())) {
         RiverHandler.riverVegetation(tw, random, data, rawX, surfaceY, rawZ);
         if (GenUtils.chance(random, TConfig.c.BIOME_CLAY_DEPOSIT_CHANCE_OUT_OF_THOUSAND, 1000)) {
            BlockUtils.generateClayDeposit(rawX, surfaceY, rawZ, data, random);
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      (new MuddyBogHandler()).populateLargeItems(tw, random, data);
   }
}
