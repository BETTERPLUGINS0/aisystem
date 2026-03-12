package me.frep.vulcan.spigot;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.Date;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;

public final class Vulcan_X7 {
   private static final String Vulcan_Z;
   private static Vulcan_G Vulcan_J;
   private static File Vulcan_V;
   private static final long a = Vulcan_n.a(-9195219020362369581L, -1834497768586487959L, MethodHandles.lookup().lookupClass()).a(129429179813991L);
   private static final String[] b;

   public static void Vulcan_B(Object[] var0) {
      long var1 = (Long)var0[0];
      var1 ^= a;
      long var3 = var1 ^ 109869313997187L;

      try {
         Vulcan_J.save(Vulcan_V);
      } catch (Exception var8) {
         File var10002 = Vulcan_Xs.INSTANCE.Vulcan_J().getDataFolder();
         StringBuilder var10003 = new StringBuilder();
         String[] var7 = b;
         File var6 = new File(var10002, var10003.append(var7[4]).append((new Date()).getTime()).toString());
         Vulcan_Xs.INSTANCE.Vulcan_J().getLogger().log(Level.SEVERE, var7[12]);
         Vulcan_Xs.INSTANCE.Vulcan_J().getLogger().log(Level.SEVERE, var7[0] + var6.getName());
         Vulcan_Xs.INSTANCE.Vulcan_J().getLogger().log(Level.SEVERE, var7[10]);
         Vulcan_V.renameTo(var6);
         Vulcan_k(new Object[]{var3});
      }

   }

   public static void Vulcan_A(Object[] var0) {
      long var1 = (Long)var0[0];
      var1 ^= a;
      long var3 = var1 ^ 111242959075019L;

      try {
         Vulcan_J.load(Vulcan_V);
      } catch (Exception var8) {
         File var10002 = Vulcan_Xs.INSTANCE.Vulcan_J().getDataFolder();
         StringBuilder var10003 = new StringBuilder();
         String[] var7 = b;
         File var6 = new File(var10002, var10003.append(var7[2]).append((new Date()).getTime()).toString());
         Vulcan_Xs.INSTANCE.Vulcan_J().getLogger().log(Level.SEVERE, var7[1]);
         Vulcan_Xs.INSTANCE.Vulcan_J().getLogger().log(Level.SEVERE, var7[6] + var6.getName());
         Vulcan_Xs.INSTANCE.Vulcan_J().getLogger().log(Level.SEVERE, var7[7]);
         Vulcan_V.renameTo(var6);
         Vulcan_k(new Object[]{var3});
      }

   }

