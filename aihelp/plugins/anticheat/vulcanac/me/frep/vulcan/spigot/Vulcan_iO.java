package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Vulcan_iO {
   private final Vulcan_iE Vulcan_n;
   private int Vulcan_x = 100;
   private int Vulcan_P;
   private int Vulcan_R;
   private int Vulcan_j;
   private Entity Vulcan_E;
   private Player Vulcan_Q;
   private Player Vulcan_T;
   private double Vulcan_g;
   private double Vulcan_h;
   private final Vulcan_c Vulcan_X = new Vulcan_c(20);
   private static int[] Vulcan_M;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(1432245830194069998L, -5030827130523187276L, MethodHandles.lookup().lookupClass()).a(73657222801821L);

   public Vulcan_iO(Vulcan_iE var1) {
      this.Vulcan_n = var1;
   }

   public void Vulcan_Q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_E(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_K(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_D(Object[] var1) {
      this.Vulcan_X.clear();
   }

   public void Vulcan_W(Object[] var1) {
      this.Vulcan_P = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public void Vulcan_S(Object[] var1) {
      this.Vulcan_R = Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]);
   }

   public int Vulcan_y(Object[] var1) {
      return Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]) - this.Vulcan_P;
   }

   public int Vulcan_d(Object[] var1) {
      return Vulcan_Xs.INSTANCE.Vulcan_S().Vulcan_z(new Object[0]) - this.Vulcan_R;
   }

   public void Vulcan_X(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iE Vulcan_Q(Object[] var1) {
      return this.Vulcan_n;
   }

   public int Vulcan_c(Object[] var1) {
      return this.Vulcan_x;
   }

   public int Vulcan_I(Object[] var1) {
      return this.Vulcan_P;
   }

   public int Vulcan_G(Object[] var1) {
      return this.Vulcan_R;
   }

   public int Vulcan__(Object[] var1) {
      return this.Vulcan_j;
   }

   public Entity Vulcan_N(Object[] var1) {
      return this.Vulcan_E;
   }

   public Player Vulcan_I(Object[] var1) {
      return this.Vulcan_Q;
   }

   public Player Vulcan_n(Object[] var1) {
      return this.Vulcan_T;
   }

   public double Vulcan_j(Object[] var1) {
      return this.Vulcan_g;
   }

   public double Vulcan_W(Object[] var1) {
      return this.Vulcan_h;
   }

   public Vulcan_c Vulcan_D(Object[] var1) {
      return this.Vulcan_X;
   }

   private static double lambda$handleFlying$1(Vector var0, Vulcan_b6 var1) {
      Vector var2 = ((Location)var1.Vulcan_w(new Object[0])).toVector().setY(0);
      return var0.distance(var2) - 0.52D;
   }

   private static boolean lambda$handleFlying$0(int param0, int param1, Vulcan_b6 param2) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_P(int[] var0) {
      Vulcan_M = var0;
   }

   public static int[] Vulcan__() {
      return Vulcan_M;
   }

   static {
      if (Vulcan__() == null) {
         Vulcan_P(new int[5]);
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
