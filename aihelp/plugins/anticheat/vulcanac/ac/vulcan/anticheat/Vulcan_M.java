package ac.vulcan.anticheat;

import java.util.Calendar;

class Vulcan_M implements Vulcan_iM {
   static final Vulcan_M Vulcan_E = new Vulcan_M();

   public int Vulcan_p(Object[] var1) {
      long var2 = (Long)var1[0];
      return 2;
   }

   public void Vulcan_e(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      Calendar var5 = (Calendar)var1[1];
      long var2 = (Long)var1[2];
      long var6 = var2 ^ 73487869171594L;
      this.Vulcan_Z(new Object[]{new Long(var6), var4, new Integer(var5.get(2) + 1)});
   }

   public final void Vulcan_Z(Object[] var1) {
      long var3 = (Long)var1[0];
      StringBuffer var2 = (StringBuffer)var1[1];
      int var5 = (Integer)var1[2];
      var2.append((char)(var5 / 10 + 48));
      var2.append((char)(var5 % 10 + 48));
   }
}
