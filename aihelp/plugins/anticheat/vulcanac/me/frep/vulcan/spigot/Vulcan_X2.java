package me.frep.vulcan.spigot;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.entity.Player;

public class Vulcan_X2 {
   private static String[] Vulcan_Z;
   private static final long a = Vulcan_n.a(6963203389538808611L, 1094119294424928809L, MethodHandles.lookup().lookupClass()).a(273158842914291L);
   private static final String[] b;

   public void Vulcan_v(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public static String spigot() {
      return b[51];
   }

   public static String nonce() {
      return b[31];
   }

   public static String resource() {
      return b[49];
   }

   private void Vulcan_q(Object[] var1) {
      Player var4 = (Player)var1[0];
      String var5 = (String)var1[1];
      long var2 = (Long)var1[2];
      long var10000 = a ^ var2;

      try {
         if (!Vulcan_i9.Vulcan_Qh) {
            return;
         }
      } catch (RuntimeException var8) {
         throw a((Exception)var8);
      }

      ByteArrayDataOutput var6 = ByteStreams.newDataOutput();
      String[] var7 = b;
      var6.writeUTF(var7[39]);
      var6.writeUTF(var5);
      var4.sendPluginMessage(Vulcan_Xs.INSTANCE.Vulcan_J(), var7[13], var6.toByteArray());
   }

   private static void lambda$handlePunishment$2(AbstractCheck var0) {
      var0.setVl(0);
   }

   private static void lambda$handlePunishment$1(Vulcan_iE param0, AbstractCheck param1, int param2, String param3) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$handlePunishment$0(Vulcan_iE param0, AbstractCheck param1) {
      // $FF: Couldn't be decompiled
   }

   static void Vulcan_T(Object[] var0) {
      Vulcan_X2 var1 = (Vulcan_X2)var0[0];
      long var4 = (Long)var0[1];
      Player var3 = (Player)var0[2];
      String var2 = (String)var0[3];
      var4 ^= a;
      long var6 = var4 ^ 130039596540481L;
      var1.Vulcan_q(new Object[]{var3, var2, var6});
   }

   public static void Vulcan_q(String[] var0) {
      Vulcan_Z = var0;
   }

   public static String[] Vulcan_V() {
      return Vulcan_Z;
   }

   static {
      long var0 = a ^ 74733588104724L;
      Vulcan_q((String[])null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[61];
      int var7 = 0;
      String var6 = "p+ÐobÚâ\u001c³°{\u0012\u0010x:^\bcuJÙ»0f(\b[N\u00ad@èYd\u0080\böç\u0094Û¼\u0096 \u009d\u0018u\u001e»\u0081\u000f\u0080M\u0094Ðø~C$éºÔ±Ã´\u0099ãléÊ\u0010r\u000eÏæ®\u008b\t\u009a}ø}lBðÛH\u0010·Ê\u0096Ê\u001ftËn\u001dvpÎ-\f«¶\u0010Æd\u008b,4\u00981õ¯ë\u0098\u0015w^¦2\u0018^iü\u0097«\u008c\u001c¾\u0010\u008d{\u0015÷4\u0094³ç\u0084\u0013hç,\u001fL\u0010ÔÄ6¤V²@®\u009dX»\u0096Ãº0°\u0010u\u001e»\u0081\u000f\u0080M\u0094\u0091\u009aØpé{3\u0097\u0010Æd\u008b,4\u00981õ¯ë\u0098\u0015w^¦2\b\u009aß·Î\u008cØ\u0099\u0093\u0010g=ôä\bÙD\u009e\u0080 >\u0092¦ÿd\u0019\u0010ÍÛ\u009c\u0087Ü\u009cTðbÙÖ¼¯Hs\u0006\b\u008aè\u0082+ZõèC\bòr-\u0085¢/\"\r\u0018u\u001e»\u0081\u000f\u0080M\u0094Ðø~C$éºÔ±Ã´\u0099ãléÊ\b\u008aè\u0082+ZõèC\u0010£èM\u001e(|\u0013K,¦\u008ff\u007fW¾h\u0010L\u001eåèü*¾.]ÝN\u0083y\u008de3\u0010p+ÐobÚâ\u001c³°{\u0012\u0010x:^\u0010Æd\u008b,4\u00981õû U\u0084ÌÆ\u0080\u008b\bØÎ£ùù\u0095rÞ\u0010jt\nµÅ\u0004Ý\u0088ýFqa¬·\u008cü\b«¬\u0088r±ámm\u0010u\u001e»\u0081\u000f\u0080M\u0094\u0091\u009aØpé{3\u0097\b\fg¡\u0083\u0083Á\u0087ç\u0010¬ò|7Ç\u0016å\u000fF\u0001m\u009d´\u001e)\u0007\b\r\u007f»\u000bí\u0018\u0099z\b\u0015T\u009dèàÆJì\u0010\u001d\u00877k1V\u000føôáÄ\u0004\b\u0097\u008b!\u0010\t\u0011`Y\u000eö\tpX@\u000fû4·F±\u0010éa`û£?CKÙ\u008ená?-ëè\u0010L\u001eåèü*¾.]ÝN\u0083y\u008de3\b\u009c\\uSËÙ\u0000J\b\u009aß·Î\u008cØ\u0099\u0093\u0010éa`û£?CKÙ\u008ená?-ëè\b\fg¡\u0083\u0083Á\u0087ç\u0010jt\nµÅ\u0004Ý\u0088Ù\fÎÉ(\u0085\u0081ò\u0010;\u0010NÒñ©ðmÿ\u0018æµþ§ÀG\bØÎ£ùù\u0095rÞ\u0010\u0097\u001d\\µoû\u009a]\u0093,+oô±\u0017¶\u0010á$èu|ç®£³\\Øâo¯\u0083a\b÷Eâ¨äg\u0090>\u0010ç\u001c\u0005 ¤\u0086MA?\"¿\u0099Öog:\b[N\u00ad@èYd\u0080\b÷Eâ¨äg\u0090>\u0010\u0017ºt@~\u0015\u009fÑKj\u0012\u0083ÇÏoW\u0018õô\u001c¯+\u0095\u0019æ½Ö\\¡\u0084\u0098Y\u0014zi4Ámc\u0090Ù\u0010ÍÛ\u009c\u0087Ü\u009cTðbÙÖ¼¯Hs\u0006\u0010.r\u001b´}1B¹Î}ÒÁ' KU\bcuJÙ»0f(\b«¬\u0088r±ámm\u0010ç\u001c\u0005 ¤\u0086MA?\"¿\u0099Öog:\u0010Æd\u008b,4\u00981õû U\u0084ÌÆ\u0080\u008b\u0010þ\u000b³I\u0006³D«µ =ÈMÑ\u0093L\u0010á$èu|ç®£³\\Øâo¯\u0083a\béxÌj¿\u0082\u0017û";
      int var8 = "p+ÐobÚâ\u001c³°{\u0012\u0010x:^\bcuJÙ»0f(\b[N\u00ad@èYd\u0080\böç\u0094Û¼\u0096 \u009d\u0018u\u001e»\u0081\u000f\u0080M\u0094Ðø~C$éºÔ±Ã´\u0099ãléÊ\u0010r\u000eÏæ®\u008b\t\u009a}ø}lBðÛH\u0010·Ê\u0096Ê\u001ftËn\u001dvpÎ-\f«¶\u0010Æd\u008b,4\u00981õ¯ë\u0098\u0015w^¦2\u0018^iü\u0097«\u008c\u001c¾\u0010\u008d{\u0015÷4\u0094³ç\u0084\u0013hç,\u001fL\u0010ÔÄ6¤V²@®\u009dX»\u0096Ãº0°\u0010u\u001e»\u0081\u000f\u0080M\u0094\u0091\u009aØpé{3\u0097\u0010Æd\u008b,4\u00981õ¯ë\u0098\u0015w^¦2\b\u009aß·Î\u008cØ\u0099\u0093\u0010g=ôä\bÙD\u009e\u0080 >\u0092¦ÿd\u0019\u0010ÍÛ\u009c\u0087Ü\u009cTðbÙÖ¼¯Hs\u0006\b\u008aè\u0082+ZõèC\bòr-\u0085¢/\"\r\u0018u\u001e»\u0081\u000f\u0080M\u0094Ðø~C$éºÔ±Ã´\u0099ãléÊ\b\u008aè\u0082+ZõèC\u0010£èM\u001e(|\u0013K,¦\u008ff\u007fW¾h\u0010L\u001eåèü*¾.]ÝN\u0083y\u008de3\u0010p+ÐobÚâ\u001c³°{\u0012\u0010x:^\u0010Æd\u008b,4\u00981õû U\u0084ÌÆ\u0080\u008b\bØÎ£ùù\u0095rÞ\u0010jt\nµÅ\u0004Ý\u0088ýFqa¬·\u008cü\b«¬\u0088r±ámm\u0010u\u001e»\u0081\u000f\u0080M\u0094\u0091\u009aØpé{3\u0097\b\fg¡\u0083\u0083Á\u0087ç\u0010¬ò|7Ç\u0016å\u000fF\u0001m\u009d´\u001e)\u0007\b\r\u007f»\u000bí\u0018\u0099z\b\u0015T\u009dèàÆJì\u0010\u001d\u00877k1V\u000føôáÄ\u0004\b\u0097\u008b!\u0010\t\u0011`Y\u000eö\tpX@\u000fû4·F±\u0010éa`û£?CKÙ\u008ená?-ëè\u0010L\u001eåèü*¾.]ÝN\u0083y\u008de3\b\u009c\\uSËÙ\u0000J\b\u009aß·Î\u008cØ\u0099\u0093\u0010éa`û£?CKÙ\u008ená?-ëè\b\fg¡\u0083\u0083Á\u0087ç\u0010jt\nµÅ\u0004Ý\u0088Ù\fÎÉ(\u0085\u0081ò\u0010;\u0010NÒñ©ðmÿ\u0018æµþ§ÀG\bØÎ£ùù\u0095rÞ\u0010\u0097\u001d\\µoû\u009a]\u0093,+oô±\u0017¶\u0010á$èu|ç®£³\\Øâo¯\u0083a\b÷Eâ¨äg\u0090>\u0010ç\u001c\u0005 ¤\u0086MA?\"¿\u0099Öog:\b[N\u00ad@èYd\u0080\b÷Eâ¨äg\u0090>\u0010\u0017ºt@~\u0015\u009fÑKj\u0012\u0083ÇÏoW\u0018õô\u001c¯+\u0095\u0019æ½Ö\\¡\u0084\u0098Y\u0014zi4Ámc\u0090Ù\u0010ÍÛ\u009c\u0087Ü\u009cTðbÙÖ¼¯Hs\u0006\u0010.r\u001b´}1B¹Î}ÒÁ' KU\bcuJÙ»0f(\b«¬\u0088r±ámm\u0010ç\u001c\u0005 ¤\u0086MA?\"¿\u0099Öog:\u0010Æd\u008b,4\u00981õû U\u0084ÌÆ\u0080\u008b\u0010þ\u000b³I\u0006³D«µ =ÈMÑ\u0093L\u0010á$èu|ç®£³\\Øâo¯\u0083a\béxÌj¿\u0082\u0017û".length();
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

               var6 = "þ\u000b³I\u0006³D«µ =ÈMÑ\u0093L\b8h¹Ä\u0082K\\ ";
               var8 = "þ\u000b³I\u0006³D«µ =ÈMÑ\u0093L\b8h¹Ä\u0082K\\ ".length();
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
