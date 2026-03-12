package me.frep.vulcan.spigot;

import com.google.common.collect.Maps;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public enum Vulcan_Xy implements Vulcan_i4 {
   public static final Vulcan_Xy DOWN;
   public static final Vulcan_Xy UP;
   public static final Vulcan_Xy NORTH;
   public static final Vulcan_Xy SOUTH;
   public static final Vulcan_Xy WEST;
   public static final Vulcan_Xy EAST;
   private final int Vulcan_f;
   private final int Vulcan_z;
   private final int Vulcan_L;
   private final String Vulcan_r;
   private final Vulcan_iK Vulcan_d;
   private final Vulcan_XR Vulcan_O;
   private final Vulcan_il Vulcan_M;
   private static final Vulcan_Xy[] Vulcan_j;
   private static final Vulcan_Xy[] Vulcan_D;
   private static final Map Vulcan_t;
   private static final String Vulcan_P;
   private static final Vulcan_Xy[] Vulcan_G;
   private static final long a = Vulcan_n.a(4801544277402851023L, 5340311908451058628L, MethodHandles.lookup().lookupClass()).a(61682440264683L);
   private static final String[] b;

   private Vulcan_Xy(int var3, int var4, int var5, String var6, Vulcan_XR var7, Vulcan_iK var8, Vulcan_il var9) {
      this.Vulcan_f = var3;
      this.Vulcan_L = var5;
      this.Vulcan_z = var4;
      this.Vulcan_r = var6;
      this.Vulcan_d = var8;
      this.Vulcan_O = var7;
      this.Vulcan_M = var9;
   }

   public int Vulcan_i() {
      return this.Vulcan_f;
   }

   public int Vulcan_o() {
      return this.Vulcan_L;
   }

   public Vulcan_XR Vulcan_C() {
      return this.Vulcan_O;
   }

   public Vulcan_Xy Vulcan_D() {
      return Vulcan_H(this.Vulcan_z);
   }

   public Vulcan_Xy Vulcan_b(Vulcan_iK param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_Xy Vulcan_m() {
      // $FF: Couldn't be decompiled
   }

   private Vulcan_Xy Vulcan_e() {
      // $FF: Couldn't be decompiled
   }

   private Vulcan_Xy Vulcan_n() {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_Xy Vulcan_F() {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_c() {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_g() {
      // $FF: Couldn't be decompiled
   }

   public int Vulcan_A() {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_W() {
      return this.Vulcan_r;
   }

   public Vulcan_iK Vulcan_F() {
      return this.Vulcan_d;
   }

   public static Vulcan_Xy Vulcan_a(String param0) {
      // $FF: Couldn't be decompiled
   }

   public static Vulcan_Xy Vulcan_H(int var0) {
      long var1 = a ^ 133184630865771L;
      long var3 = var1 ^ 128706715035527L;
      int var10002 = var0 % Vulcan_j.length;
      return Vulcan_j[Vulcan_eN.Vulcan_g(new Object[]{var3, var10002})];
   }

   public static Vulcan_Xy Vulcan_V(int var0) {
      long var1 = a ^ 52708695365046L;
      long var3 = var1 ^ 39443294276954L;
      int var10002 = var0 % Vulcan_D.length;
      return Vulcan_D[Vulcan_eN.Vulcan_g(new Object[]{var3, var10002})];
   }

   public static Vulcan_Xy Vulcan_d(double var0) {
      long var2 = a ^ 105472645575559L;
      long var4 = var2 ^ 139467180225896L;
      return Vulcan_V(Vulcan_eN.Vulcan_c(new Object[]{var4, var0 / 90.0D + 0.5D}) & 3);
   }

   public static Vulcan_Xy Vulcan_w(Random var0) {
      return values()[var0.nextInt(values().length)];
   }

   public static Vulcan_Xy Vulcan_s(float var0, float var1, float var2) {
      long var3 = a ^ 84986523519141L;
      Vulcan_Xy var6 = NORTH;
      float var7 = Float.MIN_VALUE;
      String var10000 = Vulcan_il.Vulcan_U();
      Vulcan_Xy[] var8 = values();
      int var9 = var8.length;
      String var5 = var10000;
      int var10 = 0;

      Vulcan_Xy var14;
      while(true) {
         if (var10 < var9) {
            var14 = var8[var10];
            if (var5 == null) {
               break;
            }

            Vulcan_Xy var11 = var14;
            float var12 = var0 * (float)var11.Vulcan_M.Vulcan_Q(new Object[0]) + var1 * (float)var11.Vulcan_M.Vulcan_x(new Object[0]) + var2 * (float)var11.Vulcan_M.Vulcan_w(new Object[0]);

            label30: {
               label29: {
                  try {
                     if (var5 == null) {
                        break label30;
                     }

                     if (!(var12 > var7)) {
                        break label29;
                     }
                  } catch (IllegalStateException var13) {
                     throw a(var13);
                  }

                  var7 = var12;
                  var6 = var11;
               }

               ++var10;
            }

            if (var5 != null) {
               continue;
            }
         }

         var14 = var6;
         break;
      }

      return var14;
   }

   public String toString() {
      return this.Vulcan_r;
   }

   public String Vulcan_h() {
      return this.Vulcan_r;
   }

   public Vulcan_il Vulcan_M() {
      return this.Vulcan_M;
   }

   static {
      long var0 = a ^ 124286055670108L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[18];
      int var7 = 0;
      String var6 = "º3\u0000Ï«õóìh\u009bv¯¡\u0004\u008b¨\bÿ e0i?BÐ\b$ú\u009fñ¡`\u0016\t(×\r ÿîx\u001díÞ\tÜ\u0017\u0004\u001d\u000e0\u0092X\u0093ÁÍè0\u001dÍb®Èä*\u0085\u000bº®Q£¦>¤\n\bUÁ O³<Õ\u0011\bí\u0012\u0093\u009eL\u008f\u007f\u0007(×\r ÿîx\u001dí\u0089Í\u0098\u0091\u001c×\u001c\u0002\u0091+h\u0097\u0002°\u0095ÆÛ\u008c¥G÷\u001f\u000fóÐ.þÊ\u009dÂQÀ(×\r ÿîx\u001dí³Ò \u0010I\u0018Ùµl\u0001\u008dwG\u001dû)×X\u0080\u008f\u00056\u0011Bd\u0096>_èÄ\u001e+\bá«ÉùÙA\u0002O\b¦\u000eûjÁ\u0084\fO\bp\u0086Õ\u001c½v3è ×\r ÿîx\u001dí\u000fØ\u0099(|1ê\u009c§âÕ\u0087P¤\u008c\u0096îrã\u0019YÛ\u001dß\bb¦,\u0087R\u000e%\u0013\bYS\u00806}¾\u0084þ(×\r ÿîx\u001dí\u000fÔâ\u0014géàV\u0010\u0088§s\u0097Tc4B|è\u0089\u0004\u0007¿\u009eB\u001cmª³mÊV\b0¤WbÚ3-E";
      int var8 = "º3\u0000Ï«õóìh\u009bv¯¡\u0004\u008b¨\bÿ e0i?BÐ\b$ú\u009fñ¡`\u0016\t(×\r ÿîx\u001díÞ\tÜ\u0017\u0004\u001d\u000e0\u0092X\u0093ÁÍè0\u001dÍb®Èä*\u0085\u000bº®Q£¦>¤\n\bUÁ O³<Õ\u0011\bí\u0012\u0093\u009eL\u008f\u007f\u0007(×\r ÿîx\u001dí\u0089Í\u0098\u0091\u001c×\u001c\u0002\u0091+h\u0097\u0002°\u0095ÆÛ\u008c¥G÷\u001f\u000fóÐ.þÊ\u009dÂQÀ(×\r ÿîx\u001dí³Ò \u0010I\u0018Ùµl\u0001\u008dwG\u001dû)×X\u0080\u008f\u00056\u0011Bd\u0096>_èÄ\u001e+\bá«ÉùÙA\u0002O\b¦\u000eûjÁ\u0084\fO\bp\u0086Õ\u001c½v3è ×\r ÿîx\u001dí\u000fØ\u0099(|1ê\u009c§âÕ\u0087P¤\u008c\u0096îrã\u0019YÛ\u001dß\bb¦,\u0087R\u000e%\u0013\bYS\u00806}¾\u0084þ(×\r ÿîx\u001dí\u000fÔâ\u0014géàV\u0010\u0088§s\u0097Tc4B|è\u0089\u0004\u0007¿\u009eB\u001cmª³mÊV\b0¤WbÚ3-E".length();
      char var5 = 16;
      int var4 = -1;

      label50:
      while(true) {
         ++var4;
         String var17 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var17.getBytes("ISO-8859-1"));
            String var21 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var21;
               if ((var4 += var5) >= var8) {
                  b = var9;
                  Vulcan_P = b[0];
                  String[] var16 = b;
                  DOWN = new Vulcan_Xy(var16[2], 0, 0, 1, -1, var16[9], Vulcan_XR.NEGATIVE, Vulcan_iK.Y, new Vulcan_il(0, -1, 0));
                  UP = new Vulcan_Xy(var16[4], 1, 1, 0, -1, var16[15], Vulcan_XR.POSITIVE, Vulcan_iK.Y, new Vulcan_il(0, 1, 0));
                  NORTH = new Vulcan_Xy(var16[5], 2, 2, 3, 2, var16[16], Vulcan_XR.NEGATIVE, Vulcan_iK.Z, new Vulcan_il(0, 0, -1));
                  SOUTH = new Vulcan_Xy(var16[10], 3, 3, 2, 0, var16[13], Vulcan_XR.POSITIVE, Vulcan_iK.Z, new Vulcan_il(0, 0, 1));
                  WEST = new Vulcan_Xy(var16[17], 4, 4, 5, 1, var16[12], Vulcan_XR.NEGATIVE, Vulcan_iK.X, new Vulcan_il(-1, 0, 0));
                  EAST = new Vulcan_Xy(var16[8], 5, 5, 4, 3, var16[1], Vulcan_XR.POSITIVE, Vulcan_iK.X, new Vulcan_il(1, 0, 0));
                  Vulcan_G = new Vulcan_Xy[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
                  Vulcan_j = new Vulcan_Xy[6];
                  Vulcan_D = new Vulcan_Xy[4];
                  Vulcan_t = Maps.newHashMap();
                  Vulcan_Xy[] var11 = values();
                  int var12 = var11.length;

                  for(int var13 = 0; var13 < var12; ++var13) {
                     Vulcan_Xy var14 = var11[var13];

                     try {
                        Vulcan_j[var14.Vulcan_f] = var14;
                        if (var14.Vulcan_F().Vulcan_u()) {
                           Vulcan_D[var14.Vulcan_L] = var14;
                        }
                     } catch (IllegalStateException var15) {
                        throw a(var15);
                     }

                     Vulcan_t.put(var14.Vulcan_W().toLowerCase(), var14);
                  }

                  return;
               }

               var5 = var6.charAt(var4);
               break;
            default:
               var9[var7++] = var21;
               if ((var4 += var5) < var8) {
                  var5 = var6.charAt(var4);
                  continue label50;
               }

               var6 = "Ü×¤|\u0011\u0090ä(\b\u001b\\6@¤\u0092\u008fq";
               var8 = "Ü×¤|\u0011\u0090ä(\b\u001b\\6@¤\u0092\u008fq".length();
               var5 = '\b';
               var4 = -1;
            }

            ++var4;
            var17 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   private static IllegalStateException a(IllegalStateException var0) {
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
