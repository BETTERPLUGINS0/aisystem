package com.volmit.iris.util.matter;

import lombok.Generated;

public class MatterFluidBody {
   private final boolean body;
   private final String customBiome;
   private final boolean lava;

   @Generated
   public boolean isBody() {
      return this.body;
   }

   @Generated
   public String getCustomBiome() {
      return this.customBiome;
   }

   @Generated
   public boolean isLava() {
      return this.lava;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MatterFluidBody)) {
         return false;
      } else {
         MatterFluidBody var2 = (MatterFluidBody)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isBody() != var2.isBody()) {
            return false;
         } else if (this.isLava() != var2.isLava()) {
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
      return var1 instanceof MatterFluidBody;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var4 = var2 * 59 + (this.isBody() ? 79 : 97);
      var4 = var4 * 59 + (this.isLava() ? 79 : 97);
      String var3 = this.getCustomBiome();
      var4 = var4 * 59 + (var3 == null ? 43 : var3.hashCode());
      return var4;
   }

   @Generated
   public String toString() {
      boolean var10000 = this.isBody();
      return "MatterFluidBody(body=" + var10000 + ", customBiome=" + this.getCustomBiome() + ", lava=" + this.isLava() + ")";
   }

   @Generated
   public MatterFluidBody(final boolean body, final String customBiome, final boolean lava) {
      this.body = var1;
      this.customBiome = var2;
      this.lava = var3;
   }
}
