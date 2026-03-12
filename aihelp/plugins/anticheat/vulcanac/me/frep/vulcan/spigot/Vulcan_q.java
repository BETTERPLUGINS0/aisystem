package me.frep.vulcan.spigot;

import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.command.CommandSender;

public class Vulcan_q {
   private static String Vulcan_N;
   private static final long a = Vulcan_n.a(-3038766610483908029L, -1518200435701235018L, MethodHandles.lookup().lookupClass()).a(48414069134783L);
   private static final String[] c;

   public void Vulcan_g(Object[] var1) {
      long var2 = (Long)var1[0];
      CommandSender var5 = (CommandSender)var1[1];
      String var4 = (String)var1[2];
      var2 ^= a;
      long var6 = var2 ^ 83954147493239L;
      String var10000 = Vulcan_W();
      var5.sendMessage(Vulcan_eR.Vulcan_m(new Object[]{var4.replaceAll(c[1], Vulcan_eR.Vulcan_m(new Object[]{Vulcan_i9.Vulcan_hN, var6})), var6}));
      String var8 = var10000;
      if (var8 != null) {
         int var9 = AbstractCheck.Vulcan_V();
         ++var9;
         AbstractCheck.Vulcan_H(var9);
      }

   }

   public void Vulcan_q(Object[] var1) {
      CommandSender var2 = (CommandSender)var1[0];
      long var3 = (Long)var1[1];
      var3 ^= a;
      long var5 = var3 ^ 78962664933659L;
      String var10003 = c[0];
      this.Vulcan_g(new Object[]{var5, var2, var10003});
   }

   public static void Vulcan_d(String var0) {
      Vulcan_N = var0;
   }

   public static String Vulcan_W() {
      return Vulcan_N;
   }

   static {
      long var0 = a ^ 123645863806677L;
      Vulcan_d((String)null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[2];
      int var7 = 0;
      String var6 = "Ï\u009e#,m\u0003\u009c\u0096ÿ>^\u0094I'\u0084\u000e¨\u0001Ï¸X*+Åðpß\u0097Å\u0002É\u0080ä½\n-·' ì6Ø¦»!{\u009bB4?\\hÑôk®9ßØ\u009f\u0091_\u008e\b\u0010\u0089ÍN² W\u008d ó5\u0084~\u0011Ür\u009f";
      int var8 = "Ï\u009e#,m\u0003\u009c\u0096ÿ>^\u0094I'\u0084\u000e¨\u0001Ï¸X*+Åðpß\u0097Å\u0002É\u0080ä½\n-·' ì6Ø¦»!{\u009bB4?\\hÑôk®9ßØ\u009f\u0091_\u008e\b\u0010\u0089ÍN² W\u008d ó5\u0084~\u0011Ür\u009f".length();
      char var5 = '@';
      int var4 = -1;

      while(true) {
         ++var4;
         byte[] var10 = var2.doFinal(var6.substring(var4, var4 + var5).getBytes("ISO-8859-1"));
         String var12 = a(var10).intern();
         boolean var10001 = true;
         var9[var7++] = var12;
         if ((var4 += var5) >= var8) {
            c = var9;
            return;
         }

         var5 = var6.charAt(var4);
      }
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
