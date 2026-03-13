package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;

@Snippet("stilt-settings")
@Desc("Defines stilting behaviour.")
public class IrisStiltSettings {
   @MinNumber(0.0D)
   @MaxNumber(64.0D)
   @Desc("Defines the maximum amount of blocks the object stilts verticially before overstilting and randomRange.")
   private int yMax;
   @MinNumber(0.0D)
   @MaxNumber(64.0D)
   @Desc("Defines the upper boundary for additional blocks after overstilting and/or maxStiltRange.")
   private int yRand;
   @MaxNumber(64.0D)
   @MinNumber(0.0D)
   @Desc("If the place mode is set to stilt, you can over-stilt it even further into the ground. Especially useful when using fast stilt due to inaccuracies.")
   private int overStilt;
   @Desc("If defined, stilting will be done using this block palette rather than the last layer of the object.")
   private IrisMaterialPalette palette;

   @Generated
   public IrisStiltSettings() {
   }

   @Generated
   public IrisStiltSettings(final int yMax, final int yRand, final int overStilt, final IrisMaterialPalette palette) {
      this.yMax = var1;
      this.yRand = var2;
      this.overStilt = var3;
      this.palette = var4;
   }

   @Generated
   public int getYMax() {
      return this.yMax;
   }

   @Generated
   public int getYRand() {
      return this.yRand;
   }

   @Generated
   public int getOverStilt() {
      return this.overStilt;
   }

   @Generated
   public IrisMaterialPalette getPalette() {
      return this.palette;
   }

   @Generated
   public IrisStiltSettings setYMax(final int yMax) {
      this.yMax = var1;
      return this;
   }

   @Generated
   public IrisStiltSettings setYRand(final int yRand) {
      this.yRand = var1;
      return this;
   }

   @Generated
   public IrisStiltSettings setOverStilt(final int overStilt) {
      this.overStilt = var1;
      return this;
   }

   @Generated
   public IrisStiltSettings setPalette(final IrisMaterialPalette palette) {
      this.palette = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisStiltSettings)) {
         return false;
      } else {
         IrisStiltSettings var2 = (IrisStiltSettings)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getYMax() != var2.getYMax()) {
            return false;
         } else if (this.getYRand() != var2.getYRand()) {
            return false;
         } else if (this.getOverStilt() != var2.getOverStilt()) {
            return false;
         } else {
            IrisMaterialPalette var3 = this.getPalette();
            IrisMaterialPalette var4 = var2.getPalette();
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
      return var1 instanceof IrisStiltSettings;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.getYMax();
      var4 = var4 * 59 + this.getYRand();
      var4 = var4 * 59 + this.getOverStilt();
      IrisMaterialPalette var3 = this.getPalette();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      int var10000 = this.getYMax();
      return "IrisStiltSettings(yMax=" + var10000 + ", yRand=" + this.getYRand() + ", overStilt=" + this.getOverStilt() + ", palette=" + String.valueOf(this.getPalette()) + ")";
   }
}
