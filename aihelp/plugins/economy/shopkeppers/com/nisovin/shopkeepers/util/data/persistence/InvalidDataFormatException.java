package com.nisovin.shopkeepers.util.data.persistence;

import org.checkerframework.checker.nullness.qual.Nullable;

public class InvalidDataFormatException extends Exception {
   private static final long serialVersionUID = 599830910420908046L;

   public InvalidDataFormatException(@Nullable String message) {
      super(message);
   }

   public InvalidDataFormatException(@Nullable String message, @Nullable Throwable cause) {
      super(message, cause);
   }
}
