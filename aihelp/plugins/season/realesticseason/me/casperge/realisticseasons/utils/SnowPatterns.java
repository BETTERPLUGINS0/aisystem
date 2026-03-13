package me.casperge.realisticseasons.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class SnowPatterns {
   private static HashMap<Integer, Integer[]> arrays = new HashMap();

   public static Integer[] getArray(int var0, int var1) {
      return getArrayFromCoords(Math.abs(var0) % 10, Math.abs(var1) % 10);
   }

   public static void generate() {
      for(int var0 = 0; var0 < 10; ++var0) {
         for(int var1 = 0; var1 < 10; ++var1) {
            arrays.put(var0 * 10 + var1, generateRandomArray());
         }
      }

   }

   private static Integer[] generateRandomArray() {
      Integer[] var0 = new Integer[256];

      for(int var1 = 0; var1 < var0.length; ++var1) {
         var0[var1] = var1;
      }

      Collections.shuffle(Arrays.asList(var0));
      return var0;
   }

   private static Integer[] getArrayFromCoords(int var0, int var1) {
      return (Integer[])arrays.get(var0 * 10 + var1);
   }
}
