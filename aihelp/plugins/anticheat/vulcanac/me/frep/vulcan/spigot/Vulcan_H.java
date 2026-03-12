package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Vulcan_H extends Vulcan_q implements CommandExecutor {
   private static final long b = Vulcan_n.a(-8385846306911608274L, 2535813062279984097L, MethodHandles.lookup().lookupClass()).a(22106736520539L);
   private static final String[] d;

   public boolean onCommand(CommandSender param1, Command param2, String param3, String[] param4) {
      // $FF: Couldn't be decompiled
   }

   static {
      long var0 = b ^ 129520782081L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[26];
      int var7 = 0;
      String var6 = "¼eoKFî<Ô\u0010¸µ|)ýÍK¸ó\u0004\u0004\u0000U«\u000eâ\b\u0089 \u009dé;åÔ9\b\u008f¹o\u008e\u001aNÜã\u0010¸µ|)ýÍK¸\u009f°\u0090*Þ¥¡c\b\u001eµt(\u009eO²\u00ad\b\u0089 \u009dé;åÔ9\b\u001eµt(\u009eO²\u00ad\u0010,yÛ\u0087ï\u0095±c:éè: \\gá\b³d\u009f\u0083\u0017O\u0013t\b³d\u009f\u0083\u0017O\u0013t\u0010IäcßO)p|õXe\u0098©æUÚ\u0010¸µ|)ýÍK¸ó\u0004\u0004\u0000U«\u000eâ\u0010\u009e\u0092ã¡ñv2>H´\u0013å6ð:\u0015\u0018HmI\u008e\\¢RØýéº\u0087\u001fØ\u00931fq]bÆD\u0085¢\u0010,yÛ\u0087ï\u0095±c:éè: \\gá\u0010¸µ|)ýÍK¸\u009f°\u0090*Þ¥¡c\u0010RTÎbqÁck¶&ED/\u009d1C\bîÖ·Ìÿ\u009a¯Ä\u0010\u009e\u0092ã¡ñv2>H´\u0013å6ð:\u0015\b\\\tÖI\u009d\\u\u0013\br\u0094\u0092öl£Vª\u0010Phóp\u008d\u0006+%½Që®\u0011Lu=\u0010IäcßO)p|õXe\u0098©æUÚ";
      int var8 = "¼eoKFî<Ô\u0010¸µ|)ýÍK¸ó\u0004\u0004\u0000U«\u000eâ\b\u0089 \u009dé;åÔ9\b\u008f¹o\u008e\u001aNÜã\u0010¸µ|)ýÍK¸\u009f°\u0090*Þ¥¡c\b\u001eµt(\u009eO²\u00ad\b\u0089 \u009dé;åÔ9\b\u001eµt(\u009eO²\u00ad\u0010,yÛ\u0087ï\u0095±c:éè: \\gá\b³d\u009f\u0083\u0017O\u0013t\b³d\u009f\u0083\u0017O\u0013t\u0010IäcßO)p|õXe\u0098©æUÚ\u0010¸µ|)ýÍK¸ó\u0004\u0004\u0000U«\u000eâ\u0010\u009e\u0092ã¡ñv2>H´\u0013å6ð:\u0015\u0018HmI\u008e\\¢RØýéº\u0087\u001fØ\u00931fq]bÆD\u0085¢\u0010,yÛ\u0087ï\u0095±c:éè: \\gá\u0010¸µ|)ýÍK¸\u009f°\u0090*Þ¥¡c\u0010RTÎbqÁck¶&ED/\u009d1C\bîÖ·Ìÿ\u009a¯Ä\u0010\u009e\u0092ã¡ñv2>H´\u0013å6ð:\u0015\b\\\tÖI\u009d\\u\u0013\br\u0094\u0092öl£Vª\u0010Phóp\u008d\u0006+%½Që®\u0011Lu=\u0010IäcßO)p|õXe\u0098©æUÚ".length();
      char var5 = '\b';
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

               var6 = "\u00ad¸rú½Ê\u00ado\b\u008f¹o\u008e\u001aNÜã";
               var8 = "\u00ad¸rú½Ê\u00ado\b\u008f¹o\u008e\u001aNÜã".length();
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
