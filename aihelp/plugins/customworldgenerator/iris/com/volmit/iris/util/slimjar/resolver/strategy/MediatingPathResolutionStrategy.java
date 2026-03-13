package com.volmit.iris.util.slimjar.resolver.strategy;

import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import com.volmit.iris.util.slimjar.resolver.data.Repository;
import java.util.Collection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class MediatingPathResolutionStrategy implements PathResolutionStrategy {
   @NotNull
   private final PathResolutionStrategy releaseStrategy;
   @NotNull
   private final PathResolutionStrategy snapshotStrategy;

   @Contract(
      pure = true
   )
   public MediatingPathResolutionStrategy(@NotNull PathResolutionStrategy var1, @NotNull PathResolutionStrategy var2) {
      this.releaseStrategy = var1;
      this.snapshotStrategy = var2;
   }

   @Contract(
      pure = true
   )
   @NotNull
   public Collection<String> pathTo(@NotNull Repository var1, @NotNull Dependency var2) {
      return var2.snapshotId() != null ? this.snapshotStrategy.pathTo(var1, var2) : this.releaseStrategy.pathTo(var1, var2);
   }
}
