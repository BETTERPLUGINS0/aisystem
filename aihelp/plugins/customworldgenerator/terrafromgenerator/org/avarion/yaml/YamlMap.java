package org.avarion.yaml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface YamlMap {
   String value();

   Leniency lenient() default Leniency.UNDEFINED;

   Class<? extends YamlMap.YamlMapProcessor<? extends YamlFileInterface>> processor();

   public interface YamlMapProcessor<T extends YamlFileInterface> {
      void read(T var1, String var2, Map<String, Object> var3);

      Map<String, Object> write(T var1, String var2, Object var3);
   }
}
