package me.casperge.realisticseasons.crop;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import me.casperge.realisticseasons.RealisticSeasons;
import org.apache.commons.io.FileUtils;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

public class CropFileLoader {
   private static final String[] crops = new String[]{"WHEAT", "BEETROOTS", "CARROTS", "POTATOES", "MELON_STEM", "PUMPKIN_STEM", "BAMBOO", "COCOA", "SUGAR_CANE", "SWEET_BERRY_BUSH", "CACTUS", "NETHER_WART"};
   private YamlConfiguration conf;
   private static final String[] seasons = new String[]{"SUMMER", "WINTER", "SPRING", "FALL"};
   private CropSettings cropSettings;

   public CropFileLoader(RealisticSeasons var1) {
      boolean var2 = true;
      File var3 = new File(var1.getDataFolder(), "crops.yml");
      if (!var3.exists()) {
         var2 = false;
         InputStream var4 = var1.getResource("crops.yml");

         try {
            FileUtils.copyInputStreamToFile(var4, var3);
         } catch (IOException var20) {
            var20.printStackTrace();
         }
      }

      this.conf = YamlConfiguration.loadConfiguration(var3);
      if (!var2) {
         HashSet var21 = new HashSet();
         Material[] var5 = Material.values();
         int var6 = var5.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            Material var8 = var5[var7];
            var21.add(var8.name());
         }

         boolean var22 = var1.getSettings().affectCropsSummer;
         boolean var23 = var1.getSettings().affectCropsWinter;
         if (!var22 && !var23) {
            this.conf.set("seasonal-growth-speed.enabled", false);
         } else {
            this.conf.set("seasonal-growth-speed.enabled", true);
         }

         HashMap var24 = new HashMap();
         HashMap var25 = new HashMap();
         HashMap var9 = new HashMap();
         HashMap var10 = new HashMap();
         if (var23) {
            var24.put("SUMMER", 2.0F);
            var25.put("SUMMER", 1.4F);
         } else {
            var24.put("SUMMER", 1.0F);
            var25.put("SUMMER", 1.0F);
         }

         var9.put("SUMMER", false);
         var10.put("SUMMER", 8);
         if (var22) {
            var24.put("WINTER", 0.0F);
            var25.put("WINTER", 1.0F);
            var9.put("WINTER", true);
         } else {
            var24.put("WINTER", 1.0F);
            var25.put("WINTER", 1.0F);
            var9.put("SUMMER", false);
         }

         var10.put("WINTER", 8);
         var24.put("SPRING", 1.2F);
         var25.put("SPRING", 1.0F);
         var9.put("SPRING", false);
         var10.put("SPRING", 8);
         var24.put("FALL", 0.8F);
         var25.put("FALL", 1.0F);
         var9.put("FALL", false);
         var10.put("FALL", 8);
         String[] var11 = crops;
         int var12 = var11.length;

         for(int var13 = 0; var13 < var12; ++var13) {
            String var14 = var11[var13];
            if (var21.contains(var14)) {
               this.conf.set("seasonal-growth-speed." + var14 + ".enabled", true);
               String[] var15 = seasons;
               int var16 = var15.length;

               for(int var17 = 0; var17 < var16; ++var17) {
                  String var18 = var15[var17];
                  this.conf.set("seasonal-growth-speed." + var14 + ".seasons." + var18 + ".outdoor.growth-speed", var24.get(var18));
                  this.conf.set("seasonal-growth-speed." + var14 + ".seasons." + var18 + ".indoor.growth-speed", var25.get(var18));
                  this.conf.set("seasonal-growth-speed." + var14 + ".seasons." + var18 + ".light.requires-constant-block-light", var9.get(var18));
                  if (var14.equals("NETHER_WART")) {
                     this.conf.set("seasonal-growth-speed." + var14 + ".seasons." + var18 + ".light.requires-constant-block-light", false);
                  }

                  this.conf.set("seasonal-growth-speed." + var14 + ".seasons." + var18 + ".light.light-level", var10.get(var18));
               }
            }
         }

         try {
            this.conf.save(var3);
         } catch (IOException var19) {
            var19.printStackTrace();
         }
      }

      this.cropSettings = new CropSettings(this.conf);
      new CropGrowEvent(var1);
   }

   public CropSettings getCropSettings() {
      return this.cropSettings;
   }
}
