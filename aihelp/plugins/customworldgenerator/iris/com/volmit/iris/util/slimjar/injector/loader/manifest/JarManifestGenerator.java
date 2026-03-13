package com.volmit.iris.util.slimjar.injector.loader.manifest;

import com.volmit.iris.util.slimjar.exceptions.InjectorException;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class JarManifestGenerator implements ManifestGenerator {
   @NotNull
   private final Map<String, String> attributes = new HashMap();
   @NotNull
   private final URI jarURI;

   @Contract(
      pure = true
   )
   public JarManifestGenerator(@NotNull URI var1) {
      this.jarURI = var1;
   }

   @Contract(
      value = "_, _ -> this",
      mutates = "this"
   )
   @NotNull
   public ManifestGenerator attribute(@NotNull String var1, @NotNull String var2) {
      this.attributes.put(var1, var2);
      return this;
   }

   public void generate() {
      Map var1 = Map.of("create", "true");
      URI var2 = URI.create(String.format("jar:%s", this.jarURI));

      try {
         FileSystem var3 = FileSystems.newFileSystem(var2, var1);

         try {
            Path var4 = var3.getPath("META-INF/MANIFEST.MF");
            Files.createDirectories(var4.getParent());
            BufferedWriter var5 = Files.newBufferedWriter(var4, StandardCharsets.UTF_8, StandardOpenOption.CREATE);

            try {
               Iterator var6 = this.attributes.entrySet().iterator();

               while(var6.hasNext()) {
                  Entry var7 = (Entry)var6.next();
                  var5.write(String.format("%s: %s%n", var7.getKey(), var7.getValue()));
               }
            } catch (Throwable var10) {
               if (var5 != null) {
                  try {
                     var5.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (var5 != null) {
               var5.close();
            }
         } catch (Throwable var11) {
            if (var3 != null) {
               try {
                  var3.close();
               } catch (Throwable var8) {
                  var11.addSuppressed(var8);
               }
            }

            throw var11;
         }

         if (var3 != null) {
            var3.close();
         }

      } catch (IOException var12) {
         throw new InjectorException("Failed to generate manifest.", var12);
      }
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public static ManifestGenerator with(@NotNull URI var0) {
      return new JarManifestGenerator(var0);
   }
}
