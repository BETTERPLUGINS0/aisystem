package com.nisovin.shopkeepers.util.data.serialization;

import org.checkerframework.checker.nullness.qual.Nullable;

public class InvalidDataException extends Exception {
   private static final long serialVersionUID = 7161171181992930356L;

   public InvalidDataException(@Nullable String message) {
      super(message);
   }

   public InvalidDataException(@Nullable String message, @Nullable Throwable cause) {
      super(message, cause);
   }
}
