package ac.vulcan.anticheat;

import java.util.Calendar;

class Vulcan_i0 implements Vulcan_e3 {
   private final char Vulcan_W;

   Vulcan_i0(char var1) {
      this.Vulcan_W = var1;
   }

   public int Vulcan_p(Object[] var1) {
      long var2 = (Long)var1[0];
      return 1;
   }

   public void Vulcan_e(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      Calendar var3 = (Calendar)var1[1];
      long var4 = (Long)var1[2];
      var2.append(this.Vulcan_W);
   }
}
