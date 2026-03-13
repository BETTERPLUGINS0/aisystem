package com.volmit.iris.util.slimjar.resolver;

import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DependencyResolver {
   @NotNull
   Optional<ResolutionResult> resolve(@NotNull Dependency var1);
}
