package com.volmit.iris.util.slimjar.resolver.strategy;

import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import java.util.Collection;
import java.util.Locale;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MavenChecksumPathResolutionStrategy implements PathResolutionStrategy {
   @NotNull
   private final PathResolutionStrategy resolutionStrategy;
   @NotNull
   private final String algorithm;

   @Contract(
      pure = true
   )
   public MavenChecksumPathResolutionStrategy(@NotNull String var1, @NotNull PathResolutionStrategy var2) {
      this.algorithm = var1.replaceAll("[ -]", "").toLowerCase(Locale.ENGLISH);
      this.resolutionStrategy = var2;
   }

   @Contract(
      pure = true
   )
   @NotNull
   public Collection<String> pathTo(@NotNull Repository var1, @NotNull Dependency var2) {
      return (Collection)this.resolutionStrategy.pathTo(var1, var2).stream().map((var1x) -> {
         return var1x + "." + this.algorithm;
      }).collect(Collectors.toSet());
   }
}
