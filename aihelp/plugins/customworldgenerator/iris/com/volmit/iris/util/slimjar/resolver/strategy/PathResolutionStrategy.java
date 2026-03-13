package com.volmit.iris.util.slimjar.resolver.strategy;

import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface PathResolutionStrategy {
   @NotNull
   Collection<String> pathTo(@NotNull Repository var1, @NotNull Dependency var2);
}
