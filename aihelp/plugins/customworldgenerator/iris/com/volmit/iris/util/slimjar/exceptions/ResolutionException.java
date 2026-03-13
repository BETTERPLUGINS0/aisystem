package com.volmit.iris.util.slimjar.exceptions;

import org.jetbrains.annotations.NotNull;

public final class ResolutionException extends SlimJarException {
   public ResolutionException(@NotNull String var1) {
      super(var1);
   }

   public ResolutionException(@NotNull String var1, @NotNull Throwable var2) {
      super(var1, var2);
   }
}
