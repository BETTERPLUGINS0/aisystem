package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;
import me.frep.vulcan.spigot.check.AbstractCheck;

public class Vulcan_e1 {
   private final double Vulcan_Z;
   private final double Vulcan_b;
   private final double Vulcan_k;
   private boolean Vulcan_w;
   private boolean Vulcan_A;
   private boolean Vulcan_e;
   private double Vulcan_S;
   private double Vulcan_X;
   private double Vulcan_d;
   private double Vulcan_z;
   private float Vulcan_v;
   private Set Vulcan_Y;
   private static AbstractCheck[] Vulcan_C;
   private static final long a = Vulcan_n.a(-333381740926541550L, 2634410918849847703L, MethodHandles.lookup().lookupClass()).a(13373462751257L);

   public Vulcan_e1(double var1, double var3, double var5) {
      long var7 = a ^ 104782156842505L;
      AbstractCheck[] var10000 = Vulcan_V();
      super();
      this.Vulcan_w = false;
      AbstractCheck[] var9 = var10000;
      this.Vulcan_A = false;
      this.Vulcan_e = false;
      this.Vulcan_S = 0.0D;
      this.Vulcan_X = 0.0D;
      this.Vulcan_d = 0.0D;
      this.Vulcan_z = 0.0D;
      this.Vulcan_v = 0.6F;
      this.Vulcan_Y = new HashSet();
      this.Vulcan_Z = var1;
      this.Vulcan_b = var3;
      this.Vulcan_k = var5;
      if (var9 == null) {
         int var10 = AbstractCheck.Vulcan_V();
         ++var10;
         AbstractCheck.Vulcan_H(var10);
      }

   }

   public double Vulcan_z(Object[] var1) {
      return this.Vulcan_Z;
   }

   public double Vulcan_j(Object[] var1) {
      return this.Vulcan_b;
   }

   public double Vulcan_K(Object[] var1) {
      return this.Vulcan_k;
   }

   public boolean Vulcan__(Object[] var1) {
      return this.Vulcan_w;
   }

   public boolean Vulcan_a(Object[] var1) {
      return this.Vulcan_A;
   }

   public boolean Vulcan_c(Object[] var1) {
      return this.Vulcan_e;
   }

   public double Vulcan_R(Object[] var1) {
      return this.Vulcan_S;
   }

   public double Vulcan_n(Object[] var1) {
      return this.Vulcan_X;
   }

   public double Vulcan_y(Object[] var1) {
      return this.Vulcan_d;
   }

   public double Vulcan_e(Object[] var1) {
      return this.Vulcan_z;
   }

   public float Vulcan_U(Object[] var1) {
      return this.Vulcan_v;
   }

   public Set Vulcan_B(Object[] var1) {
      return this.Vulcan_Y;
   }

   public void Vulcan_G(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_w = var2;
   }

   public void Vulcan_l(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_A = var2;
   }

   public void Vulcan_V(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_e = var2;
   }

   public void Vulcan_U(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_S = var2;
   }

   public void Vulcan_f(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_X = var2;
   }

   public void Vulcan_D(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_d = var2;
   }

   public void Vulcan_o(Object[] var1) {
      double var2 = (Double)var1[0];
      this.Vulcan_z = var2;
   }

   public void Vulcan_g(Object[] var1) {
      float var2 = (Float)var1[0];
      this.Vulcan_v = var2;
   }

   public void Vulcan_k(Object[] var1) {
      Set var2 = (Set)var1[0];
      this.Vulcan_Y = var2;
   }

   public static void Vulcan_R(AbstractCheck[] var0) {
      Vulcan_C = var0;
   }

   public static AbstractCheck[] Vulcan_V() {
      return Vulcan_C;
   }

   static {
      if (Vulcan_V() == null) {
         Vulcan_R(new AbstractCheck[1]);
      }

   }
}
