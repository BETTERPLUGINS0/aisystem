package fr.xephi.authme.libs.com.alessiodp.libby.configuration;

import org.jetbrains.annotations.NotNull;

public class ConfigurationException extends RuntimeException {
   public ConfigurationException(@NotNull String message) {
      super(message);
   }

   public ConfigurationException(@NotNull String message, @NotNull Throwable cause) {
      super(message, cause);
   }

   public ConfigurationException(@NotNull Throwable cause) {
      super(cause);
   }
}
