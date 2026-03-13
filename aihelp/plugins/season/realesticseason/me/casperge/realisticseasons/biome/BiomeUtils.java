package me.casperge.realisticseasons.biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BiomeUtils {
   public static List<int[]> treeFallpackets = new ArrayList();
   public static Random r = new Random();

   public static int getSeasonsTemperature(float var0) {
      if ((double)var0 <= 0.1D) {
         return -12;
      } else if ((double)var0 > 0.1D && (double)var0 <= 0.3D) {
         return -4;
      } else if ((double)var0 > 0.3D && (double)var0 <= 1.0D) {
         return 0;
      } else {
         return (double)var0 > 1.0D && (double)var0 <= 1.4D ? 10 : 15;
      }
   }

   public static int[] updateBiomes(int[] var0, int var1, int var2, int var3, int var4) {
      boolean var5 = false;
      if (var2 != 2) {
         var5 = true;
         if (var2 > 2) {
            --var2;
         }
      }

      int var6;
      switch(var1) {
      case 0:
         if (var5) {
            for(var6 = 0; var6 < var0.length; ++var6) {
               if (BiomeRegister.getMixWinterReplacement(var0[var6], var2) != 5555) {
                  var0[var6] = BiomeRegister.getMixWinterReplacement(var0[var6], var2);
               }
            }
         } else {
            for(var6 = 0; var6 < var0.length; ++var6) {
               if (BiomeRegister.getWinterReplacement(var0[var6]) != 5555) {
                  var0[var6] = BiomeRegister.getWinterReplacement(var0[var6]);
               }
            }
         }

         return var0;
      case 1:
         if (var5) {
            for(var6 = 0; var6 < var0.length; ++var6) {
               if (BiomeRegister.getMixSummerReplacement(var0[var6], var2) != 5555) {
                  var0[var6] = BiomeRegister.getMixSummerReplacement(var0[var6], var2);
               }
            }
         } else {
            for(var6 = 0; var6 < var0.length; ++var6) {
               if (BiomeRegister.getSummerReplacement(var0[var6]) != 5555) {
                  var0[var6] = BiomeRegister.getSummerReplacement(var0[var6]);
               }
            }
         }

         return var0;
      case 2:
         if (var5) {
            for(var6 = 0; var6 < var0.length; ++var6) {
               if (BiomeRegister.getMixSpringReplacement(var0[var6], var2) != 5555) {
                  var0[var6] = BiomeRegister.getMixSpringReplacement(var0[var6], var2);
               }
            }
         } else {
            for(var6 = 0; var6 < var0.length; ++var6) {
               if (BiomeRegister.getSpringReplacement(var0[var6]) != 5555) {
                  var0[var6] = BiomeRegister.getSpringReplacement(var0[var6]);
               }
            }
         }

         return var0;
      case 3:
         if (var5) {
            for(var6 = 0; var6 < var0.length; ++var6) {
               if (BiomeRegister.getMixFallReplacement(var0[var6], var2) != 5555) {
                  var0[var6] = BiomeRegister.getMixFallReplacement(var0[var6], var2);
               }
            }
         } else {
            long var11 = (long)var4;
            var11 = (long)var3 + (var11 << 32);
            Random var8 = new Random(var11);
            int var9 = var8.nextInt(5);

            for(int var10 = 0; var10 < var0.length; ++var10) {
               if (BiomeRegister.getFallReplacements(var0[var10]).size() == 1) {
                  if ((Integer)BiomeRegister.getFallReplacements(var0[var10]).get(0) != 5555) {
                     var0[var10] = (Integer)BiomeRegister.getFallReplacements(var0[var10]).get(0);
                  }
               } else if (var10 < 1024) {
                  var0[var10] = (Integer)BiomeRegister.getFallReplacements(var0[var10]).get(((int[])treeFallpackets.get(var9))[var10]);
               } else {
                  var0[var10] = (Integer)BiomeRegister.getFallReplacements(var0[var10]).get(((int[])treeFallpackets.get(var9))[var10 % 1024]);
               }
            }
         }

         return var0;
      default:
         return var0;
      }
   }

   public static int getDefaultBiomeTemperature(String var0) {
      if (var0.equals("BADLANDS")) {
         return 15;
      } else if (var0.equals("JUNGLE")) {
         return 12;
      } else if (var0.equals("BEACH")) {
         return 0;
      } else if (var0.equals("BIRCH_FOREST")) {
         return 0;
      } else if (var0.equals("OCEAN")) {
         return 0;
      } else if (var0.equals("DARK_FOREST")) {
         return 0;
      } else if (var0.equals("DESERT")) {
         return 15;
      } else if (var0.equals("FLOWER_FOREST")) {
         return 0;
      } else if (var0.equals("FOREST")) {
         return 0;
      } else if (var0.equals("TAIGA")) {
         return -4;
      } else if (var0.equals("MOUNTAINS")) {
         return -4;
      } else if (var0.equals("MUSHROOM_FIELDS")) {
         return 0;
      } else if (var0.equals("PLAINS")) {
         return 0;
      } else if (var0.equals("RIVER")) {
         return 0;
      } else if (var0.equals("SAVANNA")) {
         return 10;
      } else if (var0.equals("CAVES")) {
         return 5;
      } else if (!var0.equals("FROZEN_BIOMES") && !var0.equals("FROZEN_MOUNTAINS")) {
         return var0.equals("SWAMP") ? 0 : 0;
      } else {
         return -12;
      }
   }
}
