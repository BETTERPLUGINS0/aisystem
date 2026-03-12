package ac.vulcan.anticheat;

import java.util.Calendar;

class Vulcan_ef implements Vulcan_e3 {
   private final int Vulcan_n;
   private final String[] Vulcan_O;

   Vulcan_ef(int var1, String[] var2) {
      this.Vulcan_n = var1;
      this.Vulcan_O = var2;
   }

   public int Vulcan_p(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_e(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      Calendar var2 = (Calendar)var1[1];
      long var4 = (Long)var1[2];
      var3.append(this.Vulcan_O[var2.get(this.Vulcan_n)]);
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
