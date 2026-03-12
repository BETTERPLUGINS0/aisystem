package me.frep.vulcan.spigot;

import ac.vulcan.anticheat.Vulcan_iF;
import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.api.event.VulcanViolationResetEvent;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.Bukkit;

public class Vulcan_iR implements Runnable {
   private static Vulcan_iF Vulcan_S;
   static final boolean Vulcan_a;
   private static boolean Vulcan_Y;
   private static final long a = Vulcan_n.a(7827967205265253883L, 8753772760486860271L, MethodHandles.lookup().lookupClass()).a(146653674139031L);
   private static final String[] b;

   public static void Vulcan_c(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_m(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_J(Object[] var1) {
      long var2 = (Long)var1[0];
      long var10000 = a ^ var2;
      boolean var4 = Vulcan_U();

      Vulcan_iF var6;
      label22: {
         try {
            var6 = Vulcan_S;
            if (var4) {
               break label22;
            }

            if (var6 == null) {
               return;
            }
         } catch (RuntimeException var5) {
            throw a(var5);
         }

         var6 = Vulcan_S;
      }

      var6.Vulcan_q(new Object[0]);
      Vulcan_S = null;
   }

   public void run() {
      long var1 = a ^ 15111927276996L;
      long var3 = var1 ^ 52123591329893L;
      Vulcan_c(new Object[]{var3});
   }

   private static void lambda$reset$2(Vulcan_iE var0) {
      var0.Vulcan_Q(new Object[]{0});
      var0.Vulcan_X(new Object[]{0});
      var0.Vulcan_R(new Object[]{0});
      var0.Vulcan_q(new Object[]{0});
      var0.Vulcan_r(new Object[]{0});
      var0.Vulcan_e(new Object[]{0});
      var0.Vulcan_K(new Object[]{0});
      var0.Vulcan_N(new Object[0]).forEach(Vulcan_iR::lambda$null$1);
   }

   private static void lambda$null$1(AbstractCheck var0) {
      var0.setVl(0);
      var0.Vulcan_L(new Object[0]);
   }

   private static void lambda$reset$0(VulcanViolationResetEvent var0) {
      Bukkit.getPluginManager().callEvent(var0);
   }

   static {
      long var0 = a ^ 6511552178276L;
      Vulcan_c(false);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[2];
      int var7 = 0;
      String var6 = "\u001f\r\u0091A¶ì\u0092lw\u0091¨\u0002VKr\u0012\u0015\u008eBw¿¹4vÆ³T*$N Þ2B\u008dlÈ©_\u00930G¸¬húÍ\u0098U\u001e\u0083Ð¼²å¼\u0014\u0011M\u0006úe\u0013n¿\u0016Uè$£%:]gö\bõñ©ÞBHï\u0082\b\u0019\u009f3È";
      int var8 = "\u001f\r\u0091A¶ì\u0092lw\u0091¨\u0002VKr\u0012\u0015\u008eBw¿¹4vÆ³T*$N Þ2B\u008dlÈ©_\u00930G¸¬húÍ\u0098U\u001e\u0083Ð¼²å¼\u0014\u0011M\u0006úe\u0013n¿\u0016Uè$£%:]gö\bõñ©ÞBHï\u0082\b\u0019\u009f3È".length();
      char var5 = '(';
      int var4 = -1;

      while(true) {
         ++var4;
         byte[] var10 = var2.doFinal(var6.substring(var4, var4 + var5).getBytes("ISO-8859-1"));
         String var14 = a(var10).intern();
         boolean var10001 = true;
         var9[var7++] = var14;
         if ((var4 += var5) >= var8) {
            b = var9;

            boolean var12;
            label25: {
               try {
                  if (!Vulcan_iR.class.desiredAssertionStatus()) {
                     var12 = true;
                     break label25;
                  }
               } catch (RuntimeException var11) {
                  throw a(var11);
               }

               var12 = false;
            }

            Vulcan_a = var12;
            return;
         }

         var5 = var6.charAt(var4);
      }
   }

   public static void Vulcan_c(boolean var0) {
      Vulcan_Y = var0;
   }

   public static boolean Vulcan_U() {
      return Vulcan_Y;
   }

   public static boolean Vulcan_I() {
      boolean var0 = Vulcan_U();

      try {
         return !var0;
      } catch (RuntimeException var1) {
         throw a(var1);
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
