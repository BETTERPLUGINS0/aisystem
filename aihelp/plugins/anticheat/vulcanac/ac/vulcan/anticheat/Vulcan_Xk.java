package ac.vulcan.anticheat;

import java.util.Calendar;

class Vulcan_Xk implements Vulcan_iM {
   private final int Vulcan_T;

   Vulcan_Xk(int var1) {
      this.Vulcan_T = var1;
   }

   public int Vulcan_p(Object[] var1) {
      long var2 = (Long)var1[0];
      return 2;
   }

   public void Vulcan_e(Object[] var1) {
      StringBuffer var2 = (StringBuffer)var1[0];
      Calendar var5 = (Calendar)var1[1];
      long var3 = (Long)var1[2];
      long var6 = var3 ^ 73487869171594L;
      this.Vulcan_Z(new Object[]{new Long(var6), var2, new Integer(var5.get(this.Vulcan_T))});
   }

   public final void Vulcan_Z(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
