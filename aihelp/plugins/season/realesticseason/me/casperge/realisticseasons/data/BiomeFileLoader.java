package me.casperge.realisticseasons.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import me.casperge.interfaces.CustomBiome;
import me.casperge.realisticseasons.RealisticSeasons;
import me.casperge.realisticseasons.Version;
import me.casperge.realisticseasons.api.SeasonBiome;
import me.casperge.realisticseasons.biome.BiomeRegister;
import me.casperge.realisticseasons.biome.BiomeUtils;
import me.casperge.realisticseasons.season.Season;
import me.casperge.realisticseasons.utils.BiomeMappings;
import me.casperge.realisticseasons.utils.ChunkUtils;
import me.casperge.realisticseasons.utils.JavaUtils;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;

public class BiomeFileLoader {
   private List<CustomBiome> created = new ArrayList();
   private Season[] seasons;
   private String[] filenames;
   private HashMap<Season, HashMap<int[], BiomeFileLoader.SeasonCustomBiome>> biomes;
   private RealisticSeasons main;
   private BiomeMappings mappings;

   public BiomeFileLoader(RealisticSeasons var1) {
      this.seasons = new Season[]{Season.SPRING, Season.FALL, Season.SUMMER, Season.WINTER};
      this.biomes = new HashMap();
      String[] var2;
      if (Version.is_1_21_4_or_up()) {
         var2 = new String[]{"BADLANDS", "JUNGLE", "BEACH", "BIRCH_FOREST", "OCEAN", "DARK_FOREST", "DESERT", "FLOWER_FOREST", "FOREST", "TAIGA", "MOUNTAINS", "MUSHROOM_FIELDS", "PLAINS", "SAVANNA", "RIVER", "FROZEN_BIOMES", "SWAMP", "FROZEN_MOUNTAINS", "CAVES", "PALE_GARDEN"};
         this.filenames = var2;
      } else {
         var2 = new String[]{"BADLANDS", "JUNGLE", "BEACH", "BIRCH_FOREST", "OCEAN", "DARK_FOREST", "DESERT", "FLOWER_FOREST", "FOREST", "TAIGA", "MOUNTAINS", "MUSHROOM_FIELDS", "PLAINS", "SAVANNA", "RIVER", "FROZEN_BIOMES", "SWAMP", "FROZEN_MOUNTAINS", "CAVES"};
         this.filenames = var2;
      }

      this.main = var1;
      this.biomes.put(Season.FALL, new HashMap());
      this.biomes.put(Season.SUMMER, new HashMap());
      this.biomes.put(Season.WINTER, new HashMap());
      this.biomes.put(Season.SPRING, new HashMap());
      this.load();
   }

   public void load() {
      this.main.getNMSUtils().changeRegistryLock(false);
      this.created.clear();
      this.mappings = new BiomeMappings(this.main);
      BiomeRegister.fallreplacements.clear();
      BiomeRegister.springreplacements.clear();
      BiomeRegister.summerreplacements.clear();
      BiomeRegister.winterreplacements.clear();
      this.checkDataFolder();
      ArrayList var1 = new ArrayList();
      String[] var2 = this.filenames;
      int var3 = var2.length;

      int var4;
      for(var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];
         CustomBiomeData var6 = this.fromFile(var5, false, false);
         if (var6 != null) {
            this.loadFile(var6, false, false);
         }
      }

      File var14 = new File("plugins/RealisticSeasons/biomes/");
      File[] var15 = var14.listFiles();
      var4 = var15.length;

      int var16;
      File var17;
      for(var16 = 0; var16 < var4; ++var16) {
         var17 = var15[var16];
         if (this.isCustomBiomeFile(var17)) {
            CustomBiomeData var7 = this.fromFile(var17.getName(), true, false);
            if (var7 != null) {
               String var8 = var7.getRelatedConf();
               if (var8 != null && !var8.equalsIgnoreCase("NONE") && !var8.equalsIgnoreCase("ALL")) {
                  String[] var9 = var8.trim().split(",");
                  int var10 = var9.length;

                  for(int var11 = 0; var11 < var10; ++var11) {
                     String var12 = var9[var11];
                     var1.add(var12.trim());
                  }

                  this.loadFile(var7, true, false);
               }
            }
         }
      }

      var15 = var14.listFiles();
      var4 = var15.length;

