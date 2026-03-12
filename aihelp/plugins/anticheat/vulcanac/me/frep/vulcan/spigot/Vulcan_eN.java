package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.Random;
import java.util.UUID;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;

public class Vulcan_eN {
   public static final float Vulcan_S;
   private static final float[] Vulcan_e;
   private static final int[] Vulcan_b;
   private static final String Vulcan_L;
   private static final long a = Vulcan_n.a(6176786137873068914L, -1035253428736940625L, MethodHandles.lookup().lookupClass()).a(204042611245346L);
   private static final String[] b;

   public static float Vulcan_h(Object[] var0) {
      float var1 = (Float)var0[0];
      return Vulcan_e[(int)(var1 * 10430.378F) & '\uffff'];
   }

   public static String spigot() {
      return b[0];
   }

   public static String nonce() {
      return b[2];
   }

   public static String resource() {
      return b[1];
   }

   public static float Vulcan_E(Object[] var0) {
      float var1 = (Float)var0[0];
      return Vulcan_e[(int)(var1 * 10430.378F + 16384.0F) & '\uffff'];
   }

   public static float Vulcan_a(Object[] var0) {
      float var1 = (Float)var0[0];
      return (float)Math.sqrt((double)var1);
   }

   public static float Vulcan_R(Object[] var0) {
      double var1 = (Double)var0[0];
      return (float)Math.sqrt(var1);
   }

