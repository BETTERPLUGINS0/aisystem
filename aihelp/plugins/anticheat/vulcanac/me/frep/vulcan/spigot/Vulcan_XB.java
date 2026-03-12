package me.frep.vulcan.spigot;

import ac.vulcan.anticheat.UniversalRunnable;
import java.lang.invoke.MethodHandles;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;

class Vulcan_XB extends UniversalRunnable {
   final List Vulcan_i;
   final Vulcan_iE Vulcan_Q;
   final int Vulcan_U;
   final AbstractCheck Vulcan_f;
   final Vulcan_X2 Vulcan_p;
   private static final long b = Vulcan_n.a(1454601424908533540L, 5293827450921985634L, MethodHandles.lookup().lookupClass()).a(186165461465722L);
   private static final String[] c;

   Vulcan_XB(Vulcan_X2 var1, List var2, Vulcan_iE var3, int var4, AbstractCheck var5) {
      long var6 = b ^ 117550295327759L;
      String[] var10000 = Vulcan_X2.Vulcan_V();
      this.Vulcan_p = var1;
      this.Vulcan_i = var2;
      this.Vulcan_Q = var3;
      this.Vulcan_U = var4;
      this.Vulcan_f = var5;
      String[] var8 = var10000;
      super();
      if (var8 != null) {
         int var9 = AbstractCheck.Vulcan_m();
         ++var9;
         AbstractCheck.Vulcan_H(var9);
      }

   }

   public void run() {
      long var1 = b ^ 33691084380225L;
      Vulcan_X2.Vulcan_V();
      this.Vulcan_i.forEach(this::lambda$run$0);

      try {
         if (AbstractCheck.Vulcan_m() != 0) {
            Vulcan_X2.Vulcan_q(new String[1]);
         }

      } catch (RuntimeException var4) {
         throw a(var4);
      }
   }

   private void lambda$run$0(Vulcan_iE param1, int param2, AbstractCheck param3, String param4) {
      // $FF: Couldn't be decompiled
   }

   static {
      long var0 = b ^ 16589461443217L;
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
      String var6 = ";}$?WO\u0088\u009c,«F\u008eÚ1Ü \bø]¡%¥#QO\b»ò\u0091T´\u0095\u0095N\u0010^ZÂ\u008e\u0097B.Lsw\fÒ\u008d\u0083\u0007Ë\u0010^ZÂ\u008e\u0097B.Lsw\fÒ\u008d\u0083\u0007Ë\bn4\bÉ\u008c\u0019\u009b\u0003\u0010)Ó£÷·Â0#5ó¬ï\u009aÛÒÚ\bS¬\u00931xn)B\b£\u0097Ì\u0012\u0099\u001bÞ¢\u0010É\u0010Ùyp\u008d\u0090Æõ9ÕDû³2\u0080\u0010ª·WÁ\u0012Z\u000fhö)ýó°!\u001b9\u0018ä\u0018\u0019ãìöd\u0090o-n@é*\u0094d\u001b\u0019\u0011$Xþ\u009fÞ\u0010ä\u0018\u0019ãìöd\u0090H¦\u0097PÁ\u001c©)\u0010ª·WÁ\u0012Z\u000fhö)ýó°!\u001b9\u0010)Ó£÷·Â0#5ó¬ï\u009aÛÒÚ\b»ò\u0091T´\u0095\u0095N\bå'6\u0013SAòO\b£\u0097Ì\u0012\u0099\u001bÞ¢\u0010\u0090G$¶,%\u008e\u008f\u00ad©_\u0083®~\r\u0004\u0010º&v\u001e\u008aèÕñ·\u0082PN\u009cÑÌ\u0000\bå'6\u0013SAòO\bø]¡%¥#QO\u0010;}$?WO\u0088\u009c,«F\u008eÚ1Ü \u0010Ê\u009fp<#}¦\u0098¬Sd0\u001f\u0087<s\u0010º&v\u001e\u008aèÕñ·\u0082PN\u009cÑÌ\u0000\u0010\u0090G$¶,%\u008e\u008f\u00ad©_\u0083®~\r\u0004\u0010ä\u0018\u0019ãìöd\u0090H¦\u0097PÁ\u001c©)\u0010É\u0010Ùyp\u008d\u0090Æõ9ÕDû³2\u0080\bn4\bÉ\u008c\u0019\u009b\u0003\u0010Ê\u009fp<#}¦\u0098¬Sd0\u001f\u0087<s\u0018ä\u0018\u0019ãìöd\u0090o-n@é*\u0094d\u001b\u0019\u0011$Xþ\u009fÞ\bK\u0013±\u0099½Ç\u001e=";
      int var8 = ";}$?WO\u0088\u009c,«F\u008eÚ1Ü \bø]¡%¥#QO\b»ò\u0091T´\u0095\u0095N\u0010^ZÂ\u008e\u0097B.Lsw\fÒ\u008d\u0083\u0007Ë\u0010^ZÂ\u008e\u0097B.Lsw\fÒ\u008d\u0083\u0007Ë\bn4\bÉ\u008c\u0019\u009b\u0003\u0010)Ó£÷·Â0#5ó¬ï\u009aÛÒÚ\bS¬\u00931xn)B\b£\u0097Ì\u0012\u0099\u001bÞ¢\u0010É\u0010Ùyp\u008d\u0090Æõ9ÕDû³2\u0080\u0010ª·WÁ\u0012Z\u000fhö)ýó°!\u001b9\u0018ä\u0018\u0019ãìöd\u0090o-n@é*\u0094d\u001b\u0019\u0011$Xþ\u009fÞ\u0010ä\u0018\u0019ãìöd\u0090H¦\u0097PÁ\u001c©)\u0010ª·WÁ\u0012Z\u000fhö)ýó°!\u001b9\u0010)Ó£÷·Â0#5ó¬ï\u009aÛÒÚ\b»ò\u0091T´\u0095\u0095N\bå'6\u0013SAòO\b£\u0097Ì\u0012\u0099\u001bÞ¢\u0010\u0090G$¶,%\u008e\u008f\u00ad©_\u0083®~\r\u0004\u0010º&v\u001e\u008aèÕñ·\u0082PN\u009cÑÌ\u0000\bå'6\u0013SAòO\bø]¡%¥#QO\u0010;}$?WO\u0088\u009c,«F\u008eÚ1Ü \u0010Ê\u009fp<#}¦\u0098¬Sd0\u001f\u0087<s\u0010º&v\u001e\u008aèÕñ·\u0082PN\u009cÑÌ\u0000\u0010\u0090G$¶,%\u008e\u008f\u00ad©_\u0083®~\r\u0004\u0010ä\u0018\u0019ãìöd\u0090H¦\u0097PÁ\u001c©)\u0010É\u0010Ùyp\u008d\u0090Æõ9ÕDû³2\u0080\bn4\bÉ\u008c\u0019\u009b\u0003\u0010Ê\u009fp<#}¦\u0098¬Sd0\u001f\u0087<s\u0018ä\u0018\u0019ãìöd\u0090o-n@é*\u0094d\u001b\u0019\u0011$Xþ\u009fÞ\bK\u0013±\u0099½Ç\u001e=".length();
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
                  c = var9;
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

               var6 = "K\u0013±\u0099½Ç\u001e=\bS¬\u00931xn)B";
               var8 = "K\u0013±\u0099½Ç\u001e=\bS¬\u00931xn)B".length();
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
