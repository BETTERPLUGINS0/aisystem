package com.volmit.iris.util.matter;

import com.volmit.iris.util.collection.KList;
import lombok.Generated;

public class MatterEntityGroup {
   private final KList<MatterEntity> entities = new KList();

   @Generated
   public KList<MatterEntity> getEntities() {
      return this.entities;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MatterEntityGroup)) {
         return false;
      } else {
         MatterEntityGroup var2 = (MatterEntityGroup)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            KList var3 = this.getEntities();
            KList var4 = var2.getEntities();
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
      return var1 instanceof MatterEntityGroup;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      KList var3 = this.getEntities();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      return "MatterEntityGroup(entities=" + String.valueOf(this.getEntities()) + ")";
   }
}
