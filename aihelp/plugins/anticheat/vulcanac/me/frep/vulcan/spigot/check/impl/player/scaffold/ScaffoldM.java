package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.block.BlockFace;

@CheckInfo(
   name = "Scaffold",
   type = 'M',
   complexType = "Expand",
   description = "Invalid block face."
)
public class ScaffoldM extends AbstractCheck {
   private static final long b = Vulcan_n.a(-4046789761943210039L, -2309755256891329529L, MethodHandles.lookup().lookupClass()).a(155849472172595L);
   private static final String[] d;

   public ScaffoldM(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] var1) {
      long var2 = (Long)var1[0];
      PacketEvent var4 = (PacketEvent)var1[1];
   }

   public void Vulcan_i(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private BlockFace Vulcan_F(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private BlockFace Vulcan_w(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   static {
      long var0 = b ^ 39312807680526L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[3];
      int var7 = 0;
      String var6 = "øs`üPúè-1%²¢L\b\u001dõ\u0010¬P\u00997Írt\u009fÄºÁ\u009aüuÉ*\u0010Èø\nIé-\u0099\u0016|v\u00adTÉ\u0087ø\u0093";
      int var8 = "øs`üPúè-1%²¢L\b\u001dõ\u0010¬P\u00997Írt\u009fÄºÁ\u009aüuÉ*\u0010Èø\nIé-\u0099\u0016|v\u00adTÉ\u0087ø\u0093".length();
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
