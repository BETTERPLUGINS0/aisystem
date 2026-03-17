package me.PM2.infinitevehicles.xseries.profiles.exceptions;

import org.jetbrains.annotations.NotNull;

public final class UnknownPlayerException extends InvalidProfileException {
   private final Object unknownObject;

   public UnknownPlayerException(Object var1, String var2) {
      super(var1.toString(), var2);
      this.unknownObject = var1;
   }

   @NotNull
   public Object getUnknownObject() {
      return this.unknownObject;
   }
}
