package com.volmit.iris.util.slimjar.util;

import com.volmit.iris.util.slimjar.app.module.ModuleExtractor;
import com.volmit.iris.util.slimjar.exceptions.ResolutionException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public final class Modules {
   private Modules() {
   }

   @NotNull
   public static URL findModule(@NotNull String var0) {
      ClassLoader var1 = Modules.class.getClassLoader();
      return (URL)Objects.requireNonNull(var1.getResource("modules/" + var0 + ".isolated-jar"));
   }

   @NotNull
   public static URL[] extract(@NotNull ModuleExtractor var0, @NotNull Collection<String> var1) {
      URL[] var2 = new URL[var1.size()];
      int var3 = 0;

      URL var7;
      for(Iterator var4 = var1.iterator(); var4.hasNext(); var2[var3++] = var7) {
         String var5 = (String)var4.next();
         URL var6 = findModule(var5);
         var7 = var0.extractModule(var6, var5);
      }

      return var2;
   }

   @NotNull
   public static Set<String> findLocalModules() {
      URL var0 = Modules.class.getProtectionDomain().getCodeSource().getLocation();

      Path var1;
      FileSystem var2;
      try {
         Path var3 = Paths.get(var0.toURI());
         if (Files.isRegularFile(var3, new LinkOption[0]) && var3.getFileName().toString().endsWith(".jar")) {
            var2 = FileSystems.newFileSystem(URI.create("jar:" + String.valueOf(var3.toUri()) + "!/"), Map.of());
            var3 = var2.getPath("/");
         } else {
            var2 = null;
         }

         var1 = var3.resolve("modules");
      } catch (IOException | URISyntaxException var20) {
         throw new ResolutionException("Failed to resolve local modules", var20);
      }

      Set var4;
      try {
         Stream var21 = Files.walk(var1, 1, new FileVisitOption[0]);

         try {
            var4 = (Set)var21.map((var1x) -> {
               return var1.relativize(var1x).toString();
            }).filter((var0x) -> {
               return var0x.endsWith(".isolated-jar");
            }).map((var0x) -> {
               return var0x.substring(0, var0x.length() - ".isolated-jar".length());
            }).filter((var0x) -> {
               return !var0x.equals("loader-agent");
            }).collect(Collectors.toUnmodifiableSet());
         } catch (Throwable var17) {
            if (var21 != null) {
               try {
                  var21.close();
               } catch (Throwable var16) {
                  var17.addSuppressed(var16);
               }
            }

            throw var17;
         }

         if (var21 != null) {
            var21.close();
         }
      } catch (IOException var18) {
         throw new ResolutionException("Encountered exception while walking files.", var18);
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (IOException var15) {
            }
         }

      }

      return var4;
   }
}
