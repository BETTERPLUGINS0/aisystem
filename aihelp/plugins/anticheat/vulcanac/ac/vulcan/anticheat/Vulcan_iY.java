package ac.vulcan.anticheat;

import java.util.Map;

abstract class Vulcan_iY implements Vulcan_iS {
   protected final Map Vulcan_P;
   protected final Map Vulcan_m;

   Vulcan_iY(Map var1, Map var2) {
      this.Vulcan_P = var1;
      this.Vulcan_m = var2;
   }

   public void Vulcan_E(Object[] var1) {
      long var4 = (Long)var1[0];
      String var2 = (String)var1[1];
      int var3 = (Integer)var1[2];
      this.Vulcan_P.put(var2, new Integer(var3));
      this.Vulcan_m.put(new Integer(var3), var2);
   }

   public String Vulcan_d(Object[] var1) {
      long var2 = (Long)var1[0];
      int var4 = (Integer)var1[1];
      return (String)this.Vulcan_m.get(new Integer(var4));
   }

   public int Vulcan_q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
