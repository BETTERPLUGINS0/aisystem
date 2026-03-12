package me.frep.vulcan.spigot.check.impl.player.fastbreak;

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

@CheckInfo(
   name = "Fast Break",
   type = 'A',
   complexType = "Delay",
   description = "Breaking blocks too quickly."
)
public class FastBreakA extends AbstractCheck {
   private long Vulcan_W;
   private static String[] Vulcan_Z;
   private static final long b = Vulcan_n.a(6376733215158576959L, 8503977966272735071L, MethodHandles.lookup().lookupClass()).a(277680141628730L);
   private static final String[] d;

   public FastBreakA(Vulcan_iE var1) {
      super(var1);
   }

   public void Vulcan_O(Object[] var1) {
      long var2 = (Long)var1[0];
      PacketEvent var4 = (PacketEvent)var1[1];
   }

   public void Vulcan_i(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_m(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private void Vulcan_R(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean Vulcan_x(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean Vulcan_h(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean Vulcan_k(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_Z(String[] var0) {
      Vulcan_Z = var0;
   }

   public static String[] Vulcan_b() {
      return Vulcan_Z;
   }

   static {
      String[] var10000 = new String[3];
      long var0 = b ^ 45679873452156L;
      Vulcan_Z(var10000);
      Cipher var2;
      Cipher var11 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var11.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[17];
      int var7 = 0;
      String var6 = "@\u0006~ \u008bVD%\u0010¨)Ä\u008a;\u0095Á\t,OäÆRú\u0002à\u0018Z\u00ad\u001e\u008c\u0015\u007f\u0099#\u008c\u0011O/\u0096Ø¦Áµ\u008a\u001f\u009f\u0017Õ\u001bû\b@\u0006~ \u008bVD%\b\u00adÂ\u000f=Û e~\u0010¼^8\u000b\u0091yGÅ\u001bÛãû·héc\u0010\u0083\u0097õe[I\u0017=ÃCÿI»8D*\bñÀ,4òÏÊ\u007f\b\u0081É\u0018\u001cJ|×Y\b~\u008dC°¹í\u0001\u0012\u00107V\u008d9fö\u0080×\u0086jý\u0096Dä\u001a\u0001\bâ,\u0085\u0018NB\u0080¿\u0010ý\u000fÈ|¿,\u000f\u0014\u009eK\u00adfÜ\u0099ül\u0010ê\u009ck\u00895Ù\u0013Ñ@¡hÆ½Nêv\b\u0095^U¤F\u008c¥\u008c";
      int var8 = "@\u0006~ \u008bVD%\u0010¨)Ä\u008a;\u0095Á\t,OäÆRú\u0002à\u0018Z\u00ad\u001e\u008c\u0015\u007f\u0099#\u008c\u0011O/\u0096Ø¦Áµ\u008a\u001f\u009f\u0017Õ\u001bû\b@\u0006~ \u008bVD%\b\u00adÂ\u000f=Û e~\u0010¼^8\u000b\u0091yGÅ\u001bÛãû·héc\u0010\u0083\u0097õe[I\u0017=ÃCÿI»8D*\bñÀ,4òÏÊ\u007f\b\u0081É\u0018\u001cJ|×Y\b~\u008dC°¹í\u0001\u0012\u00107V\u008d9fö\u0080×\u0086jý\u0096Dä\u001a\u0001\bâ,\u0085\u0018NB\u0080¿\u0010ý\u000fÈ|¿,\u000f\u0014\u009eK\u00adfÜ\u0099ül\u0010ê\u009ck\u00895Ù\u0013Ñ@¡hÆ½Nêv\b\u0095^U¤F\u008c¥\u008c".length();
      char var5 = '\b';
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var12 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var12.getBytes("ISO-8859-1"));
            String var16 = b(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var16;
               if ((var4 += var5) >= var8) {
                  d = var9;
                  return;
               }

               var5 = var6.charAt(var4);
               break;
            default:
               var9[var7++] = var16;
               if ((var4 += var5) < var8) {
                  var5 = var6.charAt(var4);
                  continue label27;
               }

               var6 = "hz\u00adz¬\u0097Dû\b\u007fÎ q\u000b\u001dÖ¤";
               var8 = "hz\u00adz¬\u0097Dû\b\u007fÎ q\u000b\u001dÖ¤".length();
               var5 = '\b';
               var4 = -1;
            }

            ++var4;
            var12 = var6.substring(var4, var4 + var5);
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
