package ac.vulcan.anticheat;

import java.util.HashMap;
import java.util.Map;

class Vulcan_S implements Vulcan_iS {
   private final Map Vulcan_x = new HashMap();
   private final Vulcan_L Vulcan_p = new Vulcan_L();

   public void Vulcan_E(Object[] var1) {
      long var3 = (Long)var1[0];
      String var5 = (String)var1[1];
      int var2 = (Integer)var1[2];
      long var6 = var3 ^ 79596991089183L;
      this.Vulcan_x.put(var5, new Integer(var2));
      this.Vulcan_p.Vulcan_b(new Object[]{new Integer(var2), new Long(var6), var5});
   }

   public String Vulcan_d(Object[] var1) {
      long var2 = (Long)var1[0];
      int var4 = (Integer)var1[1];
      long var5 = var2 ^ 99832890299030L;
      return (String)this.Vulcan_p.Vulcan_m(new Object[]{new Integer(var4), new Long(var5)});
   }

   public int Vulcan_q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
