package ac.vulcan.anticheat;

import java.util.Calendar;

class Vulcan_iQ implements Vulcan_iM {
   static final Vulcan_iQ Vulcan_k = new Vulcan_iQ();

   public int Vulcan_p(Object[] var1) {
      long var2 = (Long)var1[0];
      return 2;
   }

   public void Vulcan_e(Object[] var1) {
      StringBuffer var4 = (StringBuffer)var1[0];
      Calendar var5 = (Calendar)var1[1];
      long var2 = (Long)var1[2];
      long var6 = var2 ^ 73487869171594L;
      this.Vulcan_Z(new Object[]{new Long(var6), var4, new Integer(var5.get(1) % 100)});
   }

   public final void Vulcan_Z(Object[] var1) {
      long var4 = (Long)var1[0];
      StringBuffer var3 = (StringBuffer)var1[1];
      int var2 = (Integer)var1[2];
      var3.append((char)(var2 / 10 + 48));
      var3.append((char)(var2 % 10 + 48));
   }
}
