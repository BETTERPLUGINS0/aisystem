package ac.vulcan.anticheat;

import java.util.Calendar;

class Vulcan_X implements Vulcan_e3 {
   private final String Vulcan__;

   Vulcan_X(String var1) {
      this.Vulcan__ = var1;
   }

   public int Vulcan_p(Object[] var1) {
      long var2 = (Long)var1[0];
      return this.Vulcan__.length();
   }

   public void Vulcan_e(Object[] var1) {
      StringBuffer var5 = (StringBuffer)var1[0];
      Calendar var2 = (Calendar)var1[1];
      long var3 = (Long)var1[2];
      var5.append(this.Vulcan__);
   }
}
