package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Vulcan_Xr {
   private static int[] Vulcan_Q;
   private static final long a = Vulcan_n.a(-7929073938401897288L, -5781031270976986192L, MethodHandles.lookup().lookupClass()).a(100257082890344L);
   private static final String[] b;

   public static void Vulcan_b(Object[] var0) {
      Vulcan_Xs.INSTANCE.Vulcan_V().execute(Vulcan_Xr::lambda$executeBanWave$4);
   }

   public static boolean Vulcan_D(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_g(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$executeBanWave$4() {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$null$3(int var0, String var1) {
      Vulcan_eG.Vulcan__(var1.replaceAll(b[20], Integer.toString(var0)));
   }

   private static void lambda$null$2(String var0, String var1) {
      Vulcan_eG.Vulcan__(var1.replaceAll(b[12], var0));
   }

   private static void lambda$null$1(String var0) {
      Vulcan_i9.Vulcan_hf.forEach(Vulcan_Xr::lambda$null$0);
   }

   private static void lambda$null$0(String var0, String var1) {
      long var2 = a ^ 82474860257163L;
      long var4 = var2 ^ 71902277393856L;
      Vulcan_eG.Vulcan_I(new Object[]{var1.replaceAll(b[6], var0), var4});
   }

   public static void Vulcan_J(int[] var0) {
      Vulcan_Q = var0;
   }

   public static int[] Vulcan_N() {
      return Vulcan_Q;
   }

   static {
      int[] var10000 = new int[2];
      long var0 = a ^ 112275126285939L;
      Vulcan_J(var10000);
      Cipher var2;
      Cipher var11 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var11.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[24];
      int var7 = 0;
      String var6 = "\u008dù¥ò\u008a\u0092ÊÏÍ\u008fêºæ\u000fÖ\u0097\b&Òî(>\u0084ä&\u0010ÅÝ\u007f$5a!Ð©\u0097\tìá½»\u0013\b\u0001ôÚby\u0086¹¢\b&Òî(>\u0084ä&\u0010Ù¾,HG·û²À8jÆ·óÆ|\u0010\u0019ï\fû\u0019\u0014|\u0081\\(ð\u0082\u0085F\u0099¶\b@\r\u0097ÐÇ%´\u009a\u0010H:=$³Ì\u0091Ø\r\u009d<ÄrD\u0002ì\u0010çwnÅÂ£#\u0087\u0018Ø\u0002Ò\u0091Þî%\b@\r\u0097ÐÇ%´\u009a\u0010ÅÝ\u007f$5a!Ð©\u0097\tìá½»\u0013\u0010\u0019ï\fû\u0019\u0014|\u0081\\(ð\u0082\u0085F\u0099¶\u0010Ù¾,HG·û²*\u0096(PíQ¶ò\b8å¤6oy-\u0018\u0010\u001bç£¦æÙ©<\u000b\u0012Ò&\u009a\u000b:±\u0010Ù¾,HG·û²À8jÆ·óÆ|\u0010\u008dù¥ò\u008a\u0092ÊÏÍ\u008fêºæ\u000fÖ\u0097\u0010çwnÅÂ£#\u0087\u0018Ø\u0002Ò\u0091Þî%\u0010H:=$³Ì\u0091Ø\r\u009d<ÄrD\u0002ì\u0010\u0096ä+\u0085\u001dj@+ä\u000b\u008b\u0094v¸ê¦\b\u0001ôÚby\u0086¹¢";
      int var8 = "\u008dù¥ò\u008a\u0092ÊÏÍ\u008fêºæ\u000fÖ\u0097\b&Òî(>\u0084ä&\u0010ÅÝ\u007f$5a!Ð©\u0097\tìá½»\u0013\b\u0001ôÚby\u0086¹¢\b&Òî(>\u0084ä&\u0010Ù¾,HG·û²À8jÆ·óÆ|\u0010\u0019ï\fû\u0019\u0014|\u0081\\(ð\u0082\u0085F\u0099¶\b@\r\u0097ÐÇ%´\u009a\u0010H:=$³Ì\u0091Ø\r\u009d<ÄrD\u0002ì\u0010çwnÅÂ£#\u0087\u0018Ø\u0002Ò\u0091Þî%\b@\r\u0097ÐÇ%´\u009a\u0010ÅÝ\u007f$5a!Ð©\u0097\tìá½»\u0013\u0010\u0019ï\fû\u0019\u0014|\u0081\\(ð\u0082\u0085F\u0099¶\u0010Ù¾,HG·û²*\u0096(PíQ¶ò\b8å¤6oy-\u0018\u0010\u001bç£¦æÙ©<\u000b\u0012Ò&\u009a\u000b:±\u0010Ù¾,HG·û²À8jÆ·óÆ|\u0010\u008dù¥ò\u008a\u0092ÊÏÍ\u008fêºæ\u000fÖ\u0097\u0010çwnÅÂ£#\u0087\u0018Ø\u0002Ò\u0091Þî%\u0010H:=$³Ì\u0091Ø\r\u009d<ÄrD\u0002ì\u0010\u0096ä+\u0085\u001dj@+ä\u000b\u008b\u0094v¸ê¦\b\u0001ôÚby\u0086¹¢".length();
      char var5 = 16;
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var12 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var12.getBytes("ISO-8859-1"));
            String var16 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var16;
               if ((var4 += var5) >= var8) {
                  b = var9;
                  return;
               }

               var5 = var6.charAt(var4);
               break;
            default:
               var9[var7++] = var16;
               if ((var4 += var5) < var8) {
                  var5 = var6.charAt(var4);
                  continue label27;
               }

               var6 = "8å¤6oy-\u0018\u0010Ù¾,HG·û²*\u0096(PíQ¶ò";
               var8 = "8å¤6oy-\u0018\u0010Ù¾,HG·û²*\u0096(PíQ¶ò".length();
               var5 = '\b';
               var4 = -1;
            }

            ++var4;
            var12 = var6.substring(var4, var4 + var5);
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
