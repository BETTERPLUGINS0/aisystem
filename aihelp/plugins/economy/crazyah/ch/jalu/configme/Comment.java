package ch.jalu.configme;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NotNull;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Comment {
   @NotNull
   String[] value();

   boolean repeat() default false;
}
