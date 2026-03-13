package org.terraform.biome;

import java.util.Collection;
import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import org.terraform.coregen.HeightMap;
import org.terraform.coregen.bukkit.TerraformGenerator;
import org.terraform.data.SimpleLocation;
import org.terraform.data.TerraformWorld;

public class BiomeBlender {
   final boolean blendBiomeGrid;
   final boolean blendWater;
   private final TerraformWorld tw;
   double gridBlendingFactor;
   int riverThreshold;
   boolean blendBeachesToo;
   int smoothBlendTowardsRivers;

   public BiomeBlender(TerraformWorld tw, boolean blendBiomeGrid, boolean blendWater) {
      this.gridBlendingFactor = 1.0D;
      this.riverThreshold = 5;
      this.blendBeachesToo = true;
      this.smoothBlendTowardsRivers = -1;
      this.tw = tw;
      this.blendBiomeGrid = blendBiomeGrid;
      this.blendWater = blendWater;
   }

   public BiomeBlender(TerraformWorld tw) {
      this(tw, true, true);
   }

   public double getEdgeFactor(BiomeBank currentBiome, int x, int z) {
      return this.getEdgeFactor(currentBiome, x, z, this.blendBeachesToo ? HeightMap.RIVER.getHeight(this.tw, x, z) : 0.0D);
   }

   public double getEdgeFactor(BiomeBank currentBiome, int x, int z, double riverDepth) {
      double factor = 1.0D;
      double riverFactor;
      if (this.blendWater) {
         if (this.smoothBlendTowardsRivers == -1) {
            riverFactor = this.blendBeachesToo ? riverDepth / (double)(-this.riverThreshold) : (HeightMap.getPreciseHeight(this.tw, x, z) - (double)TerraformGenerator.seaLevel) / (double)this.riverThreshold;
         } else {
            double height = HeightMap.getPreciseHeight(this.tw, x, z);
            if (height > (double)(TerraformGenerator.seaLevel + this.smoothBlendTowardsRivers)) {
               riverFactor = 1.0D;
            } else if (height <= (double)TerraformGenerator.seaLevel) {
               riverFactor = 0.0D;
            } else {
               riverFactor = (height - (double)TerraformGenerator.seaLevel) / (double)this.smoothBlendTowardsRivers;
            }
         }

         if (riverFactor < factor) {
            factor = Math.max(0.0D, riverFactor);
         }
      }

      if (this.blendBiomeGrid) {
         riverFactor = this.getGridEdgeFactor(currentBiome, this.tw, x, z);
         if (riverFactor < factor) {
            factor = riverFactor;
         }
      }

      return factor;
   }

   public double getGridEdgeFactor(BiomeBank currentBiome, TerraformWorld tw, int x, int z) {
      SimpleLocation target = new SimpleLocation(x, 0, z);
      Collection<BiomeSection> sections = BiomeSection.getSurroundingSections(tw, 3, x, z);
      BiomeSection mostDominantTarget = null;
      double dominance = -100.0D;
      Iterator var10 = sections.iterator();

      while(var10.hasNext()) {
         BiomeSection section = (BiomeSection)var10.next();
         if (section.getBiomeBank() == currentBiome) {
            double dom = (double)section.getDominance(target);
            if (dom > dominance) {
               mostDominantTarget = section;
               dominance = dom;
            }
         }
      }

      if (mostDominantTarget == null) {
         return 0.0D;
      } else {
         double factor = 1.0D;
         Iterator var18 = sections.iterator();

         while(var18.hasNext()) {
            BiomeSection section = (BiomeSection)var18.next();
            if (section.getBiomeBank() != currentBiome) {
               float dom = section.getDominance(target);
               double diff = Math.max(0.0D, dominance - (double)dom);
               factor = Math.min(factor, diff * this.gridBlendingFactor);
            }
         }

         return Math.min(factor, 1.0D);
      }
   }

   @NotNull
   public BiomeBlender setGridBlendingFactor(double gridBlendingFactor) {
      this.gridBlendingFactor = gridBlendingFactor;
      return this;
   }

   @NotNull
   public BiomeBlender setRiverThreshold(int riverThreshold) {
      this.riverThreshold = riverThreshold;
      return this;
   }

   @NotNull
   public BiomeBlender setSmoothBlendTowardsRivers(int smoothBlendTowardsRivers) {
      this.smoothBlendTowardsRivers = smoothBlendTowardsRivers;
      return this;
   }

   @NotNull
   public BiomeBlender setBlendBeaches(boolean blendBeachesToo) {
      this.blendBeachesToo = blendBeachesToo;
      return this;
   }
}
