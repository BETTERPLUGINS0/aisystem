package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;

public class Vulcan_it {
   private final Vulcan_iE Vulcan_D;
   private long Vulcan_x = -1L;
   private long Vulcan_G;
   private long Vulcan_X = System.currentTimeMillis();
   private long Vulcan_w = System.currentTimeMillis();
   private long Vulcan_C = System.currentTimeMillis();
   private final Vulcan_c Vulcan_L = new Vulcan_c(20);
   private double Vulcan_m;
   private double Vulcan__;
   private static boolean Vulcan_a;
   private static final long a = Vulcan_n.a(-1547421262810425522L, 5994861270236172960L, MethodHandles.lookup().lookupClass()).a(198661512761472L);

   public Vulcan_it(Vulcan_iE var1) {
      this.Vulcan_D = var1;
   }

   public void Vulcan_k(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iE Vulcan_e(Object[] var1) {
      return this.Vulcan_D;
   }

   public long Vulcan_O(Object[] var1) {
      return this.Vulcan_x;
   }

   public long Vulcan_j(Object[] var1) {
      return this.Vulcan_G;
   }

   public long Vulcan_h(Object[] var1) {
      return this.Vulcan_X;
   }

   public long Vulcan_A(Object[] var1) {
      return this.Vulcan_w;
   }

   public long Vulcan_R(Object[] var1) {
      return this.Vulcan_C;
   }

   public Vulcan_c Vulcan_m(Object[] var1) {
      return this.Vulcan_L;
   }

   public double Vulcan_M(Object[] var1) {
      return this.Vulcan_m;
   }

   public double Vulcan_l(Object[] var1) {
      return this.Vulcan__;
   }

   public void Vulcan__(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_x = var2;
   }

   public void Vulcan_U(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_G = var2;
   }

   public void Vulcan_Y(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_X = var2;
   }

   public void Vulcan_n(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_w = var2;
   }

   public void Vulcan_v(Object[] var1) {
      long var2 = (Long)var1[0];
      this.Vulcan_C = var2;
   }

   public void Vulcan_J(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_m = var2;
   }

   public void Vulcan_B(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan__ = var2;
   }

   public static void Vulcan_o(boolean var0) {
      Vulcan_a = var0;
   }

   public static boolean Vulcan_T() {
      return Vulcan_a;
   }

   public static boolean Vulcan_n() {
      boolean var0 = Vulcan_T();

      try {
         return !var0;
      } catch (RuntimeException var1) {
         throw a(var1);
      }
   }

   static {
      if (!Vulcan_T()) {
         Vulcan_o(true);
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
