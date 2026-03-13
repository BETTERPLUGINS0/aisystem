package com.volmit.iris.util.slimjar.resolver.reader.resolution;

import com.volmit.iris.util.slimjar.resolver.ResolutionResult;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PreResolutionDataProvider {
   @NotNull
   Map<String, ResolutionResult> get();
}
