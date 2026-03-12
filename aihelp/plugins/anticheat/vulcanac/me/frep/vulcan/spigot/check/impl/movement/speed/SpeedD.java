package me.frep.vulcan.spigot.check.impl.movement.speed;

import java.lang.invoke.MethodHandles;
import java.util.UUID;
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
   name = "Speed",
   type = 'D',
   complexType = "Ground",
   description = "Moving too quickly on the ground."
)
public class SpeedD extends AbstractCheck {
   public final UUID Vulcan_w;
   private static final long b = Vulcan_n.a(8202920746652798725L, -7644198727626345838L, MethodHandles.lookup().lookupClass()).a(178367576184810L);
   private static final String[] d;

   public SpeedD(Vulcan_iE var1) {
      long var2 = b ^ 68068917809713L;
      super(var1);
      int[] var10000 = SpeedE.Vulcan_I();
      this.Vulcan_w = UUID.fromString(d[4]);
      int[] var4 = var10000;
      if (var4 == null) {
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

   static {
      long var0 = b ^ 101720959534465L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[7];
      int var7 = 0;
      String var6 = "\u009a\u0095ï\u0011Z#¢\u0006\b.Pº\u008c\u0080Û\u0012Ã\u0010÷ò\u000e\u0087×LðÁçe\u0082¦Þ\u0092\u009ev\bÎ1`\u0003Ç\u0000:x(ü\u0093\u0095\u001f|JUa§\u0003&zª¹RÞêá_Ó$Â\u0019ê\u0080\u0003àuÝ\u0006\u007ftB\u0013½íù\bÇT";
      int var8 = "\u009a\u0095ï\u0011Z#¢\u0006\b.Pº\u008c\u0080Û\u0012Ã\u0010÷ò\u000e\u0087×LðÁçe\u0082¦Þ\u0092\u009ev\bÎ1`\u0003Ç\u0000:x(ü\u0093\u0095\u001f|JUa§\u0003&zª¹RÞêá_Ó$Â\u0019ê\u0080\u0003àuÝ\u0006\u007ftB\u0013½íù\bÇT".length();
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

               var6 = "~âç\u0013Ã7\u0004\u0006\b6_\u0012¡m!ó\u0082";
               var8 = "~âç\u0013Ã7\u0004\u0006\b6_\u0012¡m!ó\u0082".length();
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
