package com.volmit.iris.util.slimjar.exceptions;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class OutputWriterException extends SlimJarException {
   @Contract
   public OutputWriterException(@NotNull String var1) {
      super(var1);
   }

   public OutputWriterException(@NotNull String var1, @NotNull Throwable var2) {
      super(var1, var2);
   }
}
