package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerHeldItemChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public final class Vulcan_bQ {
   private static String Vulcan_M;
   private static final long a = Vulcan_n.a(6605357918960240326L, -4215843179098278295L, MethodHandles.lookup().lookupClass()).a(108882283772666L);
   private static final String[] b;

   public static int Vulcan_R(Object[] var0) {
      Player var1 = (Player)var0[0];

      try {
         return PacketEvents.getAPI().getPlayerManager().getPing(var1);
      } catch (Exception var3) {
         return 0;
      }
   }

   public static void Vulcan_Y(Object[] var0) {
      Vulcan_iE var2 = (Vulcan_iE)var0[0];
      PacketWrapper var1 = (PacketWrapper)var0[1];
      PacketEvents.getAPI().getProtocolManager().sendPacket(var2.Vulcan_I(new Object[0]).getChannel(), var1);
   }

   public static boolean Vulcan_P(Object[] var0) {
      Vulcan_iE var1 = (Vulcan_iE)var0[0];
      return var1.Vulcan_I(new Object[0]).getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_17);
   }

   public static boolean Vulcan_l(Object[] var0) {
      Vulcan_iE var1 = (Vulcan_iE)var0[0];
      return var1.Vulcan_I(new Object[0]).getClientVersion().isNewerThan(ClientVersion.V_1_9);
   }

   public static boolean Vulcan_o(Object[] var0) {
      Vulcan_iE var1 = (Vulcan_iE)var0[0];
      return var1.Vulcan_I(new Object[0]).getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_13);
   }

   public static void Vulcan_k(Object[] var0) {
      Vulcan_iE var1 = (Vulcan_iE)var0[0];
      int var2 = (Integer)var0[1];
      Vulcan_Y(new Object[]{var1, new WrapperPlayServerWindowConfirmation(0, (short)var2, false)});
   }

   public static void Vulcan_c(Object[] var0) {
      Vulcan_iE var2 = (Vulcan_iE)var0[0];
      int var1 = (Integer)var0[1];
      Vulcan_Y(new Object[]{var2, new WrapperPlayServerPing(var1)});
   }

   public static boolean Vulcan_T(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_M(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_k(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_N(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_Q(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_v(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static double Vulcan_f(Object[] var0) {
      Player var2 = (Player)var0[0];
      Player var1 = (Player)var0[1];
      return (double)Math.abs(180.0F - Math.abs(var2.getLocation().getYaw() - var1.getLocation().getYaw()));
   }

   public static boolean Vulcan_Z(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_N(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_r(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_T(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_F(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_A(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_p(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_P(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static List Vulcan_L(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_t(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_I(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_g(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_f(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_z(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_h(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_n(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_W(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_e(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_s(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_d(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static int Vulcan_U(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_b(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan__(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_m(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_Y(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_F(Object[] var0) {
      Player var1 = (Player)var0[0];
      int var2 = ThreadLocalRandom.current().nextInt(8);
      Vulcan_Y(new Object[]{Vulcan_Xs.INSTANCE.Vulcan_e().Vulcan_Z(new Object[]{var1}), new WrapperPlayServerHeldItemChange(var2)});
   }

   public static float Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_E(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static void Vulcan_A(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   public static boolean Vulcan_A(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private Vulcan_bQ() {
      throw new UnsupportedOperationException(b[3]);
   }

   private static void lambda$rotateRandomly$1(Player var0, Location var1) {
      var0.teleport(var1, TeleportCause.UNKNOWN);
   }

   private static void lambda$rotateRandomly$0(Vulcan_iE var0, Location var1) {
      var0.Vulcan_q(new Object[0]).teleport(var1, TeleportCause.UNKNOWN);
   }

   public static void Vulcan_U(String var0) {
      Vulcan_M = var0;
   }

   public static String Vulcan_X() {
      return Vulcan_M;
   }

   static {
      long var0 = a ^ 83597841350672L;
      Vulcan_U("aj67Gc");
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
      String var6 = "ö]\u000b\u00900XV\u0090\bö]\u000b\u00900XV\u0090";
      int var8 = "ö]\u000b\u00900XV\u0090\bö]\u000b\u00900XV\u0090".length();
      char var5 = '\b';
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

               var6 = "Qt\"<\u0000\u000e\u0087Ì8}];UáÐÇ\u0010_\u009e~o9éRúv'gk`ê>S¾W~<ÐÌ\"ô\u0007þ_ó\u0088r5\u0085óÁ\u00861VÐì³\u0084òÁB;½¤Ä";
               var8 = "Qt\"<\u0000\u000e\u0087Ì8}];UáÐÇ\u0010_\u009e~o9éRúv'gk`ê>S¾W~<ÐÌ\"ô\u0007þ_ó\u0088r5\u0085óÁ\u00861VÐì³\u0084òÁB;½¤Ä".length();
               var5 = '\b';
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   private static UnsupportedOperationException a(UnsupportedOperationException var0) {
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
