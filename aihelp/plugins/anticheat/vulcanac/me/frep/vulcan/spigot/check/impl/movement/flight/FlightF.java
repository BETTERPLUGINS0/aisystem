package me.frep.vulcan.spigot.check.impl.movement.flight;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.event.Event;

@CheckInfo(
   name = "Flight",
   type = 'F',
   experimental = true,
   complexType = "Prediction",
   description = "Invalid Y-Axis movement."
)
public class FlightF extends AbstractCheck {
   private static int Vulcan_B;
   private static final long b = Vulcan_n.a(8755830687623926707L, -45339933406449301L, MethodHandles.lookup().lookupClass()).a(51354968342611L);
   private static final String[] d;

   public FlightF(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   private double Vulcan_Y(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_X(int var0) {
      Vulcan_B = var0;
   }

   public static int Vulcan_r() {
      return Vulcan_B;
   }

   public static int Vulcan_D() {
      int var0 = Vulcan_r();

      try {
         return var0 == 0 ? 112 : 0;
      } catch (RuntimeException var1) {
         throw a(var1);
      }
   }

   static {
      long var0 = b ^ 78481922153894L;
      Vulcan_X(0);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[9];
      int var7 = 0;
      String var6 = "¹ÓX\u0010Eâ\u0004´\bÌ\r\u0017É½Z\u000e\u0088\b:¦xÚié\u0084'\bÃß^\u0001~çÞ\u001c\b\u0096\u0083HÒ\u0089l{ú\bÖ\u0018C\u0015ÝÆ\rB\bÈv\u0082\u0088§\u00ad\u007f\u0088";
      int var8 = "¹ÓX\u0010Eâ\u0004´\bÌ\r\u0017É½Z\u000e\u0088\b:¦xÚié\u0084'\bÃß^\u0001~çÞ\u001c\b\u0096\u0083HÒ\u0089l{ú\bÖ\u0018C\u0015ÝÆ\rB\bÈv\u0082\u0088§\u00ad\u007f\u0088".length();
      char var5 = '\b';
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var11 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var11.getBytes("ISO-8859-1"));
            String var15 = b(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var15;
               if ((var4 += var5) >= var8) {
                  d = var9;
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

               var6 = "tYÜÕR|xN\b_öÙsz\u0086.>";
               var8 = "tYÜÕR|xN\b_öÙsz\u0086.>".length();
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
