package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;

@Snippet("slope-clip")
@Desc("Translate objects")
public class IrisSlopeClip {
   @MinNumber(0.0D)
   @MaxNumber(1024.0D)
   @Desc("The minimum slope for placement")
   private double minimumSlope = 0.0D;
   @MinNumber(0.0D)
   @MaxNumber(1024.0D)
   @Desc("The maximum slope for placement")
   private double maximumSlope = 10.0D;

   public boolean isDefault() {
      return this.minimumSlope <= 0.0D && this.maximumSlope >= 10.0D;
   }

   public boolean isValid(double slope) {
      if (this.isDefault()) {
         return true;
      } else {
         return !(this.minimumSlope > var1) && !(this.maximumSlope < var1);
      }
   }

   @Generated
   public IrisSlopeClip() {
   }

   @Generated
   public IrisSlopeClip(final double minimumSlope, final double maximumSlope) {
      this.minimumSlope = var1;
      this.maximumSlope = var3;
   }

   @Generated
   public double getMinimumSlope() {
      return this.minimumSlope;
   }

   @Generated
   public double getMaximumSlope() {
      return this.maximumSlope;
   }

   @Generated
   public IrisSlopeClip setMinimumSlope(final double minimumSlope) {
      this.minimumSlope = var1;
      return this;
   }

   @Generated
   public IrisSlopeClip setMaximumSlope(final double maximumSlope) {
      this.maximumSlope = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisSlopeClip)) {
         return false;
      } else {
         IrisSlopeClip var2 = (IrisSlopeClip)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getMinimumSlope(), var2.getMinimumSlope()) != 0) {
            return false;
         } else {
            return Double.compare(this.getMaximumSlope(), var2.getMaximumSlope()) == 0;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisSlopeClip;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getMinimumSlope());
      int var7 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getMaximumSlope());
      var7 = var7 * 59 + (int)(var5 >>> 32 ^ var5);
      return var7;
   }

   @Generated
   public String toString() {
      double var10000 = this.getMinimumSlope();
      return "IrisSlopeClip(minimumSlope=" + var10000 + ", maximumSlope=" + this.getMaximumSlope() + ")";
   }
}
