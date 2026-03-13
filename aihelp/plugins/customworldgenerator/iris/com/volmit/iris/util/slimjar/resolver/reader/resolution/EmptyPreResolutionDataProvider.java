package com.volmit.iris.util.slimjar.resolver.reader.resolution;

import com.volmit.iris.util.slimjar.resolver.ResolutionResult;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public final class EmptyPreResolutionDataProvider implements PreResolutionDataProvider {
   @NotNull
   public Map<String, ResolutionResult> get() {
      return Map.of();
   }
}
