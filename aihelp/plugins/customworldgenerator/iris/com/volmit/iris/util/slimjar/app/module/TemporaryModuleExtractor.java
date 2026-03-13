package com.volmit.iris.util.slimjar.app.module;

import com.volmit.iris.util.slimjar.exceptions.ModuleExtractorException;
import java.io.IOException;
import java.net.URL;
import org.jetbrains.annotations.NotNull;

public final class TemporaryModuleExtractor implements ModuleExtractor {
   @NotNull
   public URL extractModule(@NotNull URL var1, @NotNull String var2) {
      try {
         return ModuleExtractor.extractFile(var1, var2).toURI().toURL();
      } catch (IOException var4) {
         throw new ModuleExtractorException("Encountered IOException.", var4);
      }
   }
}
