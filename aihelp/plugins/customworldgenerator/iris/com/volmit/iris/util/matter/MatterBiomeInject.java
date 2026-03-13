package com.volmit.iris.util.matter;

import lombok.Generated;
import org.bukkit.block.Biome;

public class MatterBiomeInject {
   private final boolean custom;
   private final Integer biomeId;
   private final Biome biome;

   @Generated
   public boolean isCustom() {
      return this.custom;
   }

   @Generated
   public Integer getBiomeId() {
      return this.biomeId;
   }

   @Generated
   public Biome getBiome() {
      return this.biome;
   }

   @Generated
   public boolean equals(final Object o) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof MatterBiomeInject)) {
         return false;
      } else {
         MatterBiomeInject var2 = (MatterBiomeInject)var1;
         if (!var2.canEqual(this)) {
            return false;
         } else if (this.isCustom() != var2.isCustom()) {
            return false;
         } else {
            Integer var3 = this.getBiomeId();
            Integer var4 = var2.getBiomeId();
            if (var3 == null) {
               if (var4 != null) {
                  return false;
               }
            } else if (!var3.equals(var4)) {
               return false;
            }

            Biome var5 = this.getBiome();
            Biome var6 = var2.getBiome();
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
      return var1 instanceof MatterBiomeInject;
   }

   @Generated
   public int hashCode() {
      boolean var1 = true;
      byte var2 = 1;
      int var5 = var2 * 59 + (this.isCustom() ? 79 : 97);
      Integer var3 = this.getBiomeId();
      var5 = var5 * 59 + (var3 == null ? 43 : var3.hashCode());
      Biome var4 = this.getBiome();
      var5 = var5 * 59 + (var4 == null ? 43 : var4.hashCode());
      return var5;
   }

   @Generated
   public String toString() {
      boolean var10000 = this.isCustom();
      return "MatterBiomeInject(custom=" + var10000 + ", biomeId=" + this.getBiomeId() + ", biome=" + String.valueOf(this.getBiome()) + ")";
   }

   @Generated
   public MatterBiomeInject(final boolean custom, final Integer biomeId, final Biome biome) {
      this.custom = var1;
      this.biomeId = var2;
      this.biome = var3;
   }
}
