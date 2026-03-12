package com.nisovin.shopkeepers.util.data.serialization;

import org.checkerframework.checker.nullness.qual.Nullable;

public class MissingDataException extends InvalidDataException {
   private static final long serialVersionUID = 7542690388367072712L;

   public MissingDataException(@Nullable String message) {
      super(message);
   }

   public MissingDataException(@Nullable String message, @Nullable Throwable cause) {
      super(message, cause);
   }
}
