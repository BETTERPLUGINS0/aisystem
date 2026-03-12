package ac.vulcan.anticheat;

import java.util.Locale;
import java.util.TimeZone;

class Vulcan_F implements Vulcan_e3 {
   private final TimeZone Vulcan_K;
   private final boolean Vulcan_m;
   private final Locale Vulcan_t;
   private final int Vulcan_w;
   private final String Vulcan_E;
   private final String Vulcan_o;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(137952343662236243L, -7265507242507357825L, (Object)null).a(203824240022221L);

   Vulcan_F(TimeZone var1, boolean var2, Locale var3, int var4) {
      long var5 = a ^ 110056813426014L;
      long var7 = var5 ^ 54403910101972L;
      super();
      this.Vulcan_K = var1;
      this.Vulcan_m = var2;
      this.Vulcan_t = var3;
      this.Vulcan_w = var4;
      if (var2) {
         this.Vulcan_E = Vulcan_i6.Vulcan_S(new Object[]{var1, new Long(var7), new Boolean(false), new Integer(var4), var3});
         this.Vulcan_o = Vulcan_i6.Vulcan_S(new Object[]{var1, new Long(var7), new Boolean(true), new Integer(var4), var3});
      } else {
         this.Vulcan_E = null;
         this.Vulcan_o = null;
      }

   }

   public int Vulcan_p(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_e(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
