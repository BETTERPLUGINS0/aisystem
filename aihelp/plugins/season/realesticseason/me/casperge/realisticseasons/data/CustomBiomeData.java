package me.casperge.realisticseasons.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.casperge.realisticseasons.season.Season;

public class CustomBiomeData {
   private boolean modifyplants;
   private boolean freezeinwinter;
   private String relatedconf;
   public List<String> relatedbiomescustom = new ArrayList();
   private HashMap<Season, CustomBiomeData.SeasonColorData> colordata = new HashMap();
   private String originalBiomeName;
   private String s;
   private String biomeregname;
   private int temperatureModifier;
   public HashMap<Season, Integer> watertemps;
   public HashMap<Season, Boolean> doSnow = new HashMap();

   public void setS(String var1) {
      this.s = var1;
   }

   public void setDoSnow(Season var1, boolean var2) {
      this.doSnow.put(var1, var2);
   }

   public boolean isSnow(Season var1) {
      return (Boolean)this.doSnow.get(var1);
   }

   public void setOriginalBiomeName(String var1) {
      this.originalBiomeName = var1;
   }

   public void setBiomeRegName(String var1) {
      this.biomeregname = var1;
   }

   public String getBiomeRegName() {
      return this.biomeregname;
   }

   public void setModifyPlants(boolean var1) {
      this.modifyplants = var1;
   }

   public void setFreezeInWinter(boolean var1) {
      this.freezeinwinter = var1;
   }

   public void setRelatedConf(String var1) {
      this.relatedconf = var1;
   }

   public void addSeasonColorData(Season var1, CustomBiomeData.SeasonColorData var2) {
      this.colordata.put(var1, var2);
   }

   public boolean getModifyPlants() {
      return this.modifyplants;
   }

   public boolean getFreezeInWinter() {
      return this.freezeinwinter;
   }

   public String getRelatedConf() {
      return this.relatedconf;
   }

   public CustomBiomeData.SeasonColorData getSeasonColorData(Season var1) {
      return (CustomBiomeData.SeasonColorData)this.colordata.get(var1);
   }

   public String getS() {
      return this.s;
   }

   public String getOriginalBiomeName() {
      return this.originalBiomeName;
   }

   public int getTemperatureModifier() {
      return this.temperatureModifier;
   }

   public void setTemperatureModifier(int var1) {
      this.temperatureModifier = var1;
   }

   public void setBiomeWaterTemperatures(Season var1, int var2) {
      if (this.watertemps == null) {
         this.watertemps = new HashMap();
      }

      this.watertemps.put(var1, var2);
   }

   public HashMap<Season, Integer> getBiomeWaterTemps() {
      return this.watertemps;
   }

   public class SeasonColorData {
      private String skycolor;
      private String watercolor;
      private String waterfogcolor;
      private String grasscolor;
      private String treecolor;
      private String fogcolor;

      public String getSkyColor() {
         return this.skycolor;
      }

      public String getWaterColor() {
         return this.watercolor;
      }

      public String getWaterFogColor() {
         return this.waterfogcolor;
      }

      public String getGrassColor() {
         return this.grasscolor;
      }

      public String getTreeColor() {
         return this.treecolor;
      }

      public String getFogColor() {
         return this.fogcolor;
      }

      public void setSkyColor(String var1) {
         this.skycolor = var1;
      }

      public void setWaterColor(String var1) {
         this.watercolor = var1;
      }

      public void setWaterFogColor(String var1) {
         this.waterfogcolor = var1;
      }

      public void setGrassColor(String var1) {
         this.grasscolor = var1;
      }

      public void setTreeColor(String var1) {
         this.treecolor = var1;
      }

      public void setFogColor(String var1) {
         this.fogcolor = var1;
      }
   }
}
