package me.frep.vulcan.spigot.check.impl.combat.killaura;

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
   name = "Kill Aura",
   type = 'A',
   complexType = "Post",
   description = "Post UseEntity packets."
)
public class KillAuraA extends AbstractCheck {
   private long Vulcan_N;
   private boolean Vulcan_f;
   private static final long b = Vulcan_n.a(4458338922138044086L, -6405797745188310521L, MethodHandles.lookup().lookupClass()).a(16964883699302L);
   private static final String d;

   public KillAuraA(Vulcan_iE var1) {
      long var2 = b ^ 19796894142629L;
      AbstractCheck[] var10000 = KillAuraL.Vulcan_z();
      super(var1);
      AbstractCheck[] var4 = var10000;
      this.Vulcan_N = 0L;
      this.Vulcan_f = false;
      if (var4 != null) {
         int var5 = AbstractCheck.Vulcan_V();
         ++var5;
         AbstractCheck.Vulcan_H(var5);
      }

   }

   public void Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_i(Object[] var1) {
      long var2 = (Long)var1[0];
      Event var4 = (Event)var1[1];
   }

   static {
      long var0 = b ^ 9557631609035L;
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      byte[] var4 = var2.doFinal("M¹öìZ}ä\u0091".getBytes("ISO-8859-1"));
      String var5 = b(var4).intern();
      boolean var10001 = true;
      d = var5;
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
