package ac.vulcan.anticheat;

import java.util.Calendar;

class Vulcan_r implements Vulcan_iM {
   private final Vulcan_iM Vulcan_E;

   Vulcan_r(Vulcan_iM var1) {
      this.Vulcan_E = var1;
   }

   public int Vulcan_p(Object[] var1) {
      long var2 = (Long)var1[0];
      long var4 = var2 ^ 0L;
      return this.Vulcan_E.Vulcan_p(new Object[]{new Long(var4)});
   }

   public void Vulcan_e(Object[] var1) {
      StringBuffer var3 = (StringBuffer)var1[0];
      Calendar var2 = (Calendar)var1[1];
      long var4 = (Long)var1[2];
      long var6 = var4 ^ 73487869171594L;
      String[] var10000 = Vulcan_i6.Vulcan_x();
      int var9 = var2.get(11);
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

         var9 = var2.getMaximum(11) + 1;
      }

      this.Vulcan_E.Vulcan_Z(new Object[]{new Long(var6), var3, new Integer(var9)});
   }

   public void Vulcan_Z(Object[] var1) {
      long var4 = (Long)var1[0];
      StringBuffer var2 = (StringBuffer)var1[1];
      int var3 = (Integer)var1[2];
      long var6 = var4 ^ 0L;
      this.Vulcan_E.Vulcan_Z(new Object[]{new Long(var6), var2, new Integer(var3)});
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
