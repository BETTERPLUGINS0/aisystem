package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Vulcan_iv implements Listener {
   private static String Vulcan_K;
   private static final long a = Vulcan_n.a(-5046385559772602341L, -4704171738953284395L, MethodHandles.lookup().lookupClass()).a(122337191570197L);
   private static final String[] b;

   @EventHandler
   public void Vulcan_e(PlayerJoinEvent param1) {
      // $FF: Couldn't be decompiled
   }

   @EventHandler
   public void Vulcan_g(PlayerQuitEvent var1) {
      long var2 = a ^ 89144189650071L;
      String var10000 = Vulcan_v();
      Player var6 = var1.getPlayer();
      String var4 = var10000;
      Vulcan_Xs.INSTANCE.Vulcan_y().Vulcan_K(new Object[0]).remove(var6);
      Vulcan_Xs.INSTANCE.Vulcan_y().Vulcan_j(new Object[0]).remove(var6);
      Vulcan_Xs.INSTANCE.Vulcan_x().remove(var6.getUniqueId());
      Vulcan_iE var7 = Vulcan_Xs.INSTANCE.Vulcan_e().Vulcan_Z(new Object[]{var6});

      Vulcan_iE var10;
      label36: {
         try {
            var10 = var7;
            if (var4 == null) {
               break label36;
            }

            if (var7 == null) {
               return;
            }
         } catch (RuntimeException var9) {
            throw a(var9);
         }

         var10 = var7;
      }

      label26: {
         try {
            if (!var10.Vulcan_e(new Object[0]).Vulcan_lV(new Object[0])) {
               break label26;
            }
         } catch (RuntimeException var8) {
            throw a(var8);
         }

         Vulcan_Xs.INSTANCE.Vulcan_y().Vulcan_I(new Object[]{Vulcan_i9.Vulcan_QQ.replaceAll(b[1], var6.getName())});
         Vulcan_i9.Vulcan_ap.forEach(Vulcan_iv::lambda$onQuit$1);
      }

      if (var4 == null) {
         int var5 = AbstractCheck.Vulcan_V();
         ++var5;
         AbstractCheck.Vulcan_H(var5);
      }

   }

   private static void lambda$onQuit$1(Player var0, String var1) {
      long var2 = a ^ 114688054739581L;
      long var4 = var2 ^ 127507347272403L;
      long var6 = var2 ^ 13827110451505L;
      Vulcan_eG.Vulcan_I(new Object[]{Vulcan_eR.Vulcan_m(new Object[]{var1.replaceAll(b[3], var0.getName()), var4}), var6});
   }

   private static void lambda$onJoin$0(Player var0, Vulcan_iE var1, String var2) {
      long var3 = a ^ 40872280601012L;
      long var5 = var3 ^ 68810394861850L;
      String[] var7 = b;
      var0.sendMessage(Vulcan_eR.Vulcan_m(new Object[]{var2.replaceAll(var7[0], var1.getClientBrand()).replaceAll(var7[1], var0.getName()), var5}));
   }

   public static void Vulcan_d(String var0) {
      Vulcan_K = var0;
   }

   public static String Vulcan_v() {
      return Vulcan_K;
   }

   static {
      long var0 = a ^ 83647038542063L;
      Vulcan_d("uWJpDb");
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
      String var6 = "t\u0088Èco\u0092X´C\u000bV/*\bÔS\u0010Íoc\tM\u0011ÏWr{ÛöE\u000e\u0086X";
      int var8 = "t\u0088Èco\u0092X´C\u000bV/*\bÔS\u0010Íoc\tM\u0011ÏWr{ÛöE\u000e\u0086X".length();
      char var5 = 16;
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var11 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var11.getBytes("ISO-8859-1"));
            String var15 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var15;
               if ((var4 += var5) >= var8) {
                  b = var9;
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

               var6 = "F±xùÑÊ_ûÀ\u0019é©PF<\u0003\u0010Íoc\tM\u0011ÏWr{ÛöE\u000e\u0086X";
               var8 = "F±xùÑÊ_ûÀ\u0019é©PF<\u0003\u0010Íoc\tM\u0011ÏWr{ÛöE\u000e\u0086X".length();
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

   private static String a(byte[] var0) {
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
