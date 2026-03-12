package ac.vulcan.anticheat;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.Plugin;

public class Vulcan_bV implements Vulcan_iF {
   private final ScheduledTask Vulcan_x;

   public Vulcan_bV(ScheduledTask var1) {
      this.Vulcan_x = var1;
   }

   public void Vulcan_q(Object[] var1) {
      this.Vulcan_x.cancel();
   }

   public boolean Vulcan_M(Object[] var1) {
      return this.Vulcan_x.isCancelled();
   }

   public Plugin Vulcan_u(Object[] var1) {
      return this.Vulcan_x.getOwningPlugin();
   }

   public boolean Vulcan_P(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_S(Object[] var1) {
      return this.Vulcan_x.isRepeatingTask();
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
