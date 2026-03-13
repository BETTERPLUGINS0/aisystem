package me.casperge.realisticseasons.crop;

import java.util.HashMap;
import java.util.Iterator;
import me.casperge.realisticseasons.season.Season;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public class CropSettings {
   private boolean enabled;
   private HashMap<Material, CropSettings.CropConfig> cropconfigs;

   public CropSettings(YamlConfiguration var1) {
      ConfigurationSection var2 = var1.getConfigurationSection("seasonal-growth-speed");
      this.enabled = var2.getBoolean("enabled");
      this.cropconfigs = new HashMap();
      Iterator var3 = var2.getKeys(false).iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         if (!var4.equals("enabled")) {
            CropSettings.CropConfig var5 = new CropSettings.CropConfig(var1.getConfigurationSection("seasonal-growth-speed." + var4));
            Material var6 = Material.valueOf(var4);
            this.cropconfigs.put(var6, var5);
         }
      }

   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public double getOutdoorSpeed(Material var1, Season var2) {
      if (!this.cropconfigs.containsKey(var1)) {
         return 1.0D;
      } else {
         CropSettings.CropConfig var3 = (CropSettings.CropConfig)this.cropconfigs.get(var1);
         return !var3.enabled ? 1.0D : var3.getOutdoorSpeed(var2);
      }
   }

   public double getIndoorSpeed(Material var1, Season var2) {
      if (!this.cropconfigs.containsKey(var1)) {
         return 1.0D;
      } else {
         CropSettings.CropConfig var3 = (CropSettings.CropConfig)this.cropconfigs.get(var1);
         return !var3.enabled ? 1.0D : var3.getIndoorSpeed(var2);
      }
   }

   public boolean requiresBlockLight(Material var1, Season var2) {
      if (!this.cropconfigs.containsKey(var1)) {
         return false;
      } else {
         CropSettings.CropConfig var3 = (CropSettings.CropConfig)this.cropconfigs.get(var1);
         return !var3.enabled ? false : var3.requiresBlockLight(var2);
      }
   }

   public int getMinLightLevel(Material var1, Season var2) {
      if (!this.cropconfigs.containsKey(var1)) {
         return 9;
      } else {
         CropSettings.CropConfig var3 = (CropSettings.CropConfig)this.cropconfigs.get(var1);
         return !var3.enabled ? 9 : var3.getMinLightLevel(var2);
      }
   }

   class CropConfig {
      boolean enabled;
      HashMap<Season, CropSettings.SeasonCrop> seasoncrops;

      private CropConfig(ConfigurationSection param2) {
         this.enabled = var2.getBoolean("enabled");
         this.seasoncrops = new HashMap();
         Iterator var3 = var2.getConfigurationSection("seasons").getKeys(false).iterator();

         while(var3.hasNext()) {
            String var4 = (String)var3.next();
            Season var5 = Season.valueOf(var4);
            CropSettings.SeasonCrop var6 = CropSettings.this.new SeasonCrop(var2.getConfigurationSection("seasons." + var4));
            this.seasoncrops.put(var5, var6);
         }

      }

      public double getOutdoorSpeed(Season var1) {
         return ((CropSettings.SeasonCrop)this.seasoncrops.get(var1)).outdoorSpeed;
      }

      public double getIndoorSpeed(Season var1) {
         return ((CropSettings.SeasonCrop)this.seasoncrops.get(var1)).indoorSpeed;
      }

      public boolean requiresBlockLight(Season var1) {
         return ((CropSettings.SeasonCrop)this.seasoncrops.get(var1)).requiresBlockLight;
      }

      public int getMinLightLevel(Season var1) {
         return ((CropSettings.SeasonCrop)this.seasoncrops.get(var1)).lightLevel;
      }

      public boolean isEnabled() {
         return this.enabled;
      }

      // $FF: synthetic method
      CropConfig(ConfigurationSection var2, Object var3) {
         this(var2);
      }
   }

   class SeasonCrop {
      double outdoorSpeed;
      double indoorSpeed;
      boolean requiresBlockLight;
      int lightLevel;

      private SeasonCrop(ConfigurationSection param2) {
         this.outdoorSpeed = var2.getDouble("outdoor.growth-speed");
         this.indoorSpeed = var2.getDouble("indoor.growth-speed");
         this.requiresBlockLight = var2.getBoolean("light.requires-constant-block-light");
         this.lightLevel = var2.getInt("light.light-level");
      }

      // $FF: synthetic method
      SeasonCrop(ConfigurationSection var2, Object var3) {
         this(var2);
      }
   }
}
