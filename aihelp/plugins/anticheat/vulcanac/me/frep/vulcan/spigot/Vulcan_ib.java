package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

public class Vulcan_ib implements TabCompleter {
   private static String Vulcan_T;
   private static final long a = Vulcan_n.a(-1128999921707953621L, 6205531932748775993L, MethodHandles.lookup().lookupClass()).a(70304288907603L);
   private static final String[] b;

   @Nullable
   public List onTabComplete(@NotNull CommandSender param1, @NotNull Command param2, @NotNull String param3, String[] param4) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_v(String var0) {
      Vulcan_T = var0;
   }

   public static String Vulcan_E() {
      return Vulcan_T;
   }

   static {
      long var0 = a ^ 89584428876052L;
      Vulcan_v((String)null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[16];
      int var7 = 0;
      String var6 = "¦}á6`qyô\b=¦Ùï\u0012\u0003®c\u0010Ã¾\u001c\u0095ÒÁ\u009eÈÕ&ìZhþß£\u0010¸\u0088 hNÓ\u0015ébò\t\u009c\u009d&Sâ\bZ\u0014TÕ\u0086£³-\bvù\u0091:âXö\u0011\u0010Øz`HÏ\u007f²\u0082\u008d>:ßçÆú\u0081\bÿåÎ\u0085?\u008b\u0013\u0019\u0010H6Õ]0Ýªß\u001fEQ¸%\u0099}\u001b\bÒâ\u0014sOpºT\býô\\\u007f\u0015ÇD¦\b\u0010\u008b\u001c3¨úu\u0088\b\u0088H\u00ad\u0099\u0018\u0017\u0090S\u0010H6Õ]0Ýªß\u001fEQ¸%\u0099}\u001b";
      int var8 = "¦}á6`qyô\b=¦Ùï\u0012\u0003®c\u0010Ã¾\u001c\u0095ÒÁ\u009eÈÕ&ìZhþß£\u0010¸\u0088 hNÓ\u0015ébò\t\u009c\u009d&Sâ\bZ\u0014TÕ\u0086£³-\bvù\u0091:âXö\u0011\u0010Øz`HÏ\u007f²\u0082\u008d>:ßçÆú\u0081\bÿåÎ\u0085?\u008b\u0013\u0019\u0010H6Õ]0Ýªß\u001fEQ¸%\u0099}\u001b\bÒâ\u0014sOpºT\býô\\\u007f\u0015ÇD¦\b\u0010\u008b\u001c3¨úu\u0088\b\u0088H\u00ad\u0099\u0018\u0017\u0090S\u0010H6Õ]0Ýªß\u001fEQ¸%\u0099}\u001b".length();
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

               var6 = "0né\u0084Ü7Ïî\bUe\u009acG{\u009cz";
               var8 = "0né\u0084Ü7Ïî\bUe\u009acG{\u009cz".length();
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
