package fr.xephi.authme.libs.com.alessiodp.libby.configuration;

import org.jetbrains.annotations.NotNull;

public class MalformedConfigurationException extends ConfigurationException {
   public MalformedConfigurationException(@NotNull String message) {
      super(message);
   }

   public MalformedConfigurationException(@NotNull String message, @NotNull Throwable cause) {
      super(message, cause);
   }

   public MalformedConfigurationException(@NotNull Throwable cause) {
      super(cause);
   }
}
