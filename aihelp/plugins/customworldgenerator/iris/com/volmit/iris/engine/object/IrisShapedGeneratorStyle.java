package com.volmit.iris.engine.object;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.RNG;
import lombok.Generated;

@Snippet("shaped-style")
@Desc("This represents a generator with a min and max height")
public class IrisShapedGeneratorStyle {
   @Required
   @Desc("The generator id")
   private IrisGeneratorStyle generator;
   @Required
   @MinNumber(-2032.0D)
   @MaxNumber(2032.0D)
   @Desc("The min block value")
   private int min;
   @Required
   @MinNumber(-2032.0D)
   @MaxNumber(2032.0D)
   @Desc("The max block value")
   private int max;

   public IrisShapedGeneratorStyle(NoiseStyle style, int min, int max) {
      this(var1);
      this.min = var2;
      this.max = var3;
   }

   public IrisShapedGeneratorStyle(NoiseStyle style) {
      this.generator = new IrisGeneratorStyle(NoiseStyle.IRIS);
      this.min = 0;
      this.max = 0;
      this.generator = new IrisGeneratorStyle(var1);
   }

   public double get(RNG rng, IrisData data, double... dim) {
      return this.generator.create(var1, var2).fitDouble((double)this.min, (double)this.max, var3);
   }

   public boolean isFlat() {
      return this.min == this.max || this.getGenerator().isFlat();
   }

   public int getMid() {
      return (this.getMax() + this.getMin()) / 2;
   }

   @Generated
   public IrisShapedGeneratorStyle() {
      this.generator = new IrisGeneratorStyle(NoiseStyle.IRIS);
      this.min = 0;
      this.max = 0;
   }

   @Generated
   public IrisShapedGeneratorStyle(final IrisGeneratorStyle generator, final int min, final int max) {
      this.generator = new IrisGeneratorStyle(NoiseStyle.IRIS);
      this.min = 0;
      this.max = 0;
      this.generator = var1;
      this.min = var2;
      this.max = var3;
   }

   @Generated
   public IrisGeneratorStyle getGenerator() {
      return this.generator;
   }

   @Generated
   public int getMin() {
      return this.min;
   }

   @Generated
   public int getMax() {
      return this.max;
   }

   @Generated
   public IrisShapedGeneratorStyle setGenerator(final IrisGeneratorStyle generator) {
      this.generator = var1;
      return this;
   }

   @Generated
   public IrisShapedGeneratorStyle setMin(final int min) {
      this.min = var1;
      return this;
   }

   @Generated
   public IrisShapedGeneratorStyle setMax(final int max) {
      this.max = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisShapedGeneratorStyle)) {
         return false;
      } else {
         IrisShapedGeneratorStyle var2 = (IrisShapedGeneratorStyle)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getMin() != var2.getMin()) {
            return false;
         } else if (this.getMax() != var2.getMax()) {
            return false;
         } else {
            IrisGeneratorStyle var3 = this.getGenerator();
            IrisGeneratorStyle var4 = var2.getGenerator();
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
      return var1 instanceof IrisShapedGeneratorStyle;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + this.getMin();
      var4 = var4 * 59 + this.getMax();
      IrisGeneratorStyle var3 = this.getGenerator();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getGenerator());
      return "IrisShapedGeneratorStyle(generator=" + var10000 + ", min=" + this.getMin() + ", max=" + this.getMax() + ")";
   }
}
