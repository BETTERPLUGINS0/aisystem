package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import org.bukkit.Location;

public final class Vulcan_Xj {
   private double Vulcan_Z;
   private double Vulcan_m;
   private double Vulcan_L;
   private double Vulcan_G;
   private double Vulcan_X;
   private double Vulcan_V;
   private final long Vulcan_a;
   private static String Vulcan_A;
   private static final long a = Vulcan_n.a(5284391898656761408L, -6891135269840604830L, MethodHandles.lookup().lookupClass()).a(58707629298606L);

   public Vulcan_Xj(double var1, double var3, double var5) {
      this(var1, var1, var3, var3, var5, var5);
   }

   public Vulcan_Xj(double param1, double param3, double param5, double param7, double param9, double param11) {
      // $FF: Couldn't be decompiled
   }

   public double Vulcan_Z(Object[] var1) {
      Location var2 = (Location)var1[0];
      return Math.sqrt(Math.min(Math.pow(var2.getX() - this.Vulcan_Z, 2.0D), Math.pow(var2.getX() - this.Vulcan_G, 2.0D)) + Math.min(Math.pow(var2.getZ() - this.Vulcan_L, 2.0D), Math.pow(var2.getZ() - this.Vulcan_V, 2.0D)));
   }

   public double Vulcan_e(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = Math.min(Math.pow(var2 - this.Vulcan_Z, 2.0D), Math.pow(var2 - this.Vulcan_G, 2.0D));
      double var8 = Math.min(Math.pow(var4 - this.Vulcan_L, 2.0D), Math.pow(var4 - this.Vulcan_V, 2.0D));
      return Math.sqrt(var6 + var8);
   }

   public double Vulcan_R(Object[] var1) {
      Vulcan_Xj var2 = (Vulcan_Xj)var1[0];
      double var3 = Math.min(Math.pow(var2.Vulcan_Z - this.Vulcan_Z, 2.0D), Math.pow(var2.Vulcan_G - this.Vulcan_G, 2.0D));
      double var5 = Math.min(Math.pow(var2.Vulcan_L - this.Vulcan_L, 2.0D), Math.pow(var2.Vulcan_V - this.Vulcan_V, 2.0D));
      return Math.sqrt(var3 + var5);
   }

   public Vulcan_Xj Vulcan_F(Object[] var1) {
      Vulcan_Xj var2 = (Vulcan_Xj)var1[0];
      this.Vulcan_Z += var2.Vulcan_Z;
      this.Vulcan_m += var2.Vulcan_m;
      this.Vulcan_L += var2.Vulcan_L;
      this.Vulcan_G += var2.Vulcan_G;
      this.Vulcan_X += var2.Vulcan_X;
      this.Vulcan_V += var2.Vulcan_V;
      return this;
   }

   public Vulcan_Xj Vulcan_B(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      this.Vulcan_Z += var2;
      this.Vulcan_m += var4;
      this.Vulcan_L += var6;
      this.Vulcan_G += var2;
      this.Vulcan_X += var4;
      this.Vulcan_V += var6;
      return this;
   }

   public Vulcan_Xj Vulcan_r(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      this.Vulcan_Z -= var2;
      this.Vulcan_m -= var4;
      this.Vulcan_L -= var6;
      this.Vulcan_G += var2;
      this.Vulcan_X += var4;
      this.Vulcan_V += var6;
      return this;
   }

   public boolean Vulcan__(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public double Vulcan_s(Object[] var1) {
      return (this.Vulcan_Z + this.Vulcan_G) / 2.0D;
   }

   public double Vulcan_f(Object[] var1) {
      return (this.Vulcan_m + this.Vulcan_X) / 2.0D;
   }

   public double Vulcan_n(Object[] var1) {
      return (this.Vulcan_L + this.Vulcan_V) / 2.0D;
   }

   private boolean Vulcan_K(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean Vulcan_F(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean Vulcan_X(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_X8 Vulcan_j(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_Xj Vulcan_v(Object[] var1) {
      return new Vulcan_Xj(this.Vulcan_Z, this.Vulcan_G, this.Vulcan_m, this.Vulcan_X, this.Vulcan_L, this.Vulcan_V);
   }

   public double Vulcan_T(Object[] var1) {
      return this.Vulcan_Z;
   }

   public double Vulcan_E(Object[] var1) {
      return this.Vulcan_m;
   }

   public double Vulcan_Y(Object[] var1) {
      return this.Vulcan_L;
   }

   public double Vulcan_O(Object[] var1) {
      return this.Vulcan_G;
   }

   public double Vulcan_I(Object[] var1) {
      return this.Vulcan_X;
   }

   public double Vulcan_g(Object[] var1) {
      return this.Vulcan_V;
   }

   public long Vulcan_g(Object[] var1) {
      return this.Vulcan_a;
   }

   public void Vulcan_w(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_Z = var2;
   }

   public void Vulcan_F(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_m = var2;
   }

   public void Vulcan_t(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_L = var2;
   }

   public void Vulcan_M(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_G = var2;
   }

   public void Vulcan_g(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_X = var2;
   }

   public void Vulcan_E(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_V = var2;
   }

   public static void Vulcan_W(String var0) {
      Vulcan_A = var0;
   }

   public static String Vulcan_q() {
      return Vulcan_A;
   }

   static {
      if (Vulcan_q() != null) {
         Vulcan_W("rjJFu");
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
