package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class Vulcan_m {
   private static final long a = Vulcan_n.a(-7446676958620823210L, -572545819218832045L, MethodHandles.lookup().lookupClass()).a(208282655136879L);
   private static final String[] b;

   public void Vulcan_m(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$handle$0(Vulcan_iE var0, Location var1) {
      var0.Vulcan_q(new Object[0]).teleport(var1, TeleportCause.UNKNOWN);
   }

   static {
      long var0 = a ^ 121800580447429L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[8];
      int var7 = 0;
      String var6 = "o\f\u0092 Ò.\u0018Mµ\u0016ób/êH\u001fÍ\u0007Ð<ài\u00830(ð\\\\b¬:â\u0081\u0003èæë»a£\u001c[câÆW¹4MàÇÔä¿3¼EÒó¯q\u0004Q<y(ð\\\\b¬:â\u0081\u0003èæë»a£\u001c[câÆW¹4MàÇÔä¿3¼EÒó¯q\u0004Q<y\b\u0096sXÁáM,\u008d(\u0099c^~\u009b3D\u0095×fÊÕÄÿ0%¨\\\u0010\u000fª\u0001cUµõ$u<+\u008f\u0006yc/Ñ\u0019õ[q\u0010\u0095á\u0083W\u0096¦Óº'\u0010îÀ)\u000bXý";
      int var8 = "o\f\u0092 Ò.\u0018Mµ\u0016ób/êH\u001fÍ\u0007Ð<ài\u00830(ð\\\\b¬:â\u0081\u0003èæë»a£\u001c[câÆW¹4MàÇÔä¿3¼EÒó¯q\u0004Q<y(ð\\\\b¬:â\u0081\u0003èæë»a£\u001c[câÆW¹4MàÇÔä¿3¼EÒó¯q\u0004Q<y\b\u0096sXÁáM,\u008d(\u0099c^~\u009b3D\u0095×fÊÕÄÿ0%¨\\\u0010\u000fª\u0001cUµõ$u<+\u008f\u0006yc/Ñ\u0019õ[q\u0010\u0095á\u0083W\u0096¦Óº'\u0010îÀ)\u000bXý".length();
      char var5 = 24;
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

               var6 = "\u0095á\u0083W\u0096¦Óº'\u0010îÀ)\u000bXý(\u0099c^~\u009b3D\u0095×fÊÕÄÿ0%¨\\\u0010\u000fª\u0001cUµõ$u<+\u008f\u0006yc/Ñ\u0019õ[q";
               var8 = "\u0095á\u0083W\u0096¦Óº'\u0010îÀ)\u000bXý(\u0099c^~\u009b3D\u0095×fÊÕÄÿ0%¨\\\u0010\u000fª\u0001cUµõ$u<+\u008f\u0006yc/Ñ\u0019õ[q".length();
               var5 = 16;
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
