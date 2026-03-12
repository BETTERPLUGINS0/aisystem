package fr.xephi.authme.libs.ch.jalu.configme.beanmapper;

import fr.xephi.authme.libs.ch.jalu.configme.exception.ConfigMeException;

public class ConfigMeMapperException extends ConfigMeException {
   private static final long serialVersionUID = 5439842847683269906L;

   public ConfigMeMapperException(String message) {
      super(message);
   }

   public ConfigMeMapperException(String message, Throwable cause) {
      super(message, cause);
   }

   public ConfigMeMapperException(MappingContext mappingContext, String message) {
      super(constructMessage(mappingContext, message));
   }

   public ConfigMeMapperException(MappingContext mappingContext, String message, Throwable cause) {
      super(constructMessage(mappingContext, message), cause);
   }

   private static String constructMessage(MappingContext mappingContext, String message) {
      return message + ", for mapping of: [" + mappingContext.createDescription() + "]";
   }
}
