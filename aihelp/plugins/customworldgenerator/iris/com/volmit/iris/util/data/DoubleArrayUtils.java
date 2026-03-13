package com.volmit.iris.util.data;

import com.google.common.util.concurrent.AtomicDoubleArray;
import java.util.Arrays;

public class DoubleArrayUtils {
   public static void shiftRight(double[] values, double push) {
      if (var0.length - 2 + 1 >= 0) {
         System.arraycopy(var0, 0, var0, 1, var0.length - 2 + 1);
      }

      var0[0] = var1;
   }

   public static void wrapRight(double[] values) {
      double var1 = var0[var0.length - 1];
      shiftRight(var0, var1);
   }

   public static void fill(double[] values, double value) {
      Arrays.fill(var0, var1);
   }

   public static void fill(AtomicDoubleArray values, double value) {
      for(int var3 = 0; var3 < var0.length(); ++var3) {
         var0.set(var3, var1);
      }

   }
}
