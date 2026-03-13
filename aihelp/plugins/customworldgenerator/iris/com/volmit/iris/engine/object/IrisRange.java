package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.RNG;
import lombok.Generated;

@Snippet("range")
@Desc("Represents a range")
public class IrisRange {
   @Desc("The minimum value")
   private double min = 16.0D;
   @Desc("The maximum value")
   private double max = 32.0D;

   public double get(RNG rng) {
      return this.min == this.max ? this.min : var1.d(this.min, this.max);
   }

   public boolean contains(int v) {
      return (double)var1 >= this.min && (double)var1 <= this.max;
   }

   public IrisRange merge(IrisRange other) {
      this.min = Math.min(this.min, var1.min);
      this.max = Math.max(this.max, var1.max);
      return this;
   }

   @Generated
   public IrisRange() {
   }

   @Generated
   public IrisRange(final double min, final double max) {
      this.min = var1;
      this.max = var3;
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
   public IrisRange setMin(final double min) {
      this.min = var1;
      return this;
   }

   @Generated
   public IrisRange setMax(final double max) {
      this.max = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisRange)) {
         return false;
      } else {
         IrisRange var2 = (IrisRange)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getMin(), var2.getMin()) != 0) {
            return false;
         } else {
            return Double.compare(this.getMax(), var2.getMax()) == 0;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisRange;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getMin());
      int var7 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getMax());
      var7 = var7 * 59 + (int)(var5 >>> 32 ^ var5);
      return var7;
   }

   @Generated
   public String toString() {
      double var10000 = this.getMin();
      return "IrisRange(min=" + var10000 + ", max=" + this.getMax() + ")";
   }
}
