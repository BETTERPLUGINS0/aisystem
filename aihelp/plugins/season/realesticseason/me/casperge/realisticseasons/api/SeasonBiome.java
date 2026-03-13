package me.casperge.realisticseasons.api;

import me.casperge.realisticseasons.season.Season;

public class SeasonBiome {
   private Season season;
   private String originalBiome;
   private String fogColor;
   private String waterColor;
   private String waterFogColor;
   private String skyColor;
   private String[] foliageColor;
   private String grassColor;

   public SeasonBiome(Season var1, String var2, String var3, String var4, String var5, String var6, String var7, String... var8) {
      this.season = var1;
      this.originalBiome = var2;
      this.fogColor = var3;
      this.waterColor = var4;
      this.waterFogColor = var5;
      this.skyColor = var6;
      this.foliageColor = var8;
      this.grassColor = var7;
   }

   public Season getSeason() {
      return this.season;
   }

   public String getOriginalBiome() {
      return this.originalBiome;
   }

   public String getFogColorHex() {
      return this.fogColor;
   }

   public String getWaterColoHex() {
      return this.waterColor;
   }

   public String getWaterFogColorHex() {
      return this.waterFogColor;
   }

   public String getSkyColorHex() {
      return this.skyColor;
   }

   public String[] getFoliageColorsHex() {
      return this.foliageColor;
   }

   public String getGrassColorHex() {
      return this.grassColor;
   }
}
