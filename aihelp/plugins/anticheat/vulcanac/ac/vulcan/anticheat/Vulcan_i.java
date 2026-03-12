package ac.vulcan.anticheat;

import java.util.Calendar;

class Vulcan_i implements Vulcan_iM {
   private final int Vulcan_s;

   Vulcan_i(int var1) {
      this.Vulcan_s = var1;
   }

   public int Vulcan_p(Object[] var1) {
      long var2 = (Long)var1[0];
      return 4;
   }

   public void Vulcan_e(Object[] var1) {
      StringBuffer var5 = (StringBuffer)var1[0];
      Calendar var4 = (Calendar)var1[1];
      long var2 = (Long)var1[2];
      long var6 = var2 ^ 73487869171594L;
      this.Vulcan_Z(new Object[]{new Long(var6), var5, new Integer(var4.get(this.Vulcan_s))});
   }

   public final void Vulcan_Z(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
