package com.volmit.iris.util.slimjar.exceptions;

import com.volmit.iris.util.slimjar.resolver.data.Dependency;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class NotComparableDependencyException extends SlimJarException {
   @Contract(
      pure = true
   )
   public NotComparableDependencyException(@NotNull Dependency var1, @NotNull Dependency var2) {
      String var10001 = String.valueOf(var1);
      super("Dependency " + var10001 + " does not match " + String.valueOf(var2) + "and cannot be compared.");
   }
}
