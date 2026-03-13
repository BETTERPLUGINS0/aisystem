package org.avarion.yaml;

import java.lang.reflect.InvocationTargetException;
import org.jetbrains.annotations.NotNull;

public class YamlWrapperFactory {
   private YamlWrapperFactory() {
   }

   @NotNull
   public static YamlWrapper create() {
      try {
         return getYamlWrapper();
      } catch (Exception var1) {
         throw new RuntimeException("Failed to create YamlWrapper", var1);
      }
   }

   @NotNull
   private static YamlWrapper getYamlWrapper() throws ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      try {
         Class.forName("org.yaml.snakeyaml.representer.Representer").getDeclaredConstructor();
         return (YamlWrapper)Class.forName("org.avarion.yaml.v1.YamlWrapperImpl").getDeclaredConstructor().newInstance();
      } catch (NoSuchMethodException var1) {
         return (YamlWrapper)Class.forName("org.avarion.yaml.v2.YamlWrapperImpl").getDeclaredConstructor().newInstance();
      }
   }
}
