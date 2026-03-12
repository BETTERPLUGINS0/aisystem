package ac.vulcan.anticheat;

import io.papermc.paper.threadedregions.scheduler.AsyncScheduler;
import io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler;
import io.papermc.paper.threadedregions.scheduler.RegionScheduler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class Vulcan_C implements Vulcan_n {
   final Plugin Vulcan_N;
   private final RegionScheduler Vulcan_d;
   private final GlobalRegionScheduler Vulcan_H;
   private final AsyncScheduler Vulcan_p;
   private static String Vulcan_R;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-3287532855574534257L, -8823653262719974834L, MethodHandles.lookup().lookupClass()).a(102964211439172L);

   public Vulcan_C(Plugin var1) {
      long var2 = a ^ 87366728886158L;
      super();
      this.Vulcan_d = Bukkit.getServer().getRegionScheduler();
      this.Vulcan_H = Bukkit.getServer().getGlobalRegionScheduler();
      String var10000 = Vulcan_W();
      this.Vulcan_p = Bukkit.getServer().getAsyncScheduler();
      this.Vulcan_N = var1;
      String var4 = var10000;
      if (var4 != null) {
         int var5 = AbstractCheck.Vulcan_m();
         ++var5;
         AbstractCheck.Vulcan_H(var5);
      }

   }

   public boolean Vulcan_L(Object[] var1) {
      return Bukkit.getServer().isGlobalTickThread();
   }

   public boolean Vulcan_S(Object[] var1) {
      return Bukkit.getServer().isPrimaryThread();
   }

   public boolean Vulcan_f(Object[] var1) {
      Entity var2 = (Entity)var1[0];
      return Bukkit.getServer().isOwnedByCurrentRegion(var2);
   }

   public boolean Vulcan_u(Object[] var1) {
      Location var2 = (Location)var1[0];
      return Bukkit.getServer().isOwnedByCurrentRegion(var2);
   }

   public Vulcan_iF Vulcan_H(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      return new Vulcan_bV(this.Vulcan_H.run(this.Vulcan_N, Vulcan_C::lambda$runTask$0));
   }

   public Vulcan_iF Vulcan_d(Object[] var1) {
      Runnable var6 = (Runnable)var1[0];
      long var2 = (Long)var1[1];
      long var4 = (Long)var1[2];

      try {
         if (var2 <= 0L) {
            return this.Vulcan_H(new Object[]{var6});
         }
      } catch (RuntimeException var7) {
         throw a(var7);
      }

      return new Vulcan_bV(this.Vulcan_H.runDelayed(this.Vulcan_N, Vulcan_C::lambda$runTaskLater$1, var2));
   }

   public Vulcan_iF Vulcan_R(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      long var3 = (Long)var1[1];
      long var5 = (Long)var1[2];
      long var7 = (Long)var1[3];
      long var10001 = var7 ^ 61389928398706L;
      int var9 = (int)((var7 ^ 61389928398706L) >>> 48);
      int var10 = (int)(var10001 << 16 >>> 48);
      int var11 = (int)(var10001 << 32 >>> 32);
      var3 = this.Vulcan_q(new Object[]{Integer.valueOf((short)var9), var3, Integer.valueOf((short)var10), var11});
      return new Vulcan_bV(this.Vulcan_H.runAtFixedRate(this.Vulcan_N, Vulcan_C::lambda$runTaskTimer$2, var3, var5));
   }

   public Vulcan_iF Vulcan_w(Object[] var1) {
      Plugin var2 = (Plugin)var1[0];
      Runnable var3 = (Runnable)var1[1];
      return new Vulcan_bV(this.Vulcan_H.run(var2, Vulcan_C::lambda$runTask$3));
   }

   public Vulcan_iF Vulcan_j(Object[] var1) {
      Plugin var2 = (Plugin)var1[0];
      long var3 = (Long)var1[1];
      Runnable var5 = (Runnable)var1[2];
      long var6 = (Long)var1[3];

      try {
         if (var6 <= 0L) {
            return this.Vulcan_w(new Object[]{var2, var5});
         }
      } catch (RuntimeException var8) {
         throw a(var8);
      }

      return new Vulcan_bV(this.Vulcan_H.runDelayed(var2, Vulcan_C::lambda$runTaskLater$4, var6));
   }

   public Vulcan_iF Vulcan_v(Object[] var1) {
      Plugin var2 = (Plugin)var1[0];
      long var3 = (Long)var1[1];
      Runnable var9 = (Runnable)var1[2];
      long var5 = (Long)var1[3];
      long var7 = (Long)var1[4];
      long var10001 = var3 ^ 48803466348314L;
      int var10 = (int)((var3 ^ 48803466348314L) >>> 48);
      int var11 = (int)(var10001 << 16 >>> 48);
      int var12 = (int)(var10001 << 32 >>> 32);
      var5 = this.Vulcan_q(new Object[]{Integer.valueOf((short)var10), var5, Integer.valueOf((short)var11), var12});
      return new Vulcan_bV(this.Vulcan_H.runAtFixedRate(var2, Vulcan_C::lambda$runTaskTimer$5, var5, var7));
   }

   public Vulcan_iF Vulcan_X(Object[] var1) {
      Location var2 = (Location)var1[0];
      Runnable var3 = (Runnable)var1[1];
      return new Vulcan_bV(this.Vulcan_d.run(this.Vulcan_N, var2, Vulcan_C::lambda$runTask$6));
   }

   public Vulcan_iF Vulcan_h(Object[] var1) {
      Location var4 = (Location)var1[0];
      long var5 = (Long)var1[1];
      Runnable var7 = (Runnable)var1[2];
      long var2 = (Long)var1[3];

      try {
         if (var2 <= 0L) {
            return this.Vulcan_H(new Object[]{var7});
         }
      } catch (RuntimeException var8) {
         throw a(var8);
      }

      return new Vulcan_bV(this.Vulcan_d.runDelayed(this.Vulcan_N, var4, Vulcan_C::lambda$runTaskLater$7, var2));
   }

   public Vulcan_iF Vulcan_F(Object[] var1) {
      Location var2 = (Location)var1[0];
      long var4 = (Long)var1[1];
      Runnable var3 = (Runnable)var1[2];
      long var6 = (Long)var1[3];
      long var8 = (Long)var1[4];
      long var10001 = var4 ^ 12616735045525L;
      int var10 = (int)((var4 ^ 12616735045525L) >>> 48);
      int var11 = (int)(var10001 << 16 >>> 48);
      int var12 = (int)(var10001 << 32 >>> 32);
      var6 = this.Vulcan_q(new Object[]{Integer.valueOf((short)var10), var6, Integer.valueOf((short)var11), var12});
      return new Vulcan_bV(this.Vulcan_d.runAtFixedRate(this.Vulcan_N, var2, Vulcan_C::lambda$runTaskTimer$8, var6, var8));
   }

   public Vulcan_iF Vulcan_L(Object[] var1) {
      Entity var3 = (Entity)var1[0];
      Runnable var2 = (Runnable)var1[1];
      return new Vulcan_bV(var3.getScheduler().run(this.Vulcan_N, Vulcan_C::lambda$runTask$9, (Runnable)null));
   }

   public Vulcan_iF Vulcan_V(Object[] var1) {
      long var6 = (Long)var1[0];
      Entity var5 = (Entity)var1[1];
      Runnable var2 = (Runnable)var1[2];
      long var3 = (Long)var1[3];

      try {
         if (var3 <= 0L) {
            return this.Vulcan_L(new Object[]{var5, var2});
         }
      } catch (RuntimeException var8) {
         throw a(var8);
      }

      return new Vulcan_bV(var5.getScheduler().runDelayed(this.Vulcan_N, Vulcan_C::lambda$runTaskLater$10, (Runnable)null, var3));
   }

   public Vulcan_iF Vulcan_Z(Object[] var1) {
      long var8 = (Long)var1[0];
      Entity var7 = (Entity)var1[1];
      Runnable var2 = (Runnable)var1[2];
      long var3 = (Long)var1[3];
      long var5 = (Long)var1[4];
      long var10001 = var8 ^ 130972046631683L;
      int var10 = (int)((var8 ^ 130972046631683L) >>> 48);
      int var11 = (int)(var10001 << 16 >>> 48);
      int var12 = (int)(var10001 << 32 >>> 32);
      var3 = this.Vulcan_q(new Object[]{Integer.valueOf((short)var10), var3, Integer.valueOf((short)var11), var12});
      return new Vulcan_bV(var7.getScheduler().runAtFixedRate(this.Vulcan_N, Vulcan_C::lambda$runTaskTimer$11, (Runnable)null, var3, var5));
   }

   public Vulcan_iF Vulcan_W(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      return new Vulcan_bV(this.Vulcan_p.runNow(this.Vulcan_N, Vulcan_C::lambda$runTaskAsynchronously$12));
   }

   public Vulcan_iF Vulcan_O(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      long var3 = (Long)var1[1];
      long var5 = (Long)var1[2];
      long var10001 = var5 ^ 132458194752491L;
      int var7 = (int)((var5 ^ 132458194752491L) >>> 48);
      int var8 = (int)(var10001 << 16 >>> 48);
      int var9 = (int)(var10001 << 32 >>> 32);
      var3 = this.Vulcan_q(new Object[]{Integer.valueOf((short)var7), var3, Integer.valueOf((short)var8), var9});
      return new Vulcan_bV(this.Vulcan_p.runDelayed(this.Vulcan_N, Vulcan_C::lambda$runTaskLaterAsynchronously$13, var3 * 50L, TimeUnit.MILLISECONDS));
   }

   public Vulcan_iF Vulcan_c(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      long var3 = (Long)var1[1];
      long var5 = (Long)var1[2];
      return new Vulcan_bV(this.Vulcan_p.runAtFixedRate(this.Vulcan_N, Vulcan_C::lambda$runTaskTimerAsynchronously$14, var3 * 50L, var5 * 50L, TimeUnit.MILLISECONDS));
   }

   public Vulcan_iF Vulcan_A(Object[] var1) {
      Plugin var3 = (Plugin)var1[0];
      Runnable var2 = (Runnable)var1[1];
      return new Vulcan_bV(this.Vulcan_p.runNow(var3, Vulcan_C::lambda$runTaskAsynchronously$15));
   }

   public Vulcan_iF Vulcan_J(Object[] var1) {
      long var2 = (Long)var1[0];
      Plugin var7 = (Plugin)var1[1];
      Runnable var6 = (Runnable)var1[2];
      long var4 = (Long)var1[3];
      long var10001 = var2 ^ 110047918345757L;
      int var8 = (int)((var2 ^ 110047918345757L) >>> 48);
      int var9 = (int)(var10001 << 16 >>> 48);
      int var10 = (int)(var10001 << 32 >>> 32);
      var4 = this.Vulcan_q(new Object[]{Integer.valueOf((short)var8), var4, Integer.valueOf((short)var9), var10});
      return new Vulcan_bV(this.Vulcan_p.runDelayed(var7, Vulcan_C::lambda$runTaskLaterAsynchronously$16, var4 * 50L, TimeUnit.MILLISECONDS));
   }

   public Vulcan_iF Vulcan_E(Object[] var1) {
      long var2 = (Long)var1[0];
      Plugin var9 = (Plugin)var1[1];
      Runnable var4 = (Runnable)var1[2];
      long var5 = (Long)var1[3];
      long var7 = (Long)var1[4];
      long var10001 = var2 ^ 89184131446396L;
      int var10 = (int)((var2 ^ 89184131446396L) >>> 48);
      int var11 = (int)(var10001 << 16 >>> 48);
      int var12 = (int)(var10001 << 32 >>> 32);
      Vulcan_W();
      var5 = this.Vulcan_q(new Object[]{Integer.valueOf((short)var10), var5, Integer.valueOf((short)var11), var12});

      try {
         Vulcan_bV var10000 = new Vulcan_bV(this.Vulcan_p.runAtFixedRate(var9, Vulcan_C::lambda$runTaskTimerAsynchronously$17, var5 * 50L, var7 * 50L, TimeUnit.MILLISECONDS));
         if (AbstractCheck.Vulcan_V() == 0) {
            Vulcan_y("Pntpqc");
         }

         return var10000;
      } catch (RuntimeException var14) {
         throw a(var14);
      }
   }

   public void Vulcan_Y(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      this.Vulcan_H.execute(this.Vulcan_N, var2);
   }

   public void Vulcan_m(Object[] var1) {
      Location var2 = (Location)var1[0];
      Runnable var3 = (Runnable)var1[1];
      this.Vulcan_d.execute(this.Vulcan_N, var2, var3);
   }

   public void Vulcan_W(Object[] var1) {
      Entity var3 = (Entity)var1[0];
      Runnable var2 = (Runnable)var1[1];
      var3.getScheduler().execute(this.Vulcan_N, var2, (Runnable)null, 1L);
   }

   public void Vulcan_y(Object[] var1) {
      this.Vulcan_H.cancelTasks(this.Vulcan_N);
      this.Vulcan_p.cancelTasks(this.Vulcan_N);
   }

   public void Vulcan_x(Object[] var1) {
      Plugin var2 = (Plugin)var1[0];
      this.Vulcan_H.cancelTasks(var2);
      this.Vulcan_p.cancelTasks(var2);
   }

   private long Vulcan_q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$runTaskTimerAsynchronously$17(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskLaterAsynchronously$16(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskAsynchronously$15(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskTimerAsynchronously$14(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskLaterAsynchronously$13(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskAsynchronously$12(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskTimer$11(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskLater$10(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTask$9(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskTimer$8(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskLater$7(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTask$6(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskTimer$5(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskLater$4(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTask$3(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskTimer$2(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTaskLater$1(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   private static void lambda$runTask$0(Runnable var0, ScheduledTask var1) {
      var0.run();
   }

   public static void Vulcan_y(String var0) {
      Vulcan_R = var0;
   }

   public static String Vulcan_W() {
      return Vulcan_R;
   }

   static {
      if (Vulcan_W() != null) {
         Vulcan_y("yDyYHc");
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
