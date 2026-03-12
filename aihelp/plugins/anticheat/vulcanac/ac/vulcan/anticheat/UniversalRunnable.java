package ac.vulcan.anticheat;

import java.lang.invoke.MethodHandles;
import org.bukkit.plugin.Plugin;

public abstract class UniversalRunnable implements Runnable {
   Vulcan_iF Vulcan_S;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(3231824132762808895L, 8272634139497973149L, MethodHandles.lookup().lookupClass()).a(63743777921087L);

   public synchronized void Vulcan_J(Object[] var1) {
      this.Vulcan_u(new Object[0]);
      this.Vulcan_S.Vulcan_q(new Object[0]);
   }

   public synchronized boolean Vulcan_u(Object[] var1) {
      this.Vulcan_u(new Object[0]);
      return this.Vulcan_S.Vulcan_M(new Object[0]);
   }

   public synchronized Vulcan_iF Vulcan_r(Object[] var1) {
      Plugin var2 = (Plugin)var1[0];
      this.Vulcan_c(new Object[0]);
      return this.Vulcan_Y(new Object[]{UniversalScheduler.Vulcan_F(new Object[]{var2}).Vulcan_H(new Object[]{this})});
   }

   public synchronized Vulcan_iF Vulcan_s(Object[] var1) {
      Plugin var2 = (Plugin)var1[0];
      this.Vulcan_c(new Object[0]);
      return this.Vulcan_Y(new Object[]{UniversalScheduler.Vulcan_F(new Object[]{var2}).Vulcan_W(new Object[]{this})});
   }

   public synchronized Vulcan_iF Vulcan_x(Object[] var1) {
      long var3 = (Long)var1[0];
      Plugin var2 = (Plugin)var1[1];
      long var5 = (Long)var1[2];
      var3 ^= a;
      long var7 = var3 ^ 12424928447286L;
      this.Vulcan_c(new Object[0]);
      return this.Vulcan_Y(new Object[]{UniversalScheduler.Vulcan_F(new Object[]{var2}).Vulcan_d(new Object[]{this, var5, var7})});
   }

   public synchronized Vulcan_iF Vulcan_C(Object[] var1) {
      Plugin var2 = (Plugin)var1[0];
      long var3 = (Long)var1[1];
      long var5 = (Long)var1[2];
      var5 ^= a;
      long var7 = var5 ^ 132107477117685L;
      this.Vulcan_c(new Object[0]);
      return this.Vulcan_Y(new Object[]{UniversalScheduler.Vulcan_F(new Object[]{var2}).Vulcan_O(new Object[]{this, var3, var7})});
   }

   public synchronized Vulcan_iF Vulcan_V(Object[] var1) {
      Plugin var8 = (Plugin)var1[0];
      long var2 = (Long)var1[1];
      long var4 = (Long)var1[2];
      long var6 = (Long)var1[3];
      var6 ^= a;
      long var9 = var6 ^ 83392227589620L;
      this.Vulcan_c(new Object[0]);
      return this.Vulcan_Y(new Object[]{UniversalScheduler.Vulcan_F(new Object[]{var8}).Vulcan_R(new Object[]{this, var2, var4, var9})});
   }

   public synchronized Vulcan_iF Vulcan_k(Object[] var1) {
      Plugin var6 = (Plugin)var1[0];
      long var2 = (Long)var1[1];
      long var4 = (Long)var1[2];
      this.Vulcan_c(new Object[0]);
      return this.Vulcan_Y(new Object[]{UniversalScheduler.Vulcan_F(new Object[]{var6}).Vulcan_c(new Object[]{this, var2, var4})});
   }

   private void Vulcan_u(Object[] var1) {
      try {
         if (this.Vulcan_S == null) {
            throw new IllegalStateException("Not scheduled yet");
         }
      } catch (IllegalStateException var2) {
         throw a(var2);
      }
   }

   private void Vulcan_c(Object[] var1) {
      try {
         if (this.Vulcan_S != null) {
            throw new IllegalStateException("Already scheduled");
         }
      } catch (IllegalStateException var2) {
         throw a(var2);
      }
   }

   private Vulcan_iF Vulcan_Y(Object[] var1) {
      Vulcan_iF var2 = (Vulcan_iF)var1[0];
      this.Vulcan_S = var2;
      return var2;
   }

   private static IllegalStateException a(IllegalStateException var0) {
      return var0;
   }
}
