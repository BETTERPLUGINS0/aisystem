package fr.xephi.authme.service.yaml;

import fr.xephi.authme.libs.ch.jalu.configme.exception.ConfigMeException;
import java.util.Optional;

public class YamlParseException extends RuntimeException {
   private final String file;

   public YamlParseException(String file, ConfigMeException configMeException) {
      super((Throwable)Optional.ofNullable(configMeException.getCause()).orElse(configMeException));
      this.file = file;
   }

   public String getFile() {
      return this.file;
   }
}
