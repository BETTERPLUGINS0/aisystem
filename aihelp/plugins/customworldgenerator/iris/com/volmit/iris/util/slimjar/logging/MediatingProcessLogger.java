package com.volmit.iris.util.slimjar.logging;

import java.util.Collection;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class MediatingProcessLogger implements ProcessLogger {
   @NotNull
   private final Collection<ProcessLogger> loggers;

   @Contract(
      pure = true
   )
   public MediatingProcessLogger(@NotNull Collection<ProcessLogger> var1) {
      this.loggers = var1;
   }

   public void info(@NotNull String var1, @Nullable Object... var2) {
      this.loggers.forEach((var2x) -> {
         var2x.info(var1, var2);
      });
   }

   public void debug(@NotNull String var1, @Nullable Object... var2) {
      this.loggers.forEach((var2x) -> {
         var2x.debug(var1, var2);
      });
   }

   public void error(@NotNull String var1, @Nullable Object... var2) {
      this.loggers.forEach((var2x) -> {
         var2x.error(var1, var2);
      });
   }

   @Contract(
      pure = true
   )
   public void addLogger(@NotNull ProcessLogger var1) {
      this.loggers.add(var1);
   }

   @Contract(
      pure = true
   )
   public void removeLogger(@NotNull ProcessLogger var1) {
      this.loggers.remove(var1);
   }

   @Contract(
      pure = true
   )
   public void clearLoggers() {
      this.loggers.clear();
   }
}
