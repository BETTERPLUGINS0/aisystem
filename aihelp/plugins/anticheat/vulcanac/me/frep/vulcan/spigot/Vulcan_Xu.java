package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;

public final class Vulcan_Xu {
   private static final long a = Vulcan_n.a(-4637578470480105284L, -8604115154180892711L, MethodHandles.lookup().lookupClass()).a(233269153673655L);
   private static final String[] b;

   public static void Vulcan_O(Object[] var0) {
      long var1 = (Long)var0[0];
      long var10000 = a ^ var1;
      Vulcan_i9.Vulcan_FS.clear();
      Vulcan_i9.Vulcan_FU.clear();
      Vulcan_i9.Vulcan_a1.clear();
      Vulcan_i9.Vulcan_Qf.clear();
      int[] var5 = Vulcan_eG.Vulcan_R();
      Vulcan_i9.Vulcan_dW.clear();
      Vulcan_i9.Vulcan_Fz.clear();
      Vulcan_i9.Vulcan_ht.clear();
      Vulcan_i9.Vulcan_d.clear();
      Vulcan_i9.Vulcan_hl.clear();
      Vulcan_i9.Vulcan_QB.clear();
      int[] var3 = var5;
      Vulcan_i9.Vulcan_Q7.clear();
      Vulcan_i9.Vulcan_FV.clear();
      Vulcan_i9.Vulcan_hu.clear();
      Vulcan_i9.Vulcan_d3.clear();
      Vulcan_i9.Vulcan_dB.clear();
      Vulcan_i9.Vulcan_aZ.clear();
      Vulcan_i9.Vulcan_Q3.clear();
      Vulcan_i9.Vulcan_dr.clear();
      Vulcan_i9.Vulcan_d5.clear();
      Vulcan_i9.Vulcan_dI.clear();
      if (var3 != null) {
         int var4 = AbstractCheck.Vulcan_V();
         ++var4;
         AbstractCheck.Vulcan_H(var4);
      }

   }

