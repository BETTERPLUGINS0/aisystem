package ac.vulcan.anticheat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.frep.vulcan.spigot.check.AbstractCheck;

class Vulcan_iu {
   final Map Vulcan_K;
   final Map Vulcan_q;
   final List Vulcan_m;
   final List Vulcan_T;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-6789025603658574176L, -2732174822549232245L, (Object)null).a(256127473903750L);

   protected Vulcan_iu() {
      long var1 = a ^ 25698842644098L;
      super();
      this.Vulcan_K = new HashMap();
      Vulcan_XY.Vulcan_N();
      this.Vulcan_q = Collections.unmodifiableMap(this.Vulcan_K);
      this.Vulcan_m = new ArrayList(25);
      this.Vulcan_T = Collections.unmodifiableList(this.Vulcan_m);

      try {
         if (AbstractCheck.Vulcan_m() != 0) {
            Vulcan_XY.Vulcan_y(new AbstractCheck[2]);
         }

      } catch (RuntimeException var4) {
         throw a(var4);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
