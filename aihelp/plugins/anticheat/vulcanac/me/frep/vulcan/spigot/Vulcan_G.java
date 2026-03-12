package me.frep.vulcan.spigot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public final class Vulcan_G extends YamlConfiguration {
   private final Map Vulcan_M = new HashMap();
   private boolean Vulcan_f = false;
   private static String Vulcan_z;
   private static final long a = Vulcan_n.a(-1114165628584508911L, -960773364990668142L, MethodHandles.lookup().lookupClass()).a(140544019099054L);
   private static final String[] b;

   public void Vulcan_S(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public void Vulcan_Q(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   public String Vulcan_D(Object[] var1) {
      String var2 = (String)var1[0];
      return this.Vulcan_f(new Object[]{var2, null});
   }

   public String Vulcan_f(Object[] var1) {
      String var2 = (String)var1[0];
      String var3 = (String)var1[1];
      return (String)this.Vulcan_M.getOrDefault(var2, var3);
   }

   public boolean Vulcan_A(Object[] var1) {
      String var2 = (String)var1[0];
      long var3 = (Long)var1[1];
      long var10000 = a ^ var3;

      boolean var6;
      try {
         if (this.Vulcan_D(new Object[]{var2}) != null) {
            var6 = true;
            return var6;
         }
      } catch (RuntimeException var5) {
         throw a((Throwable)var5);
      }

      var6 = false;
      return var6;
   }

   public boolean Vulcan_x(Object[] var1) {
      return this.Vulcan_f;
   }

   public void loadFromString(String param1) {
      // $FF: Couldn't be decompiled
   }

   public String saveToString() {
      // $FF: Couldn't be decompiled
   }

   private boolean Vulcan_O(Object[] param1) {
      // $FF: Couldn't be decompiled
   }

   private Vulcan_G Vulcan_f(Object[] var1) {
      this.Vulcan_f = true;
      return this;
   }

   public static Vulcan_G Vulcan_o(Object[] var0) {
      long var2 = (Long)var0[0];
      File var1 = (File)var0[1];
      var2 ^= a;
      long var4 = var2 ^ 119959818893953L;

      try {
         FileInputStream var6 = new FileInputStream(var1);
         return Vulcan_e(new Object[]{var4, new InputStreamReader(var6, StandardCharsets.UTF_8)});
      } catch (FileNotFoundException var8) {
         Logger var10000 = Bukkit.getLogger();
         StringBuilder var10001 = new StringBuilder();
         String[] var7 = b;
         var10000.warning(var10001.append(var7[3]).append(var1.getName()).append(var7[0]).toString());
         return (new Vulcan_G()).Vulcan_f(new Object[0]);
      }
   }

   public static Vulcan_G Vulcan_y(Object[] var0) {
      long var1 = (Long)var0[0];
      InputStream var3 = (InputStream)var0[1];
      var1 ^= a;
      long var4 = var1 ^ 31185886035758L;
      return Vulcan_e(new Object[]{var4, new InputStreamReader(var3, StandardCharsets.UTF_8)});
   }

   public static Vulcan_G Vulcan_e(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static boolean Vulcan_g(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static String Vulcan_E(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static boolean Vulcan_V(Object[] var0) {
      String var1 = (String)var0[0];
      String var2 = var1.trim();
      return var2.startsWith(b[1]);
   }

   private static String Vulcan_R(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static void Vulcan_i(Object[] param0) {
      // $FF: Couldn't be decompiled
   }

   private static List Vulcan_N(Object[] var0) {
      long var2 = (Long)var0[0];
      ConfigurationSection var1 = (ConfigurationSection)var0[1];
      var2 ^= a;
      String var10000 = Vulcan_f();
      ArrayList var5 = new ArrayList();
      String var4 = var10000;
      Iterator var6 = var1.getKeys(false).iterator();

      label34:
      while(true) {
         ArrayList var9;
         if (var6.hasNext()) {
            String var7 = (String)var6.next();

            do {
               try {
                  if (var2 > 0L) {
                     var9 = var5;
                     if (var4 != null) {
                        return var9;
                     }

                     var5.add(new Vulcan_ed(var7, var1.get(var7)));
                  }

                  if (var4 == null) {
                     continue label34;
                  }
               } catch (RuntimeException var8) {
                  throw a((Throwable)var8);
               }
            } while(var2 < 0L);
         }

         var9 = var5;
         return var9;
      }
   }

   private static void Vulcan_h(Object[] var0) {
      ConfigurationSection var3 = (ConfigurationSection)var0[0];
      long var1 = (Long)var0[1];
      long var10000 = a ^ var1;
      String var7 = Vulcan_f();
      Iterator var5 = var3.getKeys(false).iterator();
      String var4 = var7;

      while(var5.hasNext()) {
         String var6 = (String)var5.next();
         var3.set(var6, (Object)null);
         if (var4 != null) {
            break;
         }
      }

   }

   public static void Vulcan_C(String var0) {
      Vulcan_z = var0;
   }

   public static String Vulcan_f() {
      return Vulcan_z;
   }

   static {
      long var0 = a ^ 65189281715004L;
      Vulcan_C((String)null);
      Cipher var2;
      Cipher var10000 = var2 = Cipher.getInstance("DES/CBC/PKCS5Padding");
      SecretKeyFactory var10002 = SecretKeyFactory.getInstance("DES");
      byte[] var10003 = new byte[]{(byte)((int)(var0 >>> 56)), 0, 0, 0, 0, 0, 0, 0};

      for(int var3 = 1; var3 < 8; ++var3) {
         var10003[var3] = (byte)((int)(var0 << var3 * 8 >>> 56));
      }

      var10000.init(2, var10002.generateSecret(new DESKeySpec(var10003)), new IvParameterSpec(new byte[8]));
      String[] var9 = new String[5];
      int var7 = 0;
      String var6 = "\u0090\u008f>K&ED\u0086×þ\u0002\u0080¦5\u0096; \u0080É_\u001e\u001eÀoùnÉà°§¥\u0004¡\u001cvÌU¨_7»~Æâ\u0097\u001cöó\u0094\b¦Ý·Yik\u0087/";
      int var8 = "\u0090\u008f>K&ED\u0086×þ\u0002\u0080¦5\u0096; \u0080É_\u001e\u001eÀoùnÉà°§¥\u0004¡\u001cvÌU¨_7»~Æâ\u0097\u001cöó\u0094\b¦Ý·Yik\u0087/".length();
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

               var6 = "ó\u0012\"ü\u008a\u0087k¾\b¦Ý·Yik\u0087/";
               var8 = "ó\u0012\"ü\u008a\u0087k¾\b¦Ý·Yik\u0087/".length();
               var5 = '\b';
               var4 = -1;
            }

            ++var4;
            var11 = var6.substring(var4, var4 + var5);
            var10001 = 0;
         }
      }
   }

   private static Throwable a(Throwable var0) {
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
