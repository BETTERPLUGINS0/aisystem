package me.casperge.realisticseasons.biome;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BiomeRegister {
   public static List<int[]> preGeneratedFallpackets = new ArrayList();
   public static int kaas;
   public static HashMap<Integer, Integer> springreplacements = new HashMap();
   public static HashMap<Integer, Integer> summerreplacements = new HashMap();
   public static HashMap<Integer, Integer> winterreplacements = new HashMap();
   public static HashMap<Integer, List<Integer>> fallreplacements = new HashMap();
   public static HashMap<Integer, HashMap<Integer, Integer>> mixspringreplacements = new HashMap();
   public static HashMap<Integer, HashMap<Integer, Integer>> mixsummerreplacements = new HashMap();
   public static HashMap<Integer, HashMap<Integer, Integer>> mixwinterreplacements = new HashMap();
   public static HashMap<Integer, HashMap<Integer, Integer>> mixfallreplacements = new HashMap();
   public static List<Integer> frozeninwinter = new ArrayList();
   private static int biomeOverride;
   private static boolean isBiomeOverride = false;

   public static int getWinterReplacement(int var0) {
      if (isBiomeOverride) {
         return biomeOverride;
      } else {
         return winterreplacements.containsKey(var0) ? (Integer)winterreplacements.get(var0) : 5555;
      }
   }

   public static int getMixWinterReplacement(int var0, int var1) {
      if (isBiomeOverride) {
         return biomeOverride;
      } else {
         return ((HashMap)mixwinterreplacements.get(var1)).containsKey(var0) ? (Integer)((HashMap)mixwinterreplacements.get(var1)).get(var0) : 5555;
      }
   }

   public static void update1_18BlockParticleColor() {
      setBiomeOverride(kaas);
   }

   public static int getSpringReplacement(int var0) {
      if (isBiomeOverride) {
         return biomeOverride;
      } else {
         return springreplacements.containsKey(var0) ? (Integer)springreplacements.get(var0) : 5555;
      }
   }

   public static int getMixSpringReplacement(int var0, int var1) {
      if (isBiomeOverride) {
         return biomeOverride;
      } else {
         return ((HashMap)mixspringreplacements.get(var1)).containsKey(var0) ? (Integer)((HashMap)mixspringreplacements.get(var1)).get(var0) : 5555;
      }
   }

   public static int getSummerReplacement(int var0) {
      if (isBiomeOverride) {
         return biomeOverride;
      } else {
         return summerreplacements.containsKey(var0) ? (Integer)summerreplacements.get(var0) : 5555;
      }
   }

   public static int getMixSummerReplacement(int var0, int var1) {
      if (isBiomeOverride) {
         return biomeOverride;
      } else {
         return ((HashMap)mixsummerreplacements.get(var1)).containsKey(var0) ? (Integer)((HashMap)mixsummerreplacements.get(var1)).get(var0) : 5555;
      }
   }

   public static List<Integer> getFallReplacements(int var0) {
      ArrayList var1;
      if (isBiomeOverride) {
         var1 = new ArrayList();
         var1.add(biomeOverride);
         return var1;
      } else if (fallreplacements.containsKey(var0)) {
         return (List)fallreplacements.get(var0);
      } else {
         var1 = new ArrayList();
         var1.add(5555);
         return var1;
      }
   }

   public static int getMixFallReplacement(int var0, int var1) {
      if (isBiomeOverride) {
         return biomeOverride;
      } else {
         return ((HashMap)mixfallreplacements.get(var1)).containsKey(var0) ? (Integer)((HashMap)mixfallreplacements.get(var1)).get(var0) : 5555;
      }
   }

   public static void setBiomeOverride(int var0) {
      biomeOverride = var0;
      isBiomeOverride = true;
   }

   public static boolean isBiomeOverride() {
      return isBiomeOverride;
   }

   public static void disableBiomeOverride() {
      isBiomeOverride = false;
   }
}
