package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class Vulcan_ez {
   private final double Vulcan_v;
   private final double Vulcan_r;
   private final double Vulcan_e;
   private static String[] Vulcan_X;
   private static final long a = Vulcan_n.a(3172807900302218161L, -5484384363967343923L, MethodHandles.lookup().lookupClass()).a(211418945703714L);
   private static final String[] b;

   public Vulcan_ez(double var1, double var3, double var5) {
      long var7 = a ^ 47595817598670L;
      String[] var10000 = Vulcan_V();
      super();
      String[] var9 = var10000;

      int var13;
      label53: {
         label52: {
            try {
               double var15;
               var13 = (var15 = var1 - -0.0D) == 0.0D ? 0 : (var15 < 0.0D ? -1 : 1);
               if (var9 != null) {
                  break label53;
               }

               if (var13 != 0) {
                  break label52;
               }
            } catch (RuntimeException var12) {
               throw a(var12);
            }

            var1 = 0.0D;
         }

         double var16;
         var13 = (var16 = var3 - -0.0D) == 0.0D ? 0 : (var16 < 0.0D ? -1 : 1);
      }

      label44: {
         double var14;
         label57: {
            label42: {
               label41: {
                  try {
                     if (var9 != null) {
                        break label42;
                     }

                     if (var13 != 0) {
                        break label41;
                     }
                  } catch (RuntimeException var11) {
                     throw a(var11);
                  }

                  var3 = 0.0D;
               }

               try {
                  var14 = var5;
                  if (var9 != null) {
                     break label57;
                  }

                  double var17;
                  var13 = (var17 = var5 - -0.0D) == 0.0D ? 0 : (var17 < 0.0D ? -1 : 1);
               } catch (RuntimeException var10) {
                  throw a(var10);
               }
            }

            if (var13 != 0) {
               break label44;
            }

            var14 = 0.0D;
         }

         var5 = var14;
      }

      this.Vulcan_v = var1;
      this.Vulcan_r = var3;
      this.Vulcan_e = var5;
   }

   public Vulcan_ez(Vulcan_ez var1) {
      this(var1.Vulcan_N(new Object[0]), var1.Vulcan_H(new Object[0]), var1.Vulcan_P(new Object[0]));
   }

   public Vulcan_ez(Vector var1) {
      this(var1.getX(), var1.getY(), var1.getZ());
   }

   public Vulcan_ez Vulcan_H(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      return new Vulcan_ez(var2.Vulcan_v - this.Vulcan_v, var2.Vulcan_r - this.Vulcan_r, var2.Vulcan_e - this.Vulcan_e);
   }

   public Vulcan_ez Vulcan_W(Object[] var1) {
      long var2 = (Long)var1[0];
      long var10000 = a ^ var2;
      double var4 = (double)Vulcan_eN.Vulcan_R(new Object[]{this.Vulcan_v * this.Vulcan_v + this.Vulcan_r * this.Vulcan_r + this.Vulcan_e * this.Vulcan_e});

      Vulcan_ez var7;
      try {
         if (var4 < 1.0E-4D) {
            var7 = new Vulcan_ez(0.0D, 0.0D, 0.0D);
            return var7;
         }
      } catch (RuntimeException var6) {
         throw a(var6);
      }

      var7 = new Vulcan_ez(this.Vulcan_v / var4, this.Vulcan_r / var4, this.Vulcan_e / var4);
      return var7;
   }

   public double Vulcan_r(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      return this.Vulcan_v * var2.Vulcan_v + this.Vulcan_r * var2.Vulcan_r + this.Vulcan_e * var2.Vulcan_e;
   }

   public Vulcan_ez Vulcan_l(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      return new Vulcan_ez(this.Vulcan_r * var2.Vulcan_e - this.Vulcan_e * var2.Vulcan_r, this.Vulcan_e * var2.Vulcan_v - this.Vulcan_v * var2.Vulcan_e, this.Vulcan_v * var2.Vulcan_r - this.Vulcan_r * var2.Vulcan_v);
   }

   public Vulcan_ez Vulcan_N(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      double var10002 = var2.Vulcan_r;
      return this.Vulcan_u(new Object[]{var2.Vulcan_v, var10002, var2.Vulcan_e});
   }

   public Vulcan_ez Vulcan_u(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      return this.Vulcan_c(new Object[]{-var2, -var4, -var6});
   }

   public Vulcan_ez Vulcan_j(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      double var10002 = var2.Vulcan_r;
      return this.Vulcan_c(new Object[]{var2.Vulcan_v, var10002, var2.Vulcan_e});
   }

   public Vulcan_ez Vulcan_c(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      return new Vulcan_ez(this.Vulcan_v + var2, this.Vulcan_r + var4, this.Vulcan_e + var6);
   }

   public double Vulcan_j(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      double var3 = var2.Vulcan_v - this.Vulcan_v;
      double var5 = var2.Vulcan_r - this.Vulcan_r;
      double var7 = var2.Vulcan_e - this.Vulcan_e;
      return (double)Vulcan_eN.Vulcan_R(new Object[]{var3 * var3 + var5 * var5 + var7 * var7});
   }

   public double Vulcan_R(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      double var3 = var2.Vulcan_v - this.Vulcan_v;
      double var5 = var2.Vulcan_e - this.Vulcan_e;
      return (double)Vulcan_eN.Vulcan_R(new Object[]{var3 * var3 + var5 * var5});
   }

   public double Vulcan_g(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      double var3 = var2.Vulcan_v - this.Vulcan_v;
      double var5 = var2.Vulcan_r - this.Vulcan_r;
      double var7 = var2.Vulcan_e - this.Vulcan_e;
      return var3 * var3 + var5 * var5 + var7 * var7;
   }

   public double Vulcan_b(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      double var3 = var2.Vulcan_v - this.Vulcan_v;
      double var5 = var2.Vulcan_e - this.Vulcan_e;
      return var3 * var3 + var5 * var5;
   }

   public double Vulcan_c(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      return var2.Vulcan_r - this.Vulcan_r;
   }

   public double Vulcan_h(Object[] var1) {
      return (double)Vulcan_eN.Vulcan_R(new Object[]{this.Vulcan_v * this.Vulcan_v + this.Vulcan_r * this.Vulcan_r + this.Vulcan_e * this.Vulcan_e});
   }

   public double Vulcan_y(Object[] var1) {
      return this.Vulcan_v * this.Vulcan_v + this.Vulcan_r * this.Vulcan_r + this.Vulcan_e * this.Vulcan_e;
   }

   public double Vulcan_d(Object[] var1) {
      return this.Vulcan_v * this.Vulcan_v + this.Vulcan_e * this.Vulcan_e;
   }

   public Vulcan_ez Vulcan_w(Object[] var1) {
      BlockFace var2 = (BlockFace)var1[0];
      return new Vulcan_ez(this.Vulcan_v + (double)var2.getModX(), this.Vulcan_r + (double)var2.getModY(), this.Vulcan_e + (double)var2.getModZ());
   }

   public Vulcan_ez Vulcan_b(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_ez Vulcan_Y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_ez Vulcan_q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_ez Vulcan_k(Object[] var1) {
      long var3 = (Long)var1[0];
      float var2 = (Float)var1[1];
      long var10000 = a ^ var3;
      Vulcan_V();
      float var6 = Vulcan_eN.Vulcan_E(new Object[]{var2});
      float var7 = Vulcan_eN.Vulcan_h(new Object[]{var2});
      double var8 = this.Vulcan_v;
      double var10 = this.Vulcan_r * (double)var6 + this.Vulcan_e * (double)var7;
      double var12 = this.Vulcan_e * (double)var6 - this.Vulcan_r * (double)var7;

      try {
         Vulcan_ez var15 = new Vulcan_ez(var8, var10, var12);
         if (AbstractCheck.Vulcan_V() == 0) {
            Vulcan_F(new String[3]);
         }

         return var15;
      } catch (RuntimeException var14) {
         throw a(var14);
      }
   }

   public Vulcan_ez Vulcan_B(Object[] var1) {
      float var4 = (Float)var1[0];
      long var2 = (Long)var1[1];
      long var10000 = a ^ var2;
      float var7 = Vulcan_eN.Vulcan_E(new Object[]{var4});
      float var8 = Vulcan_eN.Vulcan_h(new Object[]{var4});
      double var9 = this.Vulcan_v * (double)var7 + this.Vulcan_e * (double)var8;
      double var11 = this.Vulcan_r;
      String[] var15 = Vulcan_V();
      double var13 = this.Vulcan_e * (double)var7 - this.Vulcan_v * (double)var8;
      String[] var5 = var15;
      Vulcan_ez var16 = new Vulcan_ez(var9, var11, var13);
      if (var5 != null) {
         int var6 = AbstractCheck.Vulcan_V();
         ++var6;
         AbstractCheck.Vulcan_H(var6);
      }

      return var16;
   }

   public Vector Vulcan_Z(Object[] var1) {
      return new Vector(this.Vulcan_v, this.Vulcan_r, this.Vulcan_e);
   }

   public int Vulcan_r(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 23553675313885L;
      double var10001 = this.Vulcan_v;
      return Vulcan_eN.Vulcan_c(new Object[]{var4, var10001});
   }

   public int Vulcan_v(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 1860721939427L;
      double var10001 = this.Vulcan_r;
      return Vulcan_eN.Vulcan_c(new Object[]{var4, var10001});
   }

   public int Vulcan_f(Object[] var1) {
      long var2 = (Long)var1[0];
      var2 ^= a;
      long var4 = var2 ^ 28656719778718L;
      double var10001 = this.Vulcan_e;
      return Vulcan_eN.Vulcan_c(new Object[]{var4, var10001});
   }

   public Vulcan_ez Vulcan_o(Object[] var1) {
      double var2 = (Double)var1[0];
      return this.Vulcan_e(new Object[]{var2, var2, var2});
   }

   public Vulcan_ez Vulcan_e(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      return new Vulcan_ez(this.Vulcan_v * var2, this.Vulcan_r * var4, this.Vulcan_e * var6);
   }

   public double Vulcan_V(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      return this.Vulcan_v * var2.Vulcan_v + this.Vulcan_r * var2.Vulcan_r + this.Vulcan_e * var2.Vulcan_e;
   }

   public float Vulcan__(Object[] var1) {
      Vulcan_ez var2 = (Vulcan_ez)var1[0];
      double var3 = this.Vulcan_V(new Object[]{var2}) / (this.Vulcan_h(new Object[0]) * var2.Vulcan_h(new Object[0]));
      return (float)Math.acos(var3);
   }

   public String toString() {
      StringBuilder var10000 = new StringBuilder();
      String[] var1 = b;
      return var10000.append(var1[1]).append(this.Vulcan_v).append(var1[0]).append(this.Vulcan_r).append(var1[2]).append(this.Vulcan_e).append('}').toString();
   }

   public double Vulcan_N(Object[] var1) {
      return this.Vulcan_v;
   }

   public double Vulcan_H(Object[] var1) {
      return this.Vulcan_r;
   }

   public double Vulcan_P(Object[] var1) {
      return this.Vulcan_e;
   }

   public static void Vulcan_F(String[] var0) {
      Vulcan_X = var0;
   }

   public static String[] Vulcan_V() {
      return Vulcan_X;
   }

   static {
      long var0 = a ^ 116579720428191L;
      Vulcan_F((String[])null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[3];
      int var7 = 0;
      String var6 = "3ùÅ\u001c=¢TC\u0010pèß\u0015ÚSàïßÑ\u0001#÷y9?\b.\u0094\u0097.P\u0099Jö";
      int var8 = "3ùÅ\u001c=¢TC\u0010pèß\u0015ÚSàïßÑ\u0001#÷y9?\b.\u0094\u0097.P\u0099Jö".length();
      char var5 = '\b';
      int var4 = -1;

      while(true) {
         ++var4;
         byte[] var10 = var2.doFinal(var6.substring(var4, var4 + var5).getBytes("ISO-8859-1"));
         String var12 = a(var10).intern();
         boolean var10001 = true;
         var9[var7++] = var12;
         if ((var4 += var5) >= var8) {
            b = var9;
            return;
         }

         var5 = var6.charAt(var4);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
      return var0;
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
