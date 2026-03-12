package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import java.lang.invoke.MethodHandles;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.Bukkit;

public final class Vulcan_eG {
   private static final boolean Vulcan_x;
   private static final boolean Vulcan_T;
   private static final boolean Vulcan_L;
   private static final boolean Vulcan_V;
   private static final boolean Vulcan_R;
   private static final boolean Vulcan_v;
   private static final boolean Vulcan_a;
   private static final boolean Vulcan_H;
   private static final boolean Vulcan_d;
   private static final boolean Vulcan_U;
   private static final boolean Vulcan_E;
   private static final boolean Vulcan_n;
   private static final boolean Vulcan_G;
   private static final boolean Vulcan_W;
   private static final boolean Vulcan_F;
   private static final boolean Vulcan_X;
   private static final boolean Vulcan_p;
   private static final boolean Vulcan_K;
   private static final boolean Vulcan_D;
   private static int[] Vulcan_M;
   private static final long a = me.frep.vulcan.spigot.Vulcan_n.a(-4784203650916429320L, 2156792398042744792L, MethodHandles.lookup().lookupClass()).a(42146681938853L);
   private static final String[] b;

   public static double Vulcan_X(Object[] var0) {
      long var1 = (Long)var0[0];
      long var10000 = a ^ var1;

      double var4;
      try {
         if (Vulcan_Xs.INSTANCE.Vulcan_g()) {
            var4 = 20.0D;
            return var4;
         }
      } catch (UnsupportedOperationException var3) {
         throw a(var3);
      }

      var4 = Math.min(20.0D, SpigotReflectionUtil.getTPS());
      return var4;
   }

   public static void Vulcan_v(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 11451177534616L;
      Vulcan_Xs.INSTANCE.Vulcan_O().log(Level.INFO, Vulcan_eR.Vulcan_m(new Object[]{var3, var4}));
   }

   public static void Vulcan_V(Object[] var0) {
      long var2 = (Long)var0[0];
      String var1 = (String)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 78811573011962L;
      Vulcan_Xs.INSTANCE.Vulcan_O().log(Level.INFO, Vulcan_eR.Vulcan_m(new Object[]{var1, var4}));
   }

   public static ServerVersion Vulcan_t(Object[] var0) {
      return PacketEvents.getAPI().getServerManager().getVersion();
   }

   public static boolean Vulcan_L(Object[] var0) {
      try {
         Class.forName(b[2]);
         return true;
      } catch (ClassNotFoundException var2) {
         return false;
      }
   }

   public static boolean Vulcan_t(Object[] var0) {
      return Vulcan_E;
   }

   public static boolean Vulcan_I(Object[] var0) {
      return Vulcan_W;
   }

   public static boolean Vulcan_q(Object[] var0) {
      return Vulcan_n;
   }

   public static boolean Vulcan_y(Object[] var0) {
      return Vulcan_U;
   }

   public static boolean Vulcan_A(Object[] var0) {
      return Vulcan_x;
   }

   public static boolean Vulcan_E(Object[] var0) {
      return Vulcan_v;
   }

   public static boolean Vulcan_o(Object[] var0) {
      return Vulcan_T;
   }

   public static boolean Vulcan_u(Object[] var0) {
      return Vulcan_a;
   }

   public static boolean Vulcan_S(Object[] var0) {
      return Vulcan_H;
   }

   public static boolean Vulcan_X(Object[] var0) {
      return Vulcan_L;
   }

   public static boolean Vulcan_a(Object[] var0) {
      return Vulcan_V;
   }

   public static boolean Vulcan__(Object[] var0) {
      return Vulcan_R;
   }

   public static boolean Vulcan_s(Object[] var0) {
      return Vulcan_G;
   }

   public static boolean Vulcan_c(Object[] var0) {
      return Vulcan_p;
   }

   public static boolean Vulcan_J(Object[] var0) {
      return Vulcan_F;
   }

   public static boolean Vulcan_M(Object[] var0) {
      return Vulcan_X;
   }

   public static void Vulcan_x(Object[] var0) {
      Object var3 = (Object)var0[0];
      long var1 = (Long)var0[1];
      long var10000 = a ^ var1;

      try {
         if (Vulcan_i9.Vulcan_G) {
            Vulcan__(b[0] + var3);
         }

      } catch (UnsupportedOperationException var4) {
         throw a(var4);
      }
   }

