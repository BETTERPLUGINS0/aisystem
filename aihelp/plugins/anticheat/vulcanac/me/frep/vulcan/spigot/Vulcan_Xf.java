package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Vulcan_Xf implements PacketListener {
   private static String[] Vulcan_i;
   private static final long a = Vulcan_n.a(2552110521079874837L, 7714966953538725443L, MethodHandles.lookup().lookupClass()).a(1141577496647L);
   private static final String[] b;

   public void onPacketReceive(PacketReceiveEvent param1) {
      // $FF: Couldn't be decompiled
   }

   public void onPacketSend(PacketSendEvent param1) {
      // $FF: Couldn't be decompiled
   }

   public void onUserDisconnect(UserDisconnectEvent param1) {
      // $FF: Couldn't be decompiled
   }

   public void onUserConnect(UserConnectEvent param1) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$onPacketSend$0(PacketSendEvent var0) {
      long var1 = a ^ 22402851982056L;
      long var10001 = var1 ^ 6170002581678L;
      int var3 = (int)((var1 ^ 6170002581678L) >>> 32);
      int var4 = (int)(var10001 << 32 >>> 56);
      int var5 = (int)(var10001 << 40 >>> 40);
      Vulcan_Xs.INSTANCE.Vulcan_H().Vulcan__(new Object[]{var3, Integer.valueOf((byte)var4), var0.getUser(), var5});
   }

   public static void Vulcan_l(String[] var0) {
      Vulcan_i = var0;
   }

   public static String[] Vulcan_n() {
      return Vulcan_i;
   }

   static {
      long var0 = a ^ 26893928908913L;
      Vulcan_l((String[])null);
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
      String var6 = "?r¸dS\u0004¦z\b?r¸dS\u0004¦z(vÏ·\u0019$¬ßPð\u00917s¥ru%R-E¢Ô&¾)½H»ti\u0091â»ÿ]ô\u0005ÿs\u007f;";
      int var8 = "?r¸dS\u0004¦z\b?r¸dS\u0004¦z(vÏ·\u0019$¬ßPð\u00917s¥ru%R-E¢Ô&¾)½H»ti\u0091â»ÿ]ô\u0005ÿs\u007f;".length();
      char var5 = '\b';
      int var4 = -1;

      while(true) {
         ++var4;
         byte[] var10 = var2.doFinal(var6.substring(var4, var4 + var5).getBytes("ISO-8859-1"));
         String var12 = a(var10).intern();
         boolean var10001 = true;
         var9[var7++] = var12;
         if ((var4 += var5) >= var8) {
            b = var9;
            return;
         }

         var5 = var6.charAt(var4);
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
