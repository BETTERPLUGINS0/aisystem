package com.volmit.iris.util.slimjar.resolver.reader.resolution;

import java.net.URL;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record WrappingPreResolutionDataProviderFactory(@NotNull PreResolutionDataReader reader) implements PreResolutionDataProviderFactory {
   public WrappingPreResolutionDataProviderFactory(@NotNull PreResolutionDataReader reader) {
      this.reader = var1;
   }

   @Contract(
      pure = true
   )
   @NotNull
   public PreResolutionDataProvider create(@Nullable URL var1) {
      return (PreResolutionDataProvider)(var1 == null ? new EmptyPreResolutionDataProvider() : new WrappingPreResolutionDataProvider(this.reader, var1));
   }

   @NotNull
   public PreResolutionDataReader reader() {
      return this.reader;
   }
}
