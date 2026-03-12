package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Vulcan_XH {
   public final double Vulcan_F;
   public final double Vulcan_g;
   public final double Vulcan_k;
   private static final String Vulcan_y;
   private static final long a = Vulcan_n.a(-2954245010853425021L, -697713561771497839L, MethodHandles.lookup().lookupClass()).a(237290760929477L);
   private static final String[] b;

   public static String spigot() {
      return b[0];
   }

   public static String nonce() {
      return b[3];
   }

   public static String resource() {
      return b[2];
   }

   public Vulcan_XH(double var1, double var3, double var5) {
      long var7 = a ^ 18972143944463L;
      String var10000 = Vulcan_il.Vulcan_U();
      super();
      String var9 = var10000;

      int var13;
      label53: {
         label52: {
            try {
               double var15;
               var13 = (var15 = var1 - -0.0D) == 0.0D ? 0 : (var15 < 0.0D ? -1 : 1);
               if (var9 == null) {
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
                     if (var9 == null) {
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
                  if (var9 == null) {
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

      this.Vulcan_F = var1;
      this.Vulcan_g = var3;
      this.Vulcan_k = var5;
   }

   public Vulcan_XH Vulcan_T(Object[] var1) {
      Vulcan_XH var2 = (Vulcan_XH)var1[0];
      return new Vulcan_XH(var2.Vulcan_F - this.Vulcan_F, var2.Vulcan_g - this.Vulcan_g, var2.Vulcan_k - this.Vulcan_k);
   }

   public Vulcan_XH Vulcan_t(Object[] var1) {
      long var2 = (Long)var1[0];
      long var10000 = a ^ var2;
      double var4 = (double)Vulcan_eN.Vulcan_R(new Object[]{this.Vulcan_F * this.Vulcan_F + this.Vulcan_g * this.Vulcan_g + this.Vulcan_k * this.Vulcan_k});

      Vulcan_XH var7;
      try {
         if (var4 < 1.0E-4D) {
            var7 = new Vulcan_XH(0.0D, 0.0D, 0.0D);
            return var7;
         }
      } catch (RuntimeException var6) {
         throw a(var6);
      }

      var7 = new Vulcan_XH(this.Vulcan_F / var4, this.Vulcan_g / var4, this.Vulcan_k / var4);
      return var7;
   }

   public double Vulcan_F(Object[] var1) {
      Vulcan_XH var2 = (Vulcan_XH)var1[0];
      return this.Vulcan_F * var2.Vulcan_F + this.Vulcan_g * var2.Vulcan_g + this.Vulcan_k * var2.Vulcan_k;
   }

   public Vulcan_XH Vulcan_r(Object[] var1) {
      Vulcan_XH var2 = (Vulcan_XH)var1[0];
      return new Vulcan_XH(this.Vulcan_g * var2.Vulcan_k - this.Vulcan_k * var2.Vulcan_g, this.Vulcan_k * var2.Vulcan_F - this.Vulcan_F * var2.Vulcan_k, this.Vulcan_F * var2.Vulcan_g - this.Vulcan_g * var2.Vulcan_F);
   }

   public Vulcan_XH Vulcan_Z(Object[] var1) {
      Vulcan_XH var2 = (Vulcan_XH)var1[0];
      double var10002 = var2.Vulcan_g;
      return this.Vulcan__(new Object[]{var2.Vulcan_F, var10002, var2.Vulcan_k});
   }

   public Vulcan_XH Vulcan__(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      return this.Vulcan_H(new Object[]{-var2, -var4, -var6});
   }

   public Vulcan_XH Vulcan_R(Object[] var1) {
      Vulcan_XH var2 = (Vulcan_XH)var1[0];
      double var10002 = var2.Vulcan_g;
      return this.Vulcan_H(new Object[]{var2.Vulcan_F, var10002, var2.Vulcan_k});
   }

   public Vulcan_XH Vulcan_H(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      return new Vulcan_XH(this.Vulcan_F + var2, this.Vulcan_g + var4, this.Vulcan_k + var6);
   }

   public double Vulcan_L(Object[] var1) {
      Vulcan_XH var2 = (Vulcan_XH)var1[0];
      double var3 = var2.Vulcan_F - this.Vulcan_F;
      double var5 = var2.Vulcan_g - this.Vulcan_g;
      double var7 = var2.Vulcan_k - this.Vulcan_k;
      return (double)Vulcan_eN.Vulcan_R(new Object[]{var3 * var3 + var5 * var5 + var7 * var7});
   }

   public double Vulcan_T(Object[] var1) {
      Vulcan_XH var2 = (Vulcan_XH)var1[0];
      double var3 = var2.Vulcan_F - this.Vulcan_F;
      double var5 = var2.Vulcan_g - this.Vulcan_g;
      double var7 = var2.Vulcan_k - this.Vulcan_k;
      return var3 * var3 + var5 * var5 + var7 * var7;
   }

   public double Vulcan_k(Object[] var1) {
      return (double)Vulcan_eN.Vulcan_R(new Object[]{this.Vulcan_F * this.Vulcan_F + this.Vulcan_g * this.Vulcan_g + this.Vulcan_k * this.Vulcan_k});
   }

   public Vulcan_XH Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_XH Vulcan_e(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_XH Vulcan_c(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      StringBuilder var10000 = (new StringBuilder()).append("(").append(this.Vulcan_F);
      String[] var1 = b;
      return var10000.append(var1[4]).append(this.Vulcan_g).append(var1[5]).append(this.Vulcan_k).append(")").toString();
   }

   public Vulcan_XH Vulcan_l(Object[] var1) {
      float var2 = (Float)var1[0];
      float var3 = Vulcan_eN.Vulcan_E(new Object[]{var2});
      float var4 = Vulcan_eN.Vulcan_h(new Object[]{var2});
      double var5 = this.Vulcan_F;
      double var7 = this.Vulcan_g * (double)var3 + this.Vulcan_k * (double)var4;
      double var9 = this.Vulcan_k * (double)var3 - this.Vulcan_g * (double)var4;
      return new Vulcan_XH(var5, var7, var9);
   }

   public Vulcan_XH Vulcan_K(Object[] var1) {
      float var2 = (Float)var1[0];
      float var3 = Vulcan_eN.Vulcan_E(new Object[]{var2});
      float var4 = Vulcan_eN.Vulcan_h(new Object[]{var2});
      double var5 = this.Vulcan_F * (double)var3 + this.Vulcan_k * (double)var4;
      double var7 = this.Vulcan_g;
      double var9 = this.Vulcan_k * (double)var3 - this.Vulcan_F * (double)var4;
      return new Vulcan_XH(var5, var7, var9);
   }

   public double Vulcan_j(Object[] var1) {
      return this.Vulcan_F;
   }

   public double Vulcan_d(Object[] var1) {
      return this.Vulcan_g;
   }

   public double Vulcan_G(Object[] var1) {
      return this.Vulcan_k;
   }

   static {
      long var0 = a ^ 115968278898234L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[6];
      int var7 = 0;
      String var6 = "ñð?¢ú\u0004\u0018;¨\u0096\u0092ÿácõ\"\u0010\u00ad\u0007ún\u001eS\u009b\u009eki\u0094ÿÐù¯·\u0018\u0000ÿþàl\u00172i\u0092\b:\u001fü\u009eP\u0084t\r\u008c\u0084éo\u0012\u008f\u0010\u001cå¹×\u0007I\u001cV7Ì6,öÌ\u0013\u0089";
      int var8 = "ñð?¢ú\u0004\u0018;¨\u0096\u0092ÿácõ\"\u0010\u00ad\u0007ún\u001eS\u009b\u009eki\u0094ÿÐù¯·\u0018\u0000ÿþàl\u00172i\u0092\b:\u001fü\u009eP\u0084t\r\u008c\u0084éo\u0012\u008f\u0010\u001cå¹×\u0007I\u001cV7Ì6,öÌ\u0013\u0089".length();
      char var5 = 16;
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var11 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var11.getBytes("ISO-8859-1"));
            String var15 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var15;
               if ((var4 += var5) >= var8) {
                  b = var9;
                  Vulcan_y = b[1];
                  return;
               }

               var5 = var6.charAt(var4);
               break;
            default:
               var9[var7++] = var15;
               if ((var4 += var5) < var8) {
                  var5 = var6.charAt(var4);
                  continue label27;
               }

               var6 = "$>\u008f?¿ïH¼\b$>\u008f?¿ïH¼";
               var8 = "$>\u008f?¿ïH¼\b$>\u008f?¿ïH¼".length();
               var5 = '\b';
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
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
