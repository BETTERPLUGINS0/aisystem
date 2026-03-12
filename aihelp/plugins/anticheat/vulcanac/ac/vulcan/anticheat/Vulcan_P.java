package ac.vulcan.anticheat;

import java.util.Calendar;

class Vulcan_P implements Vulcan_iM {
   private final Vulcan_iM Vulcan_n;

   Vulcan_P(Vulcan_iM var1) {
      this.Vulcan_n = var1;
   }

   public int Vulcan_p(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 0L;
      return this.Vulcan_n.Vulcan_p(new Object[]{new Long(var4)});
   }

   public void Vulcan_e(Object[] var1) {
      StringBuffer var5 = (StringBuffer)var1[0];
      Calendar var2 = (Calendar)var1[1];
      long var3 = (Long)var1[2];
      long var6 = var3 ^ 73487869171594L;
      String[] var10000 = Vulcan_i6.Vulcan_x();
      int var9 = var2.get(10);
      String[] var8 = var10000;

      label20: {
         try {
            if (var8 != null) {
               return;
            }

            if (var9 != 0) {
               break label20;
            }
         } catch (RuntimeException var10) {
            throw a(var10);
         }

         var9 = var2.getLeastMaximum(10) + 1;
      }

      this.Vulcan_n.Vulcan_Z(new Object[]{new Long(var6), var5, new Integer(var9)});
   }

   public void Vulcan_Z(Object[] var1) {
      long var2 = (Long)var1[0];
      StringBuffer var4 = (StringBuffer)var1[1];
      int var5 = (Integer)var1[2];
      long var6 = var2 ^ 0L;
      this.Vulcan_n.Vulcan_Z(new Object[]{new Long(var6), var4, new Integer(var5)});
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
