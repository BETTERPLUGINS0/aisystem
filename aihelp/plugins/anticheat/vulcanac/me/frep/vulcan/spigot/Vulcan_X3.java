package me.frep.vulcan.spigot;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPing;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerWindowConfirmation;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ThreadLocalRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Vulcan_X3 {
   private static int[] Vulcan_u;
   private static final long a = Vulcan_n.a(-7458264706874550582L, 6240556127630929922L, MethodHandles.lookup().lookupClass()).a(224545323217923L);
   private static final String[] b;

   public void Vulcan_j(Vulcan_iE param1, PacketSendEvent param2) {
      // $FF: Couldn't be decompiled
   }

   private static boolean lambda$handle$1(EntityData param0) {
      // $FF: Couldn't be decompiled
   }

   private static void lambda$handle$0(Vulcan_iE var0) {
      long var3;
      Object var6;
      int var7;
      label22: {
         long var1 = a ^ 22792571074595L;
         var3 = var1 ^ 120565573965351L;
         int[] var10000 = Vulcan_E();
         var7 = ThreadLocalRandom.current().nextInt(-11500, -11000);
         int[] var5 = var10000;
         if (Vulcan_eG.Vulcan_t(new Object[0])) {
            var6 = new WrapperPlayServerPing(var7);
            if (var5 != null) {
               break label22;
            }
         }

         var6 = new WrapperPlayServerWindowConfirmation(0, (short)var7, false);
      }

      try {
         if (Vulcan_i9.Vulcan_hp) {
            Vulcan_eG.Vulcan_V(new Object[]{var3, b[6] + var7 + b[1] + var0.Vulcan_I(new Object[0])});
         }
      } catch (RuntimeException var8) {
         throw a(var8);
      }

      var0.Vulcan_P(new Object[0]).Vulcan_F(new Object[]{Integer.valueOf((short)var7)});
      var0.Vulcan_I(new Object[0]).sendPacket((PacketWrapper)var6);
   }

   public static void Vulcan_P(int[] var0) {
      Vulcan_u = var0;
   }

   public static int[] Vulcan_E() {
      return Vulcan_u;
   }

   static {
      int[] var10000 = new int[1];
      long var0 = a ^ 115626305642761L;
      Vulcan_P(var10000);
      Cipher var2;
      Cipher var11 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var11.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[8];
      int var7 = 0;
      String var6 = "í|Æ¢vv_Õ,ï£Ã\u0094\u001fÛe\u0003\nS\u0087Æ³\u0004÷<\u009c\u0085¶ÜÄe\u000foÄ0\u0012\u0019±·S\b\u0017RÇ\u0083y\t\u008ax(í|Æ¢vv_Õ,ï£Ã\u0094\u001fÛe\u0003\nS\u0087Æ³\u0004÷<\u009c\u0085¶ÜÄe\u000foÄ0\u0012\u0019±·S(E\u0018Vóêú{>\u0016¸\u00153/B\u0098\bC/`×(6Û}Ñ]\u0019£0p1F©ôrG\u0007w$\u009a\b\u0006êðÀØï3\u0082(E\u0018Vóêú{>\u0016¸\u00153/B\u0098\bC/`×(6Û}Ñ]\u0019£0p1F©ôrG\u0007w$\u009a";
      int var8 = "í|Æ¢vv_Õ,ï£Ã\u0094\u001fÛe\u0003\nS\u0087Æ³\u0004÷<\u009c\u0085¶ÜÄe\u000foÄ0\u0012\u0019±·S\b\u0017RÇ\u0083y\t\u008ax(í|Æ¢vv_Õ,ï£Ã\u0094\u001fÛe\u0003\nS\u0087Æ³\u0004÷<\u009c\u0085¶ÜÄe\u000foÄ0\u0012\u0019±·S(E\u0018Vóêú{>\u0016¸\u00153/B\u0098\bC/`×(6Û}Ñ]\u0019£0p1F©ôrG\u0007w$\u009a\b\u0006êðÀØï3\u0082(E\u0018Vóêú{>\u0016¸\u00153/B\u0098\bC/`×(6Û}Ñ]\u0019£0p1F©ôrG\u0007w$\u009a".length();
      char var5 = '(';
      int var4 = -1;

      label27:
      while(true) {
         ++var4;
         String var12 = var6.substring(var4, var4 + var5);
         byte var10001 = -1;

         while(true) {
            byte[] var10 = var2.doFinal(var12.getBytes("ISO-8859-1"));
            String var16 = a(var10).intern();
            switch(var10001) {
            case 0:
               var9[var7++] = var16;
               if ((var4 += var5) >= var8) {
                  b = var9;
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

               var6 = "\u0015\u009fÃ\u0087Ê,/%QÑKÆL\u000f\u0002NØwÉ\u0018\n!~\u0007\u0080±LoæÔ+ü\b\u0006êðÀØï3\u0082";
               var8 = "\u0015\u009fÃ\u0087Ê,/%QÑKÆL\u000f\u0002NØwÉ\u0018\n!~\u0007\u0080±LoæÔ+ü\b\u0006êðÀØï3\u0082".length();
               var5 = ' ';
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
