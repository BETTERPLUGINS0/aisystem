package org.terraform.biome.flat;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeBlender;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class GorgeHandler extends BiomeHandler {
   static final BiomeHandler plainsHandler;
   static final boolean slabs;
   static BiomeBlender biomeBlender;

   @NotNull
   private static BiomeBlender getBiomeBlender(TerraformWorld tw) {
      if (biomeBlender == null) {
         biomeBlender = (new BiomeBlender(tw, true, true)).setGridBlendingFactor(2.0D).setSmoothBlendTowardsRivers(4);
      }

      return biomeBlender;
   }

   public boolean isOcean() {
      return plainsHandler.isOcean();
   }

   public Biome getBiome() {
      return plainsHandler.getBiome();
   }

   public double calculateHeight(TerraformWorld tw, int x, int z) {
      double height = super.calculateHeight(tw, x, z);
      double riverDepth = HeightMap.getRawRiverDepth(tw, x, z);
      if (riverDepth > 0.0D) {
         height += riverDepth;
      }

      return height;
   }

   public Material[] getSurfaceCrust(Random rand) {
      return plainsHandler.getSurfaceCrust(rand);
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      SimpleBlock target = new SimpleBlock(data, rawX, surfaceY + 1, rawZ);

      boolean wasBelowSea;
      for(wasBelowSea = false; target.getY() <= TerraformGenerator.seaLevel - 20; target = target.getUp()) {
         wasBelowSea = true;
         if (target.getType() == Material.WATER) {
            BlockFace[] var9 = BlockUtils.directBlockFaces;
            int var10 = var9.length;

            for(int var11 = 0; var11 < var10; ++var11) {
               BlockFace face = var9[var11];
               if (BlockUtils.isAir(target.getRelative(face).getType())) {
                  target.getRelative(face).setType(Material.STONE);
               }
            }
         }
      }

      if (!wasBelowSea) {
         target = target.getGround();
         if (!BlockUtils.isWet(target.getUp()) && target.getType() == Material.STONE) {
            target.setType(Material.GRASS_BLOCK);
            target.getDown().setType(Material.DIRT);
            if (random.nextBoolean()) {
               target.getDown(2).setType(Material.DIRT);
               if (random.nextBoolean()) {
                  target.getDown(3).setType(Material.DIRT);
               }
            }
         }

         plainsHandler.populateSmallItems(world, random, rawX, surfaceY, rawZ, data);
      }
   }

   public BiomeHandler getTransformHandler() {
      return this;
   }

   public void transformTerrain(@NotNull ChunkCache cache, TerraformWorld tw, Random random, @NotNull ChunkData chunk, int x, int z, int chunkX, int chunkZ) {
      FastNoise cliffNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_GORGE_CLIFFNOISE, (world) -> {
         FastNoise n = new FastNoise();
         n.SetNoiseType(FastNoise.NoiseType.CubicFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.04F);
         return n;
      });
      FastNoise detailsNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_GORGE_DETAILS, (world) -> {
         FastNoise n = new FastNoise();
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFrequency(0.03F);
         return n;
      });
      double threshold = 0.1D;
      int heightFactor = 12;
      int rawX = chunkX * 16 + x;
      int rawZ = chunkZ * 16 + z;
      double preciseHeight = HeightMap.getPreciseHeight(tw, rawX, rawZ);
      int height = (int)preciseHeight;
      double rawCliffNoiseVal = (double)cliffNoise.GetNoise((float)rawX, (float)rawZ);
      double noiseValue = rawCliffNoiseVal * getBiomeBlender(tw).getEdgeFactor(BiomeBank.GORGE, rawX, rawZ);
      double detailsValue = (double)detailsNoise.GetNoise((float)rawX, (float)rawZ);
      if (noiseValue >= 0.0D) {
         double d = noiseValue / threshold - (double)((int)(noiseValue / threshold)) - 0.5D;
         double platformHeight = (double)((int)(noiseValue / threshold) * heightFactor) + 64.0D * Math.pow(d, 7.0D) * (double)heightFactor + detailsValue * (double)heightFactor * 0.5D;
         if (Math.round(platformHeight) >= 1L) {
            cache.writeTransformedHeight(x, z, (short)((int)(Math.round(platformHeight) + (long)height)));
         }

         for(int y = 1; y <= (int)Math.round(platformHeight); ++y) {
            Material material = (Material)GenUtils.randChoice((Object[])(Material.STONE, Material.STONE, Material.STONE, Material.STONE, Material.COBBLESTONE, Material.COBBLESTONE, Material.ANDESITE, Material.ANDESITE));
            if (slabs && material != Material.GRASS_BLOCK && y == (int)Math.round(platformHeight) && platformHeight - (double)((int)platformHeight) >= 0.5D) {
               material = Material.getMaterial(material.name() + "_SLAB");
            }

            chunk.setBlock(x, height + y, z, material);
         }

         if (detailsValue < 0.2D && GenUtils.chance(3, 4)) {
            chunk.setBlock(x, height + (int)Math.round(platformHeight), z, Material.GRASS_BLOCK);
         }
      } else {
         int depth = (int)Math.sqrt(Math.abs(rawCliffNoiseVal * getBiomeBlender(tw).getEdgeFactor(BiomeBank.GORGE, rawX, rawZ)) * 200.0D * 50.0D);
         int y;
         if (height - depth < TerraformGenerator.seaLevel - 20) {
            y = height - (TerraformGenerator.seaLevel - 20);
            depth = (int)((long)y + Math.round(Math.sqrt((double)(depth - y))));
         }

         if (depth > height - 10) {
            depth = height - 10;
         }

         cache.writeTransformedHeight(x, z, (short)(height - depth));

         for(y = 0; y < depth; ++y) {
            if (TerraformGenerator.seaLevel - 20 >= height - y) {
               chunk.setBlock(x, height - y, z, Material.WATER);
            } else {
               chunk.setBlock(x, height - y, z, Material.AIR);
            }
         }

         if (height - depth <= TerraformGenerator.seaLevel - 20) {
            chunk.setBlock(x, height - depth, z, Material.STONE);
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      plainsHandler.populateLargeItems(tw, random, data);
      SimpleLocation[] rocks = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 17, 0.4F);
      SimpleLocation[] var5 = rocks;
      int var6 = rocks.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome()) {
            int rockY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(rockY);
            if (rockY <= TerraformGenerator.seaLevel - 18) {
               BlockUtils.replaceSphere(random.nextInt(91822), (float)GenUtils.randDouble(random, 3.0D, 6.0D), (float)GenUtils.randDouble(random, 4.0D, 7.0D), (float)GenUtils.randDouble(random, 3.0D, 6.0D), new SimpleBlock(data, sLoc), true, (Material)GenUtils.randChoice((Object[])(Material.GRANITE, Material.ANDESITE, Material.DIORITE)));
            }
         }
      }

   }

   static {
      plainsHandler = BiomeBank.PLAINS.getHandler();
      slabs = TConfig.c.MISC_USE_SLABS_TO_SMOOTH;
   }
}
