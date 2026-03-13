package com.volmit.iris.util.misc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerProperties {
   public static final Properties DATA = new Properties();
   public static final File SERVER_PROPERTIES;
   public static final File BUKKIT_YML;
   public static final String LEVEL_NAME;

   private static String parse(@NotNull String current, @Nullable String next, String fallback, @NotNull String... keys) {
      String[] var4 = var3;
      int var5 = var3.length;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = var4[var6];
         if (var0.equals(var7) && var1 != null) {
            return var1;
         }

         if (var0.startsWith(var7 + "=") && var0.length() > var7.length() + 1) {
            return var0.substring(var7.length() + 1);
         }
      }

      return var2;
   }

   static {
      String[] var0 = (String[])ProcessHandle.current().info().arguments().orElse(new String[0]);
      String var1 = "server.properties";
      String var2 = "bukkit.yml";
      String var3 = null;

      for(int var4 = 0; var4 < var0.length; ++var4) {
         String var5 = var0[var4];
         String var6 = var4 < var0.length - 1 ? var0[var4 + 1] : null;
         var1 = parse(var5, var6, var1, "-c", "--config");
         var2 = parse(var5, var6, var2, "-b", "--bukkit-settings");
         var3 = parse(var5, var6, var3, "-w", "--level-name", "--world");
      }

      SERVER_PROPERTIES = new File(var1);
      BUKKIT_YML = new File(var2);

      try {
         FileInputStream var10 = new FileInputStream(SERVER_PROPERTIES);

         try {
            DATA.load(var10);
         } catch (Throwable var8) {
            try {
               var10.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         var10.close();
      } catch (IOException var9) {
         throw new RuntimeException(var9);
      }

      if (var3 != null) {
         LEVEL_NAME = var3;
      } else {
         LEVEL_NAME = DATA.getProperty("level-name", "world");
      }

   }
}
