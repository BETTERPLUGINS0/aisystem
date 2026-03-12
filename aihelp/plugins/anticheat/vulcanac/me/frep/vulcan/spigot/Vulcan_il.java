package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;

public class Vulcan_il implements Comparable {
   public static final Vulcan_il Vulcan_i;
   private final int Vulcan_F;
   private final int Vulcan_p;
   private final int Vulcan_r;
   private static final String Vulcan_l;
   private static String Vulcan_v;
   private static final long a = Vulcan_n.a(-5792177789456160806L, 8756841918006735421L, MethodHandles.lookup().lookupClass()).a(247632753708419L);

   public Vulcan_il(int var1, int var2, int var3) {
      this.Vulcan_F = var1;
      this.Vulcan_p = var2;
      this.Vulcan_r = var3;
   }

   public Vulcan_il(double var1, double var3, double var5) {
      long var7 = a ^ 62104778511344L;
      long var9 = var7 ^ 117674364368754L;
      this(Vulcan_eN.Vulcan_c(new Object[]{var9, var1}), Vulcan_eN.Vulcan_c(new Object[]{var9, var3}), Vulcan_eN.Vulcan_c(new Object[]{var9, var5}));
   }

   public boolean equals(Object param1) {
      // $FF: Couldn't be decompiled
   }

   public int hashCode() {
      return (this.Vulcan_x(new Object[0]) + this.Vulcan_w(new Object[0]) * 31) * 31 + this.Vulcan_Q(new Object[0]);
   }

   public int Vulcan_B(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_Q(Object[] var1) {
      return this.Vulcan_F;
   }

   public int Vulcan_x(Object[] var1) {
      return this.Vulcan_p;
   }

   public int Vulcan_w(Object[] var1) {
      return this.Vulcan_r;
   }

   public Vulcan_il Vulcan_K(Object[] var1) {
      Vulcan_il var2 = (Vulcan_il)var1[0];
      return new Vulcan_il(this.Vulcan_x(new Object[0]) * var2.Vulcan_w(new Object[0]) - this.Vulcan_w(new Object[0]) * var2.Vulcan_x(new Object[0]), this.Vulcan_w(new Object[0]) * var2.Vulcan_Q(new Object[0]) - this.Vulcan_Q(new Object[0]) * var2.Vulcan_w(new Object[0]), this.Vulcan_Q(new Object[0]) * var2.Vulcan_x(new Object[0]) - this.Vulcan_x(new Object[0]) * var2.Vulcan_Q(new Object[0]));
   }

   public double Vulcan_Q(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      double var8 = (double)this.Vulcan_Q(new Object[0]) - var2;
      double var10 = (double)this.Vulcan_x(new Object[0]) - var4;
      double var12 = (double)this.Vulcan_w(new Object[0]) - var6;
      return var8 * var8 + var10 * var10 + var12 * var12;
   }

   public double Vulcan_e(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      double var8 = (double)this.Vulcan_Q(new Object[0]) + 0.5D - var2;
      double var10 = (double)this.Vulcan_x(new Object[0]) + 0.5D - var4;
      double var12 = (double)this.Vulcan_w(new Object[0]) + 0.5D - var6;
      return var8 * var8 + var10 * var10 + var12 * var12;
   }

   public double Vulcan_X(Object[] var1) {
      Vulcan_XH var2 = (Vulcan_XH)var1[0];
      return this.Vulcan_Q(new Object[]{var2.Vulcan_j(new Object[0]), var2.Vulcan_d(new Object[0]), var2.Vulcan_G(new Object[0])});
   }

   public int compareTo(Object var1) {
      long var2 = a ^ 132520921053772L;
      long var4 = var2 ^ 14711681831170L;
      return this.Vulcan_B(new Object[]{var4, (Vulcan_il)var1});
   }

   static {
      if (Vulcan_U() == null) {
         Vulcan_f("SLzCtb");
      }

      char[] var10003 = "\u000fr,VO-u~\rBS".toCharArray();
      int var10004 = var10003.length;
      int var0 = 0;
      byte var4 = 11;
      char[] var10002 = var10003;
      int var1 = var10004;
      boolean var3;
      char[] var5;
      String var6;
      byte var9;
      String var10000;
      int var10005;
      char var10006;
      byte var10007;
      char var10008;
      if (var10004 <= 1) {
         var5 = var10003;
         var10005 = var0;
         var10008 = var10003[var0];
         var10007 = 11;
         var10006 = var10008;
         switch(var0 % 7) {
         case 0:
            var9 = 71;
            break;
         case 1:
            var9 = 53;
            break;
         case 2:
            var9 = 120;
            break;
         case 3:
            var9 = 109;
            break;
         case 4:
            var9 = 116;
            break;
         case 5:
            var9 = 22;
            break;
         default:
            var9 = 78;
         }
      } else {
         var4 = 11;
         var1 = var10004;
         if (var10004 <= var0) {
            var6 = (new String(var10003)).intern();
            var3 = true;
            Vulcan_l = var6;
            var10000 = Vulcan_l;
            Vulcan_i = new Vulcan_il(0, 0, 0);
            return;
         }

         var5 = var10003;
         var10005 = var0;
         var10008 = var10003[var0];
         var10007 = 11;
         var10006 = var10008;
         switch(var0 % 7) {
         case 0:
            var9 = 71;
            break;
         case 1:
            var9 = 53;
            break;
         case 2:
            var9 = 120;
            break;
         case 3:
            var9 = 109;
            break;
         case 4:
            var9 = 116;
            break;
         case 5:
            var9 = 22;
            break;
         default:
            var9 = 78;
         }
      }

      while(true) {
         while(true) {
            var5[var10005] = (char)(var10006 ^ var10007 ^ var9);
            ++var0;
            if (var4 == 0) {
               var5 = var10002;
               var10005 = var4;
               var10008 = var10002[var4];
               var10007 = var4;
               var10006 = var10008;
               switch(var0 % 7) {
               case 0:
                  var9 = 71;
                  break;
               case 1:
                  var9 = 53;
                  break;
               case 2:
                  var9 = 120;
                  break;
               case 3:
                  var9 = 109;
                  break;
               case 4:
                  var9 = 116;
                  break;
               case 5:
                  var9 = 22;
                  break;
               default:
                  var9 = 78;
               }
            } else {
               if (var1 <= var0) {
                  var6 = (new String(var10002)).intern();
                  var3 = true;
                  Vulcan_l = var6;
                  var10000 = Vulcan_l;
                  Vulcan_i = new Vulcan_il(0, 0, 0);
                  return;
               }

               var5 = var10002;
               var10005 = var0;
               var10008 = var10002[var0];
               var10007 = var4;
               var10006 = var10008;
               switch(var0 % 7) {
               case 0:
                  var9 = 71;
                  break;
               case 1:
                  var9 = 53;
                  break;
               case 2:
                  var9 = 120;
                  break;
               case 3:
                  var9 = 109;
                  break;
               case 4:
                  var9 = 116;
                  break;
               case 5:
                  var9 = 22;
                  break;
               default:
                  var9 = 78;
               }
            }
         }
      }
   }

   public static void Vulcan_f(String var0) {
      Vulcan_v = var0;
   }

   public static String Vulcan_U() {
      return Vulcan_v;
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
   }
}
