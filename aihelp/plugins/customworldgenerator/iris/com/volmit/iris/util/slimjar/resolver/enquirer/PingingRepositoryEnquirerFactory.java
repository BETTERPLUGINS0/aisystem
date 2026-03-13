package com.volmit.iris.util.slimjar.resolver.enquirer;

import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.resolver.pinger.URLPinger;
import com.volmit.iris.util.slimjar.resolver.strategy.PathResolutionStrategy;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record PingingRepositoryEnquirerFactory(@NotNull PathResolutionStrategy pathResolutionStrategy, @NotNull PathResolutionStrategy checksumURLCreationStrategy, @NotNull PathResolutionStrategy pomURLCreationStrategy, @NotNull URLPinger urlPinger) implements RepositoryEnquirerFactory {
   public PingingRepositoryEnquirerFactory(@NotNull PathResolutionStrategy pathResolutionStrategy, @NotNull PathResolutionStrategy checksumURLCreationStrategy, @NotNull PathResolutionStrategy pomURLCreationStrategy, @NotNull URLPinger urlPinger) {
      this.pathResolutionStrategy = var1;
      this.checksumURLCreationStrategy = var2;
      this.pomURLCreationStrategy = var3;
      this.urlPinger = var4;
   }

   @Contract(
      pure = true
   )
   @NotNull
   public RepositoryEnquirer create(@NotNull Repository var1) {
      return new PingingRepositoryEnquirer(var1, this.pathResolutionStrategy, this.checksumURLCreationStrategy, this.pomURLCreationStrategy, this.urlPinger);
   }

   @NotNull
   public PathResolutionStrategy pathResolutionStrategy() {
      return this.pathResolutionStrategy;
   }

   @NotNull
   public PathResolutionStrategy checksumURLCreationStrategy() {
      return this.checksumURLCreationStrategy;
   }

   @NotNull
   public PathResolutionStrategy pomURLCreationStrategy() {
      return this.pomURLCreationStrategy;
   }

   @NotNull
   public URLPinger urlPinger() {
      return this.urlPinger;
   }
}
