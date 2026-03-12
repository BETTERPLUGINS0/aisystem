package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.entity.Player;

public class Vulcan_iC {
   private static final long a = Vulcan_n.a(-3836434402787769734L, -8728670464508357004L, MethodHandles.lookup().lookupClass()).a(178425439549925L);
   private static final String[] b;

   public void Vulcan_C(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$handle$5(Vulcan_iE var0) {
      Vulcan_eQ var10000 = Vulcan_Xs.INSTANCE.Vulcan_y();
      String[] var1 = b;
      var10000.Vulcan_I(new Object[]{Vulcan_i9.Vulcan__.replaceAll(var1[10], var0.Vulcan_I(new Object[0])).replaceAll(var1[7], var0.Vulcan_I(new Object[0]).getClientVersion().getReleaseName()).replaceAll(var1[12], var0.getClientBrand())});
   }

   private static void lambda$handle$4(String var0) {
      Vulcan_Xs.INSTANCE.Vulcan_y().Vulcan_I(new Object[]{var0});
   }

   private static void lambda$handle$3(Vulcan_iE var0, String var1) {
      long var2 = a ^ 107376989644391L;
      long var4 = var2 ^ 27197044562278L;
      Player var10000 = var0.Vulcan_q(new Object[0]);
      String[] var6 = b;
      var10000.sendMessage(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_ul.replaceAll(var6[0], var1), var4}));
      var0.Vulcan_q(new Object[0]).kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_ul.replaceAll(var6[12], var1), var4}));
   }

   private static void lambda$handle$2(Vulcan_iE var0) {
      long var1 = a ^ 49460216392800L;
      long var3 = var1 ^ 94485855720289L;
      var0.Vulcan_q(new Object[0]).kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{b[5], var3}));
   }

   private static void lambda$handle$1(Vulcan_iE var0, String var1) {
      long var2 = a ^ 16995162827465L;
      long var4 = var2 ^ 130155089073608L;
      var0.Vulcan_q(new Object[0]).kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_ul.replaceAll(b[12], var1), var4}));
   }

   private static void lambda$handle$0(Vulcan_iE var0) {
      long var1 = a ^ 87413670876160L;
      long var3 = var1 ^ 60011391757057L;
      var0.Vulcan_q(new Object[0]).kickPlayer(Vulcan_eR.Vulcan_m(new Object[]{b[9], var3}));
   }

   static {
      long var0 = a ^ 25605520543375L;
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
      String var6 = "\u0000\u000fA\u007f\u0007÷·\u0004Ì\t9µêàËñ\u0018\u009b|JÃäÛ'\u0097ç¡ËÐ\u009f|\u0011¬\u0086Ï?\u00960ØÉ¢(zá7øí\u0083\u0012Öù\u0098Ûl\u001cª8\u009e\u009c\u009aï~m Eu×\u0093\u0088>¸¨;\u0083\u0017~\u0015Á¶I9+\u0010øü\\?iÅôaÔYàtÙ»y\u0086(zá7øí\u0083\u0012Öù\u0098Ûl\u001cª8\u009e\u009c\u009aï~m EuwZÌk\u000fË¿]ðu\u0095\u0003µV\u000b\u007f\u0018\u009b|JÃäÛ'\u0097ç¡ËÐ\u009f|\u0011¬\u0086Ï?\u00960ØÉ¢\u0010§ôk¶d\bOø¢JCñi¾¿»\u0018\u0000\u000fA\u007f\u0007÷·\u0004³BÎê\u0011S¬½þ7ÎïÁÕR$\u0018\u0000\u000fA\u007f\u0007÷·\u0004³BÎê\u0011S¬½þ7ÎïÁÕR$ \u009b|JÃäÛ'\u0097ç¡ËÐ\u009f|\u0011¬\u0013>®7&;*OÉú\b\u0093Q\u0013\n¦\u0010§ôk¶d\bOø¢JCñi¾¿»\bÚ\u009b\fcXð¾\f";
      int var8 = "\u0000\u000fA\u007f\u0007÷·\u0004Ì\t9µêàËñ\u0018\u009b|JÃäÛ'\u0097ç¡ËÐ\u009f|\u0011¬\u0086Ï?\u00960ØÉ¢(zá7øí\u0083\u0012Öù\u0098Ûl\u001cª8\u009e\u009c\u009aï~m Eu×\u0093\u0088>¸¨;\u0083\u0017~\u0015Á¶I9+\u0010øü\\?iÅôaÔYàtÙ»y\u0086(zá7øí\u0083\u0012Öù\u0098Ûl\u001cª8\u009e\u009c\u009aï~m EuwZÌk\u000fË¿]ðu\u0095\u0003µV\u000b\u007f\u0018\u009b|JÃäÛ'\u0097ç¡ËÐ\u009f|\u0011¬\u0086Ï?\u00960ØÉ¢\u0010§ôk¶d\bOø¢JCñi¾¿»\u0018\u0000\u000fA\u007f\u0007÷·\u0004³BÎê\u0011S¬½þ7ÎïÁÕR$\u0018\u0000\u000fA\u007f\u0007÷·\u0004³BÎê\u0011S¬½þ7ÎïÁÕR$ \u009b|JÃäÛ'\u0097ç¡ËÐ\u009f|\u0011¬\u0013>®7&;*OÉú\b\u0093Q\u0013\n¦\u0010§ôk¶d\bOø¢JCñi¾¿»\bÚ\u009b\fcXð¾\f".length();
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

               var6 = "\u0000\u000fA\u007f\u0007÷·\u0004Ì\t9µêàËñ(zá7øí\u0083\u0012Öù\u0098Ûl\u001cª8\u009e\u009c\u009aï~m Eu®ê_\u008aÆo\u0085\u0017\u000b\u000bEÒf1%\u0003";
               var8 = "\u0000\u000fA\u007f\u0007÷·\u0004Ì\t9µêàËñ(zá7øí\u0083\u0012Öù\u0098Ûl\u001cª8\u009e\u009c\u009aï~m Eu®ê_\u008aÆo\u0085\u0017\u000b\u000bEÒf1%\u0003".length();
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
