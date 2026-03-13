package org.avarion.yaml;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface YamlFile {
   @NotNull
   String header() default "";

   Leniency lenient() default Leniency.STRICT;
}
