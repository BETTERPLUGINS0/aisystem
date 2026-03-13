package org.terraform.biome.mountainous;

import java.util.Random;
import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.NotNull;
import org.terraform.biome.BiomeBank;
import org.terraform.biome.BiomeHandler;
import org.terraform.biome.BiomeSection;
import org.terraform.biome.BiomeType;
import org.terraform.coregen.HeightMap;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;
import org.terraform.main.TerraformGeneratorPlugin;
import org.terraform.main.config.TConfig;
import org.terraform.utils.BlockUtils;
import org.terraform.utils.GenUtils;

public abstract class AbstractMountainHandler extends BiomeHandler {
   protected double getPeakMultiplier(@NotNull BiomeSection section, @NotNull Random sectionRandom) {
      boolean surroundedByMountains = true;
      BlockFace[] var4 = BlockUtils.directBlockFaces;
      int var5 = var4.length;
      int var6 = 0;

      while(var6 < var5) {
         BlockFace face = var4[var6];
         switch(section.getRelative(face.getModX(), face.getModZ()).getBiomeBank().getType()) {
         default:
            surroundedByMountains = false;
         case MOUNTAINOUS:
         case HIGH_MOUNTAINOUS:
            ++var6;
         }
      }

      double lowerBound;
      double upperBound;
      if (surroundedByMountains) {
         lowerBound = (double)TConfig.c.BIOME_MOUNTAINOUS_BESIDE_MOUNT_PEAK_MIN;
         upperBound = (double)TConfig.c.BIOME_MOUNTAINOUS_BESIDE_MOUNT_PEAK_MAX;
      } else {
         lowerBound = (double)TConfig.c.BIOME_MOUNTAINOUS_BESIDE_NORMAL_PEAK_MIN;
         upperBound = (double)TConfig.c.BIOME_MOUNTAINOUS_BESIDE_NORMAL_PEAK_MAX;
      }

      return GenUtils.randDouble(sectionRandom, lowerBound, upperBound);
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
      switch(sect.getRelative(sect.getSubSection(x, z)).getBiomeBank().getType()) {
      case MOUNTAINOUS:
      case HIGH_MOUNTAINOUS:
         heightMultiplier = Math.max(heightMultiplier, (double)TConfig.c.BIOME_MOUNTAINOUS_CONNECTOR_MOUNT_MULT);
         break;
      case OCEANIC:
      case DEEP_OCEANIC:
         heightMultiplier = Math.min(heightMultiplier, (double)TConfig.c.BIOME_MOUNTAINOUS_CONNECTOR_OCEAN_MULT);
      }

      height *= heightMultiplier;
      height = this.limitSigmoid(height, (double)(TerraformGeneratorPlugin.injector.getMaxY() - 20));
      return height;
   }

   private double limitSigmoid(double inputY, double limit) {
      return inputY <= limit ? inputY : limit + (inputY - limit) * Math.pow(1.0D + Math.exp(inputY - limit - 6.0D), -1.0D);
   }
}
