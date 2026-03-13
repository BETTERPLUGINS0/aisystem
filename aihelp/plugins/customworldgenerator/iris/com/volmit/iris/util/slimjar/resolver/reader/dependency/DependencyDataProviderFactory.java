package com.volmit.iris.util.slimjar.resolver.reader.dependency;

import java.net.URL;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface DependencyDataProviderFactory {
   @NotNull
   DependencyDataProvider create(@Nullable URL var1);
}
