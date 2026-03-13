package com.volmit.iris.util.matter;

import com.volmit.iris.util.nbt.tag.CompoundTag;
import lombok.Generated;

public class MatterEntity {
   private final double xOff;
   private final double yOff;
   private final double zOff;
   private final CompoundTag entityData;

   @Generated
   public double getXOff() {
      return this.xOff;
   }

   @Generated
   public double getYOff() {
      return this.yOff;
   }

   @Generated
   public double getZOff() {
      return this.zOff;
   }

   @Generated
   public CompoundTag getEntityData() {
      return this.entityData;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MatterEntity)) {
         return false;
      } else {
         MatterEntity var2 = (MatterEntity)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (Double.compare(this.getXOff(), var2.getXOff()) != 0) {
            return false;
         } else if (Double.compare(this.getYOff(), var2.getYOff()) != 0) {
            return false;
         } else if (Double.compare(this.getZOff(), var2.getZOff()) != 0) {
            return false;
         } else {
            CompoundTag var3 = this.getEntityData();
            CompoundTag var4 = var2.getEntityData();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof MatterEntity;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      long var3 = Double.doubleToLongBits(this.getXOff());
      int var10 = var2 * 59 + (int)(var3 >>> 32 ^ var3);
      long var5 = Double.doubleToLongBits(this.getYOff());
      var10 = var10 * 59 + (int)(var5 >>> 32 ^ var5);
      long var7 = Double.doubleToLongBits(this.getZOff());
      var10 = var10 * 59 + (int)(var7 >>> 32 ^ var7);
      CompoundTag var9 = this.getEntityData();
      var10 = var10 * 59 + (var9 == null ? 43 : var9.hashCode());
      return var10;
   }

   @Generated
   public String toString() {
      double var10000 = this.getXOff();
      return "MatterEntity(xOff=" + var10000 + ", yOff=" + this.getYOff() + ", zOff=" + this.getZOff() + ", entityData=" + String.valueOf(this.getEntityData()) + ")";
   }

   @Generated
   public MatterEntity(final double xOff, final double yOff, final double zOff, final CompoundTag entityData) {
      this.xOff = var1;
      this.yOff = var3;
      this.zOff = var5;
      this.entityData = var7;
   }
}