   public static void Vulcan_k(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static void Vulcan_e(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_O(Object[] var0) {
      String var1 = (String)var0[0];
      return Vulcan_J.getInt(var1);
   }

   public static int Vulcan_J(Object[] var0) {
      return Vulcan_O(new Object[]{b[8]});
   }

   public static void Vulcan_T(Object[] var0) {
      long var1 = (Long)var0[0];
      String var3 = (String)var0[1];
      Object var4 = (Object)var0[2];
      var1 ^= a;
      long var5 = var1 ^ 105078682886310L;
      long var7 = var1 ^ 98207613890542L;
      Vulcan_J.set(var3, var4);
      Vulcan_i9.Vulcan_B();
      Vulcan_B(new Object[]{var5});
      Vulcan_A(new Object[]{var7});

      try {
         if (AbstractCheck.Vulcan_V() == 0) {
            Vulcan_i9.Vulcan_c(new int[2]);
         }

      } catch (UnsupportedOperationException var10) {
         throw a((Exception)var10);
      }
   }

   private Vulcan_X7() {
      throw new UnsupportedOperationException(b[13]);
   }

   static {
      long var0 = a ^ 19983482471017L;
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
      String var6 = "Õ;îØxX\u00973bº\bþdÁîºÀ\u0017ô¹\u0092ôj\u0003îÜ\u0084\u0015\u0089ë\u008eùå\nÆ\u009d\u0015[e8b9%\n\u0091ª/F@5ðQâjTì «\u0097\u000fLûá\bHht\u0005Qòº\u0096ß6q\u0099\u009b\u0080ûB8ÁßuS`«\u009cÔ\u0010Ü$\u001b\u008eþ\u009c±%b¿z¸\u0087\u008ej\u0017\b\u0087\"\n_k6\u0001N\u0010Ü$\u001b\u008eþ\u009c±%b¿z¸\u0087\u008ej\u0017\u0010\u000e\u0016\u008dGûå[öä\u0014À&éË\tü8Õ;îØxX\u00973bº\bþdÁîºÀ\u0017ô¹\u0092ôj\u0003îÜ\u0084\u0015\u0089ë\u008eùå\nÆ\u009d\u0015[e8b9%\n\u0091ª/F@5ðQâjTì8òÁßNE\u008aÉ\u0096W\u0006Ì\u001d[á\u0014y\u0015±¦fÅ/Ý\u00adfxë7\u00ad¨»À!ï\u0096\u0099£Ý\nI\u0081\u000b<u%g¼ßn\u0014Ò\u0013\u0085\be=\u0010hÜßL\u0014ÖV`¯°YÂJ\u001dx.\b³d\u0004¶É\u0085\u008dN8òÁßNE\u008aÉ\u0096W\u0006Ì\u001d[á\u0014y\u0015±¦fÅ/Ý\u00adfxë7\u00ad¨»À!ï\u0096\u0099£Ý\nI\u0081\u000b<u%g¼ßn\u0014Ò\u0013\u0085\be=\u0010\u000e\u0016\u008dGûå[öä\u0014À&éË\tü";
      int var8 = "Õ;îØxX\u00973bº\bþdÁîºÀ\u0017ô¹\u0092ôj\u0003îÜ\u0084\u0015\u0089ë\u008eùå\nÆ\u009d\u0015[e8b9%\n\u0091ª/F@5ðQâjTì «\u0097\u000fLûá\bHht\u0005Qòº\u0096ß6q\u0099\u009b\u0080ûB8ÁßuS`«\u009cÔ\u0010Ü$\u001b\u008eþ\u009c±%b¿z¸\u0087\u008ej\u0017\b\u0087\"\n_k6\u0001N\u0010Ü$\u001b\u008eþ\u009c±%b¿z¸\u0087\u008ej\u0017\u0010\u000e\u0016\u008dGûå[öä\u0014À&éË\tü8Õ;îØxX\u00973bº\bþdÁîºÀ\u0017ô¹\u0092ôj\u0003îÜ\u0084\u0015\u0089ë\u008eùå\nÆ\u009d\u0015[e8b9%\n\u0091ª/F@5ðQâjTì8òÁßNE\u008aÉ\u0096W\u0006Ì\u001d[á\u0014y\u0015±¦fÅ/Ý\u00adfxë7\u00ad¨»À!ï\u0096\u0099£Ý\nI\u0081\u000b<u%g¼ßn\u0014Ò\u0013\u0085\be=\u0010hÜßL\u0014ÖV`¯°YÂJ\u001dx.\b³d\u0004¶É\u0085\u008dN8òÁßNE\u008aÉ\u0096W\u0006Ì\u001d[á\u0014y\u0015±¦fÅ/Ý\u00adfxë7\u00ad¨»À!ï\u0096\u0099£Ý\nI\u0081\u000b<u%g¼ßn\u0014Ò\u0013\u0085\be=\u0010\u000e\u0016\u008dGûå[öä\u0014À&éË\tü".length();
      char var5 = '8';
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
                  Vulcan_Z = b[3];
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

               var6 = "«\u0097\u000fLûá\bHht\u0005Qòº\u0096ß6q\u0099\u009b\u0080ûB8ÁßuS`«\u009cÔ8o\\\u0011öØ\u000e[t[\u0082´\u0093\u00937\u009f¦opü\u0012~K¼F\u0093ÿµQI\u0097bÐ\u00adÁ bTN¿¶s\u0007\u0016\u001c\u0086¨\u0089\u0091Zì¦Ónþh ";
               var8 = "«\u0097\u000fLûá\bHht\u0005Qòº\u0096ß6q\u0099\u009b\u0080ûB8ÁßuS`«\u009cÔ8o\\\u0011öØ\u000e[t[\u0082´\u0093\u00937\u009f¦opü\u0012~K¼F\u0093ÿµQI\u0097bÐ\u00adÁ bTN¿¶s\u0007\u0016\u001c\u0086¨\u0089\u0091Zì¦Ónþh ".length();
               var5 = ' ';
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
