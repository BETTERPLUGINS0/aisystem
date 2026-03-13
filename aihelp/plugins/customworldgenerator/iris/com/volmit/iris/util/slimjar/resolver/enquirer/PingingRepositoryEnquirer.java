package com.volmit.iris.util.slimjar.resolver.enquirer;

import com.volmit.iris.util.slimjar.logging.LogDispatcher;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import com.volmit.iris.util.slimjar.resolver.ResolutionResult;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.resolver.pinger.URLPinger;
import com.volmit.iris.util.slimjar.resolver.strategy.PathResolutionStrategy;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record PingingRepositoryEnquirer(@NotNull Repository repository, @NotNull PathResolutionStrategy dependencyURLCreationStrategy, @NotNull PathResolutionStrategy checksumURLCreationStrategy, @NotNull PathResolutionStrategy pomURLCreationStrategy, @NotNull URLPinger urlPinger) implements RepositoryEnquirer {
   @NotNull
   private static final ProcessLogger LOGGER = LogDispatcher.getMediatingLogger();

   public PingingRepositoryEnquirer(@NotNull Repository repository, @NotNull PathResolutionStrategy dependencyURLCreationStrategy, @NotNull PathResolutionStrategy checksumURLCreationStrategy, @NotNull PathResolutionStrategy pomURLCreationStrategy, @NotNull URLPinger urlPinger) {
      this.repository = var1;
      this.dependencyURLCreationStrategy = var2;
      this.checksumURLCreationStrategy = var3;
      this.pomURLCreationStrategy = var4;
      this.urlPinger = var5;
   }

   @Contract(
      pure = true
   )
   @Nullable
   public ResolutionResult enquire(@NotNull Dependency var1) {
      LOGGER.debug("Enquiring repositories to find %s", var1);
      Stream var10000 = this.dependencyURLCreationStrategy.pathTo(this.repository, var1).stream().map(this::createURL).filter(Objects::nonNull);
      URLPinger var10001 = this.urlPinger;
      Objects.requireNonNull(var10001);
      return (ResolutionResult)var10000.filter(var10001::ping).findFirst().map((var2) -> {
         Stream var10000 = this.checksumURLCreationStrategy.pathTo(this.repository, var1).parallelStream().map(this::createURL).filter(Objects::nonNull);
         URLPinger var10001 = this.urlPinger;
         Objects.requireNonNull(var10001);
         URL var3 = (URL)var10000.filter(var10001::ping).findFirst().orElse((Object)null);
         return new ResolutionResult(this.repository, var2, var3, false, true);
      }).orElseGet(() -> {
         Stream var10000 = this.pomURLCreationStrategy.pathTo(this.repository, var1).parallelStream().map(this::createURL).filter(Objects::nonNull);
         URLPinger var10001 = this.urlPinger;
         Objects.requireNonNull(var10001);
         return (ResolutionResult)var10000.filter(var10001::ping).findFirst().map((var1x) -> {
            return new ResolutionResult(this.repository, var1x, (URL)null, true, false);
         }).orElse((Object)null);
      });
   }

   @NotNull
   public String toString() {
      return this.repository.url().toString();
   }

   @Contract(
      pure = true
   )
   @Nullable
   private URL createURL(@NotNull String var1) {
      try {
         return new URL(this.repository.url(), var1);
      } catch (MalformedURLException var3) {
         LOGGER.error("Failed to create URL for %s", var1);
         return null;
      }
   }

   @NotNull
   public Repository repository() {
      return this.repository;
   }

   @NotNull
   public PathResolutionStrategy dependencyURLCreationStrategy() {
      return this.dependencyURLCreationStrategy;
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
