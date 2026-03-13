package com.volmit.iris.util.slimjar.resolver;

import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import com.volmit.iris.util.slimjar.resolver.pinger.URLPinger;
import java.util.Collection;
import java.util.Map;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class CachingDependencyResolverFactory implements DependencyResolverFactory {
   @NotNull
   private final URLPinger urlPinger;

   @Contract(
      pure = true
   )
   public CachingDependencyResolverFactory(@NotNull URLPinger var1) {
      this.urlPinger = var1;
   }

   @Contract(
      pure = true
   )
   @NotNull
   public DependencyResolver create(@NotNull Collection<Repository> var1, @NotNull Map<String, ResolutionResult> var2, @NotNull RepositoryEnquirerFactory var3) {
      return new CachingDependencyResolver(this.urlPinger, var1, var3, var2);
   }
}
