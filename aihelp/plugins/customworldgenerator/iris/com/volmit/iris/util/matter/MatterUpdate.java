package com.volmit.iris.util.matter;

import lombok.Generated;

public class MatterUpdate {
   private final boolean update;

   @Generated
   public boolean isUpdate() {
      return this.update;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MatterUpdate)) {
         return false;
      } else {
         MatterUpdate var2 = (MatterUpdate)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            return this.isUpdate() == var2.isUpdate();
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof MatterUpdate;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var3 = var2 * 59 + (this.isUpdate() ? 79 : 97);
      return var3;
   }

   @Generated
   public String toString() {
      return "MatterUpdate(update=" + this.isUpdate() + ")";
   }

   @Generated
   public MatterUpdate(final boolean update) {
      this.update = var1;
   }
}
