package com.volmit.iris.util.slimjar.logging;

import java.lang.StackWalker.Option;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class LocationAwareProcessLogger implements ProcessLogger {
   @NotNull
   private static final StackWalker STACK_WALKER;
   @NotNull
   private final ProcessLogger logger;
   @NotNull
   private final Class<?> location;

   @Contract(
      value = "_ -> new",
      pure = true
   )
   @NotNull
   public static ProcessLogger wrapping(@NotNull ProcessLogger var0) {
      return new LocationAwareProcessLogger(var0, STACK_WALKER.getCallerClass());
   }

   @Contract(
      value = " -> new",
      pure = true
   )
   @NotNull
   public static ProcessLogger generic() {
      return new LocationAwareProcessLogger(LogDispatcher.getMediatingLogger(), STACK_WALKER.getCallerClass());
   }

   @Contract(
      pure = true
   )
   private LocationAwareProcessLogger(@NotNull ProcessLogger var1, @NotNull Class<?> var2) {
      this.logger = var1;
      this.location = var2;
   }

   public void info(@NotNull String var1, @Nullable Object... var2) {
      this.logger.info(this.formatMessage(var1), var2);
   }

   public void error(@NotNull String var1, @Nullable Object... var2) {
      this.logger.error(this.formatMessage(var1), var2);
   }

   public void debug(@NotNull String var1, @Nullable Object... var2) {
      this.logger.debug(this.formatMessage(var1), var2);
   }

   @Contract(
      pure = true
   )
   @NotNull
   private String formatMessage(@NotNull String var1) {
      return String.format("[%s] %s", this.location.getSimpleName(), var1);
   }

   static {
      STACK_WALKER = StackWalker.getInstance(Option.RETAIN_CLASS_REFERENCE);
   }
}
