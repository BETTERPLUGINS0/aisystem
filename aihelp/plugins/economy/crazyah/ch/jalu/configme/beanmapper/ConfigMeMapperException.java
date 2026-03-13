package ch.jalu.configme.beanmapper;

import ch.jalu.configme.exception.ConfigMeException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigMeMapperException extends ConfigMeException {
   private static final long serialVersionUID = 5439842847683269906L;

   public ConfigMeMapperException(@NotNull String message) {
      super(message);
   }

   public ConfigMeMapperException(@NotNull String message, @Nullable Throwable cause) {
      super(message, cause);
   }

   public ConfigMeMapperException(@NotNull MappingContext mappingContext, @NotNull String message) {
      super(constructMessage(mappingContext, message));
   }

   public ConfigMeMapperException(@NotNull MappingContext mappingContext, @NotNull String message, @Nullable Throwable cause) {
      super(constructMessage(mappingContext, message), cause);
   }

   @NotNull
   private static String constructMessage(@NotNull MappingContext mappingContext, @NotNull String message) {
      return message + ", for mapping of: [" + mappingContext.createDescription() + "]";
   }
}
