package ac.vulcan.anticheat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.frep.vulcan.spigot.check.AbstractCheck;

class Vulcan_Xg {
   final Map Vulcan_M;
   final Map Vulcan_t;
   final List Vulcan_w;
   final List Vulcan_k;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-3750633820886850917L, -5544195554406914579L, (Object)null).a(250711513750206L);

   protected Vulcan_Xg() {
      long var1 = a ^ 113666258436122L;
      Vulcan_z.Vulcan_X();
      super();

      try {
         this.Vulcan_M = new HashMap();
         this.Vulcan_t = Collections.unmodifiableMap(this.Vulcan_M);
         this.Vulcan_w = new ArrayList(25);
         this.Vulcan_k = Collections.unmodifiableList(this.Vulcan_w);
         if (AbstractCheck.Vulcan_m() != 0) {
            Vulcan_z.Vulcan_e(new String[5]);
         }

      } catch (RuntimeException var4) {
         throw a(var4);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
