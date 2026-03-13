package org.terraform.biome.flat;

import java.util.EnumSet;
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
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class PetrifiedCliffsHandler extends BiomeHandler {
   public static final EnumSet<Material> endWithStones;
   static BiomeBlender biomeBlender;

   @NotNull
   private static BiomeBlender getBiomeBlender(TerraformWorld tw) {
      if (biomeBlender == null) {
         biomeBlender = (new BiomeBlender(tw, true, true)).setRiverThreshold(4).setBlendBeaches(false);
      }

      return biomeBlender;
   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.BIRCH_FOREST;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.GRASS_BLOCK, Material.DIRT, Material.DIRT, (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE), (Material)GenUtils.randChoice(rand, Material.DIRT, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      for(int i = 0; i < 30 && (data.getType(rawX, surfaceY, rawZ) == Material.DIORITE || data.getType(rawX, surfaceY, rawZ) == Material.ANDESITE || data.getType(rawX, surfaceY, rawZ) == Material.GRANITE || data.getType(rawX, surfaceY, rawZ) == Material.POLISHED_DIORITE || data.getType(rawX, surfaceY, rawZ) == Material.POLISHED_ANDESITE || data.getType(rawX, surfaceY, rawZ) == Material.POLISHED_GRANITE); ++i) {
         --surfaceY;
      }

      if (data.getType(rawX, surfaceY, rawZ) == Material.GRASS_BLOCK) {
         SimpleBlock core = new SimpleBlock(data, rawX, surfaceY + 1, rawZ);
         boolean continueOut = false;
         BlockFace[] var9 = BlockUtils.directBlockFaces;
         int var10 = var9.length;

         for(int var11 = 0; var11 < var10; ++var11) {
            BlockFace face = var9[var11];
            Material relType = core.getRelative(face).getType();
            if (endWithStones.contains(relType)) {
               core.setType(Material.DIORITE_SLAB);
               continueOut = true;
               break;
            }
         }

         if (continueOut) {
            return;
         }

         if (GenUtils.chance(random, 1, 10)) {
            if (GenUtils.chance(random, 6, 10)) {
               PlantBuilder.GRASS.build(data, rawX, surfaceY + 1, rawZ);
               if (random.nextBoolean()) {
                  PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
               }
            } else if (GenUtils.chance(random, 7, 10)) {
               BlockUtils.pickFlower().build(data, rawX, surfaceY + 1, rawZ);
            } else {
               BlockUtils.pickTallFlower().build(data, rawX, surfaceY + 1, rawZ);
            }
         }
      }

   }

   public BiomeHandler getTransformHandler() {
      return this;
   }

   public int getMaxHeightForCaves(@NotNull TerraformWorld tw, int x, int z) {
      return (int)HeightMap.CORE.getHeight(tw, x, z);
   }

   public void transformTerrain(@NotNull ChunkCache cache, @NotNull TerraformWorld tw, Random random, @NotNull ChunkData chunk, int x, int z, int chunkX, int chunkZ) {
      FastNoise noise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_PETRIFIEDCLIFFS_CLIFFNOISE, (world) -> {
         FastNoise n = new FastNoise(tw.getHashedRand(123L, 2222, 1111).nextInt(99999));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.03F);
         return n;
      });
      FastNoise details = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_PETRIFIEDCLIFFS_INNERNOISE, (world) -> {
         FastNoise n = new FastNoise(tw.getHashedRand(111L, 102, 1).nextInt(99999));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.05F);
         return n;
      });
      int rawX = chunkX * 16 + x;
      int rawZ = chunkZ * 16 + z;
      double preciseHeight = HeightMap.getPreciseHeight(tw, rawX, rawZ);
      int height = (int)preciseHeight;
      double noiseValue = (double)Math.max(0.0F, noise.GetNoise((float)rawX, (float)rawZ)) * getBiomeBlender(tw).getEdgeFactor(BiomeBank.PETRIFIED_CLIFFS, rawX, rawZ);
      if (noiseValue != 0.0D) {
         double platformHeight = 7.0D + noiseValue * 50.0D;
         if (platformHeight > 15.0D) {
            platformHeight = 15.0D + Math.sqrt(0.5D * (platformHeight - 15.0D));
         }

         for(int y = 1; y <= (int)Math.round(platformHeight); ++y) {
            double detailsNoiseMultiplier = Math.pow(1.0D - 1.0D / Math.pow(platformHeight / 2.0D, 2.0D) * Math.pow((double)y - platformHeight / 2.0D, 2.0D), 2.0D);
            double detailsNoise = (double)details.GetNoise((float)rawX, (float)(height + y), (float)rawZ);
            if (0.85D + detailsNoise > detailsNoiseMultiplier) {
               chunk.setBlock(x, height + y, z, (Material)GenUtils.randChoice((Object[])(Material.STONE, Material.STONE, Material.STONE, Material.COBBLESTONE, Material.MOSSY_COBBLESTONE)));
               cache.writeTransformedHeight(x, z, (short)Math.max(cache.getTransformedHeight(x, z), height + y));
            }
         }

      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 13, 0.2F);
      SimpleLocation[] var5 = trees;
      int var6 = trees.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         if (random.nextBoolean()) {
            int treeY = GenUtils.getTrueHighestBlock(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(treeY);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()).toString().endsWith("STONE")) {
               FractalTypes.Tree var10000;
               switch(random.nextInt(3)) {
               case 0:
                  var10000 = FractalTypes.Tree.ANDESITE_PETRIFIED_SMALL;
                  break;
               case 1:
                  var10000 = FractalTypes.Tree.GRANITE_PETRIFIED_SMALL;
                  break;
               default:
                  var10000 = FractalTypes.Tree.DIORITE_PETRIFIED_SMALL;
               }

               FractalTypes.Tree treeType = var10000;
               (new FractalTreeBuilder(treeType)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }
         }
      }

   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.ROCKY_BEACH;
   }

   static {
      endWithStones = EnumSet.of(Material.STONE, Material.MOSSY_COBBLESTONE, Material.COBBLESTONE);
   }
}
