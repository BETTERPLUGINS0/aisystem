package org.terraform.biome.mountainous;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeSection;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
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

public class PaintedHillsHandler extends AbstractMountainHandler {
   protected double getPeakMultiplier(@NotNull BiomeSection section, @NotNull Random sectionRandom) {
      return GenUtils.randDouble(sectionRandom, 1.05D, 1.1D);
   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.SAVANNA;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.ORANGE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.ORANGE_TERRACOTTA, Material.ORANGE_TERRACOTTA, (Material)GenUtils.randChoice(rand, Material.ORANGE_TERRACOTTA, Material.STONE), (Material)GenUtils.randChoice(rand, Material.ORANGE_TERRACOTTA, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld tw, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      this.correctDirt(new SimpleBlock(data, rawX, surfaceY, rawZ));
      FastNoise paintNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_PAINTEDHILLS_NOISE, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 4L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.03F);
         return n;
      });
      if (HeightMap.getTrueHeightGradient(data, rawX, rawZ, 3) < TConfig.c.MISC_TREES_GRADIENT_LIMIT) {
         data.setType(rawX, surfaceY, rawZ, Material.GRASS_BLOCK);
         if (random.nextBoolean()) {
            data.setType(rawX, surfaceY - 1, rawZ, Material.DIRT);
         }

         if (GenUtils.chance(random, 1, 30)) {
            PlantBuilder.GRASS.build(data, rawX, surfaceY + 1, rawZ);
            if (random.nextBoolean()) {
               PlantBuilder.TALL_GRASS.build(data, rawX, surfaceY + 1, rawZ);
            } else {
               PlantBuilder.DEAD_BUSH.build(data, rawX, surfaceY + 1, rawZ);
            }
         }
      }

      int terracottaDepth = 9;

      for(int i = 0; i < terracottaDepth; ++i) {
         if (data.getType(rawX, surfaceY - i, rawZ) == Material.ORANGE_TERRACOTTA) {
            double noise = (double)paintNoise.GetNoise((float)rawX, (float)(surfaceY - i), (float)rawZ);
            Material mat;
            if (noise > 0.3D) {
               mat = Material.RED_TERRACOTTA;
            } else if (noise > 0.0D) {
               mat = Material.CYAN_TERRACOTTA;
            } else if (noise > -0.3D) {
               mat = Material.LIGHT_BLUE_TERRACOTTA;
            } else {
               mat = Material.YELLOW_TERRACOTTA;
            }

            data.setType(rawX, surfaceY - i, rawZ, mat);
         }
      }

   }

   public void populateLargeItems(@NotNull TerraformWorld tw, Random random, @NotNull PopulatorDataAbstract data) {
      SimpleLocation[] trees = GenUtils.randomObjectPositions(tw, data.getChunkX(), data.getChunkZ(), 25);
      SimpleLocation[] var5 = trees;
      int var6 = trees.length;

      for(int var7 = 0; var7 < var6; ++var7) {
         SimpleLocation sLoc = var5[var7];
         if (data.getBiome(sLoc.getX(), sLoc.getZ()) == this.getBiome()) {
            int treeY = GenUtils.getHighestGround(data, sLoc.getX(), sLoc.getZ());
            sLoc = sLoc.getAtY(treeY);
            if (data.getType(sLoc.getX(), sLoc.getY(), sLoc.getZ()) == Material.GRASS_BLOCK) {
               (new FractalTreeBuilder(FractalTypes.Tree.SAVANNA_SMALL)).build(tw, data, sLoc.getX(), sLoc.getY(), sLoc.getZ());
            }
         }
      }

   }

   private void correctDirt(@NotNull SimpleBlock start) {
      for(int depth = 0; depth < 5; ++depth) {
         BlockFace[] var3 = BlockUtils.directBlockFaces;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            BlockFace face = var3[var5];
            if (start.getRelative(face).getType() == Material.ORANGE_TERRACOTTA) {
               start.setType(Material.ORANGE_TERRACOTTA);
               break;
            }
         }

         start = start.getDown();
      }

   }

   public double calculateHeight(@NotNull TerraformWorld tw, int x, int z) {
      double height = HeightMap.CORE.getHeight(tw, x, z);
      double maxMountainRadius = (double)BiomeSection.sectionWidth;
      height += HeightMap.ATTRITION.getHeight(tw, x, z);
      BiomeSection sect = BiomeBank.getBiomeSectionFromBlockCoords(tw, x, z);
      if (sect.getBiomeBank().getType() != BiomeType.MOUNTAINOUS) {
         sect = BiomeSection.getMostDominantSection(tw, x, z);
      }

      Random sectionRand = sect.getSectionRandom();
      double maxPeak = this.getPeakMultiplier(sect, sectionRand);
      SimpleLocation mountainPeak = sect.getCenter();
      double distFromPeak = 1.42D * maxMountainRadius - Math.sqrt(Math.pow((double)(x - mountainPeak.getX()), 2.0D) + Math.pow((double)(z - mountainPeak.getZ()), 2.0D));
      double heightMultiplier = maxPeak * (distFromPeak / maxMountainRadius);
      if (heightMultiplier < 1.0D) {
         heightMultiplier = 1.0D;
      }

      height *= heightMultiplier;
      FastNoise jaggedPeaksNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_PAINTEDHILLS_ROCKS_NOISE, (world) -> {
         FastNoise n = new FastNoise((int)(world.getSeed() * 2L));
         n.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
         n.SetFractalOctaves(5);
         n.SetFrequency(0.05F);
         return n;
      });
      double noise = (double)jaggedPeaksNoise.GetNoise((float)x, (float)z);
      if (noise > 0.3D) {
         height += Math.sqrt(noise) * 40.0D;
      }

      if (height > 200.0D) {
         height = 200.0D + (height - 200.0D) * 0.5D;
      }

      if (height > 230.0D) {
         height = 230.0D + (height - 230.0D) * 0.3D;
      }

      if (height > 240.0D) {
         height = 240.0D + (height - 240.0D) * 0.1D;
      }

      if (height > 250.0D) {
         height = 250.0D + (height - 250.0D) * 0.05D;
      }

      return height;
   }
}
