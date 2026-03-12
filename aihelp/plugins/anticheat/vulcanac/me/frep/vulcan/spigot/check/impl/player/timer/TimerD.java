package me.frep.vulcan.spigot.check.impl.player.timer;

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
   name = "Timer",
   type = 'D',
   complexType = "Balance",
   description = "Increased game speed."
)
public class TimerD extends AbstractCheck {
   private long Vulcan_H;
   private long Vulcan_t;
   private int Vulcan_k;
   private static int[] Vulcan_z;
   private static final long b = Vulcan_n.a(-7147130935586443543L, 7658207414781686268L, MethodHandles.lookup().lookupClass()).a(62610801170821L);
   private static final String[] d;

   public TimerD(Vulcan_iE var1) {
      long var2 = b ^ 76052174911628L;
      Vulcan_F();
      super(var1);
      this.Vulcan_H = 0L;
      this.Vulcan_t = 0L;
      this.Vulcan_k = 0;

      try {
         if (AbstractCheck.Vulcan_V() == 0) {
            Vulcan_F(new int[3]);
         }

      } catch (RuntimeException var5) {
         throw a(var5);
      }
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }

   public static void Vulcan_F(int[] var0) {
      Vulcan_z = var0;
   }

   public static int[] Vulcan_F() {
      return Vulcan_z;
   }

   static {
      int[] var10000 = new int[5];
      long var0 = b ^ 30621911740878L;
      Vulcan_F(var10000);
      Cipher var2;
      Cipher var11 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var11.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[2];
      int var7 = 0;
      String var6 = "¼OzqpájÛ\u0010Ì6s\u0087v9^¦ÉÔS¾\u008bÔ{\u000b";
      int var8 = "¼OzqpájÛ\u0010Ì6s\u0087v9^¦ÉÔS¾\u008bÔ{\u000b".length();
      char var5 = '\b';
      int var4 = -1;

      while(true) {
         ++var4;
         byte[] var10 = var2.doFinal(var6.substring(var4, var4 + var5).getBytes("ISO-8859-1"));
         String var13 = b(var10).intern();
         boolean var10001 = true;
         var9[var7++] = var13;
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
