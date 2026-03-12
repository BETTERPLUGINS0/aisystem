package me.frep.vulcan.spigot.check.impl.combat.velocity;

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
   name = "Velocity",
   type = 'A',
   complexType = "Vertical",
   description = "Vertical velocity modifications."
)
public class VelocityA extends AbstractCheck {
   private double Vulcan_O;
   private double Vulcan_H;
   private int Vulcan_e;
   private static final long b = Vulcan_n.a(-8569598308289270322L, -8300901068619807876L, MethodHandles.lookup().lookupClass()).a(233166743894790L);
   private static final String[] d;

   public VelocityA(Vulcan_iE var1) {
      long var2 = b ^ 9272591302869L;
      VelocityD.Vulcan_I();
      super(var1);

      try {
         this.Vulcan_O = -1.0D;
         this.Vulcan_H = -1.0D;
         this.Vulcan_e = -1;
         if (AbstractCheck.Vulcan_V() == 0) {
            VelocityD.Vulcan_n(new int[5]);
         }

      } catch (RuntimeException var5) {
         throw a(var5);
      }
   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   static {
      long var0 = b ^ 85990671578173L;
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
      String var6 = "\u0096ú¬\u0096fÀ%õ\b\u0097\u001f\u008bzr3S#";
      int var8 = "\u0096ú¬\u0096fÀ%õ\b\u0097\u001f\u008bzr3S#".length();
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

               var6 = ";í¯õë\u0089u5è÷óP\u0097ýË8\u0010äRHèR\u0010\u008f\u009b¤4Þ\u0094½fë\u001a";
               var8 = ";í¯õë\u0089u5è÷óP\u0097ýË8\u0010äRHèR\u0010\u008f\u009b¤4Þ\u0094½fë\u001a".length();
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
