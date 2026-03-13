package com.volmit.iris.core.project;

import com.volmit.iris.Iris;
import com.volmit.iris.util.io.IO;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import org.zeroturnaround.zip.ZipUtil;

public class Gradle {
   private static final boolean WINDOWS = System.getProperty("os.name").toLowerCase().contains("win");
   private static final String[] ENVIRONMENT = createEnvironment();
   private static final String VERSION = "8.14.2";
   private static final String DISTRIBUTION_URL = "https://services.gradle.org/distributions/gradle-8.14.2-bin.zip";
   private static final String HASH = IO.hash("https://services.gradle.org/distributions/gradle-8.14.2-bin.zip");

   public static synchronized void wrapper(File projectDir) {
      try {
         File var1 = new File(var0, "settings.gradle.kts");
         if (!var1.exists()) {
            var1.createNewFile();
         }

         runGradle(var0, "wrapper");
      } catch (Throwable var2) {
         Iris.error("Failed to install gradle wrapper!");
         var2.printStackTrace();
         Iris.reportError(var2);
      }

   }

   public static void runGradle(File projectDir, String... args) {
      File var2 = downloadGradle(false);
      String[] var3 = new String[var1.length + 1];
      var3[0] = var2.getAbsolutePath();
      System.arraycopy(var1, 0, var3, 1, var1.length);
      Process var4 = Runtime.getRuntime().exec(var3, ENVIRONMENT, var0);
      List var5 = Collections.synchronizedList(new ArrayList());
      attach(var4.getInputStream(), var5);
      attach(var4.getErrorStream(), var5);
      int var6 = var4.waitFor();
      if (var6 == 0) {
         var5.forEach(Iris::debug);
      } else {
         var5.forEach((var0x) -> {
            Iris.error(var0x);
         });
         throw new RuntimeException("Gradle exited with code " + var6);
      }
   }

   private static synchronized File downloadGradle(boolean force) {
      File var1 = Iris.instance.getDataFolder(new String[]{"cache", HASH.substring(0, 2), HASH});
      if (var0) {
         IO.delete(var1);
         var1.mkdirs();
      }

      File var2 = new File(var1, "gradle-8.14.2/bin/gradle" + (WINDOWS ? ".bat" : ""));
      if (var2.exists()) {
         var2.setExecutable(true);
         return var2;
      } else {
         try {
            BufferedInputStream var3 = new BufferedInputStream(URI.create("https://services.gradle.org/distributions/gradle-8.14.2-bin.zip").toURL().openStream());

            try {
               ZipUtil.unpack(var3, var1);
            } catch (Throwable var7) {
               try {
                  var3.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }

               throw var7;
            }

            var3.close();
         } catch (Throwable var8) {
            throw new RuntimeException("Failed to download gradle", var8);
         }

         var2.setExecutable(true);
         return var2;
      }
   }

   private static String[] createEnvironment() {
      HashMap var0 = new HashMap(System.getenv());
      var0.put("JAVA_HOME", findJavaHome());
      return (String[])var0.entrySet().stream().map((var0x) -> {
         String var10000 = (String)var0x.getKey();
         return var10000 + "=" + (String)var0x.getValue();
      }).toArray((var0x) -> {
         return new String[var0x];
      });
   }

   private static String findJavaHome() {
      String var0 = System.getProperty("java.home");
      return var0 != null && (new File(var0 + "/bin/java" + (WINDOWS ? ".exe" : ""))).exists() ? var0 : (String)ProcessHandle.current().info().command().map((var0x) -> {
         return (new File(var0x)).getAbsoluteFile().getParentFile().getParentFile();
      }).flatMap((var0x) -> {
         return var0x.exists() ? Optional.of(var0x.getAbsolutePath()) : Optional.empty();
      }).orElseThrow(() -> {
         return new RuntimeException("Failed to find java home, please set java.home system property");
      });
   }

   private static void attach(InputStream stream, List<String> list) {
      Thread.ofPlatform().start(() -> {
         Scanner var2 = new Scanner(var0);

         try {
            while(var2.hasNextLine()) {
               String var3 = var2.nextLine();
               var1.add(var3);
            }
         } catch (Throwable var6) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         var2.close();
      });
   }
}
