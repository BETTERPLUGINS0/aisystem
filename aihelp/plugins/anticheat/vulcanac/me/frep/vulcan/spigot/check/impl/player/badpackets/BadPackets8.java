package me.frep.vulcan.spigot.check.impl.player.badpackets;

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
   name = "Bad Packets",
   type = '8',
   complexType = "Nuker",
   description = "Invalid Block Dig packets."
)
public class BadPackets8 extends AbstractCheck {
   private int Vulcan_j;
   private int Vulcan__;
   private boolean Vulcan_y;
   private long Vulcan_L;
   private static boolean Vulcan_h;
   private static final long b = Vulcan_n.a(-2977227127696104696L, -1909907538349096169L, MethodHandles.lookup().lookupClass()).a(40812881723745L);
   private static final String[] d;

   public BadPackets8(Vulcan_iE var1) {
      long var2 = b ^ 109444182342910L;
      boolean var10000 = Vulcan_l();
      super(var1);
      boolean var4 = var10000;
      this.Vulcan_j = 0;
      this.Vulcan__ = -1;
      this.Vulcan_y = false;
      this.Vulcan_L = -1L;
      if (!var4) {
         int var5 = AbstractCheck.Vulcan_V();
         ++var5;
         AbstractCheck.Vulcan_H(var5);
      }

   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }

   public static void Vulcan_M(boolean var0) {
      Vulcan_h = var0;
   }

   public static boolean Vulcan_N() {
      return Vulcan_h;
   }

   public static boolean Vulcan_l() {
      boolean var0 = Vulcan_N();

      try {
         return !var0;
      } catch (RuntimeException var1) {
         throw a(var1);
      }
   }

   static {
      long var0 = b ^ 34929902863090L;
      Vulcan_M(false);
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
      String var6 = "÷·ýØ¶÷ìÙ\bf$\u0081!\u0080Ï¹>";
      int var8 = "÷·ýØ¶÷ìÙ\bf$\u0081!\u0080Ï¹>".length();
      char var5 = '\b';
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
