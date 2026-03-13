package com.volmit.iris.engine.object;

import lombok.Generated;

public final class CarveResult {
   private final int surface;
   private final int ceiling;

   public int getHeight() {
      return this.ceiling - this.surface;
   }

   @Generated
   public CarveResult(final int surface, final int ceiling) {
      this.surface = var1;
      this.ceiling = var2;
   }

   @Generated
   public int getSurface() {
      return this.surface;
   }

   @Generated
   public int getCeiling() {
      return this.ceiling;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof CarveResult)) {
         return false;
      } else {
         CarveResult var2 = (CarveResult)var1;
         if (this.getSurface() != var2.getSurface()) {
            return false;
         } else {
            return this.getCeiling() == var2.getCeiling();
         }
      }
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getSurface();
      var3 = var3 * 59 + this.getCeiling();
      return var3;
   }

   @Generated
   public String toString() {
      int var10000 = this.getSurface();
      return "CarveResult(surface=" + var10000 + ", ceiling=" + this.getCeiling() + ")";
   }
}
