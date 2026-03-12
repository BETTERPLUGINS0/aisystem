package ac.vulcan.anticheat;

import java.util.Arrays;

final class Vulcan_eA extends Vulcan_eb {
   private final char[] Vulcan_O;

   Vulcan_eA(char[] var1) {
      this.Vulcan_O = (char[])((char[])var1.clone());
      Arrays.sort(this.Vulcan_O);
   }

   public int Vulcan_g(Object[] var1) {
      char[] var5 = (char[])var1[0];
      int var4 = (Integer)var1[1];
      int var2 = (Integer)var1[2];
      int var3 = (Integer)var1[3];
      long var6 = (Long)var1[4];
      int[] var8 = Vulcan_o.Vulcan_h();

      int var10000;
      label32: {
         try {
            var10000 = Arrays.binarySearch(this.Vulcan_O, var5[var4]);
            if (var8 != null) {
               return var10000;
            }

            if (var10000 >= 0) {
               break label32;
            }
         } catch (RuntimeException var9) {
            throw a(var9);
         }

         var10000 = 0;
         return var10000;
      }

      var10000 = 1;
      return var10000;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
