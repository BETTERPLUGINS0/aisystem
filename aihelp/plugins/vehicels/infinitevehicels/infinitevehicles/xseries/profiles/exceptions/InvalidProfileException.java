package me.PM2.infinitevehicles.xseries.profiles.exceptions;

import org.jetbrains.annotations.NotNull;

public class InvalidProfileException extends ProfileException {
   private final String value;

   public InvalidProfileException(String var1, String var2) {
      super(var2);
      this.value = var1;
   }

   public InvalidProfileException(String var1, String var2, Throwable var3) {
      super(var2, var3);
      this.value = var1;
   }

   @NotNull
   public String getValue() {
      return this.value;
   }
}
