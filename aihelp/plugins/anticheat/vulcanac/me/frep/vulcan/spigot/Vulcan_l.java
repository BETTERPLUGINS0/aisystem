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

public class Vulcan_l extends Vulcan_q implements CommandExecutor {
   private final ExecutorService Vulcan_p = Executors.newSingleThreadExecutor();
   private static final long b = Vulcan_n.a(9163490659612932319L, -7558605028014558497L, MethodHandles.lookup().lookupClass()).a(18632563896921L);
   private static final String[] d;

   public boolean onCommand(CommandSender param1, Command param2, String param3, String[] param4) {
      // $FF: Couldn't be decompiled
   }

   private void lambda$onCommand$0(String[] param1, CommandSender param2) {
      // $FF: Couldn't be decompiled
   }

   static {
      long var0 = b ^ 62488456160910L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[9];
      int var7 = 0;
      String var6 = "f\u0085{úy\u001d\u0014ge\u001b±ÔÈ×#òó%Ë82à\u0016_\u0010Ì©\u0087Þ¾\u001b5Ñäæ\u008bÓrVHO\bghÜ(~½Òð\u0010v\bDÃôÌÊ3Q¬Èþz\u0085;\u0017\b{.\u0001è²n¤p\b\u0092µa\u009e\u009bÃa8\u0018KÈ±kú\u00959ÙoIP&á£ÚF?å\u0012¸^ðà\u0089";
      int var8 = "f\u0085{úy\u001d\u0014ge\u001b±ÔÈ×#òó%Ë82à\u0016_\u0010Ì©\u0087Þ¾\u001b5Ñäæ\u008bÓrVHO\bghÜ(~½Òð\u0010v\bDÃôÌÊ3Q¬Èþz\u0085;\u0017\b{.\u0001è²n¤p\b\u0092µa\u009e\u009bÃa8\u0018KÈ±kú\u00959ÙoIP&á£ÚF?å\u0012¸^ðà\u0089".length();
      char var5 = 24;
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

               var6 = "Ì©\u0087Þ¾\u001b5Ñäæ\u008bÓrVHO\bS\u0099=Jí\u0004kA";
               var8 = "Ì©\u0087Þ¾\u001b5Ñäæ\u008bÓrVHO\bS\u0099=Jí\u0004kA".length();
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
