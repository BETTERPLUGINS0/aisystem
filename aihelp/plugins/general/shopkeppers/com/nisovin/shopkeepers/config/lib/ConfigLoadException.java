package com.nisovin.shopkeepers.config.lib;

import org.checkerframework.checker.nullness.qual.Nullable;

public class ConfigLoadException extends Exception {
   private static final long serialVersionUID = 3283134205506144514L;

   public ConfigLoadException(@Nullable String message) {
      super(message);
   }

   public ConfigLoadException(@Nullable String message, @Nullable Throwable cause) {
      super(message, cause);
   }
}
