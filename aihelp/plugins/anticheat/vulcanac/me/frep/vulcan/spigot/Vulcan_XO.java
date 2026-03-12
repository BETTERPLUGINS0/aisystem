package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes.PropertyModifier;
import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public final class Vulcan_XO {
   private static String Vulcan_o;
   private static final long a = Vulcan_n.a(-8699499084649589457L, -4684195120203251084L, MethodHandles.lookup().lookupClass()).a(94955214387168L);
   private static final String[] b;

   public static double Vulcan_L(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_O(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private Vulcan_XO() {
      throw new UnsupportedOperationException(b[0]);
   }

   private static boolean lambda$getAttributeModifierMove$0(PropertyModifier param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_E(String var0) {
      Vulcan_o = var0;
   }

   public static String Vulcan_h() {
      return Vulcan_o;
   }

   static {
      long var0 = a ^ 14044403064232L;
      Vulcan_E("ReZX3b");
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
      String var6 = "p(ï\rÂ\u0086½\u0095]ü\u00845ÓO½Õ#\u008d²Oçö\u009döOq«Þ\u0081Í|í^N\u0006\u009b6\u009dnµåýn\u000f^Õå\u008f?@u©~²\u0092¨(F\u0013d·G/`\u0084a`Ë>ïc\u009fÌ:\u008aù\u008e\u008f\u001eoù\u0098©¿yM\u008e>\u00947\u0019\u009a\u009c\u008b× +";
      int var8 = "p(ï\rÂ\u0086½\u0095]ü\u00845ÓO½Õ#\u008d²Oçö\u009döOq«Þ\u0081Í|í^N\u0006\u009b6\u009dnµåýn\u000f^Õå\u008f?@u©~²\u0092¨(F\u0013d·G/`\u0084a`Ë>ïc\u009fÌ:\u008aù\u008e\u008f\u001eoù\u0098©¿yM\u008e>\u00947\u0019\u009a\u009c\u008b× +".length();
      char var5 = '8';
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

               var6 = "Lµ2$o6wÝ#;ÕÕb\u0085\u0090ß³@\u0087\u0017×G\u0089\u009eC\u0091n0MïlÏ\u001bd&J\u0090\u008fß&\bjèôX\u001d¼\u0003\u0005";
               var8 = "Lµ2$o6wÝ#;ÕÕb\u0085\u0090ß³@\u0087\u0017×G\u0089\u009eC\u0091n0MïlÏ\u001bd&J\u0090\u008fß&\bjèôX\u001d¼\u0003\u0005".length();
               var5 = '(';
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   private static UnsupportedOperationException a(UnsupportedOperationException var0) {
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
