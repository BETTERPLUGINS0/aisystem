package com.volmit.iris.util.slimjar.resolver.reader.resolution;

import com.volmit.iris.util.slimjar.exceptions.ResolutionException;
import com.volmit.iris.util.slimjar.resolver.ResolutionResult;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WrappingPreResolutionDataProvider implements PreResolutionDataProvider {
   @NotNull
   private final PreResolutionDataReader resolutionDataReader;
   @NotNull
   private final URL resolutionFileURL;
   @Nullable
   private Map<String, ResolutionResult> cachedData = null;

   @Contract(
      pure = true
   )
   public WrappingPreResolutionDataProvider(@NotNull PreResolutionDataReader var1, @NotNull URL var2) {
      this.resolutionDataReader = var1;
      this.resolutionFileURL = var2;
   }

   @Contract(
      pure = true
   )
   @NotNull
   public Map<String, ResolutionResult> get() {
      if (this.cachedData != null) {
         return this.cachedData;
      } else {
         try {
            InputStream var1 = this.resolutionFileURL.openStream();

            Map var2;
            try {
               this.cachedData = this.resolutionDataReader.read(var1);
               var2 = this.cachedData;
            } catch (Throwable var5) {
               if (var1 != null) {
                  try {
                     var1.close();
                  } catch (Throwable var4) {
                     var5.addSuppressed(var4);
                  }
               }

               throw var5;
            }

            if (var1 != null) {
               var1.close();
            }

            return var2;
         } catch (ResolutionException | IOException var6) {
            return Collections.emptyMap();
         }
      }
   }
}
