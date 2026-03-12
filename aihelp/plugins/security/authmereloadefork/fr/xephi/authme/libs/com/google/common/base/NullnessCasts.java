package fr.xephi.authme.libs.com.google.common.base;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class NullnessCasts {
   @ParametricNullness
   static <T> T uncheckedCastNullableTToT(@CheckForNull T t) {
      return t;
   }

   private NullnessCasts() {
   }
}
