package com.volmit.iris.util.matter;

import lombok.Generated;

public class MatterCavern {
   private final boolean cavern;
   private final String customBiome;
   private final byte liquid;

   public boolean isAir() {
      return this.liquid == 0;
   }

   public boolean isWater() {
      return this.liquid == 1;
   }

   public boolean isLava() {
      return this.liquid == 2;
   }

   @Generated
   public boolean isCavern() {
      return this.cavern;
   }

   @Generated
   public String getCustomBiome() {
      return this.customBiome;
   }

   @Generated
   public byte getLiquid() {
      return this.liquid;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MatterCavern)) {
         return false;
      } else {
         MatterCavern var2 = (MatterCavern)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isCavern() != var2.isCavern()) {
            return false;
         } else if (this.getLiquid() != var2.getLiquid()) {
            return false;
         } else {
            String var3 = this.getCustomBiome();
            String var4 = var2.getCustomBiome();
            if (var3 == null) {
               if (var4 == null) {
                  return true;
               }
            } else if (var3.equals(var4)) {
               return true;
            }

            return false;
         }
      }
   }

   @Generated
   protected boolean canEqual(final Object other) {
      return var1 instanceof MatterCavern;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + (this.isCavern() ? 79 : 97);
      var4 = var4 * 59 + this.getLiquid();
      String var3 = this.getCustomBiome();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      boolean var10000 = this.isCavern();
      return "MatterCavern(cavern=" + var10000 + ", customBiome=" + this.getCustomBiome() + ", liquid=" + this.getLiquid() + ")";
   }

   @Generated
   public MatterCavern(final boolean cavern, final String customBiome, final byte liquid) {
      this.cavern = var1;
      this.customBiome = var2;
      this.liquid = var3;
   }
}
