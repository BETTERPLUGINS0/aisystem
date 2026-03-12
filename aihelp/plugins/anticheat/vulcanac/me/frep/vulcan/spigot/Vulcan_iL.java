package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class Vulcan_iL implements PluginMessageListener {
   private static int[] Vulcan_C;
   private static final long a = Vulcan_n.a(7938203450379022803L, -2259398840674369037L, MethodHandles.lookup().lookupClass()).a(182617612485923L);
   private static final String[] b;

   public void onPluginMessageReceived(String param1, Player param2, byte[] param3) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$onPluginMessageReceived$1(Player var0) {
      long var1 = a ^ 103298698878704L;
      long var3 = var1 ^ 41994550799162L;
      var0.kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{b[2], var3}));
   }

   private static void lambda$onPluginMessageReceived$0(Player var0) {
      long var1 = a ^ 138778555856960L;
      long var3 = var1 ^ 6551478652298L;
      var0.kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{b[1], var3}));
   }

   public static void Vulcan_G(int[] var0) {
      Vulcan_C = var0;
   }

   public static int[] Vulcan_N() {
      return Vulcan_C;
   }

   static {
      long var0 = a ^ 91269549284387L;
      Vulcan_G((int[])null);
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
      String var6 = "\u0084bbsg üç(xÒ!¢É\u008fY\u0018\u0000Ën\u0083Î!×¿(dbô¾\u008d¯\u0086l¥ÈÈâæ\u009c&";
      int var8 = "\u0084bbsg üç(xÒ!¢É\u008fY\u0018\u0000Ën\u0083Î!×¿(dbô¾\u008d¯\u0086l¥ÈÈâæ\u009c&".length();
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

               var6 = "\u0000Ën\u0083Î!×¿(dbô¾\u008d¯\u0086l¥ÈÈâæ\u009c&\bí¢ÝÍ¾\u001d]\u008d";
               var8 = "\u0000Ën\u0083Î!×¿(dbô¾\u008d¯\u0086l¥ÈÈâæ\u009c&\bí¢ÝÍ¾\u001d]\u008d".length();
               var5 = 24;
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   private static Exception a(Exception var0) {
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
