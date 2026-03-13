package org.terraform.biome.mountainous;

import java.lang.ref.SoftReference;
import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeBlender;
import org.terraform.biome.BiomeHandler;
import org.terraform.biome.BiomeSection;
import org.terraform.coregen.ChunkCache;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleBlock;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.small_items.PlantBuilder;
import org.terraform.tree.FractalTreeBuilder;
import org.terraform.tree.FractalTypes;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.blockdata.CommonMat;
import org.terraform.utils.version.V_1_19;

public class WindsweptHillsHandler extends AbstractMountainHandler {
   private static SoftReference<float[][]> SPIRAL;
   private static final int KERNEL_RAD = 2;
   private static final int KERNEL_WIDTH = 5;
   private static final int KERNEL_VOL = 25;

   private static void dirtStack(@NotNull PopulatorDataAbstract data, @NotNull Random rand, int x, int y, int z) {
      data.setType(x, y, z, Material.GRASS_BLOCK);
      if (GenUtils.chance(rand, 1, 10)) {
         PlantBuilder.GRASS.build(data, x, y + 1, z);
      }

      int depth = GenUtils.randInt(rand, 3, 7);

      for(int i = 1; i < depth && BlockUtils.isStoneLike(data.getType(x, y - i, z)); ++i) {
         data.setType(x, y - i, z, Material.TUFF);
         if (BlockUtils.isExposedToNonSolid(new SimpleBlock(data, x, y - i, z))) {
            ++depth;
         }
      }

   }

   @NotNull
   private static BiomeBlender getBiomeBlender(TerraformWorld tw) {
      return (new BiomeBlender(tw, true, true)).setRiverThreshold(4).setBlendBeaches(false);
   }

   public void transformTerrain(@NotNull ChunkCache cache, TerraformWorld tw, Random random, @NotNull ChunkData chunk, int x, int z, int chunkX, int chunkZ) {
      int rawX = (chunkX << 4) + x;
      int rawZ = (chunkZ << 4) + z;
      short height = cache.getTransformedHeight(x, z);
      float[][] spiralArr = calculateSpiral();
      BiomeSection sect = BiomeBank.getBiomeSectionFromBlockCoords(tw, rawX, rawZ);
      if (sect.equals(BiomeSection.getMostDominantSection(tw, rawX, rawZ))) {
         SimpleLocation bounds = sect.getLowerBounds();
         double sprVal = (double)spiralArr[rawX - bounds.x()][rawZ - bounds.z()];
         if (sprVal != 0.0D) {
            double depth = 3.0D + 5.0D * sect.getSectionRandom(74398).nextDouble();
            short newHeight = (short)((int)((double)height + depth * sprVal));
            chunk.setRegion(x, newHeight + 1, z, x + 1, height + 1, z + 1, CommonMat.AIR);
            chunk.setBlock(x, newHeight, z, V_1_19.MUD);
            cache.writeTransformedHeight(x, z, newHeight);
         }
      }
   }

   private static float[][] calculateSpiral() {
      if (SPIRAL != null) {
         float[][] arr = (float[][])SPIRAL.get();
         if (arr != null) {
            return arr;
         }
      }

      int radius = BiomeSection.sectionWidth >> 1;
      float[][] spiralArray = new float[BiomeSection.sectionWidth][BiomeSection.sectionWidth];
      float[][] blurredArray = new float[BiomeSection.sectionWidth][BiomeSection.sectionWidth];

      for(double t = 0.0D; t <= 12.566370614359172D; t += 0.05D) {
         double coeff = (double)BiomeSection.sectionWidth * 0.35D * t / 12.566370614359172D;
         int x = (int)(coeff * Math.sin(t));
         int z = (int)(coeff * Math.cos(t));

         assert x < BiomeSection.sectionWidth / 2;

         assert x >= -radius;

         assert z < BiomeSection.sectionWidth / 2;

         assert z >= -radius;

         spiralArray[x + radius][z + radius] = -10.0F;
      }

      int nz;
      int bz;
      int nx;
      byte agg;
      for(nx = 0; nx < spiralArray.length; ++nx) {
         for(nz = 0; nz < spiralArray[0].length; ++nz) {
            agg = 0;

            for(bz = Math.max(0, nx - 2); bz <= Math.min(spiralArray.length - 1, nx + 2); ++bz) {
               agg = (byte)((int)((float)agg + spiralArray[bz][nz]));
            }

            blurredArray[nx][nz] = (float)agg;
         }
      }

      for(nx = 0; nx < spiralArray.length; ++nx) {
         for(nz = 0; nz < spiralArray[0].length; ++nz) {
            agg = 0;

            for(bz = Math.max(0, nz - 2); bz <= Math.min(spiralArray[0].length - 1, nz + 2); ++bz) {
               agg = (byte)((int)((float)agg + spiralArray[nx][bz]));
            }

            blurredArray[nx][nz] += (float)agg;
            blurredArray[nx][nz] /= 25.0F;
         }
      }

      SPIRAL = new SoftReference(blurredArray);
      return blurredArray;
   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.WINDSWEPT_HILLS;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{(Material)GenUtils.randChoice(rand, Material.STONE, Material.STONE, Material.STONE, Material.STONE, Material.ANDESITE), (Material)GenUtils.randChoice(rand, Material.ANDESITE, Material.STONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.ANDESITE, Material.STONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.ANDESITE, Material.STONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.ANDESITE, Material.STONE, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if (surfaceY >= TerraformGenerator.seaLevel) {
         if (GenUtils.chance(random, 1, 25)) {
            for(int nx = -2; nx <= 2; ++nx) {
               for(int nz = -2; nz <= 2; ++nz) {
                  if (!GenUtils.chance(random, 1, 5)) {
                     surfaceY = GenUtils.getHighestGround(data, rawX + nx, rawZ + nz);
                     if (surfaceY < TerraformGenerator.seaLevel) {
                     }
                  }
               }
            }
         }

      }
   }

   public void populateLargeItems(@NotNull TerraformWorld tw, Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 14);
      SimpleLocation[] var5 = trees;
      int var6 = trees.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         if (HeightMap.getTrueHeightGradient(data, sLoc.getX(), sLoc.getZ(), 3) < 1.4D) {
            int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(treeY);
            if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome() && BlockUtils.isDirtLike(data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()))) {
               (new FractalTreeBuilder(FractalTypes.Tree.NORMAL_SMALL)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }
         }
      }

   }

   public BiomeHandler getTransformHandler() {
      return this;
   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.ROCKY_BEACH;
   }
}
