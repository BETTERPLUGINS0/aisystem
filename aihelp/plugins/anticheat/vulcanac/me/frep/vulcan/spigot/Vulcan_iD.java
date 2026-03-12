package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.entity.Entity;

public class Vulcan_iD extends Vulcan_il {
   public static final Vulcan_iD Vulcan_t;
   private static final int Vulcan_U;
   private static final int Vulcan_n;
   private static final int Vulcan_M;
   private static final int Vulcan_j;
   private static final int Vulcan_s;
   private static final long Vulcan_w;
   private static final long Vulcan_g;
   private static final long Vulcan_e;
   private static final String Vulcan_P;
   private static final String[] b;

   public Vulcan_iD(int var1, int var2, int var3) {
      super(var1, var2, var3);
   }

   public Vulcan_iD(double var1, double var3, double var5) {
      super(var1, var3, var5);
   }

   public Vulcan_iD(Entity var1) {
      this(var1.getLocation().getX(), var1.getLocation().getY(), var1.getLocation().getZ());
   }

   public Vulcan_iD(Vulcan_XH var1) {
      this(var1.Vulcan_F, var1.Vulcan_g, var1.Vulcan_k);
   }

   public Vulcan_iD(Vulcan_il var1) {
      this(var1.Vulcan_Q(new Object[0]), var1.Vulcan_x(new Object[0]), var1.Vulcan_w(new Object[0]));
   }

   public Vulcan_iD Vulcan_Q(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      return new Vulcan_iD((double)this.Vulcan_Q(new Object[0]) + var2, (double)this.Vulcan_x(new Object[0]) + var4, (double)this.Vulcan_w(new Object[0]) + var6);
   }

   public Vulcan_iD Vulcan_b(Object[] var1) {
      int var3 = (Integer)var1[0];
      int var4 = (Integer)var1[1];
      int var2 = (Integer)var1[2];
      return new Vulcan_iD(this.Vulcan_Q(new Object[0]) + var3, this.Vulcan_x(new Object[0]) + var4, this.Vulcan_w(new Object[0]) + var2);
   }

   public Vulcan_iD Vulcan_S(Object[] var1) {
      Vulcan_il var2 = (Vulcan_il)var1[0];
      return new Vulcan_iD(this.Vulcan_Q(new Object[0]) + var2.Vulcan_Q(new Object[0]), this.Vulcan_x(new Object[0]) + var2.Vulcan_x(new Object[0]), this.Vulcan_w(new Object[0]) + var2.Vulcan_w(new Object[0]));
   }

   public Vulcan_iD Vulcan_X(Object[] var1) {
      Vulcan_il var2 = (Vulcan_il)var1[0];
      return new Vulcan_iD(this.Vulcan_Q(new Object[0]) - var2.Vulcan_Q(new Object[0]), this.Vulcan_x(new Object[0]) - var2.Vulcan_x(new Object[0]), this.Vulcan_w(new Object[0]) - var2.Vulcan_w(new Object[0]));
   }

   public Vulcan_iD Vulcan_W(Object[] var1) {
      int var2 = (Integer)var1[0];
      return new Vulcan_iD(this.Vulcan_Q(new Object[0]) * var2, this.Vulcan_x(new Object[0]) * var2, this.Vulcan_w(new Object[0]) * var2);
   }

   public Vulcan_iD Vulcan_q(Object[] var1) {
      return this.Vulcan_i(new Object[]{1});
   }

   public static String spigot() {
      return b[1];
   }

   public static String nonce() {
      return b[2];
   }

   public static String resource() {
      return b[0];
   }

   public Vulcan_iD Vulcan_i(Object[] var1) {
      int var2 = (Integer)var1[0];
      return this.Vulcan_N(new Object[]{Vulcan_Xy.UP, var2});
   }

   public Vulcan_iD Vulcan_I(Object[] var1) {
      return this.Vulcan_m(new Object[]{1});
   }

   public Vulcan_iD Vulcan_m(Object[] var1) {
      int var2 = (Integer)var1[0];
      return this.Vulcan_N(new Object[]{Vulcan_Xy.DOWN, var2});
   }

   public Vulcan_iD Vulcan_s(Object[] var1) {
      return this.Vulcan_j(new Object[]{1});
   }

   public Vulcan_iD Vulcan_j(Object[] var1) {
      int var2 = (Integer)var1[0];
      return this.Vulcan_N(new Object[]{Vulcan_Xy.NORTH, var2});
   }

   public Vulcan_iD Vulcan_E(Object[] var1) {
      return this.Vulcan_J(new Object[]{1});
   }

