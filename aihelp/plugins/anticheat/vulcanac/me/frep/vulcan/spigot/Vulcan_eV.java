package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.entity.Entity;

public final class Vulcan_eV {
   private static final long a = Vulcan_n.a(8138469109938263734L, 2623667062634607144L, MethodHandles.lookup().lookupClass()).a(202357243210504L);
   private static final String[] b;

   public static boolean Vulcan_f(Object[] var0) {
      Entity var1 = (Entity)var0[0];
      return var1.getType().toString().equals(b[1]);
   }

   public static boolean Vulcan_V(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_x(Object[] var0) {
      Entity var1 = (Entity)var0[0];
      return var1.getType().toString().equals(b[3]);
   }

   private Vulcan_eV() {
      throw new UnsupportedOperationException(b[2]);
   }

   static {
      long var0 = a ^ 132907786177772L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[5];
      int var7 = 0;
      String var6 = "\u000e(§h_\u0096v3\b]S\u0084!\u0085y=¨8ô\u0095ª\u0091²\u000fñOÅïôwÁ\\\u000f\u0088Ä0ýÍpÀ\u001e\u0003<\u008f? \u0094{\u0005r>ús.w\"VÄ\u0002¹\t-\u001cÄ\u0018½\u0099ðÑ´5\u008e\bû";
      int var8 = "\u000e(§h_\u0096v3\b]S\u0084!\u0085y=¨8ô\u0095ª\u0091²\u000fñOÅïôwÁ\\\u000f\u0088Ä0ýÍpÀ\u001e\u0003<\u008f? \u0094{\u0005r>ús.w\"VÄ\u0002¹\t-\u001cÄ\u0018½\u0099ðÑ´5\u008e\bû".length();
      char var5 = '\b';
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

               var6 = "ÿ}Ü.ZÖ\u009f\u0015\u0002MÕ\u0006ÎvÔa\bÉ÷ñ\u0082\u0099\u009e\u0000Å";
               var8 = "ÿ}Ü.ZÖ\u009f\u0015\u0002MÕ\u0006ÎvÔa\bÉ÷ñ\u0082\u0099\u009e\u0000Å".length();
               var5 = 16;
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
