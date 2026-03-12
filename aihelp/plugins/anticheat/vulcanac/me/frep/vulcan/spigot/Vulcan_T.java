package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.ArrayDeque;
import org.bukkit.util.Vector;

public class Vulcan_T {
   private final Vulcan_iE Vulcan_M;
   private float Vulcan_h;
   private float Vulcan_R;
   private float Vulcan_G;
   private float Vulcan_L;
   private float Vulcan_A;
   private float Vulcan_V;
   private float Vulcan_Y;
   private float Vulcan_k;
   private float Vulcan_i;
   private float Vulcan_f;
   private float Vulcan_m;
   private float Vulcan_u;
   private float Vulcan_C;
   private float Vulcan_E;
   private float Vulcan_o;
   private float Vulcan_T;
   private float Vulcan_q;
   private float Vulcan_F;
   private boolean Vulcan_D;
   private boolean Vulcan_B;
   private boolean Vulcan_l;
   private double Vulcan_J;
   private double Vulcan_t;
   private final ArrayDeque Vulcan_d = new ArrayDeque();
   private int Vulcan_g;
   private int Vulcan_N;
   private int Vulcan_a;
   private int Vulcan_z;
   private int Vulcan_s;
   private int Vulcan_j;
   private static int Vulcan_W;
   private static final long a = Vulcan_n.a(2663160663763096832L, -1665874906002987259L, MethodHandles.lookup().lookupClass()).a(102084299267545L);

   public Vulcan_T(Vulcan_iE var1) {
      this.Vulcan_M = var1;
   }

   public void Vulcan_R(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_F(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_Y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_U(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_h(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public float Vulcan_j(Object[] var1) {
      double var2 = this.Vulcan_M.Vulcan_e(new Object[0]).Vulcan_B(new Object[0]);
      double var4 = this.Vulcan_M.Vulcan_e(new Object[0]).Vulcan_A(new Object[0]);
      Vector var6 = new Vector(var2, 0.0D, var4);
      Vector var7 = new Vector(-Math.sin((double)(this.Vulcan_h * 3.1415927F / 180.0F)) * 1.0D * 0.5D, 0.0D, Math.cos((double)(this.Vulcan_h * 3.1415927F / 180.0F)) * 1.0D * 0.5D);
      return var6.angle(var7);
   }

   public Vulcan_iE Vulcan_d(Object[] var1) {
      return this.Vulcan_M;
   }

   public float Vulcan_M(Object[] var1) {
      return this.Vulcan_h;
   }

   public float Vulcan_w(Object[] var1) {
      return this.Vulcan_R;
   }

   public float Vulcan_s(Object[] var1) {
      return this.Vulcan_G;
   }

   public float Vulcan_O(Object[] var1) {
      return this.Vulcan_L;
   }

   public float Vulcan_V(Object[] var1) {
      return this.Vulcan_A;
   }

   public float Vulcan_R(Object[] var1) {
      return this.Vulcan_V;
   }

   public float Vulcan_k(Object[] var1) {
      return this.Vulcan_Y;
   }

   public float Vulcan_L(Object[] var1) {
      return this.Vulcan_k;
   }

   public float Vulcan_e(Object[] var1) {
      return this.Vulcan_i;
   }

   public float Vulcan_n(Object[] var1) {
      return this.Vulcan_f;
   }

   public float Vulcan_Y(Object[] var1) {
      return this.Vulcan_m;
   }

   public float Vulcan_x(Object[] var1) {
      return this.Vulcan_u;
   }

   public float Vulcan_Q(Object[] var1) {
      return this.Vulcan_C;
   }

   public float Vulcan_N(Object[] var1) {
      return this.Vulcan_E;
   }

   public float Vulcan_H(Object[] var1) {
      return this.Vulcan_o;
   }

   public float Vulcan_a(Object[] var1) {
      return this.Vulcan_T;
   }

   public float Vulcan_C(Object[] var1) {
      return this.Vulcan_q;
   }

   public float Vulcan_b(Object[] var1) {
      return this.Vulcan_F;
   }

   public boolean Vulcan_R(Object[] var1) {
      return this.Vulcan_D;
   }

   public boolean Vulcan_g(Object[] var1) {
      return this.Vulcan_B;
   }

   public boolean Vulcan_D(Object[] var1) {
      return this.Vulcan_l;
   }

   public double Vulcan_I(Object[] var1) {
      return this.Vulcan_J;
   }

   public double Vulcan_O(Object[] var1) {
      return this.Vulcan_t;
   }

   public ArrayDeque Vulcan_X(Object[] var1) {
      return this.Vulcan_d;
   }

   public int Vulcan_P(Object[] var1) {
      return this.Vulcan_g;
   }

   public int Vulcan_O(Object[] var1) {
      return this.Vulcan_N;
   }

   public int Vulcan_a(Object[] var1) {
      return this.Vulcan_a;
   }

   public int Vulcan_T(Object[] var1) {
      return this.Vulcan_z;
   }

   public int Vulcan_M(Object[] var1) {
      return this.Vulcan_s;
   }

   public int Vulcan_d(Object[] var1) {
      return this.Vulcan_j;
   }

   public static void Vulcan_o(int var0) {
      Vulcan_W = var0;
   }

   public static int Vulcan_X() {
      return Vulcan_W;
   }

   public static int Vulcan_I() {
      int var0 = Vulcan_X();

      try {
         return var0 == 0 ? 94 : 0;
      } catch (RuntimeException var1) {
         throw a(var1);
      }
   }

   static {
      if (Vulcan_I() != 0) {
         Vulcan_o(107);
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
