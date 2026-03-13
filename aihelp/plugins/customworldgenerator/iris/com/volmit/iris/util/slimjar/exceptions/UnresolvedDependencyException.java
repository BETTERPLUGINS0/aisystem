package com.volmit.iris.util.slimjar.exceptions;

import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class UnresolvedDependencyException extends DownloaderException {
   @NotNull
   private final transient Dependency dependency;

   @Contract(
      pure = true
   )
   public UnresolvedDependencyException(@NotNull Dependency var1) {
      super("Could not resolve dependency : " + String.valueOf(var1));
      this.dependency = var1;
   }

   @Contract(
      pure = true
   )
   @NotNull
   public Dependency dependency() {
      return this.dependency;
   }

   @Contract(
      pure = true
   )
   @NotNull
   public String toString() {
      return String.format("UnresolvedDependencyException{dependency=%s}", this.dependency);
   }
}