      for(var16 = 0; var16 < var4; ++var16) {
         var17 = var15[var16];
         if (this.isCustomBiomeFile(var17)) {
            YamlConfiguration var18 = YamlConfiguration.loadConfiguration(var17);
            if (var18.contains("includes") && var18.getString("includes").equalsIgnoreCase("ALL")) {
               CustomBiomeData var19 = this.fromFile(var17.getName(), true, true);
               Iterator var20 = this.main.getNMSUtils().getCustomBiomes(this.main.getSettings().biomeDisplayName).iterator();

               while(var20.hasNext()) {
                  String var21 = (String)var20.next();

                  try {
                     if (!var21.contains(":")) {
                        Bukkit.getLogger().severe("[RealisticSeasons] Could not load biome: " + var21 + ". Continuing");
                     } else if (var21.split(":").length == 2) {
                        var19.setBiomeRegName(var21.split(":")[1]);
                        var19.setOriginalBiomeName(var21);
                        var19.setRelatedConf(var21);
                        ArrayList var22 = new ArrayList();
                        var22.add(var21);
                        var19.relatedbiomescustom = var22;
                        float var23 = this.main.getNMSUtils().getBiomeTemperature(var21);
                        var19.setTemperatureModifier(BiomeUtils.getSeasonsTemperature(var23));
                        if (!var1.contains(var21)) {
                           this.loadFile(var19, true, true);
                        }
                     } else {
                        Bukkit.getLogger().severe("[RealisticSeasons] Could not load biome: " + var21 + ". Ignoring");
                     }
                  } catch (Exception var13) {
                     Bukkit.getLogger().severe("[RealisticSeasons] An error occured loading biome: " + var21);
                     var13.printStackTrace();
                  }
               }
            }
         }
      }

