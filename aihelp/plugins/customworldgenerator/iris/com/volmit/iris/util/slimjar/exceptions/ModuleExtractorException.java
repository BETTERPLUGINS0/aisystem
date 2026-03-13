package com.volmit.iris.util.slimjar.exceptions;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ModuleExtractorException extends SlimJarException {
   @Contract(
      pure = true
   )
   public ModuleExtractorException(@NotNull String var1) {
      super(var1);
   }

   @Contract(
      pure = true
   )
   public ModuleExtractorException(@NotNull String var1, @NotNull Throwable var2) {
      super(var1, var2);
   }
}
