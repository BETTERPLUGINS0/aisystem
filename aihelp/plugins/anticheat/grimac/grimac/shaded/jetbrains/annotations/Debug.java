package ac.grim.grimac.shaded.jetbrains.annotations;

import ac.grim.grimac.shaded.intellij.lang.annotations.Language;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public final class Debug {
   private Debug() {
      throw new AssertionError("Debug should not be instantiated");
   }

   @Target({ElementType.TYPE})
   @Retention(RetentionPolicy.CLASS)
   public @interface Renderer {
      @Language(
         value = "JAVA",
         prefix = "class Renderer{String $text(){return ",
         suffix = ";}}"
      )
      @NonNls
      String text() default "";

      @Language(
         value = "JAVA",
         prefix = "class Renderer{Object[] $childrenArray(){return ",
         suffix = ";}}"
      )
      @NonNls
      String childrenArray() default "";

      @Language(
         value = "JAVA",
         prefix = "class Renderer{boolean $hasChildren(){return ",
         suffix = ";}}"
      )
      @NonNls
      String hasChildren() default "";
   }
}
