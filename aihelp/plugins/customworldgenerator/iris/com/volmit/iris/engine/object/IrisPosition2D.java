package com.volmit.iris.engine.object;

import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.engine.object.annotations.Snippet;
import lombok.Generated;

@Snippet("position-2d")
@Desc("Represents a position")
public class IrisPosition2D {
   @Desc("The x position")
   private int x = 0;
   @Desc("The z position")
   private int z = 0;

   @Generated
   public IrisPosition2D() {
   }

   @Generated
   public IrisPosition2D(final int x, final int z) {
      this.x = var1;
      this.z = var2;
   }

   @Generated
   public int getX() {
      return this.x;
   }

   @Generated
   public int getZ() {
      return this.z;
   }

   @Generated
   public IrisPosition2D setX(final int x) {
      this.x = var1;
      return this;
   }

   @Generated
   public IrisPosition2D setZ(final int z) {
      this.z = var1;
      return this;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisPosition2D)) {
         return false;
      } else {
         IrisPosition2D var2 = (IrisPosition2D)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getX() != var2.getX()) {
            return false;
         } else {
            return this.getZ() == var2.getZ();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisPosition2D;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getX();
      var3 = var3 * 59 + this.getZ();
      return var3;
   }

   @Generated
   public String toString() {
      int var10000 = this.getX();
      return "IrisPosition2D(x=" + var10000 + ", z=" + this.getZ() + ")";
   }
}
