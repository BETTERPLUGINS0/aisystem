package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Vulcan_A extends Vulcan_q implements CommandExecutor {
   private final ExecutorService Vulcan_n = Executors.newSingleThreadExecutor();
   private static final long b = me.frep.vulcan.spigot.Vulcan_n.a(-7321957282496748607L, -3148178244050726042L, MethodHandles.lookup().lookupClass()).a(279304240804707L);
   private static final String[] d;

   public boolean onCommand(CommandSender param1, Command param2, String param3, String[] param4) {
      // $FF: Couldn't be decompiled
   }

   public static String spigot() {
      return d[7];
   }

   public static String nonce() {
      return d[9];
   }

   public static String resource() {
      return d[2];
   }

   private void lambda$onCommand$0(String[] param1, CommandSender param2) {
      // $FF: Couldn't be decompiled
   }

   static {
      long var0 = b ^ 52384130585412L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[14];
      int var7 = 0;
      String var6 = "\u007fÀS\u008fNÔÄú)gíiî|Mg\b\u0084rú¨¾õU%\u0018Ëg\u008c¬ÈU\u0098mm{\u008d\u0080²\u0093B\u008aÑ1oÁ3dðò\bTe\n\u00ad\u009d=Ö\t\u0010\u0018Dé\u00837<45Ù\u0086\u0094ñfc7ß\u0010\u000f.·Q?\u0082?Ã\u0014\u0081\töÜêÚi\u0010$À\u0081\u001b[àèõ,\u008dÕ<&¬\u0006X\u0010\u0082Åè/)\u0091\u001a Çó\u001b¤$sF®\u0010$À\u0081\u001b[àèõ,\u008dÕ<&¬\u0006X\u0010\u0016\f>¤\u0010þ\u0007÷´.r\u000e÷ÿIä\bçb\u0089Þ\u0001écÚ\b0¤0mÕ\\ÐZ";
      int var8 = "\u007fÀS\u008fNÔÄú)gíiî|Mg\b\u0084rú¨¾õU%\u0018Ëg\u008c¬ÈU\u0098mm{\u008d\u0080²\u0093B\u008aÑ1oÁ3dðò\bTe\n\u00ad\u009d=Ö\t\u0010\u0018Dé\u00837<45Ù\u0086\u0094ñfc7ß\u0010\u000f.·Q?\u0082?Ã\u0014\u0081\töÜêÚi\u0010$À\u0081\u001b[àèõ,\u008dÕ<&¬\u0006X\u0010\u0082Åè/)\u0091\u001a Çó\u001b¤$sF®\u0010$À\u0081\u001b[àèõ,\u008dÕ<&¬\u0006X\u0010\u0016\f>¤\u0010þ\u0007÷´.r\u000e÷ÿIä\bçb\u0089Þ\u0001écÚ\b0¤0mÕ\\ÐZ".length();
      char var5 = 16;
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var11 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var11.getBytes("ISO-8859-1"));
            String var15 = b(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var15;
               if ((var4 += var5) >= var8) {
                  d = var9;
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

               var6 = "\u007fÀS\u008fNÔÄú)gíiî|Mg\bçb\u0089Þ\u0001écÚ";
               var8 = "\u007fÀS\u008fNÔÄú)gíiî|Mg\bçb\u0089Þ\u0001écÚ".length();
               var5 = 16;
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

   private static String b(byte[] var0) {
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
