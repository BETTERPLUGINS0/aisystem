package com.volmit.iris.core.nms.container;

import lombok.Generated;

public class BlockPos {
   private int x;
   private int y;
   private int z;

   @Generated
   public int getX() {
      return this.x;
   }

   @Generated
   public int getY() {
      return this.y;
   }

   @Generated
   public int getZ() {
      return this.z;
   }

   @Generated
   public void setX(final int x) {
      this.x = var1;
   }

   @Generated
   public void setY(final int y) {
      this.y = var1;
   }

   @Generated
   public void setZ(final int z) {
      this.z = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof BlockPos)) {
         return false;
      } else {
         BlockPos var2 = (BlockPos)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getX() != var2.getX()) {
            return false;
         } else if (this.getY() != var2.getY()) {
            return false;
         } else {
            return this.getZ() == var2.getZ();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof BlockPos;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + this.getX();
      var3 = var3 * 59 + this.getY();
      var3 = var3 * 59 + this.getZ();
      return var3;
   }

   @Generated
   public String toString() {
      int var10000 = this.getX();
      return "BlockPos(x=" + var10000 + ", y=" + this.getY() + ", z=" + this.getZ() + ")";
   }

   @Generated
   public BlockPos(final int x, final int y, final int z) {
      this.x = var1;
      this.y = var2;
      this.z = var3;
   }

   @Generated
   public BlockPos() {
   }
}
