package com.volmit.iris.util.slimjar.app.module;

import com.volmit.iris.util.slimjar.exceptions.ModuleExtractorException;
import com.volmit.iris.util.slimjar.exceptions.ModuleNotFoundException;
import com.volmit.iris.util.slimjar.util.Connections;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ModuleExtractor {
   @NotNull
   URL extractModule(@NotNull URL var1, @NotNull String var2) throws ModuleExtractorException;

   @NotNull
   static File extractFile(@NotNull URL url, @NotNull String name) throws ModuleExtractorException {
      File tempFile = Connections.createTempFile(name);
      JarURLConnection connection = Connections.openJarConnection(url);

      try {
         JarFile jarFile = connection.getJarFile();

         File var14;
         try {
            JarEntry module = jarFile.getJarEntry("modules/%s.isolated-jar".formatted(new Object[]{name}));
            if (module == null) {
               throw new ModuleNotFoundException(name);
            }

            InputStream stream = jarFile.getInputStream(module);

            try {
               Files.copy(stream, tempFile.toPath(), new CopyOption[]{StandardCopyOption.REPLACE_EXISTING});
            } catch (Throwable var11) {
               if (stream != null) {
                  try {
                     stream.close();
                  } catch (Throwable var10) {
                     var11.addSuppressed(var10);
                  }
               }

               throw var11;
            }

            if (stream != null) {
               stream.close();
            }

            var14 = tempFile;
         } catch (Throwable var12) {
            if (jarFile != null) {
               try {
                  jarFile.close();
               } catch (Throwable var9) {
                  var12.addSuppressed(var9);
               }
            }

            throw var12;
         }

         if (jarFile != null) {
            jarFile.close();
         }

         return var14;
      } catch (IOException var13) {
         throw new ModuleExtractorException("Encountered IOException.", var13);
      }
   }
}
