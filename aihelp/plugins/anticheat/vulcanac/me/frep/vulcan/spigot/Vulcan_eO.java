package me.frep.vulcan.spigot;

import java.io.File;
import java.lang.invoke.MethodHandles;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import me.frep.vulcan.spigot.check.AbstractCheck;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Vulcan_eO {
   private final File Vulcan_v;
   private final FileConfiguration Vulcan_V;
   private static String[] Vulcan_Y;
   private static final long a = Vulcan_n.a(-2110932532960401875L, -3772157131247283886L, MethodHandles.lookup().lookupClass()).a(28337959472935L);
   private static final String b;

   public Vulcan_eO(String var1) {
      long var2 = a ^ 124933482272014L;
      String[] var10000 = Vulcan_X();
      super();
      this.Vulcan_v = new File(Vulcan_Xs.INSTANCE.Vulcan_J().getDataFolder() + File.separator, var1 + b);
      String[] var4 = var10000;
      this.Vulcan_V = YamlConfiguration.loadConfiguration(this.Vulcan_v);
      if (var4 != null) {
         int var5 = AbstractCheck.Vulcan_V();
         ++var5;
         AbstractCheck.Vulcan_H(var5);
      }

   }

   public FileConfiguration Vulcan_u(Object[] var1) {
      return this.Vulcan_V;
   }

   public void Vulcan_o(Object[] var1) {
      try {
         this.Vulcan_u(new Object[0]).save(this.Vulcan_v);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void Vulcan_y(String[] var0) {
      Vulcan_Y = var0;
   }

   public static String[] Vulcan_X() {
      return Vulcan_Y;
   }

   static {
      long var0 = a ^ 103030920182554L;
      Vulcan_y((String[])null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      byte[] var4 = var2.doFinal("Í\u0091í\u008b\u001d±\fA".getBytes("ISO-8859-1"));
      String var5 = a(var4).intern();
      boolean var10001 = true;
      b = var5;
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
