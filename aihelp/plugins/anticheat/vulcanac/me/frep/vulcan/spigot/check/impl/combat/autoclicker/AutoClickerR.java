package me.frep.vulcan.spigot.check.impl.combat.autoclicker;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.Vulcan_c;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Auto Clicker",
   type = 'R',
   complexType = "Consistency",
   description = "Impossible consistency."
)
public class AutoClickerR extends AbstractCheck {
   private final Vulcan_c Vulcan_x = new Vulcan_c(30);
   private static final long b = Vulcan_n.a(-35710207463662898L, -866370562323090173L, MethodHandles.lookup().lookupClass()).a(148586476474030L);
   private static final String[] d;

   public AutoClickerR(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   private static boolean lambda$handle$0(Long var0) {
      long var1 = b ^ 13571942961673L;
      AbstractCheck[] var3 = AutoClickerT.Vulcan_P();

      int var10000;
      label32: {
         try {
            long var5;
            var10000 = (var5 = var0 - 150L) == 0L ? 0 : (var5 < 0L ? -1 : 1);
            if (var3 == null) {
               return (boolean)var10000;
            }

            if (var10000 > 0) {
               break label32;
            }
         } catch (RuntimeException var4) {
            throw a(var4);
         }

         var10000 = 0;
         return (boolean)var10000;
      }

      var10000 = 1;
      return (boolean)var10000;
   }

   static {
      long var0 = b ^ 20890597276421L;
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
      String var6 = "ùúç\u0007©©Û;úV\u0080x\u0004\rt\u0007\u0010\u0002À§\b\u009d#\u0011×\u0095ZùÙ¢\u0003\u0004\u0081";
      int var8 = "ùúç\u0007©©Û;úV\u0080x\u0004\rt\u0007\u0010\u0002À§\b\u009d#\u0011×\u0095ZùÙ¢\u0003\u0004\u0081".length();
      char var5 = 16;
      int var4 = -1;

      while(true) {
         ++var4;
         byte[] var10 = var2.doFinal(var6.substring(var4, var4 + var5).getBytes("ISO-8859-1"));
         String var12 = b(var10).intern();
         boolean var10001 = true;
         var9[var7++] = var12;
         if ((var4 += var5) >= var8) {
            d = var9;
            return;
         }

         var5 = var6.charAt(var4);
      }
   }

   private static RuntimeException a(RuntimeException var0) {
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
