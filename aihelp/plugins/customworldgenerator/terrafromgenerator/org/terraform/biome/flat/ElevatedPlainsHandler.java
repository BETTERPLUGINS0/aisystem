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
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.config.TConfig;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalLeaves;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public class ElevatedPlainsHandler extends BiomeHandler {
   private static final Material[] rocks;
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
      return Biome.PLAINS;
   }

   @NotNull
   public Material[] getSurfaceCrust(Random rand) {
      return new Material[]{Material.STONE};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      boolean gradient = HeightMap.getTrueHeightGradient(data, rawX, rawZ, 3) <= TConfig.c.MISC_TREES_GRADIENT_LIMIT;
      if (gradient) {
         data.setType(rawX, surfaceY, rawZ, Material.GRASS_BLOCK);
         if (random.nextBoolean()) {
            data.setType(rawX, surfaceY - 1, rawZ, Material.DIRT);
         }
      }

      if (data.getType(rawX, surfaceY, rawZ) == Material.GRASS_BLOCK && !BlockUtils.isWet(new SimpleBlock(data, rawX, surfaceY, rawZ)) && GenUtils.chance(random, 1, 10)) {
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

   public BiomeHandler getTransformHandler() {
      return this;
   }

   public void transformTerrain(@NotNull ChunkCache cache, TerraformWorld tw, @NotNull Random random, @NotNull ChunkData chunk, int x, int z, int chunkX, int chunkZ) {
      int heightFactor = 15;
      int rawX = chunkX * 16 + x;
      int rawZ = chunkZ * 16 + z;
      double preciseHeight = HeightMap.getPreciseHeight(tw, rawX, rawZ);
      int height = (int)preciseHeight;
      int noiseValue = (int)Math.round((double)heightFactor * getBiomeBlender(tw).getEdgeFactor(BiomeBank.ELEVATED_PLAINS, rawX, rawZ));
      if (noiseValue >= 1) {
         for(int y = 1; y <= noiseValue; ++y) {
            chunk.setBlock(x, height + y, z, this.getRockAt(random, x, y, z));
         }

         cache.writeTransformedHeight(x, z, (short)(height + noiseValue));
      }
   }

   @NotNull
   private Material getRockAt(@NotNull Random rand, int rawX, int y, int rawZ) {
      return rocks[(int)Math.round(0.7D * (double)rawX + 0.7D * (double)(GenUtils.randInt(rand, -1, 1) + y) + 0.7D * (double)rawZ) % rocks.length];
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, @NotNull Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 18);
      SimpleLocation[] var5 = trees;
      int var6 = trees.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome()) {
            int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(treeY);
            if (data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.GRASS_BLOCK) {
               FractalTreeBuilder builder = new FractalTreeBuilder(FractalTypes.Tree.TAIGA_SMALL);
               builder.setTrunkType(Material.OAK_LOG);
               builder.setFractalLeaves((new FractalLeaves()).setLeafNoiseFrequency(0.65F).setLeafNoiseMultiplier(0.8F).setRadius(2.0F).setMaterial(Material.OAK_LEAVES).setConeLeaves(true));
               if (builder.build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ())) {
                  BlockUtils.replaceCircularPatch(random.nextInt(99999), 2.5F, new SimpleBlock(data, sLoc), Material.PODZOL);
               }
            }
         }
      }

   }

   static {
      rocks = new Material[]{Material.GRANITE, Material.GRANITE, Material.GRANITE, Material.GRANITE, Material.GRANITE, Material.GRANITE, Material.DIORITE, Material.DIORITE, Material.DIORITE, Material.ANDESITE, Material.ANDESITE, Material.ANDESITE, Material.DIORITE, Material.DIORITE, Material.DIORITE};
   }
}
