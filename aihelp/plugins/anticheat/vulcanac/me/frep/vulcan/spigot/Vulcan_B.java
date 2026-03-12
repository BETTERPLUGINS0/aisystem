package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public final class Vulcan_B {
   private static final long a = Vulcan_n.a(-8880662989915356080L, -6777260397646179665L, MethodHandles.lookup().lookupClass()).a(12991467442240L);
   private static final String[] b;

   public static long Vulcan_k(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static int Vulcan_x(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private Vulcan_B() {
      throw new UnsupportedOperationException(b[29]);
   }

   static {
      long var0 = a ^ 131396823436924L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[34];
      int var7 = 0;
      String var6 = "y\u009fVª\"~\u0083=G\u0092¾>\" ¦]\u0010GÕä\rÏëHm \u0089\u001cMOQ®½\u0010f¬5¨d¶¯VQt<âã\u008c?\u0005\u0010;TscÂy&dB¤Ø³ã\u0085\tw\u0010\u001e\u008a\u0016Pô\u0092k\u009fg\u0003Õ\u008f\u0012×Å6\u0010¼\u0099\u0086¿V÷Æ\u008dÆyÑÞ<ÄOÄ\u0010&\u0097Lq`×èFÐ\u0015#\u0095¦\u0080\u00ad\u0001\u0010-ð\u0097\u0089|Ý\u0098°N¯\u0084¼V5&^\u0010Õ\u0095_\u009c>Gc·ÏFUã´\t\f\u0011\u0018P£KÔ=(C¡^3W\u000b\u001c\u008fÒ\rýñ÷\u008bFfj»\u0010Â\u0097Î@\u0016ï\u009fÓ<\u009a¥9<A\u008fj\u0010ª-`\u0093\u0090\f!U|h\u0017\u009cÁæ\u0004\u0088\u0010\u0013û`«±CÃ)Bÿ\u0001ï54\u0090w\u0010k\fûQ\u0085 t=Q\u001euÉ±-Q\u0094\bô8ÏºÙ\u001c¤/\u0010\u0093ûD¸\u0098áàÔb\u0081·ìÕw:\u0089\u0010Ä\u0002Õí\u008e\u000fµ\u001bE3\u0098\u0095wm\u001aý\u0018P£KÔ=(C¡?áp¬6@ó/3ìº¤\f\u0093öP\u0010ÔkÄG\nºÕÛÈ}\u0017nd(\u0007\u0014\u0010&Ð»\u0010\nÔ\u0096\u0016ÉÂ\u008a\u008cªîÑ8\u0010\u007fY\u000bwq|0»*ü\u0015\u000fiôö\u007f\u0010âë\u0015$Ë\u0000\u0087¡K$«\u0019ëv]^\u0010ÔkÄG\nºÕÛ\u0088\u001eSèG\u008c\u0006/\u0010É\u008dL»\u000e\r£\r¢pf¶Æï\u0000G\u0010ÔkÄG\nºÕÛE.ÄÑ\u001f4è×\u00102x¾u\u008cÇ 8£ÐÆ1ü\"j\t\u0010y\u009fVª\"~\u0083=Ù\u0013\u0004?,úÓ\u00ad\u0010Ç\u0087kÇ\u0018ç*|µúõÍ¿«'\u0081\u0010P£KÔ=(C¡Ì\u0016?f6ki÷8«\u0016t¤\r\u0084å·¨\u0019E¾W\u009c°AHH®!«·+·¦\u0085(j n\u0004²\u008d\u0000° %¿p@r\u001d\u008b\u0006¨KÀWËE$\u0013Jº»ö\u0010ÔkÄG\nºÕÛ[KCV²\"\r\u001f\u0010\u009eL4c\u001b\u000b\u0088ü>l%A\u0015¿¨\u008c";
      int var8 = "y\u009fVª\"~\u0083=G\u0092¾>\" ¦]\u0010GÕä\rÏëHm \u0089\u001cMOQ®½\u0010f¬5¨d¶¯VQt<âã\u008c?\u0005\u0010;TscÂy&dB¤Ø³ã\u0085\tw\u0010\u001e\u008a\u0016Pô\u0092k\u009fg\u0003Õ\u008f\u0012×Å6\u0010¼\u0099\u0086¿V÷Æ\u008dÆyÑÞ<ÄOÄ\u0010&\u0097Lq`×èFÐ\u0015#\u0095¦\u0080\u00ad\u0001\u0010-ð\u0097\u0089|Ý\u0098°N¯\u0084¼V5&^\u0010Õ\u0095_\u009c>Gc·ÏFUã´\t\f\u0011\u0018P£KÔ=(C¡^3W\u000b\u001c\u008fÒ\rýñ÷\u008bFfj»\u0010Â\u0097Î@\u0016ï\u009fÓ<\u009a¥9<A\u008fj\u0010ª-`\u0093\u0090\f!U|h\u0017\u009cÁæ\u0004\u0088\u0010\u0013û`«±CÃ)Bÿ\u0001ï54\u0090w\u0010k\fûQ\u0085 t=Q\u001euÉ±-Q\u0094\bô8ÏºÙ\u001c¤/\u0010\u0093ûD¸\u0098áàÔb\u0081·ìÕw:\u0089\u0010Ä\u0002Õí\u008e\u000fµ\u001bE3\u0098\u0095wm\u001aý\u0018P£KÔ=(C¡?áp¬6@ó/3ìº¤\f\u0093öP\u0010ÔkÄG\nºÕÛÈ}\u0017nd(\u0007\u0014\u0010&Ð»\u0010\nÔ\u0096\u0016ÉÂ\u008a\u008cªîÑ8\u0010\u007fY\u000bwq|0»*ü\u0015\u000fiôö\u007f\u0010âë\u0015$Ë\u0000\u0087¡K$«\u0019ëv]^\u0010ÔkÄG\nºÕÛ\u0088\u001eSèG\u008c\u0006/\u0010É\u008dL»\u000e\r£\r¢pf¶Æï\u0000G\u0010ÔkÄG\nºÕÛE.ÄÑ\u001f4è×\u00102x¾u\u008cÇ 8£ÐÆ1ü\"j\t\u0010y\u009fVª\"~\u0083=Ù\u0013\u0004?,úÓ\u00ad\u0010Ç\u0087kÇ\u0018ç*|µúõÍ¿«'\u0081\u0010P£KÔ=(C¡Ì\u0016?f6ki÷8«\u0016t¤\r\u0084å·¨\u0019E¾W\u009c°AHH®!«·+·¦\u0085(j n\u0004²\u008d\u0000° %¿p@r\u001d\u008b\u0006¨KÀWËE$\u0013Jº»ö\u0010ÔkÄG\nºÕÛ[KCV²\"\r\u001f\u0010\u009eL4c\u001b\u000b\u0088ü>l%A\u0015¿¨\u008c".length();
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

               var6 = "&Ð»\u0010\nÔ\u0096\u0016ÏxEÇj#<º\bö¦±`´ÄE\u001d";
               var8 = "&Ð»\u0010\nÔ\u0096\u0016ÏxEÇj#<º\bö¦±`´ÄE\u001d".length();
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