   public static void Vulcan_I(Object[] var0) {
      String var3 = (String)var0[0];
      long var1 = (Long)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 113857897368563L;
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Vulcan_eR.Vulcan_m(new Object[]{var3, var4}));
   }

   public static void Vulcan__(String var0) {
      long var1 = a ^ 127173262166823L;
      long var3 = var1 ^ 13548717220488L;
      Bukkit.broadcastMessage(Vulcan_eR.Vulcan_m(new Object[]{var0, var3}));
   }

   public static boolean Vulcan_D(Object[] var0) {
      return Vulcan_d;
   }

   public static boolean Vulcan_r(Object[] var0) {
      return Vulcan_t(new Object[0]).isOlderThan(ServerVersion.V_1_17);
   }

   public static boolean Vulcan_v(Object[] var0) {
      return Vulcan_K;
   }

   public static boolean Vulcan_f(Object[] var0) {
      return Vulcan_D;
   }

   private Vulcan_eG() {
      throw new UnsupportedOperationException(b[1]);
   }

   static {
      long var0 = a ^ 8726440894186L;
      Vulcan_R((int[])null);
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
      String var6 = "w]h\u000f«\u0000j`þÈôÅ_¬\u0003Y8áá\u008d\u0097]ç{í»V/ç´ø'wPÿ\u0090ý\u00ad\u0018âA+ò\u007fSäzËøÊY\u0018ÖÔñe?\u008b8¿2\u008d\u0080Dt\u001cxd0u$Â«8£±\u0001·Ú½u\u009f7\u00001à\u000e\u00ad\u008d::\u0092iJSVûô\u0096ø}åI\u001dÓÁÊ!²I\u0011ðµ\u000bÐ÷°Í\u0088¿Ë/ÜtæDAê\u001e\u001f";
      int var8 = "w]h\u000f«\u0000j`þÈôÅ_¬\u0003Y8áá\u008d\u0097]ç{í»V/ç´ø'wPÿ\u0090ý\u00ad\u0018âA+ò\u007fSäzËøÊY\u0018ÖÔñe?\u008b8¿2\u008d\u0080Dt\u001cxd0u$Â«8£±\u0001·Ú½u\u009f7\u00001à\u000e\u00ad\u008d::\u0092iJSVûô\u0096ø}åI\u001dÓÁÊ!²I\u0011ðµ\u000bÐ÷°Í\u0088¿Ë/ÜtæDAê\u001e\u001f".length();
      char var5 = 16;
      int var4 = -1;

      while(true) {
         ++var4;
         byte[] var10 = var2.doFinal(var6.substring(var4, var4 + var5).getBytes("ISO-8859-1"));
         String var12 = a(var10).intern();
         boolean var10001 = true;
         var9[var7++] = var12;
         if ((var4 += var5) >= var8) {
            b = var9;
            Vulcan_x = Vulcan_t(new Object[0]).isNewerThan(ServerVersion.V_1_7_10);
            Vulcan_T = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_8);
            Vulcan_L = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_9);
            Vulcan_V = Vulcan_t(new Object[0]).isOlderThan(ServerVersion.V_1_13);
            Vulcan_R = Vulcan_t(new Object[0]).isOlderThan(ServerVersion.V_1_8);
            Vulcan_v = Vulcan_t(new Object[0]).isOlderThan(ServerVersion.V_1_9);
            Vulcan_a = Vulcan_t(new Object[0]).isNewerThan(ServerVersion.V_1_13);
            Vulcan_H = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_16);
            Vulcan_d = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_12);
            Vulcan_U = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_11);
            Vulcan_E = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_17);
            Vulcan_n = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_14);
            Vulcan_G = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_18);
            Vulcan_W = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_19);
            Vulcan_F = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_20);
            Vulcan_X = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_20_2);
            Vulcan_p = Vulcan_t(new Object[0]).isOlderThan(ServerVersion.V_1_21);
            Vulcan_K = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_12_2);
            Vulcan_D = Vulcan_t(new Object[0]).isNewerThanOrEquals(ServerVersion.V_1_21_2);
            return;
         }

         var5 = var6.charAt(var4);
      }
   }

   public static void Vulcan_R(int[] var0) {
      Vulcan_M = var0;
   }

   public static int[] Vulcan_R() {
      return Vulcan_M;
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
