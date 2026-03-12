package me.frep.vulcan.spigot;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Vulcan_Xe {
   private final String Vulcan__;
   private String Vulcan_F;
   private String Vulcan_N;
   private String Vulcan_k;
   private boolean Vulcan_L;
   private List Vulcan_t = new ArrayList();
   private static String Vulcan_C;
   private static final long a = Vulcan_n.a(-4911537812998782419L, -6369139167978596387L, MethodHandles.lookup().lookupClass()).a(206648374227148L);
   private static final String[] b;

   public Vulcan_Xe(String var1) {
      this.Vulcan__ = var1;
   }

   public void Vulcan_f(Object[] var1) {
      String var2 = (String)var1[0];
      this.Vulcan_F = var2;
   }

   public void Vulcan_J(Object[] var1) {
      String var2 = (String)var1[0];
      this.Vulcan_N = var2;
   }

   public void Vulcan_A(Object[] var1) {
      String var2 = (String)var1[0];
      this.Vulcan_k = var2;
   }

   public void Vulcan_V(Object[] var1) {
      boolean var2 = (Boolean)var1[0];
      this.Vulcan_L = var2;
   }

   public void Vulcan__(Object[] var1) {
      Vulcan_by var2 = (Vulcan_by)var1[0];
      this.Vulcan_t.add(var2);
   }

   public void Vulcan_o(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_B(String var0) {
      Vulcan_C = var0;
   }

   public static String Vulcan_k() {
      return Vulcan_C;
   }

   static {
      long var0 = a ^ 120228885034170L;
      Vulcan_B((String)null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[28];
      int var7 = 0;
      String var6 = "\r\fAÂ^B\u0080\u009f\bäÿñ\u0004\u0099ô\n\\\bå6P·Ã\u0086ü®\b®°Õµj»\u0012\u0091\bå\u009eMD\u0099±§(\bº\u008a;=EQât\bäÿñ\u0004\u0099ô\n\\\u0010ÊtJ\u00adò\u0083\b\u008aa±w´\u000f\u0012Û\u0092\bX¶ÕF¥è¸_\b¿Ü\u0011\u009bÙ$Á\u001b\u0010\u0014ÿA¥±Ï'\r¶MCÚ,\nf]\bXÖc\u001f\u008b\u0097´t\u0010'1F\u0013\u0087,\u0099¿¦W\u0001ï\u009bY¬Å\b\u0015¼oCÄf|ú\u0018¸\u0010<\u009d©ÎÜÒ\u009d3À\u001d\u0007»N¶Ù¥`oB\u001fú \u0010]\t\u0087\u0004ÒK39^þA@¿z\f\u0010\bÊ\u0083c\u007fn\u0097Ðb\u0010ÑÿÃ\u008c\u0091¯ ÑIÀ3\u0083f:¯[\bûyó:´¸(6\u0010lÓ\u0016~Ä\u0080Ì¾[)ÇS3i\u008bø\u0010'1F\u0013\u0087,\u0099¿¦W\u0001ï\u009bY¬Å\bN0\u0082\u0007\u0019g\\Ø\b¿¯¶s0×ô5\u0010\r\u0013\u001b¶Äüß4«U\u001f\u0005½ôÊ¢\u0010´\u0005\u008eº\u008dlöëÎO\u0094\réKðp\bd´\u007fçjÀt~";
      int var8 = "\r\fAÂ^B\u0080\u009f\bäÿñ\u0004\u0099ô\n\\\bå6P·Ã\u0086ü®\b®°Õµj»\u0012\u0091\bå\u009eMD\u0099±§(\bº\u008a;=EQât\bäÿñ\u0004\u0099ô\n\\\u0010ÊtJ\u00adò\u0083\b\u008aa±w´\u000f\u0012Û\u0092\bX¶ÕF¥è¸_\b¿Ü\u0011\u009bÙ$Á\u001b\u0010\u0014ÿA¥±Ï'\r¶MCÚ,\nf]\bXÖc\u001f\u008b\u0097´t\u0010'1F\u0013\u0087,\u0099¿¦W\u0001ï\u009bY¬Å\b\u0015¼oCÄf|ú\u0018¸\u0010<\u009d©ÎÜÒ\u009d3À\u001d\u0007»N¶Ù¥`oB\u001fú \u0010]\t\u0087\u0004ÒK39^þA@¿z\f\u0010\bÊ\u0083c\u007fn\u0097Ðb\u0010ÑÿÃ\u008c\u0091¯ ÑIÀ3\u0083f:¯[\bûyó:´¸(6\u0010lÓ\u0016~Ä\u0080Ì¾[)ÇS3i\u008bø\u0010'1F\u0013\u0087,\u0099¿¦W\u0001ï\u009bY¬Å\bN0\u0082\u0007\u0019g\\Ø\b¿¯¶s0×ô5\u0010\r\u0013\u001b¶Äüß4«U\u001f\u0005½ôÊ¢\u0010´\u0005\u008eº\u008dlöëÎO\u0094\réKðp\bd´\u007fçjÀt~".length();
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

               var6 = "ßie\u0017 /éÿ\u001dÜv{nYÒ\u008aÆíº\u000f·Ü¾#Túá¶~\u0005A\u000bA·\tEò[Ã\u008bÉ\u0081`ú½\u008dBà\b¿Ü\u0011\u009bÙ$Á\u001b";
               var8 = "ßie\u0017 /éÿ\u001dÜv{nYÒ\u008aÆíº\u000f·Ü¾#Túá¶~\u0005A\u000bA·\tEò[Ã\u008bÉ\u0081`ú½\u008dBà\b¿Ü\u0011\u009bÙ$Á\u001b".length();
               var5 = '0';
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   private static IOException a(IOException var0) {
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