   public Vulcan_iD Vulcan_J(Object[] var1) {
      int var2 = (Integer)var1[0];
      return this.Vulcan_N(new Object[]{Vulcan_Xy.SOUTH, var2});
   }

   public Vulcan_iD Vulcan_o(Object[] var1) {
      return this.Vulcan_g(new Object[]{1});
   }

   public Vulcan_iD Vulcan_g(Object[] var1) {
      int var2 = (Integer)var1[0];
      return this.Vulcan_N(new Object[]{Vulcan_Xy.WEST, var2});
   }

   public Vulcan_iD Vulcan_l(Object[] var1) {
      return this.Vulcan_f(new Object[]{1});
   }

   public Vulcan_iD Vulcan_f(Object[] var1) {
      int var2 = (Integer)var1[0];
      return this.Vulcan_N(new Object[]{Vulcan_Xy.EAST, var2});
   }

   public Vulcan_iD Vulcan_D(Object[] var1) {
      Vulcan_Xy var2 = (Vulcan_Xy)var1[0];
      return this.Vulcan_N(new Object[]{var2, 1});
   }

   public Vulcan_iD Vulcan_N(Object[] var1) {
      Vulcan_Xy var3 = (Vulcan_Xy)var1[0];
      int var2 = (Integer)var1[1];
      return new Vulcan_iD(this.Vulcan_Q(new Object[0]) + var3.Vulcan_c() * var2, this.Vulcan_x(new Object[0]) + var3.Vulcan_g() * var2, this.Vulcan_w(new Object[0]) + var3.Vulcan_A() * var2);
   }

   public Vulcan_iD Vulcan_T(Object[] var1) {
      Vulcan_il var2 = (Vulcan_il)var1[0];
      return new Vulcan_iD(this.Vulcan_x(new Object[0]) * var2.Vulcan_w(new Object[0]) - this.Vulcan_w(new Object[0]) * var2.Vulcan_x(new Object[0]), this.Vulcan_w(new Object[0]) * var2.Vulcan_Q(new Object[0]) - this.Vulcan_Q(new Object[0]) * var2.Vulcan_w(new Object[0]), this.Vulcan_Q(new Object[0]) * var2.Vulcan_x(new Object[0]) - this.Vulcan_x(new Object[0]) * var2.Vulcan_Q(new Object[0]));
   }

   public long Vulcan_j(Object[] var1) {
      return ((long)this.Vulcan_Q(new Object[0]) & Vulcan_w) << Vulcan_s | ((long)this.Vulcan_x(new Object[0]) & Vulcan_g) << Vulcan_j | ((long)this.Vulcan_w(new Object[0]) & Vulcan_e) << 0;
   }

   public static Vulcan_iD Vulcan_H(Object[] var0) {
      long var1 = (Long)var0[0];
      int var3 = (int)(var1 << 64 - Vulcan_s - Vulcan_U >> 64 - Vulcan_U);
      int var4 = (int)(var1 << 64 - Vulcan_j - Vulcan_M >> 64 - Vulcan_M);
      int var5 = (int)(var1 << 64 - Vulcan_n >> 64 - Vulcan_n);
      return new Vulcan_iD(var3, var4, var5);
   }

   public static Iterable Vulcan_x(Object[] var0) {
      Vulcan_iD var1 = (Vulcan_iD)var0[0];
      Vulcan_iD var2 = (Vulcan_iD)var0[1];
      Vulcan_iD var3 = new Vulcan_iD(Math.min(var1.Vulcan_Q(new Object[0]), var2.Vulcan_Q(new Object[0])), Math.min(var1.Vulcan_x(new Object[0]), var2.Vulcan_x(new Object[0])), Math.min(var1.Vulcan_w(new Object[0]), var2.Vulcan_w(new Object[0])));
      Vulcan_iD var4 = new Vulcan_iD(Math.max(var1.Vulcan_Q(new Object[0]), var2.Vulcan_Q(new Object[0])), Math.max(var1.Vulcan_x(new Object[0]), var2.Vulcan_x(new Object[0])), Math.max(var1.Vulcan_w(new Object[0]), var2.Vulcan_w(new Object[0])));
      return new Vulcan_ix(var3, var4);
   }

   public static Iterable Vulcan_p(Object[] var0) {
      Vulcan_iD var2 = (Vulcan_iD)var0[0];
      Vulcan_iD var1 = (Vulcan_iD)var0[1];
      Vulcan_iD var3 = new Vulcan_iD(Math.min(var2.Vulcan_Q(new Object[0]), var1.Vulcan_Q(new Object[0])), Math.min(var2.Vulcan_x(new Object[0]), var1.Vulcan_x(new Object[0])), Math.min(var2.Vulcan_w(new Object[0]), var1.Vulcan_w(new Object[0])));
      Vulcan_iD var4 = new Vulcan_iD(Math.max(var2.Vulcan_Q(new Object[0]), var1.Vulcan_Q(new Object[0])), Math.max(var2.Vulcan_x(new Object[0]), var1.Vulcan_x(new Object[0])), Math.max(var2.Vulcan_w(new Object[0]), var1.Vulcan_w(new Object[0])));
      return new Vulcan_XC(var3, var4);
   }

