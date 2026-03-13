package com.volmit.iris.util.slimjar.resolver.reader.dependency;

import com.volmit.iris.util.slimjar.exceptions.ResolutionException;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class URLDependencyDataProvider implements DependencyDataProvider {
   @NotNull
   private final DependencyReader dependencyReader;
   @NotNull
   private final URL depFileURL;
   @Nullable
   private DependencyData cachedData = null;

   @Contract(
      pure = true
   )
   public URLDependencyDataProvider(@NotNull DependencyReader var1, @NotNull URL var2) {
      this.dependencyReader = var1;
      this.depFileURL = var2;
   }

   @Contract(
      pure = true
   )
   @NotNull
   public DependencyReader getDependencyReader() {
      return this.dependencyReader;
   }

   @Contract(
      pure = true
   )
   @NotNull
   public DependencyData get() {
      if (this.cachedData != null) {
         return this.cachedData;
      } else {
         try {
            URLConnection var1 = this.depFileURL.openConnection();
            var1.setUseCaches(false);
            InputStream var2 = var1.getInputStream();

            DependencyData var3;
            try {
               this.cachedData = this.dependencyReader.read(var2);
               var3 = this.cachedData;
            } catch (Throwable var6) {
               if (var2 != null) {
                  try {
                     var2.close();
                  } catch (Throwable var5) {
                     var6.addSuppressed(var5);
                  }
               }

               throw var6;
            }

            if (var2 != null) {
               var2.close();
            }

            return var3;
         } catch (IOException var7) {
            throw new ResolutionException("Unable to read dependency data.", var7);
         }
      }
   }
}
