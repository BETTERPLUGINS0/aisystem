package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Vulcan_iZ {
   public final double Vulcan_q;
   public final double Vulcan_B;
   public final double Vulcan_V;
   public final double Vulcan_p;
   public final double Vulcan_C;
   public final double Vulcan_g;
   private static final long a = Vulcan_n.a(-6559228464122252645L, 104279728574431407L, MethodHandles.lookup().lookupClass()).a(266520888376040L);
   private static final String[] b;

   public Vulcan_iZ(double var1, double var3, double var5, double var7, double var9, double var11) {
      this.Vulcan_q = Math.min(var1, var7);
      this.Vulcan_B = Math.min(var3, var9);
      this.Vulcan_V = Math.min(var5, var11);
      this.Vulcan_p = Math.max(var1, var7);
      this.Vulcan_C = Math.max(var3, var9);
      this.Vulcan_g = Math.max(var5, var11);
   }

   public Vulcan_iZ(Vulcan_iD var1, Vulcan_iD var2) {
      this.Vulcan_q = (double)var1.Vulcan_Q(new Object[0]);
      this.Vulcan_B = (double)var1.Vulcan_x(new Object[0]);
      this.Vulcan_V = (double)var1.Vulcan_w(new Object[0]);
      this.Vulcan_p = (double)var2.Vulcan_Q(new Object[0]);
      this.Vulcan_C = (double)var2.Vulcan_x(new Object[0]);
      this.Vulcan_g = (double)var2.Vulcan_w(new Object[0]);
   }

   public Vulcan_iZ Vulcan_m(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vulcan_iZ Vulcan_S(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      double var8 = this.Vulcan_q - var2;
      double var10 = this.Vulcan_B - var4;
      double var12 = this.Vulcan_V - var6;
      double var14 = this.Vulcan_p + var2;
      double var16 = this.Vulcan_C + var4;
      double var18 = this.Vulcan_g + var6;
      return new Vulcan_iZ(var8, var10, var12, var14, var16, var18);
   }

   public static String spigot() {
      return b[1];
   }

   public static String nonce() {
      return b[0];
   }

   public static String resource() {
      return b[5];
   }

   public Vulcan_iZ Vulcan_k(Object[] var1) {
      Vulcan_iZ var2 = (Vulcan_iZ)var1[0];
      double var3 = Math.min(this.Vulcan_q, var2.Vulcan_q);
      double var5 = Math.min(this.Vulcan_B, var2.Vulcan_B);
      double var7 = Math.min(this.Vulcan_V, var2.Vulcan_V);
      double var9 = Math.max(this.Vulcan_p, var2.Vulcan_p);
      double var11 = Math.max(this.Vulcan_C, var2.Vulcan_C);
      double var13 = Math.max(this.Vulcan_g, var2.Vulcan_g);
      return new Vulcan_iZ(var3, var5, var7, var9, var11, var13);
   }

   public static Vulcan_iZ Vulcan_V(Object[] var0) {
      double var1 = (Double)var0[0];
      double var3 = (Double)var0[1];
      double var5 = (Double)var0[2];
      double var7 = (Double)var0[3];
      double var9 = (Double)var0[4];
      double var11 = (Double)var0[5];
      double var13 = Math.min(var1, var7);
      double var15 = Math.min(var3, var9);
      double var17 = Math.min(var5, var11);
      double var19 = Math.max(var1, var7);
      double var21 = Math.max(var3, var9);
      double var23 = Math.max(var5, var11);
      return new Vulcan_iZ(var13, var15, var17, var19, var21, var23);
   }

   public Vulcan_iZ Vulcan_M(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      return new Vulcan_iZ(this.Vulcan_q + var2, this.Vulcan_B + var4, this.Vulcan_V + var6, this.Vulcan_p + var2, this.Vulcan_C + var4, this.Vulcan_g + var6);
   }

   public double Vulcan_f(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public double Vulcan_S(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public double Vulcan_b(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_f(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean Vulcan_B(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public double Vulcan_m(Object[] var1) {
      double var2 = this.Vulcan_p - this.Vulcan_q;
      double var4 = this.Vulcan_C - this.Vulcan_B;
      double var6 = this.Vulcan_g - this.Vulcan_V;
      return (var2 + var4 + var6) / 3.0D;
   }

   public Vulcan_iZ Vulcan_H(Object[] var1) {
      double var2 = (Double)var1[0];
      double var4 = (Double)var1[1];
      double var6 = (Double)var1[2];
      double var8 = this.Vulcan_q + var2;
      double var10 = this.Vulcan_B + var4;
      double var12 = this.Vulcan_V + var6;
      double var14 = this.Vulcan_p - var2;
      double var16 = this.Vulcan_C - var4;
      double var18 = this.Vulcan_g - var6;
      return new Vulcan_iZ(var8, var10, var12, var14, var16, var18);
   }

   public Vulcan_ih Vulcan_C(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean Vulcan_q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean Vulcan_X(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean Vulcan_k(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String toString() {
      StringBuilder var10000 = new StringBuilder();
      String[] var1 = b;
      return var10000.append(var1[3]).append(this.Vulcan_q).append(var1[6]).append(this.Vulcan_B).append(var1[4]).append(this.Vulcan_V).append(var1[2]).append(this.Vulcan_p).append(var1[4]).append(this.Vulcan_C).append(var1[4]).append(this.Vulcan_g).append("]").toString();
   }

   public boolean Vulcan_o(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   static {
      long var0 = a ^ 128838807453553L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[7];
      int var7 = 0;
      String var6 = "O\u008b=\u007fÿ^-\u0099`\u008búvÔü\u0013²\u0010\u001c(½_m\u0015ÇCÇR]®\u009a\u0001|d\bèþèØep\u0002\u0096\b\u0014¢°\u009eëAf\r\bÌ\u001cë:v\u009cr©";
      int var8 = "O\u008b=\u007fÿ^-\u0099`\u008búvÔü\u0013²\u0010\u001c(½_m\u0015ÇCÇR]®\u009a\u0001|d\bèþèØep\u0002\u0096\b\u0014¢°\u009eëAf\r\bÌ\u001cë:v\u009cr©".length();
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

               var6 = "¦pPnJã\u0002\u009a)ã-<ð\u0097ó)é\u0084z©ü¡ÿN\bÌ\u001cë:v\u009cr©";
               var8 = "¦pPnJã\u0002\u009a)ã-<ð\u0097ó)é\u0084z©ü¡ÿN\bÌ\u001cë:v\u009cr©".length();
               var5 = 24;
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
