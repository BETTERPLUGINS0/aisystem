package com.volmit.iris.util.slimjar.resolver.reader.dependency;

import com.volmit.iris.util.slimjar.exceptions.ResolutionException;
import com.volmit.iris.util.slimjar.resolver.data.DependencyData;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DependencyDataProvider {
   @NotNull
   DependencyData get() throws ResolutionException;
}
