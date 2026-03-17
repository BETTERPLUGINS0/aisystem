package me.PM2.infinitevehicles.xseries.profiles.exceptions;

import org.jetbrains.annotations.NotNull;

public final class InvalidProfileContainerException extends ProfileException {
   private final Object container;

   public InvalidProfileContainerException(Object var1, String var2) {
      super(var2);
      this.container = var1;
   }

   @NotNull
   public Object getContainer() {
      return this.container;
   }
}
