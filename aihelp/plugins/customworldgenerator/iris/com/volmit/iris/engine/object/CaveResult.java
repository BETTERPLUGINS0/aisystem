package com.volmit.iris.engine.object;

import lombok.Generated;

public class CaveResult {
   private int floor;
   private int ceiling;

   public CaveResult(int floor, int ceiling) {
      this.floor = var1;
      this.ceiling = var2;
   }

   public boolean isWithin(int v) {
      return var1 > this.floor || var1 < this.ceiling;
   }

   @Generated
   public int getFloor() {
      return this.floor;
   }

   @Generated
   public int getCeiling() {
      return this.ceiling;
   }

   @Generated
   public void setFloor(final int floor) {
      this.floor = var1;
   }

   @Generated
   public void setCeiling(final int ceiling) {
      this.ceiling = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof CaveResult)) {
         return false;
      } else {
         CaveResult var2 = (CaveResult)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getFloor() != var2.getFloor()) {
            return false;
         } else {
            return this.getCeiling() == var2.getCeiling();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof CaveResult;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getFloor();
      var3 = var3 * 59 + this.getCeiling();
      return var3;
   }

   @Generated
   public String toString() {
      int var10000 = this.getFloor();
      return "CaveResult(floor=" + var10000 + ", ceiling=" + this.getCeiling() + ")";
   }
}
