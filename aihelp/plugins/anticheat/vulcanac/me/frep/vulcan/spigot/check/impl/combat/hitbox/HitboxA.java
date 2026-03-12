package me.frep.vulcan.spigot.check.impl.combat.hitbox;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.Vulcan_b6;
import me.frep.vulcan.spigot.Vulcan_iE;
import me.frep.vulcan.spigot.Vulcan_n;
import me.frep.vulcan.spigot.check.AbstractCheck;
import me.frep.vulcan.spigot.check.api.CheckInfo;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.util.Vector;

@CheckInfo(
   name = "Hitbox",
   type = 'A',
   complexType = "History",
   description = "Attacked while not looking at target."
)
public class HitboxA extends AbstractCheck {
   private boolean Vulcan_b;
   private static final long b = Vulcan_n.a(-1848671868132157101L, -3213873317752454914L, MethodHandles.lookup().lookupClass()).a(119744230843586L);
   private static final String[] d;

   public HitboxA(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }

   private Vector Vulcan__(Object[] var1) {
      float var2 = (Float)var1[0];
      float var3 = (Float)var1[1];
      Vector var4 = new Vector();
      double var5 = (double)var2;
      double var7 = (double)var3;
      var4.setY(-Math.sin(Math.toRadians(var7)));
      double var9 = Math.cos(Math.toRadians(var7));
      var4.setX(-var9 * Math.sin(Math.toRadians(var5)));
      var4.setZ(var9 * Math.cos(Math.toRadians(var5)));
      return var4;
   }

   private double lambda$handle$1(Vector var1, Vulcan_b6 var2) {
      Vector var3 = ((Location)var2.Vulcan_w(new Object[0])).toVector().setY(0.0D);
      Vector var4 = var3.clone().subtract(var1);
      Vector var5 = this.Vulcan__(new Object[]{this.Vulcan_Q.Vulcan_w(new Object[0]).Vulcan_M(new Object[0]), this.Vulcan_Q.Vulcan_w(new Object[0]).Vulcan_w(new Object[0])}).setY(0.0D);
      return (double)var4.angle(var5);
   }

   private static boolean lambda$handle$0(int param0, int param1, Vulcan_b6 param2) {
      // $FF: Couldn't be decompiled
   }

   static {
      long var0 = b ^ 23931607845000L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[4];
      int var7 = 0;
      String var6 = "\u0015Ç\u00ad\"\u0011»ºe\ba¿ª\u0004\u009b]«Ý";
      int var8 = "\u0015Ç\u00ad\"\u0011»ºe\ba¿ª\u0004\u009b]«Ý".length();
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

               var6 = "KÛÞ²\u0084|x£\u009eø^ ¾Q\u009e\u0096\bÐ\u0012\u00ad\u0098\u0082ÿ¿ú";
               var8 = "KÛÞ²\u0084|x£\u009eø^ ¾Q\u009e\u0096\bÐ\u0012\u00ad\u0098\u0082ÿ¿ú".length();
               var5 = 16;
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