   public static void Vulcan_g(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private Vulcan_Xu() {
      throw new UnsupportedOperationException(b[5]);
   }

   static {
      long var0 = a ^ 137942078266967L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[29];
      int var7 = 0;
      String var6 = "\u0081Þãóuæð\u009eÝìñø\u009eù8£\u0010\u0081Þãóuæð\u009eÝìñø\u009eù8£\u0018.¯\f§ô#:8\u001e\u0082r\rø\bT¥ñ+¾\u0097mNí&\u0010\t¾i\u008bàò\u008dÌú\u0003OÌÀëKÊ \u0019\u0010v¾T \u0091R\u0000ÒÁø\u0098c»oøì0ÛêÅJ«Y>mÀä\u0089Ê\u000f8ì&f°±¨£\u000b¡i4Üü·`ð\u0097\u0017\u0089zC\u008f âÏ\u0003ä>\u0082ð´«@ëá\u001elý\u009cà\u001eÜ\f\u008abOÁÚØö-:F<\u0015<\u0010\u001a¼!Ï\"\u0083påaJ\u0016ñJìÅÇ07ì\u0080µ5ß\fëðXã\u0096\u0015\u001et/\u000e¾\u008c\u0081Îm#Dÿüøó\u0002\u0099\u008fDPÖJf°zô\u0095áÐ\u0001\u0010¬Ê\u0084ú\b\u0094\\C\u0082ñì\u009bÕ\blö{¹A,ü£\u0018uXÏ8Çö\u0086¯sÔbÚêÄ\"Ï%Ë\u0099|¶¢ð\u0015\bx\u00897\u008e\u008eáíß\b\u0010M²Jãf\u0084]\u00187ì\u0080µ5ß\fëðXã\u0096\u0015\u001et/³\u0010.·\u0000\u009c°M\u0010\u009f\u001e¦Þë\u0001VpSÕÖ\u0081Ç1C\u0019(\u0019\u0010v¾T \u0091R\u0015^\u0003\u0097\u008eÊ0X÷þJm\u008f8Û\u0011\f\u0093¼Á\u0080±\u0087\u0095ÄÕXâÿÎh6\u0010©+\u009eØB*ÝOF\u009a\u008bùÂ\u0017\\H 7ì\u0080µ5ß\fëðXã\u0096\u0015\u001et/:ý\u0095\u008b\u007f\u0080R¢S\u009d\u0016+8\u0012@Þ\b\u0094\\C\u0082ñì\u009bÕ\u0010ºÇéùaF\u008d=\n:e¨i®\u001b¤\u0010óM©\u0010\u001fàÝF\u0080Ý\u001c\u0014÷ÆØÖ\u0010Ô\u0082;ó¿YÊÚ´\u007fcí\u0088óÑ°\u0018\u0019\u0010v¾T \u0091R\u009f\u00887\u000b´GpÌ½Ñ(ð\u0083\u000bï\u0097\u00182oJ$\u009bFP]{\u009cÊD\u0095*X\u000bõµßß[?sú\u0018e×Øyþ\u008dM\u009cÂOD\tF\u0092UZËnÄ|v\t²È\u0010ºÇéùaF\u008d=qJã´\u0012É\u009fK\u0018Õ\u001f\u0086kÛ\u00066\u008d\u0088{(c õ\b\t\u0081\u009bF\u0015×<YC";
      int var8 = "\u0081Þãóuæð\u009eÝìñø\u009eù8£\u0010\u0081Þãóuæð\u009eÝìñø\u009eù8£\u0018.¯\f§ô#:8\u001e\u0082r\rø\bT¥ñ+¾\u0097mNí&\u0010\t¾i\u008bàò\u008dÌú\u0003OÌÀëKÊ \u0019\u0010v¾T \u0091R\u0000ÒÁø\u0098c»oøì0ÛêÅJ«Y>mÀä\u0089Ê\u000f8ì&f°±¨£\u000b¡i4Üü·`ð\u0097\u0017\u0089zC\u008f âÏ\u0003ä>\u0082ð´«@ëá\u001elý\u009cà\u001eÜ\f\u008abOÁÚØö-:F<\u0015<\u0010\u001a¼!Ï\"\u0083påaJ\u0016ñJìÅÇ07ì\u0080µ5ß\fëðXã\u0096\u0015\u001et/\u000e¾\u008c\u0081Îm#Dÿüøó\u0002\u0099\u008fDPÖJf°zô\u0095áÐ\u0001\u0010¬Ê\u0084ú\b\u0094\\C\u0082ñì\u009bÕ\blö{¹A,ü£\u0018uXÏ8Çö\u0086¯sÔbÚêÄ\"Ï%Ë\u0099|¶¢ð\u0015\bx\u00897\u008e\u008eáíß\b\u0010M²Jãf\u0084]\u00187ì\u0080µ5ß\fëðXã\u0096\u0015\u001et/³\u0010.·\u0000\u009c°M\u0010\u009f\u001e¦Þë\u0001VpSÕÖ\u0081Ç1C\u0019(\u0019\u0010v¾T \u0091R\u0015^\u0003\u0097\u008eÊ0X÷þJm\u008f8Û\u0011\f\u0093¼Á\u0080±\u0087\u0095ÄÕXâÿÎh6\u0010©+\u009eØB*ÝOF\u009a\u008bùÂ\u0017\\H 7ì\u0080µ5ß\fëðXã\u0096\u0015\u001et/:ý\u0095\u008b\u007f\u0080R¢S\u009d\u0016+8\u0012@Þ\b\u0094\\C\u0082ñì\u009bÕ\u0010ºÇéùaF\u008d=\n:e¨i®\u001b¤\u0010óM©\u0010\u001fàÝF\u0080Ý\u001c\u0014÷ÆØÖ\u0010Ô\u0082;ó¿YÊÚ´\u007fcí\u0088óÑ°\u0018\u0019\u0010v¾T \u0091R\u009f\u00887\u000b´GpÌ½Ñ(ð\u0083\u000bï\u0097\u00182oJ$\u009bFP]{\u009cÊD\u0095*X\u000bõµßß[?sú\u0018e×Øyþ\u008dM\u009cÂOD\tF\u0092UZËnÄ|v\t²È\u0010ºÇéùaF\u008d=qJã´\u0012É\u009fK\u0018Õ\u001f\u0086kÛ\u00066\u008d\u0088{(c õ\b\t\u0081\u009bF\u0015×<YC".length();
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

               var6 = "w¿)b\u0088m\u0080Ô\b\u0010M²Jãf\u0084]";
               var8 = "w¿)b\u0088m\u0080Ô\b\u0010M²Jãf\u0084]".length();
               var5 = '\b';
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
