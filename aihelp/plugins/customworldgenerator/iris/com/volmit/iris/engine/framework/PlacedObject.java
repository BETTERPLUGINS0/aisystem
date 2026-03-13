package com.volmit.iris.engine.framework;

import com.volmit.iris.engine.object.IrisObject;
import com.volmit.iris.engine.object.IrisObjectPlacement;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;

public class PlacedObject {
   @Nullable
   private IrisObjectPlacement placement;
   @Nullable
   private IrisObject object;
   private int id;
   private int xx;
   private int zz;

   @Nullable
   @Generated
   public IrisObjectPlacement getPlacement() {
      return this.placement;
   }

   @Nullable
   @Generated
   public IrisObject getObject() {
      return this.object;
   }

   @Generated
   public int getId() {
      return this.id;
   }

   @Generated
   public int getXx() {
      return this.xx;
   }

   @Generated
   public int getZz() {
      return this.zz;
   }

   @Generated
   public void setPlacement(@Nullable final IrisObjectPlacement placement) {
      this.placement = var1;
   }

   @Generated
   public void setObject(@Nullable final IrisObject object) {
      this.object = var1;
   }

   @Generated
   public void setId(final int id) {
      this.id = var1;
   }

   @Generated
   public void setXx(final int xx) {
      this.xx = var1;
   }

   @Generated
   public void setZz(final int zz) {
      this.zz = var1;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof PlacedObject)) {
         return false;
      } else {
         PlacedObject var2 = (PlacedObject)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.getId() != var2.getId()) {
            return false;
         } else if (this.getXx() != var2.getXx()) {
            return false;
         } else if (this.getZz() != var2.getZz()) {
            return false;
         } else {
            IrisObjectPlacement var3 = this.getPlacement();
            IrisObjectPlacement var4 = var2.getPlacement();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            IrisObject var5 = this.getObject();
            IrisObject var6 = var2.getObject();
            if (var5 == null) {
               if (var6 != null) {
                  return false;
               }
            } else if (!var5.equals(var6)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof PlacedObject;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + this.getId();
      var5 = var5 * 59 + this.getXx();
      var5 = var5 * 59 + this.getZz();
      IrisObjectPlacement var3 = this.getPlacement();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      IrisObject var4 = this.getObject();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      String var10000 = String.valueOf(this.getPlacement());
      return "PlacedObject(placement=" + var10000 + ", object=" + String.valueOf(this.getObject()) + ", id=" + this.getId() + ", xx=" + this.getXx() + ", zz=" + this.getZz() + ")";
   }

   @Generated
   public PlacedObject(@Nullable final IrisObjectPlacement placement, @Nullable final IrisObject object, final int id, final int xx, final int zz) {
      this.placement = var1;
      this.object = var2;
      this.id = var3;
      this.xx = var4;
      this.zz = var5;
   }
}
