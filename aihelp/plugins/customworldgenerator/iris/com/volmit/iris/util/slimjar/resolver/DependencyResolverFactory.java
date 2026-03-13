package com.volmit.iris.util.slimjar.resolver;

import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import java.util.Collection;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface DependencyResolverFactory {
   @NotNull
   DependencyResolver create(@NotNull Collection<Repository> var1, @NotNull Map<String, ResolutionResult> var2, @NotNull RepositoryEnquirerFactory var3);
}
