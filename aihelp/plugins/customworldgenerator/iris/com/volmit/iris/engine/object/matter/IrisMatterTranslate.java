package com.volmit.iris.engine.object.matter;

import com.volmit.iris.core.loader.IrisData;
import com.volmit.iris.engine.object.IrisStyledRange;
import com.volmit.iris.engine.object.annotations.Desc;
import com.volmit.iris.util.math.RNG;
import lombok.Generated;

@Desc("Represents a matter translator")
public class IrisMatterTranslate {
   @Desc("For varied coordinate shifts use ranges not the literal coordinate")
   private IrisStyledRange rangeX = null;
   @Desc("For varied coordinate shifts use ranges not the literal coordinate")
   private IrisStyledRange rangeY = null;
   @Desc("For varied coordinate shifts use ranges not the literal coordinate")
   private IrisStyledRange rangeZ = null;
   @Desc("Define an absolute shift instead of varied.")
   private int x = 0;
   @Desc("Define an absolute shift instead of varied.")
   private int y = 0;
   @Desc("Define an absolute shift instead of varied.")
   private int z = 0;

   public int xOffset(IrisData data, RNG rng, int rx, int rz) {
      return this.rangeX != null ? (int)Math.round(this.rangeX.get(var2, (double)var3, (double)var4, var1)) : this.x;
   }

   public int yOffset(IrisData data, RNG rng, int rx, int rz) {
      return this.rangeY != null ? (int)Math.round(this.rangeY.get(var2, (double)var3, (double)var4, var1)) : this.y;
   }

   public int zOffset(IrisData data, RNG rng, int rx, int rz) {
      return this.rangeZ != null ? (int)Math.round(this.rangeZ.get(var2, (double)var3, (double)var4, var1)) : this.z;
   }

   @Generated
   public IrisStyledRange getRangeX() {
      return this.rangeX;
   }

   @Generated
   public IrisStyledRange getRangeY() {
      return this.rangeY;
   }

   @Generated
   public IrisStyledRange getRangeZ() {
      return this.rangeZ;
   }

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
   public IrisMatterTranslate setRangeX(final IrisStyledRange rangeX) {
      this.rangeX = var1;
      return this;
   }

   @Generated
   public IrisMatterTranslate setRangeY(final IrisStyledRange rangeY) {
      this.rangeY = var1;
      return this;
   }

   @Generated
   public IrisMatterTranslate setRangeZ(final IrisStyledRange rangeZ) {
      this.rangeZ = var1;
      return this;
   }

   @Generated
   public IrisMatterTranslate setX(final int x) {
      this.x = var1;
      return this;
   }

   @Generated
   public IrisMatterTranslate setY(final int y) {
      this.y = var1;
      return this;
   }

   @Generated
   public IrisMatterTranslate setZ(final int z) {
      this.z = var1;
      return this;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getRangeX());
      return "IrisMatterTranslate(rangeX=" + var10000 + ", rangeY=" + String.valueOf(this.getRangeY()) + ", rangeZ=" + String.valueOf(this.getRangeZ()) + ", x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ")";
   }

   @Generated
   public IrisMatterTranslate() {
   }

   @Generated
   public IrisMatterTranslate(final IrisStyledRange rangeX, final IrisStyledRange rangeY, final IrisStyledRange rangeZ, final int x, final int y, final int z) {
      this.rangeX = var1;
      this.rangeY = var2;
      this.rangeZ = var3;
      this.x = var4;
      this.y = var5;
      this.z = var6;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof IrisMatterTranslate)) {
         return false;
      } else {
         IrisMatterTranslate var2 = (IrisMatterTranslate)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getX() != var2.getX()) {
            return false;
         } else if (this.getY() != var2.getY()) {
            return false;
         } else if (this.getZ() != var2.getZ()) {
            return false;
         } else {
            label54: {
               IrisStyledRange var3 = this.getRangeX();
               IrisStyledRange var4 = var2.getRangeX();
               if (var3 == null) {
                  if (var4 == null) {
                     break label54;
                  }
               } else if (var3.equals(var4)) {
                  break label54;
               }

               return false;
            }

            IrisStyledRange var5 = this.getRangeY();
            IrisStyledRange var6 = var2.getRangeY();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            IrisStyledRange var7 = this.getRangeZ();
            IrisStyledRange var8 = var2.getRangeZ();
            if (var7 == null) {
               if (var8 != null) {
                  return false;
               }
            } else if (!var7.equals(var8)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof IrisMatterTranslate;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var6 = var2 * 59 + this.getX();
      var6 = var6 * 59 + this.getY();
      var6 = var6 * 59 + this.getZ();
      IrisStyledRange var3 = this.getRangeX();
      var6 = var6 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisStyledRange var4 = this.getRangeY();
      var6 = var6 * 59 + (var4 == null ? 43 : var4.hashCode());
      IrisStyledRange var5 = this.getRangeZ();
      var6 = var6 * 59 + (var5 == null ? 43 : var5.hashCode());
      return var6;
   }
}
