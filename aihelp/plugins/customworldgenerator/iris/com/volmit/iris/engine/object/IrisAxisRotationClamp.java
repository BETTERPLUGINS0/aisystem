package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.DependsOn;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.MaxNumber;
import com.volmit.iris.engine.object.annotations.MinNumber;
import com.volmit.iris.engine.object.annotations.Required;
import com.volmit.iris.engine.object.annotations.Snippet;
import com.volmit.iris.util.math.M;
import lombok.Generated;

@Snippet("axis-rotation")
@Desc("Represents a rotation axis with intervals and maxes. The x and z axis values are defaulted to disabled. The Y axis defaults to on, rotating by 90 degree increments.")
public class IrisAxisRotationClamp {
   @Desc("Should this axis be rotated at all?")
   private boolean enabled = false;
   private transient boolean forceLock = false;
   @Required
   @DependsOn({"max"})
   @MinNumber(-360.0D)
   @MaxNumber(360.0D)
   @Desc("The minimum angle (from) or set this and max to zero for any angle degrees. Set both to the same non-zero value to force it to that angle only")
   private double min = 0.0D;
   @Required
   @DependsOn({"min"})
   @MinNumber(-360.0D)
   @MaxNumber(360.0D)
   @Desc("The maximum angle (to) or set this and min to zero for any angle degrees. Set both to the same non-zero value to force it to that angle only")
   private double max = 0.0D;
   @Required
   @DependsOn({"min", "max"})
   @MinNumber(0.0D)
   @MaxNumber(360.0D)
   @Desc("Iris spins the axis but not freely. For example an interval of 90 would mean 4 possible angles (right angles) degrees. \nSetting this to 0 means totally free rotation.\n\nNote that a lot of structures can have issues with non 90 degree intervals because the minecraft block resolution is so low.")
   private double interval = 0.0D;

   public void minMax(double fd) {
      this.min = var1;
      this.max = var1;
      this.forceLock = true;
   }

   public boolean isUnlimited() {
      return this.min == this.max && this.min == 0.0D;
   }

   public boolean isLocked() {
      return this.min == this.max && !this.isUnlimited();
   }

   public double getRadians(int rng) {
      if (this.forceLock) {
         return Math.toRadians(Math.ceil(Math.abs(this.max % 360.0D)));
      } else if (this.isUnlimited()) {
         if (this.interval < 1.0D) {
            this.interval = 1.0D;
         }

         return Math.toRadians(this.interval * Math.ceil(Math.abs((double)var1 % 360.0D / this.interval)) % 360.0D);
      } else {
         return this.min == this.max && this.min != 0.0D ? Math.toRadians(this.max) : Math.toRadians((Double)M.clip(this.interval * Math.ceil(Math.abs((double)var1 % 360.0D / this.interval)) % 360.0D, Math.min(this.min, this.max), Math.max(this.min, this.max)));
      }
   }

   @Generated
   public IrisAxisRotationClamp(final boolean enabled, final boolean forceLock, final double min, final double max, final double interval) {
      this.enabled = var1;
      this.forceLock = var2;
      this.min = var3;
      this.max = var5;
      this.interval = var7;
   }

   @Generated
   public IrisAxisRotationClamp() {
   }

   @Generated
   public boolean isEnabled() {
      return this.enabled;
   }

   @Generated
   public boolean isForceLock() {
      return this.forceLock;
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
   public double getInterval() {
      return this.interval;
   }

   @Generated
   public IrisAxisRotationClamp setEnabled(final boolean enabled) {
      this.enabled = var1;
      return this;
   }

   @Generated
   public IrisAxisRotationClamp setForceLock(final boolean forceLock) {
      this.forceLock = var1;
      return this;
   }

   @Generated
   public IrisAxisRotationClamp setMin(final double min) {
      this.min = var1;
      return this;
   }

   @Generated
   public IrisAxisRotationClamp setMax(final double max) {
      this.max = var1;
      return this;
   }

   @Generated
   public IrisAxisRotationClamp setInterval(final double interval) {
      this.interval = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisAxisRotationClamp)) {
         return false;
      } else {
         IrisAxisRotationClamp var2 = (IrisAxisRotationClamp)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isEnabled() != var2.isEnabled()) {
            return false;
         } else if (Double.compare(this.getMin(), var2.getMin()) != 0) {
            return false;
         } else if (Double.compare(this.getMax(), var2.getMax()) != 0) {
            return false;
         } else {
            return Double.compare(this.getInterval(), var2.getInterval()) == 0;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisAxisRotationClamp;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var9 = var2 * 59 + (this.isEnabled() ? 79 : 97);
      long var3 = Double.doubleToLongBits(this.getMin());
      var9 = var9 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getMax());
      var9 = var9 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getInterval());
      var9 = var9 * 59 + (int)(var7 >>> 32 ^ var7);
      return var9;
   }

   @Generated
   public String toString() {
      boolean var10000 = this.isEnabled();
      return "IrisAxisRotationClamp(enabled=" + var10000 + ", forceLock=" + this.isForceLock() + ", min=" + this.getMin() + ", max=" + this.getMax() + ", interval=" + this.getInterval() + ")";
   }
}
