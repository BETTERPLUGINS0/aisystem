package com.volmit.iris.util.slimjar.exceptions;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class VerificationException extends DownloaderException {
   @Contract(
      pure = true
   )
   public VerificationException(@NotNull String var1) {
      super(var1);
   }

   @Contract(
      pure = true
   )
   public VerificationException(@NotNull String var1, @NotNull Throwable var2) {
      super(var1, var2);
   }
}
