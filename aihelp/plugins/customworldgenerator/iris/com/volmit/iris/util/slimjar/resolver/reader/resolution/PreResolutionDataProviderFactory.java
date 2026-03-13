package com.volmit.iris.util.slimjar.resolver.reader.resolution;

import java.net.URL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface PreResolutionDataProviderFactory {
   @NotNull
   PreResolutionDataProvider create(@Nullable URL var1);
}
