package com.volmit.iris.util.slimjar.resolver.reader.dependency;

import java.net.URL;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ExternalDependencyDataProviderFactory implements DependencyDataProviderFactory {
   @NotNull
   private final DependencyReader reader;

   @Contract(
      pure = true
   )
   public ExternalDependencyDataProviderFactory(@NotNull DependencyReader var1) {
      this.reader = var1;
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public DependencyDataProvider create(@Nullable URL var1) {
      return (DependencyDataProvider)(var1 == null ? new EmptyDependencyDataProvider() : new ModuleDependencyDataProvider(this.reader, var1));
   }
}