      this.registerMixedColorBiomes();
      this.main.getNMSUtils().changeRegistryLock(true);
   }

   public CustomBiomeData fromFile(String var1, boolean var2, boolean var3) {
      YamlConfiguration var4;
      File var5;
      if (!var2) {
         var5 = new File(this.main.getDataFolder(), "biomes/" + var1 + ".yml");
         var4 = YamlConfiguration.loadConfiguration(var5);
      } else {
         var5 = new File(this.main.getDataFolder(), "biomes/" + var1);
         var4 = YamlConfiguration.loadConfiguration(var5);
      }

      String var6;
      if (!var1.equals("FROZEN_BIOMES") && !var1.equals("FROZEN_MOUNTAINS")) {
         if (var1.equals("CAVES")) {
            var6 = "LUSH_CAVES";
            if (!Version.is_1_17_or_up()) {
               return null;
            }
         } else {
            var6 = var1;
         }
      } else {
         var6 = "SNOWY_TUNDRA";
         if (var1.equals("FROZEN_MOUNTAINS") && (!Version.is_1_17_or_up() || Version.version.equals("v1_17_R1"))) {
            return null;
         }
      }

      CustomBiomeData var7 = new CustomBiomeData();
      var7.setModifyPlants(var4.getBoolean("modify-plants"));
      var7.setFreezeInWinter(var4.getBoolean("freeze-in-winter"));
      Season[] var9;
      int var10;
      int var11;
      Season var12;
      if (var4.contains("show-snow-particles-in-winter")) {
         boolean var8 = var4.getBoolean("show-snow-particles-in-winter");
         var9 = this.seasons;
         var10 = var9.length;

         for(var11 = 0; var11 < var10; ++var11) {
            var12 = var9[var11];
            if (var12 == Season.WINTER) {
               var7.setDoSnow(var12, var8);
            } else {
               var7.setDoSnow(var12, false);
            }
         }
      } else {
         Season[] var16 = this.seasons;
         int var18 = var16.length;

         for(var10 = 0; var10 < var18; ++var10) {
            Season var23 = var16[var10];
            String var24 = var23.getConfigName().toLowerCase();
            var7.setDoSnow(var23, var4.getBoolean("show-snow-instead-of-rain." + var24));
         }
      }

      if (var2) {
         var7.setRelatedConf(var4.getString("includes"));
      }

      var7.setS(var1);
      var7.setBiomeRegName(var7.getS());
      ArrayList var17 = new ArrayList();
      String var13;
      int var25;
      if (var2) {
         String var19 = var7.getRelatedConf();
         if (var19 == null) {
            return null;
         }

         if (var19.equalsIgnoreCase("NONE")) {
            return null;
         }

         String[] var21 = var19.trim().split(",");
         var11 = var21.length;

         for(var25 = 0; var25 < var11; ++var25) {
            var13 = var21[var25];
            var17.add(var13.trim());
         }

         var6 = (String)var17.get(0);
         if (var6.contains(":")) {
            var7.setBiomeRegName(var6.split(":")[1]);
         } else {
            var7.setBiomeRegName(var6);
         }
      } else {
         Biome[] var20 = this.main.getNMSUtils().getAssociatedBiomes(var1);
         Biome[] var22 = var20;
         var11 = var20.length;

         for(var25 = 0; var25 < var11; ++var25) {
            Biome var26 = var22[var25];
            var17.add(this.main.getNMSUtils().getBiomeName(var26));
         }
      }

      var7.setOriginalBiomeName(var6);
      var7.relatedbiomescustom = var17;
      if (!var4.contains("temperature-difference")) {
         if (var2) {
            var4.set("temperature-difference", 0);
            var7.setTemperatureModifier(0);
         } else {
            var4.set("temperature-difference", BiomeUtils.getDefaultBiomeTemperature(var1));
            var7.setTemperatureModifier(BiomeUtils.getDefaultBiomeTemperature(var1));
         }

         try {
            var4.save(var5);
         } catch (IOException var15) {
            var15.printStackTrace();
         }
      } else {
         var7.setTemperatureModifier(var4.getInt("temperature-difference"));
      }

      var9 = this.seasons;
      var10 = var9.length;

      for(var11 = 0; var11 < var10; ++var11) {
         var12 = var9[var11];
         var13 = var12.getConfigName().toLowerCase();
         Objects.requireNonNull(var7);
         CustomBiomeData.SeasonColorData var14 = var7.new SeasonColorData();
         var14.setSkyColor(var4.getString("colors." + var13 + ".skycolor"));
         var14.setWaterColor(var4.getString("colors." + var13 + ".watercolor"));
         var14.setWaterFogColor(var4.getString("colors." + var13 + ".waterfogcolor"));
         var14.setGrassColor(var4.getString("colors." + var13 + ".grasscolor"));
         var14.setTreeColor(var4.getString("colors." + var13 + ".treecolor"));
         var14.setFogColor(var4.getString("colors." + var13 + ".fogcolor"));
         var7.addSeasonColorData(var12, var14);
         if (var4.contains("water-temperature")) {
            var7.setBiomeWaterTemperatures(var12, var4.getInt("water-temperature." + var13));
         }
      }

      return var7;
   }

   public void loadFile(CustomBiomeData var1, boolean var2, boolean var3) {
      try {
         Iterator var4;
         String var5;
         if (!var1.getModifyPlants()) {
            var4 = var1.relatedbiomescustom.iterator();

            while(var4.hasNext()) {
               var5 = (String)var4.next();
               if (var5.contains(":")) {
                  ChunkUtils.affectflora.add(var5.split(":")[1]);
               } else {
                  ChunkUtils.affectflora.add(var5);
               }
            }
         }

         if (!var1.getFreezeInWinter()) {
            var4 = var1.relatedbiomescustom.iterator();

            while(var4.hasNext()) {
               var5 = (String)var4.next();
               if (var5.contains(":")) {
                  ChunkUtils.affectinwinter.add(var5.split(":")[1]);
               } else {
                  ChunkUtils.affectinwinter.add(var5);
               }
            }
         }

         var4 = var1.relatedbiomescustom.iterator();

         while(var4.hasNext()) {
            var5 = (String)var4.next();
            if (var5.contains(":")) {
               this.main.getTemperatureManager().getTempData().addBiomeTemperature(var5.split(":")[1], var1.getTemperatureModifier());
               if (var1.getBiomeWaterTemps() != null) {
                  this.main.getTemperatureManager().getTempData().addWaterBiomeTemperature(var5.split(":")[1], var1.getBiomeWaterTemps());
               }
            } else {
               this.main.getTemperatureManager().getTempData().addBiomeTemperature(var5, var1.getTemperatureModifier());
               if (var1.getBiomeWaterTemps() != null) {
                  this.main.getTemperatureManager().getTempData().addWaterBiomeTemperature(var5, var1.getBiomeWaterTemps());
               }
            }
         }

         Season[] var25 = this.seasons;
         int var26 = var25.length;

         for(int var6 = 0; var6 < var26; ++var6) {
            Season var7 = var25[var6];
            CustomBiomeData.SeasonColorData var8 = var1.getSeasonColorData(var7);
            String var9;
            String var10;
            String var11;
            String var12;
            String var13;
            String var14;
            int var16;
            int[] var17;
            String[] var18;
            int var19;
            int var20;
            int var21;
            int var22;
            int[] var27;
            int var28;
            int[] var29;
            int var30;
            int var31;
            int var32;
            int[] var39;
            String[] var40;
            String var42;
            if (var7 != Season.FALL) {
               if (var8.getSkyColor().equals("NONE") && var8.getWaterColor().equals("NONE") && var8.getWaterFogColor().equals("NONE") && var8.getGrassColor().equals("NONE") && var8.getTreeColor().equals("NONE") && var8.getFogColor().equals("NONE")) {
                  if (!var2) {
                     var27 = this.mappings.getBiomeIDs(var1.getS());
                  } else {
                     var27 = new int[var1.relatedbiomescustom.size()];

                     for(var28 = 0; var28 < var1.relatedbiomescustom.size(); ++var28) {
                        var27[var28] = this.main.getNMSUtils().getBiomeID((String)var1.relatedbiomescustom.get(var28));
                     }
                  }

                  var29 = var27;
                  var30 = var27.length;

                  for(var31 = 0; var31 < var30; ++var31) {
                     var32 = var29[var31];
                     if (var7 == Season.SPRING) {
                        BiomeRegister.springreplacements.put(var32, 5555);
                     } else if (var7 == Season.SUMMER) {
                        BiomeRegister.summerreplacements.put(var32, 5555);
                     } else if (var7 == Season.WINTER) {
                        BiomeRegister.winterreplacements.put(var32, 5555);
                     }
                  }
               } else {
                  var9 = var8.getSkyColor();
                  var10 = var8.getWaterColor();
                  var11 = var8.getWaterFogColor();
                  var12 = var8.getGrassColor();
                  var13 = var8.getTreeColor();
                  var14 = var8.getFogColor();
                  if (var9.equals("NONE")) {
                     var9 = "";
                  }

                  if (var10.equals("NONE")) {
                     var10 = "";
                  }

                  if (var11.equals("NONE")) {
                     var11 = "";
                  }

                  if (var12.equals("NONE")) {
                     var12 = "";
                  }

                  if (var13.equals("NONE")) {
                     var13 = "";
                  }

                  if (var14.equals("NONE")) {
                     var14 = "";
                  }

                  var9.replaceAll("\\#", "");
                  var10.replaceAll("\\#", "");
                  var11.replaceAll("\\#", "");
                  var12.replaceAll("\\#", "");
                  var13.replaceAll("\\#", "");
                  var14.replaceAll("\\#", "");
                  boolean var35 = var1.isSnow(var7);
                  if (var2) {
                     var16 = this.main.createSeasonsBiome(var1.getOriginalBiomeName(), var1.getOriginalBiomeName().split(":")[0] + "_" + var7.getConfigName().toString().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase(), var9, var10, var11, var12, var13, var14, var35);
                  } else {
                     var16 = this.main.createSeasonsBiome(var1.getOriginalBiomeName(), var7.getConfigName().toString().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase(), var9, var10, var11, var12, var13, var14, var35);
                  }

                  if (!var2) {
                     var17 = this.mappings.getBiomeIDs(var1.getS());
                     var18 = this.mappings.getBiomeNames(var1.getS());
                  } else {
                     var17 = new int[var1.relatedbiomescustom.size()];
                     var18 = new String[var1.relatedbiomescustom.size()];

                     for(var19 = 0; var19 < var1.relatedbiomescustom.size(); ++var19) {
                        var17[var19] = this.main.getNMSUtils().getBiomeID((String)var1.relatedbiomescustom.get(var19));
                        var18[var19] = (String)var1.relatedbiomescustom.get(var19);
                     }
                  }

                  if (var2) {
                     ((HashMap)this.biomes.get(var7)).put(var17, new BiomeFileLoader.SeasonCustomBiome(var1.getOriginalBiomeName(), var1.getOriginalBiomeName().split(":")[0] + "_" + var7.getConfigName().toString().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase(), var9, var10, var11, var12, var13, var14, var35, var7));
                  } else {
                     ((HashMap)this.biomes.get(var7)).put(var17, new BiomeFileLoader.SeasonCustomBiome(var1.getOriginalBiomeName(), var7.getConfigName().toString().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase(), var9, var10, var11, var12, var13, var14, var35, var7));
                  }

                  var39 = var17;
                  var20 = var17.length;

                  for(var21 = 0; var21 < var20; ++var21) {
                     var22 = var39[var21];
                     if (var7 == Season.SPRING) {
                        BiomeRegister.springreplacements.put(var22, var16);
                     } else if (var7 == Season.SUMMER) {
                        BiomeRegister.summerreplacements.put(var22, var16);
                     } else if (var7 == Season.WINTER) {
                        BiomeRegister.winterreplacements.put(var22, var16);
                        if (var1.isSnow(var7)) {
                           BiomeRegister.frozeninwinter.add(var22);
                        }
                     }
                  }

                  var40 = var18;
                  var20 = var18.length;

                  for(var21 = 0; var21 < var20; ++var21) {
                     var42 = var40[var21];
                     this.main.addSeasonBiomeForAPI(var42, new SeasonBiome(var7, var1.getOriginalBiomeName(), var14, var10, var11, var9, var12, new String[]{var13}));
                  }
               }
            } else if (var8.getSkyColor().equals("NONE") && var8.getWaterColor().equals("NONE") && var8.getWaterFogColor().equals("NONE") && var8.getGrassColor().equals("NONE") && var8.getTreeColor().equals("NONE") && var8.getFogColor().equals("NONE")) {
               if (!var2) {
                  var27 = this.mappings.getBiomeIDs(var1.getS());
               } else {
                  var27 = new int[var1.relatedbiomescustom.size()];

                  for(var28 = 0; var28 < var1.relatedbiomescustom.size(); ++var28) {
                     var27[var28] = this.main.getNMSUtils().getBiomeID((String)var1.relatedbiomescustom.get(var28));
                  }
               }

               var29 = var27;
               var30 = var27.length;

               for(var31 = 0; var31 < var30; ++var31) {
                  var32 = var29[var31];
                  ArrayList var33 = new ArrayList();
                  var33.add(5555);
                  BiomeRegister.fallreplacements.put(var32, var33);
               }
            } else {
               var9 = var8.getSkyColor();
               var10 = var8.getWaterColor();
               var11 = var8.getWaterFogColor();
               var12 = var8.getGrassColor();
               var13 = var8.getTreeColor();
               var14 = var8.getFogColor();
               if (var9.equals("NONE")) {
                  var9 = "";
               }

               if (var10.equals("NONE")) {
                  var10 = "";
               }

               if (var11.equals("NONE")) {
                  var11 = "";
               }

               if (var12.equals("NONE")) {
                  var12 = "";
               }

               if (var13.equals("NONE")) {
                  var13 = "";
               }

               if (var14.equals("NONE")) {
                  var14 = "";
               }

               var9.replaceAll("\\#", "");
               var10.replaceAll("\\#", "");
               var11.replaceAll("\\#", "");
               var12.replaceAll("\\#", "");
               var13.replaceAll("\\#", "");
               var14.replaceAll("\\#", "");
               if (var13.contains(",")) {
                  String[] var34 = var13.split(",");
                  if (var34.length != 5) {
                     Bukkit.getLogger().severe("[RealisticSeasons] Could not load file: " + var1.getS() + ". Fall tree colors should either be 1 color or 5 colors");
                  } else {
                     ArrayList var36 = new ArrayList();

                     for(int var37 = 0; var37 < var34.length; ++var37) {
                        int var38;
                        if (!var2) {
                           var38 = this.main.createSeasonsBiome(var1.getOriginalBiomeName(), var7.getConfigName().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase() + var37, var9, var10, var11, var12, var34[var37].trim(), var14, var1.isSnow(var7));
                        } else {
                           var38 = this.main.createSeasonsBiome(var1.getOriginalBiomeName(), var1.getOriginalBiomeName().split(":")[0] + var7.getConfigName().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase() + var37, var9, var10, var11, var12, var34[var37].trim(), var14, var1.isSnow(var7));
                        }

                        var36.add(var38);
                     }

                     if (!var2) {
                        var17 = this.mappings.getBiomeIDs(var1.getS());
                        var18 = this.mappings.getBiomeNames(var1.getS());
                     } else {
                        var17 = new int[var1.relatedbiomescustom.size()];
                        var18 = new String[var1.relatedbiomescustom.size()];

                        for(var19 = 0; var19 < var1.relatedbiomescustom.size(); ++var19) {
                           var17[var19] = this.main.getNMSUtils().getBiomeID((String)var1.relatedbiomescustom.get(var19));
                           var18[var19] = (String)var1.relatedbiomescustom.get(var19);
                        }
                     }

                     var39 = var17;
                     var20 = var17.length;

                     for(var21 = 0; var21 < var20; ++var21) {
                        var22 = var39[var21];
                        BiomeRegister.fallreplacements.put(var22, var36);
                     }

                     if (!var2) {
                        ((HashMap)this.biomes.get(Season.FALL)).put(var17, new BiomeFileLoader.SeasonCustomBiome(var1.getOriginalBiomeName(), var7.getConfigName().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase(), var9, var10, var11, var12, var34[0].trim(), var14, var1.isSnow(var7), Season.FALL));
                     } else {
                        ((HashMap)this.biomes.get(Season.FALL)).put(var17, new BiomeFileLoader.SeasonCustomBiome(var1.getOriginalBiomeName(), var1.getOriginalBiomeName().split(":")[0] + var7.getConfigName().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase(), var9, var10, var11, var12, var34[0].trim(), var14, var1.isSnow(var7), Season.FALL));
                     }

                     var40 = new String[var34.length];

                     for(var20 = 0; var20 < var34.length; ++var20) {
                        var40[var20] = var34[var20].trim();
                     }

                     String[] var41 = var18;
                     var21 = var18.length;

                     for(var22 = 0; var22 < var21; ++var22) {
                        String var23 = var41[var22];
                        this.main.addSeasonBiomeForAPI(var23, new SeasonBiome(Season.FALL, var1.getOriginalBiomeName(), var14, var10, var11, var9, var12, var40));
                     }
                  }
               } else {
                  ArrayList var15 = new ArrayList();
                  if (!var2) {
                     var16 = this.main.createSeasonsBiome(var1.getOriginalBiomeName(), var7.getConfigName().toString().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase(), var9, var10, var11, var12, var13, var14, var1.isSnow(var7));
                  } else {
                     var16 = this.main.createSeasonsBiome(var1.getOriginalBiomeName(), var1.getOriginalBiomeName().split(":")[0] + "_" + var7.getConfigName().toString().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase(), var9, var10, var11, var12, var13, var14, var1.isSnow(var7));
                  }

                  var15.add(var16);
                  if (!var2) {
                     var17 = this.mappings.getBiomeIDs(var1.getS());
                     var18 = this.mappings.getBiomeNames(var1.getS());
                  } else {
                     var17 = new int[var1.relatedbiomescustom.size()];
                     var18 = new String[var1.relatedbiomescustom.size()];

                     for(var19 = 0; var19 < var1.relatedbiomescustom.size(); ++var19) {
                        var17[var19] = this.main.getNMSUtils().getBiomeID((String)var1.relatedbiomescustom.get(var19));
                        var18[var19] = (String)var1.relatedbiomescustom.get(var19);
                     }
                  }

                  var39 = var17;
                  var20 = var17.length;

                  for(var21 = 0; var21 < var20; ++var21) {
                     var22 = var39[var21];
                     BiomeRegister.fallreplacements.put(var22, var15);
                  }

                  if (!var2) {
                     ((HashMap)this.biomes.get(Season.FALL)).put(var17, new BiomeFileLoader.SeasonCustomBiome(var1.getOriginalBiomeName(), var7.getConfigName().toString().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase(), var9, var10, var11, var12, var13, var14, var1.isSnow(var7), Season.FALL));
                  } else {
                     ((HashMap)this.biomes.get(Season.FALL)).put(var17, new BiomeFileLoader.SeasonCustomBiome(var1.getOriginalBiomeName(), var1.getOriginalBiomeName().split(":")[0] + "_" + var7.getConfigName().toString().toLowerCase() + "_" + var1.getBiomeRegName().toLowerCase(), var9, var10, var11, var12, var13, var14, var1.isSnow(var7), Season.FALL));
                  }

                  var40 = var18;
                  var20 = var18.length;

                  for(var21 = 0; var21 < var20; ++var21) {
                     var42 = var40[var21];
                     this.main.addSeasonBiomeForAPI(var42, new SeasonBiome(Season.FALL, var1.getOriginalBiomeName(), var14, var10, var11, var9, var12, new String[]{var13}));
                  }
               }
            }
         }
      } catch (Exception var24) {
         Bukkit.getLogger().severe("Could not load file: " + var1.getS());
         var24.printStackTrace();
      }

   }

   public void registerMixedColorBiomes() {
      double[] var1 = new double[]{0.45D, 0.25D, 0.25D, 0.45D};
      Season[] var2 = this.seasons;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Season var5 = var2[var4];
         Iterator var6 = ((HashMap)this.biomes.get(var5)).keySet().iterator();

         while(var6.hasNext()) {
            int[] var7 = (int[])var6.next();

            for(int var8 = 0; var8 < 4; ++var8) {
               BiomeFileLoader.SeasonCustomBiome var9 = (BiomeFileLoader.SeasonCustomBiome)((HashMap)this.biomes.get(var5)).get(var7);
               int[] var11 = this.main.getNMSUtils().getBiomeColors(var9.getOriginalBiomeName());
               String[] var12 = new String[var11.length];

               for(int var13 = 0; var13 < var11.length; ++var13) {
                  if (var11[var13] == -9999999) {
                     if (var13 == 4) {
                        var12[var13] = BiomeMappings.getGrassHex(var9.getOriginalBiomeName());
                     } else if (var13 == 5) {
                        var12[var13] = BiomeMappings.getFoliageColor(var9.getOriginalBiomeName());
                     } else if (var13 == 0) {
                        var12[var13] = "c0d8ff";
                     } else if (var13 == 3) {
                        var12[var13] = "79A6FF";
                     } else if (var13 == 2) {
                        var12[var13] = JavaUtils.decimalToHex(var11[2]);
                     }
                  } else {
                     var12[var13] = JavaUtils.decimalToHex(var11[var13]);
                  }
               }

               double var27 = var1[var8];
               BiomeFileLoader.SeasonCustomBiome var10;
               if (var8 <= 1) {
                  var10 = this.findMatchingBiome(var7, var5.getPreviousSeason());
               } else {
                  var10 = this.findMatchingBiome(var7, var5.getNextSeason());
               }

               String var15 = "";
               String var16 = "";
               String var17 = "";
               String var18 = "";
               String var19 = "";
               String var20 = "";
               if (var12[4].equals("CUSTOM")) {
                  var12[4] = "91bd59";
               }

               if (var12[5].equals("CUSTOM")) {
                  var12[5] = "91bd59";
               }

               if (var10 == null) {
                  if (var9.getSkyColor() != "") {
                     var15 = JavaUtils.mixColor(var12[3], var9.getSkyColor(), var27);
                  }

                  if (var9.getWaterColor() != "") {
                     var16 = JavaUtils.mixColor(var12[1], var9.getWaterColor(), var27);
                  }

                  if (var9.getWaterFogColor() != "") {
                     var17 = JavaUtils.mixColor(var12[2], var9.getWaterFogColor(), var27);
                  }

                  if (var9.getGrassColor() != "") {
                     var18 = JavaUtils.mixColor(var12[4], var9.getGrassColor(), var27);
                  }

                  if (var9.getTreeColor() != "") {
                     var19 = JavaUtils.mixColor(var12[5], var9.getTreeColor(), var27);
                  }

                  if (var9.getFogColor() != "") {
                     var20 = JavaUtils.mixColor(var12[0], var9.getFogColor(), var27);
                  }
               } else {
                  if (var9.getSkyColor() != "") {
                     if (var10.getSkyColor() == "") {
                        var15 = JavaUtils.mixColor(var12[3], var9.getSkyColor(), var27);
                     } else {
                        var15 = JavaUtils.mixColor(var10.getSkyColor(), var9.getSkyColor(), var27);
                     }
                  }

                  if (var9.getWaterColor() != "") {
                     if (var10.getWaterColor() == "") {
                        var16 = JavaUtils.mixColor(var12[1], var9.getWaterColor(), var27);
                     } else {
                        var16 = JavaUtils.mixColor(var10.getWaterColor(), var9.getWaterColor(), var27);
                     }
                  }

                  if (var9.getWaterFogColor() != "") {
                     if (var10.getWaterFogColor() == "") {
                        var17 = JavaUtils.mixColor(var12[2], var9.getWaterFogColor(), var27);
                     } else {
                        var17 = JavaUtils.mixColor(var10.getWaterFogColor(), var9.getWaterFogColor(), var27);
                     }
                  }

                  if (var9.getGrassColor() != "") {
                     if (var10.getGrassColor() == "") {
                        var18 = JavaUtils.mixColor(var12[4], var9.getGrassColor(), var27);
                     } else {
                        var18 = JavaUtils.mixColor(var10.getGrassColor(), var9.getGrassColor(), var27);
                     }
                  }

                  if (var9.getTreeColor() != "") {
                     if (var10.getTreeColor() == "") {
                        var19 = JavaUtils.mixColor(var12[5], var9.getTreeColor(), var27);
                     } else {
                        var19 = JavaUtils.mixColor(var10.getTreeColor(), var9.getTreeColor(), var27);
                     }
                  }

                  if (var9.getFogColor() != "") {
                     if (var10.getFogColor() == "") {
                        var20 = JavaUtils.mixColor(var12[0], var9.getFogColor(), var27);
                     } else {
                        var20 = JavaUtils.mixColor(var10.getFogColor(), var9.getFogColor(), var27);
                     }
                  }
               }

               if (!var19.equals("")) {
                  if (var9.getSeason() == Season.SUMMER) {
                     if (var8 < 2) {
                        if (!this.main.getSettings().SpringSummerAdjustment.equals("")) {
                           var19 = JavaUtils.mixColor(this.main.getSettings().SpringSummerAdjustment, var19, this.main.getSettings().SpringSummerWeight);
                        }
                     } else if (!this.main.getSettings().SummerFallAdjustment.equals("")) {
                        var19 = JavaUtils.mixColor(this.main.getSettings().SummerFallAdjustment, var19, this.main.getSettings().SummerFallWeight);
                     }
                  } else if (var9.getSeason() == Season.FALL) {
                     if (var8 < 2) {
                        if (!this.main.getSettings().SummerFallAdjustment.equals("")) {
                           var19 = JavaUtils.mixColor(this.main.getSettings().SummerFallAdjustment, var19, this.main.getSettings().SummerFallWeight);
                        }
                     } else if (!this.main.getSettings().FallWinterAdjustment.equals("")) {
                        var19 = JavaUtils.mixColor(this.main.getSettings().FallWinterAdjustment, var19, this.main.getSettings().FallWinterWeight);
                     }
                  } else if (var9.getSeason() == Season.WINTER) {
                     if (var8 < 2) {
                        if (!this.main.getSettings().FallWinterAdjustment.equals("")) {
                           var19 = JavaUtils.mixColor(this.main.getSettings().FallWinterAdjustment, var19, this.main.getSettings().FallWinterWeight);
                        }
                     } else if (!this.main.getSettings().WinterSpringAdjustment.equals("")) {
                        var19 = JavaUtils.mixColor(this.main.getSettings().WinterSpringAdjustment, var19, this.main.getSettings().WinterSpringWeight);
                     }
                  }

                  if (var9.getSeason() == Season.SPRING) {
                     if (var8 < 2) {
                        if (!this.main.getSettings().WinterSpringAdjustment.equals("")) {
                           var19 = JavaUtils.mixColor(this.main.getSettings().WinterSpringAdjustment, var19, this.main.getSettings().WinterSpringWeight);
                        }
                     } else if (!this.main.getSettings().SpringSummerAdjustment.equals("")) {
                        var19 = JavaUtils.mixColor(this.main.getSettings().SpringSummerAdjustment, var19, this.main.getSettings().SpringSummerWeight);
                     }
                  }
               }

               int var21 = this.main.createSeasonsBiome(var9.getOriginalBiomeName(), var9.getName() + "_blend" + var8, var15, var16, var17, var18, var19, var20, var9.isCold());
               HashMap var22;
               int[] var23;
               int var24;
               int var25;
               int var26;
               if (var9.getSeason() == Season.WINTER) {
                  if (!BiomeRegister.mixwinterreplacements.containsKey(var8)) {
                     BiomeRegister.mixwinterreplacements.put(var8, new HashMap());
                  }

                  var22 = (HashMap)BiomeRegister.mixwinterreplacements.get(var8);
                  var23 = var7;
                  var24 = var7.length;

                  for(var25 = 0; var25 < var24; ++var25) {
                     var26 = var23[var25];
                     var22.put(var26, var21);
                  }

                  BiomeRegister.mixwinterreplacements.put(var8, var22);
               } else if (var9.getSeason() == Season.SPRING) {
                  if (!BiomeRegister.mixspringreplacements.containsKey(var8)) {
                     BiomeRegister.mixspringreplacements.put(var8, new HashMap());
                  }

                  var22 = (HashMap)BiomeRegister.mixspringreplacements.get(var8);
                  var23 = var7;
                  var24 = var7.length;

                  for(var25 = 0; var25 < var24; ++var25) {
                     var26 = var23[var25];
                     var22.put(var26, var21);
                  }

                  BiomeRegister.mixspringreplacements.put(var8, var22);
               } else if (var9.getSeason() == Season.SUMMER) {
                  if (!BiomeRegister.mixsummerreplacements.containsKey(var8)) {
                     BiomeRegister.mixsummerreplacements.put(var8, new HashMap());
                  }

                  var22 = (HashMap)BiomeRegister.mixsummerreplacements.get(var8);
                  var23 = var7;
                  var24 = var7.length;

                  for(var25 = 0; var25 < var24; ++var25) {
                     var26 = var23[var25];
                     var22.put(var26, var21);
                  }

                  BiomeRegister.mixsummerreplacements.put(var8, var22);
               } else if (var9.getSeason() == Season.FALL) {
                  if (!BiomeRegister.mixfallreplacements.containsKey(var8)) {
                     BiomeRegister.mixfallreplacements.put(var8, new HashMap());
                  }

                  var22 = (HashMap)BiomeRegister.mixfallreplacements.get(var8);
                  var23 = var7;
                  var24 = var7.length;

                  for(var25 = 0; var25 < var24; ++var25) {
                     var26 = var23[var25];
                     var22.put(var26, var21);
                  }

                  BiomeRegister.mixfallreplacements.put(var8, var22);
               }
            }
         }
      }

   }

   private BiomeFileLoader.SeasonCustomBiome findMatchingBiome(int[] var1, Season var2) {
      HashMap var3 = (HashMap)this.biomes.get(var2);
      Iterator var4 = var3.keySet().iterator();

      int[] var5;
      do {
         if (!var4.hasNext()) {
            return null;
         }

         var5 = (int[])var4.next();
      } while(!Arrays.equals(var5, var1));

      return (BiomeFileLoader.SeasonCustomBiome)var3.get(var5);
   }

   public void checkDataFolder() {
      File var1 = new File(this.main.getDataFolder(), "biomes/");
      if (!var1.isDirectory()) {
         var1.mkdir();
      }

      String[] var2 = this.filenames;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         String var5 = var2[var4];

         try {
            File var6 = new File(this.main.getDataFolder(), "biomes/" + var5 + ".yml");
            if (!var6.exists()) {
               InputStream var7 = this.main.getResource(var5 + ".yml");
               FileUtils.copyInputStreamToFile(var7, var6);
            }
         } catch (IOException var9) {
            var9.printStackTrace();
         }
      }

      try {
         File var10 = new File(this.main.getDataFolder(), "biomes/CUSTOM_EXAMPLE.yml");
         if (!var10.exists()) {
            InputStream var11 = this.main.getResource("CUSTOM_EXAMPLE.yml");
            FileUtils.copyInputStreamToFile(var11, var10);
         }
      } catch (IOException var8) {
         var8.printStackTrace();
      }

   }

   public boolean isCustomBiomeFile(File var1) {
      boolean var2 = true;
      String[] var3 = this.filenames;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String var6 = var3[var5];
         if (var1.getName().equals(var6 + ".yml")) {
            var2 = false;
         }
      }

      return var2;
   }

   class SeasonCustomBiome {
      String originalBiomeName;
      String name;
      String skyColor;
      String waterColor;
      String waterFogColor;
      String grassColor;
      String treeColor;
      String fogColor;
      boolean isCold;
      Season s;

      public String getOriginalBiomeName() {
         return this.originalBiomeName;
      }

      public String getName() {
         return this.name;
      }

      public String getSkyColor() {
         return this.skyColor;
      }

      public String getWaterColor() {
         return this.waterColor;
      }

      public String getWaterFogColor() {
         return this.waterFogColor;
      }

      public String getGrassColor() {
         return this.grassColor;
      }

      public String getTreeColor() {
         return this.treeColor;
      }

      public String getFogColor() {
         return this.fogColor;
      }

      public boolean isCold() {
         return this.isCold;
      }

      public Season getSeason() {
         return this.s;
      }

      public SeasonCustomBiome(String param2, String param3, String param4, String param5, String param6, String param7, String param8, String param9, boolean param10, Season param11) {
         this.originalBiomeName = var2;
         this.name = var3;
         this.skyColor = var4;
         this.waterColor = var5;
         this.waterFogColor = var6;
         this.grassColor = var7;
         this.treeColor = var8;
         this.fogColor = var9;
         this.isCold = var10;
         this.s = var11;
      }
   }
}
