package org.terraform.biome.mountainous;

import java.util.Random;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeSection;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.populatordata.PopulatorDataAbstract;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;
import org.terraform.utils.noise.FastNoise;
import org.terraform.utils.noise.NoiseCacheHandler;

public class BadlandsCanyonHandler extends AbstractMountainHandler {
   public static void oneUnit(@NotNull Random random, @NotNull PopulatorDataAbstract data, int x, int z, boolean force) {
      int highest = GenUtils.getHighestGround(data, x, z);
      int threshold = 65;
      if (force) {
         threshold = highest - GenUtils.randInt(random, 3, 6);
      }

      for(int y = highest; y > threshold; --y) {
         if ((data.getBiome(x, z) == Biome.ERODED_BADLANDS || force || data.getBiome(x, z) != Biome.DESERT) && (data.getType(x, y, z) == Material.RED_SANDSTONE || data.getType(x, y, z) == Material.SANDSTONE || data.getType(x, y, z) == Material.RED_SAND || data.getType(x, y, z) == Material.SAND || data.getType(x, y, z) == Material.STONE)) {
            data.setType(x, y, z, BlockUtils.getTerracotta(y));
         }
      }

   }

   public boolean isOcean() {
      return false;
   }

   @NotNull
   public Biome getBiome() {
      return Biome.ERODED_BADLANDS;
   }

   @NotNull
   public Material[] getSurfaceCrust(@NotNull Random rand) {
      return new Material[]{Material.RED_SAND, Material.RED_SAND, Material.RED_SAND, Material.RED_SAND, Material.RED_SAND, Material.RED_SAND, (Material)GenUtils.randChoice(rand, Material.RED_SANDSTONE, Material.RED_SAND), (Material)GenUtils.randChoice(rand, Material.RED_SANDSTONE, Material.RED_SAND), Material.RED_SANDSTONE, Material.RED_SANDSTONE, Material.RED_SANDSTONE, Material.RED_SANDSTONE, Material.RED_SANDSTONE, Material.RED_SANDSTONE, Material.RED_SANDSTONE, Material.RED_SANDSTONE, Material.RED_SANDSTONE, Material.RED_SANDSTONE, Material.RED_SANDSTONE, (Material)GenUtils.randChoice(rand, Material.RED_SANDSTONE, Material.STONE), (Material)GenUtils.randChoice(rand, Material.RED_SANDSTONE, Material.STONE)};
   }

   public void populateSmallItems(TerraformWorld world, @NotNull Random random, int rawX, int surfaceY, int rawZ, @NotNull PopulatorDataAbstract data) {
      if ((double)surfaceY > 10.0D + HeightMap.CORE.getHeight(world, rawX, rawZ)) {
         oneUnit(random, data, rawX, rawZ, false);
      }

      if (HeightMap.getTrueHeightGradient(data, rawX, rawZ, 2) < 2.0D) {
         data.setType(rawX, surfaceY, rawZ, Material.RED_SAND);
      }

   }

   public void populateLargeItems(TerraformWorld tw, Random random, PopulatorDataAbstract data) {
   }

   @NotNull
   public BiomeBank getBeachType() {
      return BiomeBank.BADLANDS_BEACH;
   }

   public double calculateHeight(@NotNull TerraformWorld tw, int x, int z) {
      double baseHeight = HeightMap.CORE.getHeight(tw, x, z);
      FastNoise duneNoise = NoiseCacheHandler.getNoise(tw, NoiseCacheHandler.NoiseCacheEntry.BIOME_BADLANDS_CANYON_NOISE, (world) -> {
         FastNoise n = new FastNoise((int)world.getSeed());
         n.SetNoiseType(FastNoise.NoiseType.Simplex);
         n.SetFractalOctaves(3);
         n.SetFrequency(0.02F);
         return n;
      });
      double noise = (double)duneNoise.GetNoise((float)x, (float)z);
      if (noise < 0.0D) {
         noise = 0.0D;
      }

      double height = HeightMap.CORE.getHeight(tw, x, z);
      double maxMountainRadius = (double)BiomeSection.sectionWidth;
      height += noise * 20.0D;
      BiomeSection sect = BiomeBank.getBiomeSectionFromBlockCoords(tw, x, z);
      if (sect.getBiomeBank() != BiomeBank.BADLANDS_CANYON || sect.getBiomeBank() != BiomeBank.BADLANDS_CANYON_PEAK) {
         sect = BiomeSection.getMostDominantSection(tw, x, z);
      }

      Random sectionRand = sect.getSectionRandom();
      double maxPeak = this.getPeakMultiplier(sect, sectionRand);
      SimpleLocation mountainPeak = sect.getCenter();
      double angleFromPeak = (double)(new SimpleLocation(x, 0, z)).twoDAngleTo(mountainPeak);
      double circleFuzz = 1.32D + (double)Math.abs(duneNoise.GetValue((float)(10.0D * angleFromPeak), (float)('鹇' * mountainPeak.getX() + 75721 * mountainPeak.getZ())));
      double distFromPeak = circleFuzz * maxMountainRadius - Math.sqrt(Math.pow((double)(x - mountainPeak.getX()), 2.0D) + Math.pow((double)(z - mountainPeak.getZ()), 2.0D));
      double heightMultiplier = maxPeak * (distFromPeak / maxMountainRadius);
      double minMultiplier = 1.0D;
      if (heightMultiplier < minMultiplier) {
         heightMultiplier = minMultiplier;
      }

      height *= heightMultiplier;
      if (height > 75.0D) {
         if (height < 80.0D) {
            height = 80.0D;
         }

         if (height < 90.0D) {
            height = 90.0D;
         } else if (height < 105.0D) {
            height = 105.0D;
         } else if (height < 120.0D) {
            height = 120.0D;
         } else {
            height = 135.0D;
         }
      } else {
         height = baseHeight;
      }

      return height;
   }

   protected double getPeakMultiplier(@NotNull BiomeSection section, @NotNull Random sectionRandom) {
      return super.getPeakMultiplier(section, sectionRandom) * 0.9D;
   }
}
