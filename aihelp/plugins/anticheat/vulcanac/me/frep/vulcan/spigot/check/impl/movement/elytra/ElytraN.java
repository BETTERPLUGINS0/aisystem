package me.frep.vulcan.spigot.check.impl.movement.elytra;

import java.lang.invoke.MethodHandles;
import java.util.UUID;
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
   name = "Elytra",
   type = 'N',
   experimental = true,
   complexType = "Ground",
   description = "Moving too quickly on the ground."
)
public class ElytraN extends AbstractCheck {
   public final UUID Vulcan_o;
   private static int Vulcan_A;
   private static final long b = Vulcan_n.a(2644124509578478011L, -1932612740297345950L, MethodHandles.lookup().lookupClass()).a(259725445508555L);
   private static final String[] d;

   public ElytraN(Vulcan_iE var1) {
      long var2 = b ^ 59327890404695L;
      int var10000 = Vulcan_E();
      super(var1);
      int var4 = var10000;
      this.Vulcan_o = UUID.fromString(d[4]);
      if (var4 != 0) {
         int var5 = AbstractCheck.Vulcan_V();
         ++var5;
         AbstractCheck.Vulcan_H(var5);
      }

   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var3 = (Long)var1[0];
      Event var2 = (Event)var1[1];
   }

   public static void Vulcan_R(int var0) {
      Vulcan_A = var0;
   }

   public static int Vulcan_E() {
      return Vulcan_A;
   }

   public static int Vulcan_P() {
      int var0 = Vulcan_E();

      try {
         return var0 == 0 ? 112 : 0;
      } catch (RuntimeException var1) {
         throw a(var1);
      }
   }

   static {
      long var0 = b ^ 20681910417258L;
      Vulcan_R(0);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[6];
      int var7 = 0;
      String var6 = "\u009eÑØ\u0004?\u00889\u0006\b\u001e×\u0081¡îÛ³}\bè§AÃªioÀ\u0010]¸\u0016\u0080#_\u001cRÒZÌeV.\u0004\u008f";
      int var8 = "\u009eÑØ\u0004?\u00889\u0006\b\u001e×\u0081¡îÛ³}\bè§AÃªioÀ\u0010]¸\u0016\u0080#_\u001cRÒZÌeV.\u0004\u008f".length();
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

               var6 = "\u0084a\u008d\u009c^s\u00adtÇV'+ê\u0095ÑUÿuÌ@¶s¶ùðäª(\u0099\u0014Í©\u0096\u008bXÔ\u0018ÈmÆ\b¾Éòv;7íq";
               var8 = "\u0084a\u008d\u009c^s\u00adtÇV'+ê\u0095ÑUÿuÌ@¶s¶ùðäª(\u0099\u0014Í©\u0096\u008bXÔ\u0018ÈmÆ\b¾Éòv;7íq".length();
               var5 = '(';
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
