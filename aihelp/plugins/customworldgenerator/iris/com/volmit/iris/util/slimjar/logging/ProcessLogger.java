package com.volmit.iris.util.slimjar.logging;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface ProcessLogger {
   void info(@NotNull String var1, @Nullable Object... var2);

   default void debug(@NotNull String message, @Nullable Object... args) {
   }

   default void error(@NotNull String message, @Nullable Object... args) {
   }

   /** @deprecated */
   @Deprecated(
      since = "2.0.0",
      forRemoval = true
   )
   default void log(@NotNull String message, @Nullable Object... args) {
      this.info(message, args);
   }
}