   public static int Vulcan_M(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_v(Object[] var0) {
      double var1 = (Double)var0[0];
      return (int)(var1 + 1024.0D) - 1024;
   }

   public static int Vulcan_c(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static long Vulcan_T(Object[] var0) {
      double var1 = (Double)var0[0];
      long var3 = (Long)var0[1];
      long var10000 = a ^ var3;
      long var5 = (long)var1;

      try {
         if (var1 < (double)var5) {
            var10000 = var5 - 1L;
            return var10000;
         }
      } catch (RuntimeException var7) {
         throw a(var7);
      }

      var10000 = var5;
      return var10000;
   }

   public static int Vulcan_J(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static float Vulcan_S(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_g(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_l(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_o(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_Y(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static float Vulcan_V(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_v(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_F(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_f(Object[] var0) {
      long var1 = (Long)var0[0];
      double var3 = (Double)var0[1];
      double var5 = (Double)var0[2];
      long var10000 = a ^ var1;
      String var7 = Vulcan_il.Vulcan_U();

      int var11;
      label51: {
         label50: {
            try {
               double var13;
               var11 = (var13 = var3 - 0.0D) == 0.0D ? 0 : (var13 < 0.0D ? -1 : 1);
               if (var7 == null) {
                  break label51;
               }

               if (var11 >= 0) {
                  break label50;
               }
            } catch (RuntimeException var10) {
               throw a(var10);
            }

            var3 = -var3;
         }

         double var14;
         var11 = (var14 = var5 - 0.0D) == 0.0D ? 0 : (var14 < 0.0D ? -1 : 1);
      }

      double var12;
      label41: {
         label40: {
            try {
               if (var7 == null) {
                  break label41;
               }

               if (var11 >= 0) {
                  break label40;
               }
            } catch (RuntimeException var9) {
               throw a(var9);
            }

            var5 = -var5;
         }

         try {
            var12 = var3;
            if (var7 == null) {
               return var12;
            }

            double var15;
            var11 = (var15 = var3 - var5) == 0.0D ? 0 : (var15 < 0.0D ? -1 : 1);
         } catch (RuntimeException var8) {
            throw a(var8);
         }
      }

      var12 = var11 > 0 ? var3 : var5;
      return var12;
   }

   public static int Vulcan_q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_C(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static float Vulcan_c(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan__(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_O(Object[] var0) {
      long var1 = (Long)var0[0];
      long[] var3 = (long[])var0[1];
      var1 ^= a;
      String var10000 = Vulcan_il.Vulcan_U();
      long var5 = 0L;
      String var4 = var10000;
      long[] var7 = var3;
      int var8 = var3.length;
      int var9 = 0;

      long var12;
      while(true) {
         if (var9 < var8) {
            long var10 = var7[var9];
            if (var1 > 0L) {
               var12 = var5 + var10;
               if (var4 == null) {
                  break;
               }

               var5 = var12;
               ++var9;
            }

            if (var4 != null) {
               continue;
            }
         }

         var12 = var5;
         break;
      }

      return (double)var12 / (double)var3.length;
   }

   public static boolean Vulcan_J(Object[] var0) {
      long var2 = (Long)var0[0];
      float var4 = (Float)var0[1];
      float var1 = (Float)var0[2];
      var2 ^= a;
      long var5 = var2 ^ 115101166319455L;
      String var7 = Vulcan_il.Vulcan_U();

      int var10000;
      label32: {
         try {
            float var9;
            var10000 = (var9 = Vulcan_S(new Object[]{var1 - var4, var5}) - 1.0E-5F) == 0.0F ? 0 : (var9 < 0.0F ? -1 : 1);
            if (var7 == null) {
               return (boolean)var10000;
            }

            if (var10000 < 0) {
               break label32;
            }
         } catch (RuntimeException var8) {
            throw a(var8);
         }

         var10000 = 0;
         return (boolean)var10000;
      }

      var10000 = 1;
      return (boolean)var10000;
   }

   public static int Vulcan_S(Object[] var0) {
      int var2 = (Integer)var0[0];
      int var1 = (Integer)var0[1];
      return (var2 % var1 + var1) % var1;
   }

   public static float Vulcan_k(Object[] var0) {
      float var1 = (Float)var0[0];
      long var2 = (Long)var0[1];
      long var10000 = a ^ var2;
      String var7 = Vulcan_il.Vulcan_U();
      var1 %= 360.0F;
      String var4 = var7;

      int var8;
      float var9;
      label34: {
         label33: {
            try {
               float var10;
               var8 = (var10 = var1 - 180.0F) == 0.0F ? 0 : (var10 < 0.0F ? -1 : 1);
               if (var4 == null) {
                  break label34;
               }

               if (var8 < 0) {
                  break label33;
               }
            } catch (RuntimeException var6) {
               throw a(var6);
            }

            var1 -= 360.0F;
         }

         try {
            var9 = var1;
            if (var4 == null) {
               return var9;
            }

            float var11;
            var8 = (var11 = var1 - -180.0F) == 0.0F ? 0 : (var11 < 0.0F ? -1 : 1);
         } catch (RuntimeException var5) {
            throw a(var5);
         }
      }

      if (var8 < 0) {
         var1 += 360.0F;
      }

      var9 = var1;
      return var9;
   }

   public static double Vulcan_i(Object[] var0) {
      long var1 = (Long)var0[0];
      double var3 = (Double)var0[1];
      long var10000 = a ^ var1;
      String var8 = Vulcan_il.Vulcan_U();
      var3 %= 360.0D;
      String var5 = var8;

      int var9;
      double var10;
      label34: {
         label33: {
            try {
               double var11;
               var9 = (var11 = var3 - 180.0D) == 0.0D ? 0 : (var11 < 0.0D ? -1 : 1);
               if (var5 == null) {
                  break label34;
               }

               if (var9 < 0) {
                  break label33;
               }
            } catch (RuntimeException var7) {
               throw a(var7);
            }

            var3 -= 360.0D;
         }

         try {
            var10 = var3;
            if (var5 == null) {
               return var10;
            }

            double var12;
            var9 = (var12 = var3 - -180.0D) == 0.0D ? 0 : (var12 < 0.0D ? -1 : 1);
         } catch (RuntimeException var6) {
            throw a(var6);
         }
      }

      if (var9 < 0) {
         var3 += 360.0D;
      }

      var10 = var3;
      return var10;
   }

   public static int Vulcan_E(Object[] var0) {
      String var2 = (String)var0[0];
      int var1 = (Integer)var0[1];

      try {
         return Integer.parseInt(var2);
      } catch (Throwable var4) {
         return var1;
      }
   }

   public static int Vulcan__(Object[] var0) {
      String var1 = (String)var0[0];
      int var2 = (Integer)var0[1];
      int var3 = (Integer)var0[2];
      return Math.max(var3, Vulcan_E(new Object[]{var1, var2}));
   }

   public static double Vulcan_E(Object[] var0) {
      String var3 = (String)var0[0];
      double var1 = (Double)var0[1];

      try {
         return Double.parseDouble(var3);
      } catch (Throwable var5) {
         return var1;
      }
   }

   public static double Vulcan_o(Object[] var0) {
      String var1 = (String)var0[0];
      double var2 = (Double)var0[1];
      double var4 = (Double)var0[2];
      return Math.max(var4, Vulcan_E(new Object[]{var1, var2}));
   }

   public static int Vulcan_L(Object[] var0) {
      int var1 = (Integer)var0[0];
      int var2 = var1 - 1;
      var2 |= var2 >> 1;
      var2 |= var2 >> 2;
      var2 |= var2 >> 4;
      var2 |= var2 >> 8;
      var2 |= var2 >> 16;
      return var2 + 1;
   }

   private static boolean Vulcan_l(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static int Vulcan_K(Object[] var0) {
      int var3 = (Integer)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 46299342228097L;

      int var10000;
      label17: {
         try {
            if (Vulcan_l(new Object[]{var3, var4})) {
               var10000 = var3;
               break label17;
            }
         } catch (RuntimeException var6) {
            throw a(var6);
         }

         var10000 = Vulcan_L(new Object[]{var3});
      }

      var3 = var10000;
      return Vulcan_b[(int)((long)var3 * 125613361L >> 27) & 31];
   }

   public static int Vulcan_I(Object[] var0) {
      int var1 = (Integer)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 7463126287487L;
      long var6 = var2 ^ 10793689771451L;

      int var10000;
      byte var10001;
      try {
         var10000 = Vulcan_K(new Object[]{var1, var4});
         if (Vulcan_l(new Object[]{var1, var6})) {
            var10001 = 0;
            return var10000 - var10001;
         }
      } catch (RuntimeException var8) {
         throw a(var8);
      }

      var10001 = 1;
      return var10000 - var10001;
   }

   public static int Vulcan_F(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_A(Object[] var0) {
      float var2 = (Float)var0[0];
      float var1 = (Float)var0[1];
      float var3 = (Float)var0[2];
      long var4 = (Long)var0[3];
      var4 ^= a;
      long var6 = var4 ^ 57474675944367L;
      return Vulcan_b(new Object[]{Vulcan_M(new Object[]{var2 * 255.0F, var6}), Vulcan_M(new Object[]{var1 * 255.0F, var6}), Vulcan_M(new Object[]{var3 * 255.0F, var6})});
   }

   public static int Vulcan_b(Object[] var0) {
      int var2 = (Integer)var0[0];
      int var3 = (Integer)var0[1];
      int var1 = (Integer)var0[2];
      int var4 = (var2 << 8) + var3;
      var4 = (var4 << 8) + var1;
      return var4;
   }

   public static int Vulcan_m(Object[] var0) {
      int var1 = (Integer)var0[0];
      int var4 = (Integer)var0[1];
      long var2 = (Long)var0[2];
      long var10000 = a ^ var2;
      int var6 = (var1 & 16711680) >> 16;
      int var7 = (var4 & 16711680) >> 16;
      int var8 = (var1 & '\uff00') >> 8;
      Vulcan_il.Vulcan_U();
      int var9 = (var4 & '\uff00') >> 8;
      int var10 = (var1 & 255) >> 0;
      int var11 = (var4 & 255) >> 0;
      int var12 = (int)((float)var6 * (float)var7 / 255.0F);
      int var13 = (int)((float)var8 * (float)var9 / 255.0F);
      int var14 = (int)((float)var10 * (float)var11 / 255.0F);

      try {
         int var16 = var1 & -16777216 | var12 << 16 | var13 << 8 | var14;
         if (AbstractCheck.Vulcan_m() != 0) {
            Vulcan_il.Vulcan_f("i3Jkpc");
         }

         return var16;
      } catch (RuntimeException var15) {
         throw a(var15);
      }
   }

   public static long Vulcan_p(Object[] var0) {
      Vulcan_il var1 = (Vulcan_il)var0[0];
      return Vulcan_Z(new Object[]{var1.Vulcan_Q(new Object[0]), var1.Vulcan_x(new Object[0]), var1.Vulcan_w(new Object[0])});
   }

   public static long Vulcan_Z(Object[] var0) {
      int var1 = (Integer)var0[0];
      int var3 = (Integer)var0[1];
      int var2 = (Integer)var0[2];
      long var4 = (long)(var1 * 3129871) ^ (long)var2 * 116129781L ^ (long)var3;
      var4 = var4 * var4 * 42317861L + var4 * 11L;
      return var4;
   }

   public static UUID Vulcan_A(Object[] var0) {
      Random var1 = (Random)var0[0];
      long var2 = var1.nextLong() & -61441L | 16384L;
      long var4 = var1.nextLong() & 4611686018427387903L | Long.MIN_VALUE;
      return new UUID(var2, var4);
   }

   static {
      long var0 = a ^ 115303049002195L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[4];
      int var7 = 0;
      String var6 = "tV§lçî\u009f²Ìð\u001d\u0015É,\u0092\u0088\u0018\u008bÌÊ!]eZi¼8\u0014P@UâI=Õ\u0002öü»\u0087ñ";
      int var8 = "tV§lçî\u009f²Ìð\u001d\u0015É,\u0092\u0088\u0018\u008bÌÊ!]eZi¼8\u0014P@UâI=Õ\u0002öü»\u0087ñ".length();
      char var5 = 16;
      int var4 = -1;

      label41:
      while(true) {
         ++var4;
         String var13 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var13.getBytes("ISO-8859-1"));
            String var17 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var17;
               if ((var4 += var5) >= var8) {
                  b = var9;
                  Vulcan_L = b[3];
                  Vulcan_S = Vulcan_a(new Object[]{2.0F});
                  Vulcan_e = new float[65536];
                  int var11 = 0;

                  try {
                     while(var11 < 65536) {
                        Vulcan_e[var11] = (float)Math.sin((double)var11 * 3.141592653589793D * 2.0D / 65536.0D);
                        ++var11;
                     }
                  } catch (RuntimeException var12) {
                     throw a(var12);
                  }

                  Vulcan_b = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};
                  return;
               }

               var5 = var6.charAt(var4);
               break;
            default:
               var9[var7++] = var17;
               if ((var4 += var5) < var8) {
                  var5 = var6.charAt(var4);
                  continue label41;
               }

               var6 = "Ü\u008bÐ\u0007yúÞ´:à¬&é×\u0084\u0000\u0010\u009d\u0001j|\u0084k\u008cßý³Q3G\u0084\u0015\u0000";
               var8 = "Ü\u008bÐ\u0007yúÞ´:à¬&é×\u0084\u0000\u0010\u009d\u0001j|\u0084k\u008cßý³Q3G\u0084\u0015\u0000".length();
               var5 = 16;
               var4 = -1;
            }

            ++var4;
            var13 = var6.substring(var4, var4 + var5);
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
