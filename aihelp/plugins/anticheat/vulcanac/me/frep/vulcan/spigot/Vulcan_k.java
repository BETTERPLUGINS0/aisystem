package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClientTickEnd;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityVelocity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerExplosion;
import java.lang.invoke.MethodHandles;
import java.util.LinkedList;

public class Vulcan_k {
   private final Vulcan_iE Vulcan_U;
   private final LinkedList Vulcan_F = new LinkedList();
   private final LinkedList Vulcan_J = new LinkedList();
   private boolean Vulcan_w = false;
   private boolean Vulcan__ = false;
   private double Vulcan_h = 0.0D;
   private double Vulcan_t = 0.0D;
   private double Vulcan_T = 0.0D;
   private double Vulcan_q = 0.0D;
   private double Vulcan_s = 0.0D;
   private double Vulcan_k = 0.0D;
   private double Vulcan_W = 0.0D;
   private double Vulcan_y = 0.0D;
   private double Vulcan_S = 0.0D;
   private double Vulcan_j = 0.0D;
   private double Vulcan_r = 0.0D;
   private double Vulcan_n = 0.0D;
   private double Vulcan_G = 0.0D;
   private double Vulcan_Z = 0.0D;
   private double Vulcan_C = 0.0D;
   private int Vulcan_i = 0;
   private int Vulcan_N = 0;
   private int Vulcan_D = 0;
   private static String[] Vulcan_A;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-8980357516632825093L, -3636057038042602328L, MethodHandles.lookup().lookupClass()).a(233293994422301L);

   public Vulcan_k(Vulcan_iE var1) {
      this.Vulcan_U = var1;
   }

   public void Vulcan_H(Object[] var1) {
      WrapperPlayServerEntityVelocity var2 = (WrapperPlayServerEntityVelocity)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 116054338602696L;
      this.Vulcan_U.Vulcan_P(new Object[0]).Vulcan_c(new Object[]{this::lambda$handleVelocity$0, var5});
      this.Vulcan_U.Vulcan_P(new Object[0]).Vulcan_g(new Object[]{this::lambda$handleVelocity$1});
   }

   public void Vulcan_d(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_D(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_u(Object[] var1) {
      WrapperPlayClientClientTickEnd var2 = (WrapperPlayClientClientTickEnd)var1[0];
   }

   public Vulcan_iE Vulcan_F(Object[] var1) {
      return this.Vulcan_U;
   }

   public LinkedList Vulcan_B(Object[] var1) {
      return this.Vulcan_F;
   }

   public LinkedList Vulcan_c(Object[] var1) {
      return this.Vulcan_J;
   }

   public boolean Vulcan_r(Object[] var1) {
      return this.Vulcan_w;
   }

   public boolean Vulcan_G(Object[] var1) {
      return this.Vulcan__;
   }

   public double Vulcan_Q(Object[] var1) {
      return this.Vulcan_h;
   }

   public double Vulcan_U(Object[] var1) {
      return this.Vulcan_t;
   }

   public double Vulcan_C(Object[] var1) {
      return this.Vulcan_T;
   }

   public double Vulcan_g(Object[] var1) {
      return this.Vulcan_q;
   }

   public double Vulcan_E(Object[] var1) {
      return this.Vulcan_s;
   }

   public double Vulcan_e(Object[] var1) {
      return this.Vulcan_k;
   }

   public double Vulcan_A(Object[] var1) {
      return this.Vulcan_W;
   }

   public double Vulcan_P(Object[] var1) {
      return this.Vulcan_y;
   }

   public double Vulcan__(Object[] var1) {
      return this.Vulcan_S;
   }

   public double Vulcan_V(Object[] var1) {
      return this.Vulcan_j;
   }

   public double Vulcan_L(Object[] var1) {
      return this.Vulcan_r;
   }

   public double Vulcan_c(Object[] var1) {
      return this.Vulcan_n;
   }

   public double Vulcan_D(Object[] var1) {
      return this.Vulcan_G;
   }

   public double Vulcan_G(Object[] var1) {
      return this.Vulcan_Z;
   }

   public double Vulcan_B(Object[] var1) {
      return this.Vulcan_C;
   }

   public int Vulcan_t(Object[] var1) {
      return this.Vulcan_i;
   }

   public int Vulcan_P(Object[] var1) {
      return this.Vulcan_N;
   }

   public int Vulcan_s(Object[] var1) {
      return this.Vulcan_D;
   }

   private void lambda$handleExplosion$3() {
      this.Vulcan_U.Vulcan_e(new Object[0]).Vulcan_qS(new Object[]{this.Vulcan_J::poll});
   }

   private void lambda$handleExplosion$2(WrapperPlayServerExplosion var1) {
      this.Vulcan_J.add(var1.getKnockback());
   }

   private void lambda$handleVelocity$1() {
      this.Vulcan_U.Vulcan_e(new Object[0]).Vulcan_qS(new Object[]{this.Vulcan_F::poll});
   }

   private void lambda$handleVelocity$0(WrapperPlayServerEntityVelocity var1) {
      this.Vulcan_C = var1.getVelocity().getY();
      this.Vulcan_D = 0;
      this.Vulcan_F.add(var1.getVelocity());
   }

   public static void Vulcan_F(String[] var0) {
      Vulcan_A = var0;
   }

   public static String[] Vulcan_s() {
      return Vulcan_A;
   }

   static {
      if (Vulcan_s() == null) {
         Vulcan_F(new String[2]);
      }

   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