   public Vulcan_il Vulcan_K(Object[] var1) {
      Vulcan_il var2 = (Vulcan_il)var1[0];
      return this.Vulcan_T(new Object[]{var2});
   }

   static {
      long var9 = me.frep.vulcan.spigot.Vulcan_n.a(-7544789184504621821L, 6989688881149964570L, MethodHandles.lookup().lookupClass()).a(42983464875487L) ^ 79324768506590L;
      long var11 = var9 ^ 112566416550299L;
      Cipher var0;
      Cipher var10000 = var0 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var9 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var1 = 1; var1 < 8; ++var1) {
         var10003[var1] = (byte)((int)(var9 << var1 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var7 = new String[4];
      int var5 = 0;
      String var4 = "]O\bÆ\u001a\u0097\u008bV»ªT\u0082ÕÂ\u0099\u0081}#QÒÝ·Ïè\u0010\fë\u0096Ý0N\u0091o\u008aKà\u0019\u0084[³\u0095";
      int var6 = "]O\bÆ\u001a\u0097\u008bV»ªT\u0082ÕÂ\u0099\u0081}#QÒÝ·Ïè\u0010\fë\u0096Ý0N\u0091o\u008aKà\u0019\u0084[³\u0095".length();
      char var3 = 24;
      int var2 = -1;

      label27:
      while(true) {
         ++var2;
         String var13 = var4.substring(var2, var2 + var3);
         byte var10001 = -1;

         while(true) {
            byte[] var8 = var0.doFinal(var13.getBytes("ISO-8859-1"));
            String var17 = a(var8).intern();
            switch(var10001) {
            case 0:
               var7[var5++] = var17;
               if ((var2 += var3) >= var6) {
                  b = var7;
                  Vulcan_P = b[3];
                  Vulcan_t = new Vulcan_iD(0, 0, 0);
                  Vulcan_U = 1 + Vulcan_eN.Vulcan_I(new Object[]{Vulcan_eN.Vulcan_L(new Object[]{30000000}), var11});
                  Vulcan_n = Vulcan_U;
                  Vulcan_M = 64 - Vulcan_U - Vulcan_n;
                  Vulcan_j = 0 + Vulcan_n;
                  Vulcan_s = Vulcan_j + Vulcan_M;
                  Vulcan_w = (1L << Vulcan_U) - 1L;
                  Vulcan_g = (1L << Vulcan_M) - 1L;
                  Vulcan_e = (1L << Vulcan_n) - 1L;
                  return;
               }

               var3 = var4.charAt(var2);
               break;
            default:
               var7[var5++] = var17;
               if ((var2 += var3) < var6) {
                  var3 = var4.charAt(var2);
                  continue label27;
               }

               var4 = "V\u0018\u0092L\u0084P\u0082 ÀäÍj(ü\bá\u0010ÿzÕ\\ÍCc¿µ;ñÜ\u0093gMý";
               var6 = "V\u0018\u0092L\u0084P\u0082 ÀäÍj(ü\bá\u0010ÿzÕ\\ÍCc¿µ;ñÜ\u0093gMý".length();
               var3 = 16;
               var2 = -1;
            }

            ++var2;
            var13 = var4.substring(var2, var2 + var3);
            var10001 = 0;
         }
      }
   }

   private static String a(byte[] var0) {
      int var1 = 0;
      int var2;
      char[] var3 = new char[var2 = var0.length];

      for(int var4 = 0; var4 < var2; ++var4) {
         int var5;
         if ((var5 = 255 & var0[var4]) < 192) {
            var3[var1++] = (char)var5;
         } else {
            char var6;
            byte var7;
            if (var5 < 224) {
               var6 = (char)((char)(var5 & 31) << 6);
               ++var4;
               var7 = var0[var4];
               var6 |= (char)(var7 & 63);
               var3[var1++] = var6;
            } else if (var4 < var2 - 2) {
               var6 = (char)((char)(var5 & 15) << 12);
               ++var4;
               var7 = var0[var4];
               var6 = (char)(var6 | (char)(var7 & 63) << 6);
               ++var4;
               var7 = var0[var4];
               var6 |= (char)(var7 & 63);
               var3[var1++] = var6;
            }
         }
      }

      return new String(var3, 0, var1);
   }
}
