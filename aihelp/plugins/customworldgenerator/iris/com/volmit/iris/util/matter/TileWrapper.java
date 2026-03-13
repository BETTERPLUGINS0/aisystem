package com.volmit.iris.util.matter;

import com.volmit.iris.engine.object.TileData;
import lombok.Generated;

public class TileWrapper {
   private final TileData data;

   @Generated
   public TileWrapper(final TileData data) {
      this.data = var1;
   }

   @Generated
   public TileData getData() {
      return this.data;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof TileWrapper)) {
         return false;
      } else {
         TileWrapper var2 = (TileWrapper)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            TileData var3 = this.getData();
            TileData var4 = var2.getData();
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
      return var1 instanceof TileWrapper;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      TileData var3 = this.getData();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      return "TileWrapper(data=" + String.valueOf(this.getData()) + ")";
   }
}
