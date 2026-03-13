package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeBlender;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class ErodedPlainsHandler extends BiomeHandler {
   static final BiomeHandler plainsHandler;
   static final boolean slabs;
   static BiomeBlender biomeBlender;

   @NotNull
   private static BiomeBlender getBiomeBlender(TerraformWorld tw) {
      if (biomeBlender == null) {
         biomeBlender = (new BiomeBlender(tw, true, true)).setRiverThreshold(4).setBlendBeaches(false);
      }

      return biomeBlender;
   }

   public boolean isOcean() {
      return plainsHandler.isOcean();
   }

   public Biome getBiome() {
      return plainsHandler.getBiome();
   }

   public Material[] getSurfaceCrust(Random rand) {
      return plainsHandler.getSurfaceCrust(rand);
   }

   public void populateSmallItems(TerraformWorld world, Random random, int rawX, int surfaceY, int rawZ, PopulatorDataAbstract data) {
      plainsHandler.populateSmallItems(world, random, rawX, surfaceY, rawZ, data);
   }

   public BiomeHandler getTransformHandler() {
      return this;
   }

   public void transformTerrain(@NotNull ChunkCache cache, TerraformWorld tw, Random random, @NotNull ChunkData chunk, int x, int z, int chunkX, int chunkZ) {
      FastNoise noise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_ERODEDPLAINS_CLIFFNOISE, (world) -> {
         FastNoise n = new FastNoise();
         n.SetNoiseType(FastNoise.NoiseType.CubicFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.02F);
         return n;
      });
      FastNoise details = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_ERODEDPLAINS_DETAILS, (world) -> {
         FastNoise n = new FastNoise();
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFrequency(0.03F);
         return n;
      });
      double threshold = 0.1D;
      int heightFactor = 10;
      int rawX = chunkX * 16 + x;
      int rawZ = chunkZ * 16 + z;
      double preciseHeight = HeightMap.getPreciseHeight(tw, rawX, rawZ);
      int height = (int)preciseHeight;
      double noiseValue = (double)Math.max(0.0F, noise.GetNoise((float)rawX, (float)rawZ)) * getBiomeBlender(tw).getEdgeFactor(BiomeBank.ERODED_PLAINS, rawX, rawZ);
      double detailsValue = (double)details.GetNoise((float)rawX, (float)rawZ);
      double d = noiseValue / threshold - (double)((int)(noiseValue / threshold)) - 0.5D;
      double platformHeight = (double)((int)(noiseValue / threshold) * heightFactor) + 64.0D * Math.pow(d, 7.0D) * (double)heightFactor + detailsValue * (double)heightFactor * 0.5D;
      short newHeight = (short)(height + (int)Math.round(platformHeight));
      if (newHeight >= height) {
         cache.writeTransformedHeight(x, z, (short)((int)Math.round(platformHeight) + height));

         for(int y = height + 1; y <= newHeight; ++y) {
            Material material = (Material)GenUtils.randChoice((Object[])(Material.STONE, Material.STONE, Material.STONE, Material.STONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE, Material.ANDESITE));
            if (slabs && material != Material.GRASS_BLOCK && y == newHeight && platformHeight - (double)((int)platformHeight) >= 0.5D) {
               material = Material.getMaterial(material.name() + "_SLAB");
            }

            assert material != null;

            chunk.setBlock(x, y, z, material);
         }

         if (detailsValue < 0.2D && GenUtils.chance(3, 4)) {
            chunk.setBlock(x, newHeight, z, Material.GRASS_BLOCK);
         }

      }
   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
      plainsHandler.populateLargeItems(tw, random, data);
   }

   static {
      plainsHandler = BiomeBank.PLAINS.getHandler();
      slabs = TConfig.c.MISC_USE_SLABS_TO_SMOOTH;
   }
}
