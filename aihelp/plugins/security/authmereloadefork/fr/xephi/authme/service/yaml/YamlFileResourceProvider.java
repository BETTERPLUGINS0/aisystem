package fr.xephi.authme.service.yaml;

import fr.xephi.authme.libs.ch.jalu.configme.exception.ConfigMeException;
import fr.xephi.authme.libs.ch.jalu.configme.resource.PropertyReader;
import fr.xephi.authme.libs.ch.jalu.configme.resource.YamlFileResource;
import java.io.File;

public final class YamlFileResourceProvider {
   private YamlFileResourceProvider() {
   }

   public static YamlFileResource loadFromFile(File file) {
      return new YamlFileResourceProvider.AuthMeYamlFileResource(file);
   }

   private static final class AuthMeYamlFileResource extends YamlFileResource {
      AuthMeYamlFileResource(File file) {
         super(file);
      }

      public PropertyReader createReader() {
         try {
            return super.createReader();
         } catch (ConfigMeException var2) {
            throw new YamlParseException(this.getFile().getPath(), var2);
         }
      }
   }
}
