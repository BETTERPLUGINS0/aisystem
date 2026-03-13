package com.volmit.iris.util.slimjar.exceptions;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SlimJarException extends RuntimeException {
   @Contract(
      pure = true
   )
   public SlimJarException(@NotNull String var1) {
      super(var1);
   }

   @Contract(
      pure = true
   )
   public SlimJarException(@NotNull String var1, @NotNull Throwable var2) {
      super(var1);
      if (var2 instanceof SlimJarException) {
         this.addSuppressed(var2);
         this.initCause(var2);
      }
   }

   public void checkInterrupted() {
      if (this.getCause() instanceof InterruptedException) {
         Thread.currentThread().interrupt();
      }
   }
}
