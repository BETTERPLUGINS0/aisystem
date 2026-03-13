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
import org.terraform.data.DudChunkData;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.tree.MushroomBuilder;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class ArchedCliffsHandler extends BiomeHandler {
   static BiomeBlender biomeBlender;

   @NotNull
   private static BiomeBlender getBiomeBlender(TerraformWorld tw) {
      if (biomeBlender == null) {
         biomeBlender = (new BiomeBlender(tw, true, true)).setGridBlendingFactor(4.0D).setSmoothBlendTowardsRivers(4);
      }

      return biomeBlender;
   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.PLAINS;
   }

   public double calculateHeight(TerraformWorld tw, int x, int z) {
      double height = super.calculateHeight(tw, x, z);
      double riverDepth = HeightMap.getRawRiverDepth(tw, x, z);
      if (riverDepth > 0.0D) {
         height += riverDepth;
      }

      return height + 3.0D;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRASS_BLOCK, Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      SimpleBlock target = new SimpleBlock(data, rawX, surfaceY, rawZ);
      if (GenUtils.chance(random, 1, 10)) {
         if (GenUtils.chance(random, 6, 10)) {
            PlantBuilder.GRASS.build(target.getUp());
            if (random.nextBoolean()) {
               PlantBuilder.TALL_GRASS.build(target.getUp());
            }
         } else if (GenUtils.chance(random, 7, 10)) {
            BlockUtils.pickFlower().build(target.getUp());
         } else {
            BlockUtils.pickTallFlower().build(target.getUp());
         }
      }

      SimpleBlock underside = target.findAirPocket(30);
      if (underside != null && underside.getY() > TerraformGenerator.seaLevel) {
         SimpleBlock grassBottom = underside.findStonelikeFloor(50);
         if (grassBottom != null && grassBottom.getY() > TerraformGenerator.seaLevel && grassBottom.getType() == Material.GRASS_BLOCK) {
            if (GenUtils.chance(random, 1, 10)) {
               PlantBuilder.build(grassBottom.getUp(), PlantBuilder.RED_MUSHROOM, PlantBuilder.BROWN_MUSHROOM);
            }

            BlockFace[] var10 = BlockUtils.directBlockFaces;
            int var11 = var10.length;

            for(int var12 = 0; var12 < var11; ++var12) {
               BlockFace face = var10[var12];
               if (TConfig.arePlantsEnabled() && target.getRelative(face).getType() == Material.AIR && GenUtils.chance(random, 1, 5)) {
                  target.getRelative(face).downLPillar(random, random.nextInt(8), Material.OAK_LEAVES);
               }
            }
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 6);
      SimpleLocation[] shrooms = trees;
      int var6 = trees.length;

      int var7;
      for(var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = shrooms[var7];
         int highestY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
         if (!BlockUtils.isWet(new SimpleBlock(data, sLoc.getX(), highestY + 1, sLoc.getZ()))) {
            sLoc = sLoc.getAtY(highestY);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
               (new FractalTreeBuilder(FractalTypes.Tree.NORMAL_SMALL)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }
         }
      }

      shrooms = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 10);
      SimpleLocation[] var14 = shrooms;
      var7 = shrooms.length;

      for(int var15 = 0; var15 < var7; ++var15) {
         SimpleLocation sLoc = var14[var15];
         SimpleBlock target = (new SimpleBlock(data, sLoc.getX(), 0, sLoc.getZ())).getGround();
         SimpleBlock underside = target.findAirPocket(30);
         if (underside != null && underside.getY() > TerraformGenerator.seaLevel) {
            SimpleBlock grassBottom = underside.findStonelikeFloor(50);
            if (grassBottom != null && grassBottom.getY() > TerraformGenerator.seaLevel && grassBottom.getType() == Material.GRASS_BLOCK) {
               sLoc = sLoc.getAtY(grassBottom.getY());
               FractalTypes.Mushroom var10000;
               switch(random.nextInt(6)) {
               case 0:
                  var10000 = FractalTypes.Mushroom.MEDIUM_RED_MUSHROOM;
                  break;
               case 1:
                  var10000 = FractalTypes.Mushroom.MEDIUM_BROWN_MUSHROOM;
                  break;
               case 2:
                  var10000 = FractalTypes.Mushroom.MEDIUM_BROWN_FUNNEL_MUSHROOM;
                  break;
               case 3:
                  var10000 = FractalTypes.Mushroom.SMALL_BROWN_MUSHROOM;
                  break;
               case 4:
                  var10000 = FractalTypes.Mushroom.SMALL_POINTY_RED_MUSHROOM;
                  break;
               default:
                  var10000 = FractalTypes.Mushroom.SMALL_RED_MUSHROOM;
               }

               FractalTypes.Mushroom type = var10000;
               (new MushroomBuilder(type)).build(tw, data, sLoc.getX(), sLoc.getY() + 1, sLoc.getZ());
            }
         }
      }

   }

   public BiomeHandler getTransformHandler() {
      return this;
   }

   public void transformTerrain(@NotNull ChunkCache cache, @NotNull TerraformWorld tw, @NotNull Random random, @NotNull ChunkData chunk, int x, int z, int chunkX, int chunkZ) {
      FastNoise platformNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_ARCHEDCLIFFS_PLATFORMNOISE, (world) -> {
         FastNoise n = new FastNoise(tw.getRand(12115222L).nextInt());
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.01F);
         return n;
      });
      FastNoise pillarNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_ARCHEDCLIFFS_PILLARNOISE, (world) -> {
         FastNoise n = new FastNoise(tw.getRand(12544422L).nextInt());
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(4);
         n.SetFrequency(0.01F);
         return n;
      });
      int rawX = chunkX * 16 + x;
      int rawZ = chunkZ * 16 + z;
      double preciseHeight = HeightMap.getPreciseHeight(tw, rawX, rawZ);
      int height = (int)preciseHeight;
      double platformNoiseVal = (double)Math.round(Math.max((double)(platformNoise.GetNoise((float)rawX, (float)rawZ) * 70.0F) * getBiomeBlender(tw).getEdgeFactor(BiomeBank.ARCHED_CLIFFS, rawX, rawZ), 0.0D));
      if (platformNoiseVal >= 1.0D) {
         int platformHeight = (int)(HeightMap.CORE.getHeight(tw, rawX, rawZ) - HeightMap.ATTRITION.getHeight(tw, rawX, rawZ) + 55.0D);
         cache.writeTransformedHeight(x, z, (short)platformHeight);
         chunk.setBlock(x, platformHeight, z, Material.GRASS_BLOCK);
         Material[] crust = this.getSurfaceCrust(random);

         int pillarNoiseVal;
         for(pillarNoiseVal = 0; (double)pillarNoiseVal < platformNoiseVal; ++pillarNoiseVal) {
            if (pillarNoiseVal < crust.length) {
               chunk.setBlock(x, platformHeight - pillarNoiseVal, z, crust[pillarNoiseVal]);
            } else {
               chunk.setBlock(x, platformHeight - pillarNoiseVal, z, Material.STONE);
            }
         }

         if (!(chunk instanceof DudChunkData) && platformNoiseVal > 6.0D) {
            pillarNoiseVal = (int)(platformNoiseVal / 10.0D * (0.1D + (double)Math.abs(pillarNoise.GetNoise((float)rawX, (float)rawZ))) * 20.0D);
            if (pillarNoiseVal + height > platformHeight) {
               pillarNoiseVal = platformHeight - height;
            }

            boolean applyCrust = !chunk.getType(x, height + pillarNoiseVal + 1, z).isSolid();

            for(int i = pillarNoiseVal; i >= 1; --i) {
               if (pillarNoiseVal - i < crust.length && applyCrust) {
                  chunk.setBlock(x, height + i, z, crust[pillarNoiseVal - i]);
               } else {
                  chunk.setBlock(x, height + i, z, Material.STONE);
               }
            }
         }
      }

   }

   public int getMaxHeightForCaves(@NotNull TerraformWorld tw, int x, int z) {
      return (int)HeightMap.CORE.getHeight(tw, x, z);
   }
}
