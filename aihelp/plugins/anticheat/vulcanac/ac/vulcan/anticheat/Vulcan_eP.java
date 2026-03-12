package ac.vulcan.anticheat;

import java.lang.invoke.MethodHandles;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class Vulcan_eP implements Vulcan_iF {
   BukkitTask Vulcan_a;
   boolean Vulcan_G;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-5805109174564810209L, 7527964819172844595L, MethodHandles.lookup().lookupClass()).a(59967516152452L);

   public Vulcan_eP(BukkitTask var1) {
      long var2 = a ^ 12177841834684L;
      int var10000 = Vulcan_XM.Vulcan_C();
      super();
      this.Vulcan_a = var1;
      this.Vulcan_G = false;
      int var4 = var10000;

      try {
         if (AbstractCheck.Vulcan_V() == 0) {
            ++var4;
            Vulcan_XM.Vulcan_b(var4);
         }

      } catch (RuntimeException var5) {
         throw a(var5);
      }
   }

   public Vulcan_eP(BukkitTask var1, boolean var2) {
      long var3 = a ^ 60334336791753L;
      int var10000 = Vulcan_XM.Vulcan_P();
      super();
      this.Vulcan_a = var1;
      this.Vulcan_G = var2;
      int var5 = var10000;
      if (var5 == 0) {
         int var6 = AbstractCheck.Vulcan_V();
         ++var6;
         AbstractCheck.Vulcan_H(var6);
      }

   }

   public void Vulcan_q(Object[] var1) {
      this.Vulcan_a.cancel();
   }

   public boolean Vulcan_M(Object[] var1) {
      return this.Vulcan_a.isCancelled();
   }

   public Plugin Vulcan_u(Object[] var1) {
      return this.Vulcan_a.getOwner();
   }

   public boolean Vulcan_P(Object[] var1) {
      long var2 = (Long)var1[0];
      return Bukkit.getServer().getScheduler().isCurrentlyRunning(this.Vulcan_a.getTaskId());
   }

   public boolean Vulcan_S(Object[] var1) {
      return this.Vulcan_G;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
