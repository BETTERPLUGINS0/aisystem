package ac.vulcan.anticheat;

import java.util.Locale;
import java.util.TimeZone;

class Vulcan_bI {
   private final TimeZone Vulcan_C;
   private final int Vulcan_y;
   private final Locale Vulcan_M;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-1170176469709418280L, -5184095226211375399L, (Object)null).a(25985137422653L);

   Vulcan_bI(TimeZone var1, boolean var2, int var3, Locale var4) {
      long var5 = a ^ 81624638550296L;
      super();
      this.Vulcan_C = var1;
      if (var2) {
         var3 |= Integer.MIN_VALUE;
      }

      this.Vulcan_y = var3;
      this.Vulcan_M = var4;
   }

   public int hashCode() {
      return this.Vulcan_y * 31 + this.Vulcan_M.hashCode();
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
