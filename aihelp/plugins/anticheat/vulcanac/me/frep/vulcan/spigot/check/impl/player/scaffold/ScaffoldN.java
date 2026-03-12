package me.frep.vulcan.spigot.check.impl.player.scaffold;

import com.github.retrooper.packetevents.event.PacketEvent;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

@CheckInfo(
   name = "Scaffold",
   type = 'N',
   complexType = "Expand",
   description = "Invalid block face.",
   experimental = true
)
public class ScaffoldN extends AbstractCheck {
   private List Vulcan_H;
   private static String Vulcan_Z;
   private static final long b = Vulcan_n.a(-7022450893437385620L, -3533170630844819634L, MethodHandles.lookup().lookupClass()).a(33821282270989L);
   private static final String[] d;

   public ScaffoldN(Vulcan_iE var1) {
      long var2 = b ^ 114116759183904L;
      super(var1);
      Vulcan_z();
      this.Vulcan_H = new ArrayList();

      try {
         if (AbstractCheck.Vulcan_V() == 0) {
            Vulcan_D("yNj5H");
         }

      } catch (RuntimeException var5) {
         throw a(var5);
      }
   }

   public void Vulcan_O(Object[] var1) {
      long var3 = (Long)var1[0];
      PacketEvent var2 = (PacketEvent)var1[1];
   }

   public void Vulcan_i(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private BlockFace Vulcan_j(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private BlockFace Vulcan_v(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public Vector Vulcan_W(Object[] var1) {
      float var4 = (Float)var1[0];
      long var2 = (Long)var1[1];
      float var5 = (Float)var1[2];
      long var10000 = b ^ var2;
      Vector var8 = new Vector();
      double var9 = (double)var4;
      double var11 = (double)var5;
      String var15 = Vulcan_z();
      var8.setY(-Math.sin(Math.toRadians(var11)));
      double var13 = Math.cos(Math.toRadians(var11));
      var8.setX(-var13 * Math.sin(Math.toRadians(var9)));
      var8.setZ(var13 * Math.cos(Math.toRadians(var9)));
      String var6 = var15;
      if (var6 != null) {
         int var7 = AbstractCheck.Vulcan_V();
         ++var7;
         AbstractCheck.Vulcan_H(var7);
      }

      return var8;
   }

   public static void Vulcan_D(String var0) {
      Vulcan_Z = var0;
   }

   public static String Vulcan_z() {
      return Vulcan_Z;
   }

   static {
      long var0 = b ^ 51738660741963L;
      Vulcan_D((String)null);
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
      String var6 = "¿<\u0083g6B)7\u0010oZÊ\u0090â«`+ÂG@ËqcÏ\u008e";
      int var8 = "¿<\u0083g6B)7\u0010oZÊ\u0090â«`+ÂG@ËqcÏ\u008e".length();
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
