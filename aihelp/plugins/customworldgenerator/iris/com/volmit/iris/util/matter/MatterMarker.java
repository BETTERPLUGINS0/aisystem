package com.volmit.iris.util.matter;

import lombok.Generated;

public class MatterMarker {
   private final String tag;

   @Generated
   public String getTag() {
      return this.tag;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MatterMarker)) {
         return false;
      } else {
         MatterMarker var2 = (MatterMarker)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            String var3 = this.getTag();
            String var4 = var2.getTag();
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
      return var1 instanceof MatterMarker;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      String var3 = this.getTag();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      return "MatterMarker(tag=" + this.getTag() + ")";
   }

   @Generated
   public MatterMarker(final String tag) {
      this.tag = var1;
   }
}
