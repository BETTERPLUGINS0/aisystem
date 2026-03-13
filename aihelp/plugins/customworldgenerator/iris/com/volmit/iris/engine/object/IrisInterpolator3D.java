package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.function.NoiseProvider3;
import com.volmit.iris.util.interpolation.InterpolationMethod3D;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import lombok.Generated;

@Snippet("interpolator-3d")
@Desc("Configures interpolatin in 3D")
public class IrisInterpolator3D {
   @Required
   @Desc("The interpolation method when two biomes use different heights but this same generator")
   private InterpolationMethod3D function;
   @Required
   @MinNumber(1.0D)
   @MaxNumber(8192.0D)
   @Desc("The range checked in all dimensions. Smaller ranges yeild more detail but are not as smooth.")
   private double scale;

   public double interpolate(double x, double y, double z, NoiseProvider3 provider) {
      return this.interpolate((int)Math.round(var1), (int)Math.round(var3), (int)Math.round(var5), var7);
   }

   public double interpolate(int x, int y, int z, NoiseProvider3 provider) {
      return IrisInterpolation.getNoise3D(this.getFunction(), var1, var2, var3, this.getScale(), var4);
   }

   @Generated
   public IrisInterpolator3D() {
      this.function = InterpolationMethod3D.TRILINEAR;
      this.scale = 4.0D;
   }

   @Generated
   public IrisInterpolator3D(final InterpolationMethod3D function, final double scale) {
      this.function = InterpolationMethod3D.TRILINEAR;
      this.scale = 4.0D;
      this.function = var1;
      this.scale = var2;
   }

   @Generated
   public InterpolationMethod3D getFunction() {
      return this.function;
   }

   @Generated
   public double getScale() {
      return this.scale;
   }

   @Generated
   public IrisInterpolator3D setFunction(final InterpolationMethod3D function) {
      this.function = var1;
      return this;
   }

   @Generated
   public IrisInterpolator3D setScale(final double scale) {
      this.scale = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisInterpolator3D)) {
         return false;
      } else {
         IrisInterpolator3D var2 = (IrisInterpolator3D)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getScale(), var2.getScale()) != 0) {
            return false;
         } else {
            InterpolationMethod3D var3 = this.getFunction();
            InterpolationMethod3D var4 = var2.getFunction();
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
      return var1 instanceof IrisInterpolator3D;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getScale());
      int var6 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      InterpolationMethod3D var5 = this.getFunction();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getFunction());
      return "IrisInterpolator3D(function=" + var10000 + ", scale=" + this.getScale() + ")";
   }
}
