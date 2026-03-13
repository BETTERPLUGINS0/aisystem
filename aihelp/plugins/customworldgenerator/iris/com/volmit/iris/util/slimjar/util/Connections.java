package com.volmit.iris.util.slimjar.util;

import com.volmit.iris.util.slimjar.exceptions.ModuleExtractorException;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class Connections {
   @NotNull
   private static final String SLIMJAR_USER_AGENT = "SlimjarApplication/* URLDependencyDownloader";

   private Connections() {
   }

   @NotNull
   public static URL newURL(@NotNull String var0) {
      return URI.create(var0).toURL();
   }

   @NotNull
   public static URLConnection createDownloadConnection(@NotNull URL var0) {
      URLConnection var1 = var0.openConnection();
      if (var1 instanceof HttpURLConnection) {
         HttpURLConnection var2 = (HttpURLConnection)var1;
         var1.addRequestProperty("User-Agent", "SlimjarApplication/* URLDependencyDownloader");
         int var3 = var2.getResponseCode();
         if (var3 != 200) {
            String var10002 = String.valueOf(var0);
            throw new IOException("Could not download from" + var10002 + " (" + var2.getResponseMessage() + ")");
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static void tryDisconnect(@NotNull URLConnection var0) {
      if (var0 instanceof HttpURLConnection) {
         HttpURLConnection var1 = (HttpURLConnection)var0;
         var1.disconnect();
      }
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public static JarURLConnection openJarConnection(@NotNull URL var0) {
      try {
         URLConnection var1 = var0.openConnection();
         return (JarURLConnection)var1;
      } catch (IOException var2) {
         throw new ModuleExtractorException("Failed to open a connection to url (%s).".formatted(new Object[]{var0}), var2);
      } catch (ClassCastException var3) {
         throw new ModuleExtractorException("Provided Non-Jar URL (%s).".formatted(new Object[]{var0}), var3);
      }
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public static File createTempFile(@NotNull String var0) {
      try {
         File var1 = File.createTempFile(var0, ".jar");
         var1.deleteOnExit();
         return var1;
      } catch (SecurityException | IOException var2) {
         throw new ModuleExtractorException("Failed to create temporary file", var2);
      } catch (IllegalArgumentException var3) {
         throw new ModuleExtractorException("Module name must be at least 3 characters long", var3);
      }
   }
}
