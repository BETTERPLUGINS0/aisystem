package com.volmit.iris.engine.object;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap.Builder;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.collection.KList;
import com.volmit.iris.util.math.RNG;
import lombok.Generated;

@Snippet("object-scale")
@Desc("Scale objects")
public class IrisObjectScale {
   private static ConcurrentLinkedHashMap<IrisObject, KList<IrisObject>> cache = (new Builder()).initialCapacity(64).maximumWeightedCapacity(1024L).concurrencyLevel(32).build();
   @MinNumber(1.0D)
   @MaxNumber(32.0D)
   @Desc("Iris Objects are scaled and cached to speed up placements. Because of this extra memory is used, so we evenly distribute variations across the defined scale range, then pick one randomly. If the differences is small, use a lower number. For more possibilities on the scale spectrum, increase this at the cost of memory.")
   private int variations = 7;
   @MinNumber(0.01D)
   @MaxNumber(50.0D)
   @Desc("The minimum scale")
   private double minimumScale = 1.0D;
   @MinNumber(0.01D)
   @MaxNumber(50.0D)
   @Desc("The maximum height for placement (top of object)")
   private double maximumScale = 1.0D;
   @Desc("If this object is scaled up beyond its origin size, specify a 3D interpolator")
   private IrisObjectPlacementScaleInterpolator interpolation;

   public boolean shouldScale() {
      return this.minimumScale == this.maximumScale && this.maximumScale == 1.0D || this.variations <= 0;
   }

   public int getMaxSizeFor(int indim) {
      return (int)(this.getMaxScale() * (double)var1);
   }

   public double getMaxScale() {
      double var1 = 0.0D;

      for(double var3 = this.minimumScale; var3 < this.maximumScale; var3 += (this.maximumScale - this.minimumScale) / (double)Math.min(this.variations, 32)) {
         var1 = var3;
      }

      return var1;
   }

   public IrisObject get(RNG rng, IrisObject origin) {
      return this.shouldScale() ? var2 : (IrisObject)((KList)cache.computeIfAbsent(var2, (var2x) -> {
         KList var3 = new KList();

         for(double var4 = this.minimumScale; var4 < this.maximumScale; var4 += (this.maximumScale - this.minimumScale) / (double)Math.min(this.variations, 32)) {
            var3.add((Object)var2.scaled(var4, this.getInterpolation()));
         }

         return var3;
      })).getRandom(var1);
   }

   public boolean canScaleBeyond() {
      return this.shouldScale() && this.maximumScale > 1.0D;
   }

   @Generated
   public IrisObjectScale() {
      this.interpolation = IrisObjectPlacementScaleInterpolator.NONE;
   }

   @Generated
   public IrisObjectScale(final int variations, final double minimumScale, final double maximumScale, final IrisObjectPlacementScaleInterpolator interpolation) {
      this.interpolation = IrisObjectPlacementScaleInterpolator.NONE;
      this.variations = var1;
      this.minimumScale = var2;
      this.maximumScale = var4;
      this.interpolation = var6;
   }

   @Generated
   public int getVariations() {
      return this.variations;
   }

   @Generated
   public double getMinimumScale() {
      return this.minimumScale;
   }

   @Generated
   public double getMaximumScale() {
      return this.maximumScale;
   }

   @Generated
   public IrisObjectPlacementScaleInterpolator getInterpolation() {
      return this.interpolation;
   }

   @Generated
   public IrisObjectScale setVariations(final int variations) {
      this.variations = var1;
      return this;
   }

   @Generated
   public IrisObjectScale setMinimumScale(final double minimumScale) {
      this.minimumScale = var1;
      return this;
   }

   @Generated
   public IrisObjectScale setMaximumScale(final double maximumScale) {
      this.maximumScale = var1;
      return this;
   }

   @Generated
   public IrisObjectScale setInterpolation(final IrisObjectPlacementScaleInterpolator interpolation) {
      this.interpolation = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisObjectScale)) {
         return false;
      } else {
         IrisObjectScale var2 = (IrisObjectScale)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getVariations() != var2.getVariations()) {
            return false;
         } else if (Double.compare(this.getMinimumScale(), var2.getMinimumScale()) != 0) {
            return false;
         } else if (Double.compare(this.getMaximumScale(), var2.getMaximumScale()) != 0) {
            return false;
         } else {
            IrisObjectPlacementScaleInterpolator var3 = this.getInterpolation();
            IrisObjectPlacementScaleInterpolator var4 = var2.getInterpolation();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisObjectScale;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var8 = var2 * 59 + this.getVariations();
      long var3 = Double.doubleToLongBits(this.getMinimumScale());
      var8 = var8 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getMaximumScale());
      var8 = var8 * 59 + (int)(var5 >>> 32 ^ var5);
      IrisObjectPlacementScaleInterpolator var7 = this.getInterpolation();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      int var10000 = this.getVariations();
      return "IrisObjectScale(variations=" + var10000 + ", minimumScale=" + this.getMinimumScale() + ", maximumScale=" + this.getMaximumScale() + ", interpolation=" + String.valueOf(this.getInterpolation()) + ")";
   }
}
