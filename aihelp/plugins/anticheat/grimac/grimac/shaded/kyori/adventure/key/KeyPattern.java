package ac.grim.grimac.shaded.kyori.adventure.key;

import ac.grim.grimac.shaded.intellij.lang.annotations.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PARAMETER})
@Pattern("(?:([a-z0-9_\\-.]+:)?|:)[a-z0-9_\\-./]+")
public @interface KeyPattern {
   @Documented
   @Retention(RetentionPolicy.CLASS)
   @Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PARAMETER})
   @Pattern("[a-z0-9_\\-./]+")
   public @interface Value {
   }

   @Documented
   @Retention(RetentionPolicy.CLASS)
   @Target({ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.PARAMETER})
   @Pattern("[a-z0-9_\\-.]+")
   public @interface Namespace {
   }
}
