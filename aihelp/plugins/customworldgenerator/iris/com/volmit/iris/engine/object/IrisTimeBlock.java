package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;
import org.bukkit.World;

@Snippet("time-block")
@Desc("Represents a time of day (24h time, not 12h am/pm). Set both to the same number for any time. If they are both set to -1, it will always be not allowed.")
public class IrisTimeBlock {
   @Desc("The beginning hour. Set both to the same number for any time. If they are both set to -1, it will always be not allowed.")
   private double startHour = 0.0D;
   @Desc("The ending hour. Set both to the same number for any time. If they are both set to -1, it will always be not allowed.")
   private double endHour = 0.0D;

   public boolean isWithin(World world) {
      return this.isWithin(((double)var1.getTime() / 1000.0D + 6.0D) % 24.0D);
   }

   public boolean isWithin(double hour) {
      if (this.startHour == this.endHour) {
         return this.endHour != -1.0D;
      } else if (this.startHour > this.endHour) {
         return var1 >= this.startHour || var1 <= this.endHour;
      } else {
         return var1 >= this.startHour && var1 <= this.endHour;
      }
   }

   @Generated
   public double getStartHour() {
      return this.startHour;
   }

   @Generated
   public double getEndHour() {
      return this.endHour;
   }

   @Generated
   public void setStartHour(final double startHour) {
      this.startHour = var1;
   }

   @Generated
   public void setEndHour(final double endHour) {
      this.endHour = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisTimeBlock)) {
         return false;
      } else {
         IrisTimeBlock var2 = (IrisTimeBlock)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getStartHour(), var2.getStartHour()) != 0) {
            return false;
         } else {
            return Double.compare(this.getEndHour(), var2.getEndHour()) == 0;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisTimeBlock;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getStartHour());
      int var7 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getEndHour());
      var7 = var7 * 59 + (int)(var5 >>> 32 ^ var5);
      return var7;
   }

   @Generated
   public String toString() {
      double var10000 = this.getStartHour();
      return "IrisTimeBlock(startHour=" + var10000 + ", endHour=" + this.getEndHour() + ")";
   }
}
