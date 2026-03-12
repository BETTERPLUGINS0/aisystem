package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
abstract class CommonPattern {
   public abstract CommonMatcher matcher(CharSequence var1);

   public abstract String pattern();

   public abstract int flags();

   public abstract String toString();

   public static CommonPattern compile(String pattern) {
      return Platform.compilePattern(pattern);
   }

   public static boolean isPcreLike() {
      return Platform.patternCompilerIsPcreLike();
   }
}
