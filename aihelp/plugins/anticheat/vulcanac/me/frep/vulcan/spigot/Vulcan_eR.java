package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import net.md_5.bungee.api.ChatColor;

public final class Vulcan_eR {
   private static String Vulcan_W;
   private static final long a = Vulcan_n.a(-1344851477081376968L, 9151299121115939894L, MethodHandles.lookup().lookupClass()).a(229546437090694L);
   private static final String[] b;

   public static String Vulcan_m(Object[] var0) {
      String var1 = (String)var0[0];
      long var2 = (Long)var0[1];
      var2 ^= a;
      int[] var10000 = Vulcan_eG.Vulcan_R();
      String[] var8 = b;
      var1 = var1.replaceAll(var8[0], Vulcan_i9.Vulcan_hN);
      int[] var4 = var10000;

      label67: {
         char var11;
         try {
            var11 = Vulcan_eG.Vulcan_S(new Object[0]);
            if (var4 != null) {
               return ChatColor.translateAlternateColorCodes(var11, var1);
            }

            if (var11 != 0) {
               break label67;
            }
         } catch (UnsupportedOperationException var10) {
            throw a(var10);
         }

         var11 = '&';
         return ChatColor.translateAlternateColorCodes(var11, var1);
      }

      Pattern var5 = Pattern.compile(b[2]);
      Matcher var6 = var5.matcher(var1);

      String var12;
      label41:
      while(true) {
         if (var6.find()) {
            String var7 = var1.substring(var6.start(), var6.end());
            var12 = var1.replace(var7, ChatColor.of(var7) + "");
            if (var2 <= 0L) {
               return var12;
            }

            var1 = var12;
            var6 = var5.matcher(var1);

            do {
               try {
                  if (var4 != null) {
                     break label41;
                  }

                  if (var4 == null) {
                     continue label41;
                  }
               } catch (UnsupportedOperationException var9) {
                  throw a(var9);
               }
            } while(var2 <= 0L);
         }

         var1 = var1.replace('&', '§');
         break;
      }

      var12 = var1;
      return var12;
   }

   private Vulcan_eR() {
      throw new UnsupportedOperationException(b[1]);
   }

   public static void Vulcan_W(String var0) {
      Vulcan_W = var0;
   }

   public static String Vulcan_u() {
      return Vulcan_W;
   }

   static {
      long var0 = a ^ 131291779759442L;
      Vulcan_W((String)null);
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
      String var6 = "°¤wÁ< /G\u00992\u0091y0Í\"Ë8fÄÃ·¨S\u0087]\u001c£æLÞ\u000ej\u0000`\u00021É5PI~\u001câ§(\u00948\u0013\u0017F=\rÞÜ¾æaë¼öÏyQfÌ~\u0002\u0014\u008dÀvå\u0010\u0010í+\u001675Í\\\u0099W¬<á~¬Î8";
      int var8 = "°¤wÁ< /G\u00992\u0091y0Í\"Ë8fÄÃ·¨S\u0087]\u001c£æLÞ\u000ej\u0000`\u00021É5PI~\u001câ§(\u00948\u0013\u0017F=\rÞÜ¾æaë¼öÏyQfÌ~\u0002\u0014\u008dÀvå\u0010\u0010í+\u001675Í\\\u0099W¬<á~¬Î8".length();
      char var5 = 16;
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
