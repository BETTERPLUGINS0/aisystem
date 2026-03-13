package com.volmit.iris.util.matter;

import java.util.Map;
import lombok.Generated;

public class MatterStructurePOI {
   public static final MatterStructurePOI BURIED_TREASURE = new MatterStructurePOI("buried_treasure");
   private static final MatterStructurePOI UNKNOWN = new MatterStructurePOI("unknown");
   private static final Map<String, MatterStructurePOI> VALUES;
   private final String type;

   public static MatterStructurePOI get(String id) {
      MatterStructurePOI var1 = (MatterStructurePOI)VALUES.get(var0);
      return var1 != null ? var1 : new MatterStructurePOI(var0);
   }

   @Generated
   public MatterStructurePOI(final String type) {
      this.type = var1;
   }

   @Generated
   public String getType() {
      return this.type;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MatterStructurePOI)) {
         return false;
      } else {
         MatterStructurePOI var2 = (MatterStructurePOI)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else {
            String var3 = this.getType();
            String var4 = var2.getType();
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
      return var1 instanceof MatterStructurePOI;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      String var3 = this.getType();
      int var4 = var2 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      return "MatterStructurePOI(type=" + this.getType() + ")";
   }

   static {
      VALUES = Map.of("buried_treasure", BURIED_TREASURE);
   }
}
