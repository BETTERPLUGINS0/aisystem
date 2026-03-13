package com.volmit.iris.core.scripting.func;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface UpdateExecutor {
   @NotNull
   Runnable wrap(int delay, @NotNull Runnable runnable);

   @NotNull
   default Runnable wrap(@NotNull Runnable runnable) {
      return this.wrap(1, runnable);
   }

   default void execute(@NotNull Runnable runnable) {
      this.execute(1, runnable);
   }

   default void execute(int delay, @NotNull Runnable runnable) {
      this.wrap(delay, runnable).run();
   }
}
