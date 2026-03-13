package com.volmit.iris.util.slimjar.exceptions;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class ModuleNotFoundException extends ModuleExtractorException {
   @Contract(
      pure = true
   )
   public ModuleNotFoundException(@NotNull String var1) {
      super("Could not find module in jar: " + var1);
   }
}
