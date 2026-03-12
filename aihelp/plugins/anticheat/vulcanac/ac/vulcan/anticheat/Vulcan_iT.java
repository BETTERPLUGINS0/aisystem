package ac.vulcan.anticheat;

import java.util.List;

class Vulcan_iT extends Vulcan_iy {
   private final Vulcan_o Vulcan_R;

   Vulcan_iT(Vulcan_o var1) {
      this.Vulcan_R = var1;
   }

   protected List Vulcan_k(Object[] var1) {
      char[] var4 = (char[])var1[0];
      int var5 = (Integer)var1[1];
      int var6 = (Integer)var1[2];
      long var2 = (Long)var1[3];
      long var7 = var2 ^ 0L;

      try {
         if (var4 == null) {
            return super.Vulcan_k(new Object[]{this.Vulcan_R.Vulcan_H, new Integer(0), new Integer(this.Vulcan_R.Vulcan_i(new Object[0])), new Long(var7)});
         }
      } catch (RuntimeException var9) {
         throw a(var9);
      }

      return super.Vulcan_k(new Object[]{var4, new Integer(var5), new Integer(var6), new Long(var7)});
   }

   public String Vulcan_Z(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
