package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.util.function.NoiseProvider;
import com.volmit.iris.util.interpolation.InterpolationMethod;
import com.volmit.iris.util.interpolation.IrisInterpolation;
import java.util.Objects;
import lombok.Generated;

@Desc("Configures rotation for iris")
public class IrisInterpolator {
   public static final IrisInterpolator DEFAULT = new IrisInterpolator();
   @Required
   @Desc("The interpolation method when two biomes use different heights but this same generator")
   private InterpolationMethod function;
   @Required
   @MinNumber(1.0D)
   @MaxNumber(8192.0D)
   @Desc("The range checked horizontally. Smaller ranges yeild more detail but are not as smooth.")
   private double horizontalScale;

   public int hashCode() {
      return Objects.hash(new Object[]{this.horizontalScale, this.function});
   }

   public boolean equals(Object o) {
      if (!(var1 instanceof IrisInterpolator)) {
         return false;
      } else {
         IrisInterpolator var2 = (IrisInterpolator)var1;
         return var2.getFunction().equals(this.function) && var2.getHorizontalScale() == this.horizontalScale;
      }
   }

   public double interpolate(double x, double z, NoiseProvider provider) {
      return this.interpolate((int)Math.round(var1), (int)Math.round(var3), var5);
   }

   public double interpolate(int x, int z, NoiseProvider provider) {
      return IrisInterpolation.getNoise(this.getFunction(), var1, var2, this.getHorizontalScale(), var3);
   }

   @Generated
   public IrisInterpolator() {
      this.function = InterpolationMethod.BILINEAR_STARCAST_6;
      this.horizontalScale = 7.0D;
   }

   @Generated
   public IrisInterpolator(final InterpolationMethod function, final double horizontalScale) {
      this.function = InterpolationMethod.BILINEAR_STARCAST_6;
      this.horizontalScale = 7.0D;
      this.function = var1;
      this.horizontalScale = var2;
   }

   @Generated
   public InterpolationMethod getFunction() {
      return this.function;
   }

   @Generated
   public double getHorizontalScale() {
      return this.horizontalScale;
   }

   @Generated
   public IrisInterpolator setFunction(final InterpolationMethod function) {
      this.function = var1;
      return this;
   }

   @Generated
   public IrisInterpolator setHorizontalScale(final double horizontalScale) {
      this.horizontalScale = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getFunction());
      return "IrisInterpolator(function=" + var10000 + ", horizontalScale=" + this.getHorizontalScale() + ")";
   }
}
