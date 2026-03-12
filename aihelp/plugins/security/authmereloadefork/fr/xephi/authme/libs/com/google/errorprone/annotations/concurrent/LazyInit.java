package fr.xephi.authme.libs.com.google.errorprone.annotations.concurrent;

import fr.xephi.authme.libs.com.google.errorprone.annotations.IncompatibleModifiers;
import fr.xephi.authme.libs.com.google.errorprone.annotations.Modifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@IncompatibleModifiers(
   modifier = {Modifier.FINAL}
)
public @interface LazyInit {
}
