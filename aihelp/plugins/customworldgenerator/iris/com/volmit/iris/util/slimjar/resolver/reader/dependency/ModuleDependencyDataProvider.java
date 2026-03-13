package com.volmit.iris.util.slimjar.resolver.reader.dependency;

import com.volmit.iris.util.slimjar.exceptions.ResolutionException;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import com.volmit.iris.util.slimjar.util.Connections;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Objects;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ModuleDependencyDataProvider implements DependencyDataProvider {
   @NotNull
   private final DependencyReader dependencyReader;
   @NotNull
   private final URL moduleUrl;

   public ModuleDependencyDataProvider(@NotNull DependencyReader var1, @NotNull URL var2) {
      this.dependencyReader = var1;
      this.moduleUrl = var2;
   }

   @NotNull
   public DependencyData get() {
      try {
         URL var1 = this.getURL();
         URLConnection var2 = var1.openConnection();
         if (!(var2 instanceof JarURLConnection)) {
            throw new AssertionError("Invalid Module URL provided(Non-Jar File)");
         } else {
            JarURLConnection var3 = (JarURLConnection)var2;
            JarFile var4 = var3.getJarFile();
            ZipEntry var5 = var4.getEntry("slimjar.dat");
            if (var5 == null) {
               return new DependencyData(Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), Collections.emptySet());
            } else {
               InputStream var6 = var4.getInputStream(var5);

               DependencyData var7;
               try {
                  var7 = this.dependencyReader.read(var6);
               } catch (Throwable var10) {
                  if (var6 != null) {
                     try {
                        var6.close();
                     } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                     }
                  }

                  throw var10;
               }

               if (var6 != null) {
                  var6.close();
               }

               return var7;
            }
         }
      } catch (IOException var11) {
         throw new ResolutionException("Failed to get dependency data.", var11);
      }
   }

   @Contract(
      value = "-> new",
      pure = true
   )
   @NotNull
   public URL getURL() {
      return Connections.newURL("jar:file:" + this.moduleUrl.getFile() + "!/");
   }

   @NotNull
   public DependencyReader dependencyReader() {
      return this.dependencyReader;
   }

   @NotNull
   public URL moduleUrl() {
      return this.moduleUrl;
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 != null && var1.getClass() == this.getClass()) {
         ModuleDependencyDataProvider var2 = (ModuleDependencyDataProvider)var1;
         return Objects.equals(this.dependencyReader, var2.dependencyReader) && Objects.equals(this.moduleUrl, var2.moduleUrl);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.dependencyReader, this.moduleUrl});
   }

   public String toString() {
      String var10000 = String.valueOf(this.dependencyReader);
      return "ModuleDependencyDataProvider[dependencyReader=" + var10000 + ", moduleUrl=" + String.valueOf(this.moduleUrl) + "]";
   }
}
