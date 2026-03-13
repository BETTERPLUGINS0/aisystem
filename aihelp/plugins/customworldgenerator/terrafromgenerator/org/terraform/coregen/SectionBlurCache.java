package org.terraform.coregen;

import java.util.HashMap;
import org.terraform.biome.BiomeSection;
import org.terraform.data.CoordPair;

public record SectionBlurCache(BiomeSection sect, float[][] intermediate, float[][] blurred) {
   public SectionBlurCache(BiomeSection sect, float[][] intermediate, float[][] blurred) {
      this.sect = sect;
      this.intermediate = intermediate;
      this.blurred = blurred;
   }

   public void fillCache() {
      HashMap<CoordPair, Float> dominantBiomeHeights = new HashMap();

      int relX;
      int relZ;
      int arrIdX;
      int arrIdZ;
      float lineTotalHeight;
      int offsetZ;
      for(relX = this.sect.getLowerBounds().getX() - 5; relX <= this.sect.getUpperBounds().getX() + 5; ++relX) {
         for(relZ = this.sect.getLowerBounds().getZ() - 5; relZ <= this.sect.getUpperBounds().getZ() + 5; ++relZ) {
            arrIdX = relX - (this.sect.getLowerBounds().getX() - 5);
            arrIdZ = relZ - (this.sect.getLowerBounds().getZ() - 5);
            lineTotalHeight = 0.0F;

            for(offsetZ = -5; offsetZ <= 5; ++offsetZ) {
               lineTotalHeight += HeightMap.getDominantBiomeHeight(this.sect.getTw(), relX + offsetZ, relZ, dominantBiomeHeights);
            }

            this.intermediate[arrIdX][arrIdZ] = lineTotalHeight;
         }
      }

      for(relX = this.sect.getLowerBounds().getX(); relX <= this.sect.getUpperBounds().getX(); ++relX) {
         for(relZ = this.sect.getLowerBounds().getZ(); relZ <= this.sect.getUpperBounds().getZ(); ++relZ) {
            arrIdX = relX - (this.sect.getLowerBounds().getX() - 5);
            arrIdZ = relZ - (this.sect.getLowerBounds().getZ() - 5);
            lineTotalHeight = 0.0F;

            for(offsetZ = -5; offsetZ <= 5; ++offsetZ) {
               int querIdZ = relZ + offsetZ - (this.sect.getLowerBounds().getZ() - 5);
               lineTotalHeight += this.intermediate[arrIdX][querIdZ];
            }

            this.blurred[arrIdX][arrIdZ] = lineTotalHeight / 121.0F;
         }
      }

   }

   public float getBlurredHeight(int blockX, int blockZ) {
      int arrIdX = blockX - (this.sect.getLowerBounds().getX() - 5);
      int arrIdZ = blockZ - (this.sect.getLowerBounds().getZ() - 5);
      return this.blurred[arrIdX][arrIdZ];
   }

   public int hashCode() {
      return this.sect.hashCode();
   }

   public boolean equals(Object o) {
      if (o instanceof SectionBlurCache) {
         SectionBlurCache s = (SectionBlurCache)o;
         return s.sect.equals(this.sect);
      } else {
         return false;
      }
   }

   public BiomeSection sect() {
      return this.sect;
   }

   public float[][] intermediate() {
      return this.intermediate;
   }

   public float[][] blurred() {
      return this.blurred;
   }
}
