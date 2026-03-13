package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.M;
import com.volmit.iris.util.math.RNG;
import com.volmit.iris.util.stream.ProceduralStream;
import com.volmit.iris.util.stream.interpolation.Interpolated;
import lombok.Generated;

@Snippet("style-range")
@Desc("Represents a range styled with a custom generator")
public class IrisStyledRange {
   @Desc("The minimum value")
   private double min = 16.0D;
   @Desc("The maximum value")
   private double max = 32.0D;
   @Desc("The style to pick the range")
   private IrisGeneratorStyle style;

   public double get(RNG rng, double x, double z, IrisData data) {
      if (this.min == this.max) {
         return this.min;
      } else {
         return this.style.isFlat() ? M.lerp(this.min, this.max, 0.5D) : this.style.create(var1, var6).fitDouble(this.min, this.max, var2, var4);
      }
   }

   public ProceduralStream<Double> stream(RNG rng, IrisData data) {
      return ProceduralStream.of((var3, var4) -> {
         return this.get(var1, var3, var4, var2);
      }, Interpolated.DOUBLE);
   }

   public boolean isFlat() {
      return this.getMax() == this.getMin() || this.style.isFlat();
   }

   public int getMid() {
      return (int)((this.getMax() + this.getMin()) / 2.0D);
   }

   @Generated
   public IrisStyledRange() {
      this.style = new IrisGeneratorStyle(NoiseStyle.STATIC);
   }

   @Generated
   public IrisStyledRange(final double min, final double max, final IrisGeneratorStyle style) {
      this.style = new IrisGeneratorStyle(NoiseStyle.STATIC);
      this.min = var1;
      this.max = var3;
      this.style = var5;
   }

   @Generated
   public double getMin() {
      return this.min;
   }

   @Generated
   public double getMax() {
      return this.max;
   }

   @Generated
   public IrisGeneratorStyle getStyle() {
      return this.style;
   }

   @Generated
   public IrisStyledRange setMin(final double min) {
      this.min = var1;
      return this;
   }

   @Generated
   public IrisStyledRange setMax(final double max) {
      this.max = var1;
      return this;
   }

   @Generated
   public IrisStyledRange setStyle(final IrisGeneratorStyle style) {
      this.style = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisStyledRange)) {
         return false;
      } else {
         IrisStyledRange var2 = (IrisStyledRange)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getMin(), var2.getMin()) != 0) {
            return false;
         } else if (Double.compare(this.getMax(), var2.getMax()) != 0) {
            return false;
         } else {
            IrisGeneratorStyle var3 = this.getStyle();
            IrisGeneratorStyle var4 = var2.getStyle();
            if (var3 == null) {
               if (var4 == null) {
                  return true;
               }
            } else if (var3.equals(var4)) {
               return true;
            }

            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisStyledRange;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getMin());
      int var8 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getMax());
      var8 = var8 * 59 + (int)(var5 >>> 32 ^ var5);
      IrisGeneratorStyle var7 = this.getStyle();
      var8 = var8 * 59 + (var7 == null ? 43 : var7.hashCode());
      return var8;
   }

   @Generated
   public String toString() {
      double var10000 = this.getMin();
      return "IrisStyledRange(min=" + var10000 + ", max=" + this.getMax() + ", style=" + String.valueOf(this.getStyle()) + ")";
   }
}
