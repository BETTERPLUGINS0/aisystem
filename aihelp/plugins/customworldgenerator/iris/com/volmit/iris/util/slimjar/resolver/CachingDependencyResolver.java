package com.volmit.iris.util.slimjar.resolver;

import com.volmit.iris.util.slimjar.logging.LogDispatcher;
import com.volmit.iris.util.slimjar.logging.ProcessLogger;
import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import com.volmit.iris.util.slimjar.resolver.enquirer.RepositoryEnquirer;
import com.volmit.iris.util.slimjar.resolver.enquirer.RepositoryEnquirerFactory;
import com.volmit.iris.util.slimjar.resolver.pinger.URLPinger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CachingDependencyResolver implements DependencyResolver {
   @NotNull
   private static final ProcessLogger LOGGER = LogDispatcher.getMediatingLogger();
   @NotNull
   private final URLPinger urlPinger;
   @NotNull
   private final List<RepositoryEnquirer> repositories;
   @NotNull
   private final Map<Dependency, ResolutionResult> cachedResults = new ConcurrentHashMap();
   @NotNull
   private final Map<String, ResolutionResult> preResolvedResults;

   @Contract(
      pure = true
   )
   public CachingDependencyResolver(@NotNull URLPinger var1, @NotNull Collection<Repository> var2, @NotNull RepositoryEnquirerFactory var3, @NotNull Map<String, ResolutionResult> var4) {
      this.urlPinger = var1;
      this.preResolvedResults = new ConcurrentHashMap(var4);
      Stream var10001 = var2.stream();
      Objects.requireNonNull(var3);
      this.repositories = var10001.map(var3::create).distinct().toList();
   }

   @Contract(
      pure = true
   )
   @NotNull
   public Optional<ResolutionResult> resolve(@NotNull Dependency var1) {
      return this.resolve(var1, Collections.emptyList());
   }

   @Contract(
      pure = true
   )
   @NotNull
   public Optional<ResolutionResult> resolve(@NotNull Dependency var1, @NotNull List<RepositoryEnquirer> var2) {
      return Optional.ofNullable((ResolutionResult)this.cachedResults.computeIfAbsent(var1, (var2x) -> {
         return this.attemptResolve(var2x, var2);
      }));
   }

   @Contract(
      pure = true
   )
   @Nullable
   private ResolutionResult attemptResolve(@NotNull Dependency var1, @NotNull List<RepositoryEnquirer> var2) {
      ResolutionResult var3 = this.preResolvedResults.get(var1.toString()) != null ? (ResolutionResult)this.preResolvedResults.get(var1.toString()) : (ResolutionResult)this.cachedResults.get(var1);
      if (var3 != null) {
         if (var3.checked()) {
            return var3;
         }

         if (var3.aggregator()) {
            return var3;
         }

         String var4 = var3.repository().url().toString();
         boolean var5 = (var2.isEmpty() || var2.stream().anyMatch((var1x) -> {
            return var1x.toString().equals(var4);
         })) && this.urlPinger.ping(var3.dependencyURL());
         boolean var6 = var3.checksumURL() == null || this.urlPinger.ping(var3.checksumURL());
         if (var5 && var6) {
            var3.setChecked();
            return var3;
         }
      }

      List var9 = var2.isEmpty() ? this.repositories : var2;
      ArrayList var10 = new ArrayList(var9.size());
      Iterator var11 = var9.iterator();

      while(var11.hasNext()) {
         RepositoryEnquirer var7 = (RepositoryEnquirer)var11.next();
         var10.add(ForkJoinTask.adapt(() -> {
            return var7.enquire(var1);
         }).fork());
      }

      var11 = var10.iterator();

      ResolutionResult var8;
      do {
         if (!var11.hasNext()) {
            LOGGER.debug("Resolved %s @ [FAILED TO RESOLVE]", var1);
            return null;
         }

         ForkJoinTask var12 = (ForkJoinTask)var11.next();
         var8 = (ResolutionResult)var12.join();
      } while(var8 == null);

      LOGGER.debug("Resolved %s @ %s", var1, var8.dependencyURL());
      return var8;
   }
}
