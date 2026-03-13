package com.volmit.iris.util.slimjar.resolver.reader.dependency;

import java.net.URL;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class WrappingDependencyDataProviderFactory implements DependencyDataProviderFactory {
   @NotNull
   private final DependencyReader reader;

   @Contract(
      pure = true
   )
   public WrappingDependencyDataProviderFactory(@NotNull DependencyReader var1) {
      this.reader = var1;
   }

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public DependencyDataProvider create(@Nullable URL var1) {
      return (DependencyDataProvider)(var1 == null ? new EmptyDependencyDataProvider() : new URLDependencyDataProvider(this.reader, var1));
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 == this) {
         return true;
      } else if (var1 instanceof WrappingDependencyDataProviderFactory) {
         WrappingDependencyDataProviderFactory var2 = (WrappingDependencyDataProviderFactory)var1;
         return Objects.equals(this.reader, var2.reader);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.reader});
   }

   public String toString() {
      return "WrappingDependencyDataProviderFactory[reader=" + String.valueOf(this.reader) + "]";
   }
}
