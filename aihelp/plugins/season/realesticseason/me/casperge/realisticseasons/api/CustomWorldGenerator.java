package me.casperge.realisticseasons.api;

import java.util.ArrayList;
import java.util.List;

public enum CustomWorldGenerator {
   TERRALITH_2("terralith2.0", "Terralith", "terralith:desert_oasis");

   private String foldername;
   private String name;
   private String detectionBiome;

   private CustomWorldGenerator(String param3, String param4, String param5) {
      this.foldername = var3;
      this.name = var4;
      this.detectionBiome = var5;
   }

   public String toString() {
      return this.name;
   }

   public String getResourceFolderName() {
      return this.foldername;
   }

   public String getDetectionBiome() {
      return this.detectionBiome;
   }

   public static CustomWorldGenerator fromFile(String var0) {
      CustomWorldGenerator[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CustomWorldGenerator var4 = var1[var3];
         if (var4.toString().equalsIgnoreCase(var0)) {
            return var4;
         }
      }

      return null;
   }

   public static boolean isWorldGenerator(String var0) {
      CustomWorldGenerator[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CustomWorldGenerator var4 = var1[var3];
         if (var4.toString().equalsIgnoreCase(var0)) {
            return true;
         }
      }

      return false;
   }

   public static CustomWorldGenerator fromBiome(String var0) {
      CustomWorldGenerator[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CustomWorldGenerator var4 = var1[var3];
         if (var4.getDetectionBiome().equalsIgnoreCase(var0)) {
            return var4;
         }
      }

      return null;
   }

   public static boolean isKnownBiome(String var0) {
      CustomWorldGenerator[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CustomWorldGenerator var4 = var1[var3];
         if (var4.getDetectionBiome().equalsIgnoreCase(var0)) {
            return true;
         }
      }

      return false;
   }

   public static List<String> getAllGenerators() {
      ArrayList var0 = new ArrayList();
      CustomWorldGenerator[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         CustomWorldGenerator var4 = var1[var3];
         var0.add(var4.toString());
      }

      return var0;
   }

   // $FF: synthetic method
   private static CustomWorldGenerator[] $values() {
      return new CustomWorldGenerator[]{TERRALITH_2};
   }
}
