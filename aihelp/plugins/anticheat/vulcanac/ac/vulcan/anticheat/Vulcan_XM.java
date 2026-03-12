package ac.vulcan.anticheat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

public class Vulcan_XM implements Vulcan_n {
   final Plugin Vulcan_O;
   private static int Vulcan_a;

   public Vulcan_XM(Plugin var1) {
      this.Vulcan_O = var1;
   }

   public boolean Vulcan_L(Object[] var1) {
      return Bukkit.getServer().isPrimaryThread();
   }

   public boolean Vulcan_f(Object[] var1) {
      Entity var2 = (Entity)var1[0];
      return Bukkit.getServer().isPrimaryThread();
   }

   public boolean Vulcan_u(Object[] var1) {
      Location var2 = (Location)var1[0];
      return Bukkit.getServer().isPrimaryThread();
   }

   public Vulcan_iF Vulcan_H(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      return new Vulcan_eP(Bukkit.getScheduler().runTask(this.Vulcan_O, var2));
   }

   public Vulcan_iF Vulcan_d(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      long var3 = (Long)var1[1];
      long var5 = (Long)var1[2];
      return new Vulcan_eP(Bukkit.getScheduler().runTaskLater(this.Vulcan_O, var2, var3));
   }

   public Vulcan_iF Vulcan_R(Object[] var1) {
      Runnable var8 = (Runnable)var1[0];
      long var2 = (Long)var1[1];
      long var4 = (Long)var1[2];
      long var6 = (Long)var1[3];
      return new Vulcan_eP(Bukkit.getScheduler().runTaskTimer(this.Vulcan_O, var8, var2, var4));
   }

   public Vulcan_iF Vulcan_W(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      return new Vulcan_eP(Bukkit.getScheduler().runTaskAsynchronously(this.Vulcan_O, var2));
   }

   public Vulcan_iF Vulcan_O(Object[] var1) {
      Runnable var6 = (Runnable)var1[0];
      long var2 = (Long)var1[1];
      long var4 = (Long)var1[2];
      return new Vulcan_eP(Bukkit.getScheduler().runTaskLaterAsynchronously(this.Vulcan_O, var6, var2));
   }

   public Vulcan_iF Vulcan_c(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      long var3 = (Long)var1[1];
      long var5 = (Long)var1[2];
      return new Vulcan_eP(Bukkit.getScheduler().runTaskTimerAsynchronously(this.Vulcan_O, var2, var3, var5));
   }

   public Vulcan_iF Vulcan_w(Object[] var1) {
      Plugin var3 = (Plugin)var1[0];
      Runnable var2 = (Runnable)var1[1];
      return new Vulcan_eP(Bukkit.getScheduler().runTask(var3, var2));
   }

   public Vulcan_iF Vulcan_j(Object[] var1) {
      Plugin var3 = (Plugin)var1[0];
      long var6 = (Long)var1[1];
      Runnable var2 = (Runnable)var1[2];
      long var4 = (Long)var1[3];
      return new Vulcan_eP(Bukkit.getScheduler().runTaskLater(var3, var2, var4));
   }

   public Vulcan_iF Vulcan_v(Object[] var1) {
      Plugin var2 = (Plugin)var1[0];
      long var7 = (Long)var1[1];
      Runnable var9 = (Runnable)var1[2];
      long var3 = (Long)var1[3];
      long var5 = (Long)var1[4];
      return new Vulcan_eP(Bukkit.getScheduler().runTaskTimer(var2, var9, var3, var5));
   }

   public Vulcan_iF Vulcan_A(Object[] var1) {
      Plugin var3 = (Plugin)var1[0];
      Runnable var2 = (Runnable)var1[1];
      return new Vulcan_eP(Bukkit.getScheduler().runTaskAsynchronously(var3, var2));
   }

   public Vulcan_iF Vulcan_J(Object[] var1) {
      long var5 = (Long)var1[0];
      Plugin var7 = (Plugin)var1[1];
      Runnable var4 = (Runnable)var1[2];
      long var2 = (Long)var1[3];
      return new Vulcan_eP(Bukkit.getScheduler().runTaskLaterAsynchronously(var7, var4, var2));
   }

   public Vulcan_iF Vulcan_E(Object[] var1) {
      long var2 = (Long)var1[0];
      Plugin var4 = (Plugin)var1[1];
      Runnable var9 = (Runnable)var1[2];
      long var5 = (Long)var1[3];
      long var7 = (Long)var1[4];
      return new Vulcan_eP(Bukkit.getScheduler().runTaskTimerAsynchronously(var4, var9, var5, var7));
   }

   public void Vulcan_Y(Object[] var1) {
      Runnable var2 = (Runnable)var1[0];
      Bukkit.getScheduler().scheduleSyncDelayedTask(this.Vulcan_O, var2);
   }

   public void Vulcan_y(Object[] var1) {
      Bukkit.getScheduler().cancelTasks(this.Vulcan_O);
   }

   public void Vulcan_x(Object[] var1) {
      Plugin var2 = (Plugin)var1[0];
      Bukkit.getScheduler().cancelTasks(var2);
   }

   public static void Vulcan_b(int var0) {
      Vulcan_a = var0;
   }

   public static int Vulcan_C() {
      return Vulcan_a;
   }

   public static int Vulcan_P() {
      int var0 = Vulcan_C();

      try {
         return var0 == 0 ? 117 : 0;
      } catch (RuntimeException var1) {
         throw a(var1);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }

   static {
      if (Vulcan_C() != 0) {
         Vulcan_b(121);
      }

   }
}
