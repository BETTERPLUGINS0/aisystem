package org.terraform.biome.mountainous;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBlender;
import org.terraform.biome.BiomeHandler;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.DudChunkData;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class ShatteredSavannaHandler extends AbstractMountainHandler {
   static BiomeBlender biomeBlender;

   @NotNull
   private static BiomeBlender getBiomeBlender(TerraformWorld tw) {
      if (biomeBlender == null) {
         biomeBlender = (new BiomeBlender(tw, true, true)).setGridBlendingFactor(4.0D).setSmoothBlendTowardsRivers(2);
      }

      return biomeBlender;
   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.SAVANNA_PLATEAU;
   }

   @NotNull
   public Material[] getSurfaceCrust(Random rand) {
      return new Material[]{Material.GRASS_BLOCK, Material.DIRT, (Material)GenUtils.randChoice((Object[])(Material.DIRT, Material.STONE)), (Material)GenUtils.randChoice((Object[])(Material.DIRT, Material.STONE)), Material.STONE};
   }

   @NotNull
   public BiomeHandler getTransformHandler() {
      return this;
   }

   public void transformTerrain(@NotNull ChunkCache cache, @NotNull TerraformWorld tw, Random random, @NotNull ChunkData chunk, int x, int z, int chunkX, int chunkZ) {
      FastNoise creviceNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_SHATTERED_SAVANNANOISE, (world) -> {
         FastNoise n = new FastNoise(tw.getHashedRand(181234L, 32189, 16342134).nextInt());
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalType(FastNoise.FractalType.Billow);
         n.SetFractalOctaves(1);
         n.SetFrequency(0.02F);
         return n;
      });
      FastNoise yScaleNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_SHATTERED_SAVANNANOISE, (world) -> {
         FastNoise n = new FastNoise(tw.getHashedRand(982374L, 18723, 1983701).nextInt());
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFrequency(0.06F);
         return n;
      });
      int rawX = chunkX * 16 + x;
      int rawZ = chunkZ * 16 + z;
      double crevice = (double)Math.abs(creviceNoise.GetNoise((float)rawX, (float)rawZ));
      if (!(crevice < 0.4000000059604645D)) {
         short baseHeight = cache.getTransformedHeight(x, z);
         int low = (int)HeightMap.CORE.getHeight(tw, rawX, rawZ);
         boolean updateHeight = true;

         for(int y = baseHeight; y > low; --y) {
            double scale = 1.0D - 0.4D * (double)Math.abs(yScaleNoise.GetNoise((float)y, 0.0F));
            if (crevice * scale < 0.4000000059604645D) {
               updateHeight = false;
            } else {
               chunk.setBlock(x, y, z, Material.CAVE_AIR);
               if (updateHeight) {
                  cache.writeTransformedHeight(x, z, (short)(y - 1));
               }
            }
         }

         if (!(chunk instanceof DudChunkData)) {
            Material[] crust = this.getSurfaceCrust(new Random());

            for(int i = 0; i < crust.length; ++i) {
               if (BlockUtils.isAir(chunk.getType(x, cache.getTransformedHeight(x, z) - i, z))) {
                  return;
               }

               chunk.setBlock(x, cache.getTransformedHeight(x, z) - i, z, crust[i]);
            }

         }
      }
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY >= TerraformGenerator.seaLevel) {
         if (data.getType(rawX, surfaceY, rawZ) == Material.GRASS_BLOCK && !data.getType(rawX, surfaceY + 1, rawZ).isSolid() && GenUtils.chance(random, 2, 10)) {
            PlantBuilder.GRASS.build(data, rawX, surfaceY + 1, rawZ);
         }

      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 34);
      SimpleLocation[] poffs = trees;
      int var6 = trees.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = poffs[var7];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
            (new FractalTreeBuilder(FractalTypes.Tree.SAVANNA_SMALL)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
         }
      }

      poffs = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 35);
      SimpleLocation[] var15 = poffs;
      var7 = poffs.length;

      for(int var16 = 0; var16 < var7; ++var16) {
         SimpleLocation sLoc = var15[var16];
         int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         sLoc = sLoc.getAtY(treeY);
         if (TConfig.arePlantsEnabled() && data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ())) && !data.getType(sLoc.getX(), sLoc.getY() + 1, sLoc.getZ()).isSolid()) {
            SimpleBlock base = new SimpleBlock(data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ());
            int rX = GenUtils.randInt(random, 2, 4);
            int rY = GenUtils.randInt(random, 2, 4);
            int rZ = GenUtils.randInt(random, 2, 4);
            BlockUtils.replaceSphere(random.nextInt(999), (float)rX, (float)rY, (float)rZ, base, false, Material.ACACIA_LEAVES);
         }
      }

   }
}
