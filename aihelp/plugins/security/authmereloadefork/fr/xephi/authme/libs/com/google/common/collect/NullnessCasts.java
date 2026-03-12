package fr.xephi.authme.libs.com.google.common.collect;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible
final class NullnessCasts {
   @ParametricNullness
   static <T> T uncheckedCastNullableTToT(@CheckForNull T t) {
      return t;
   }

   @ParametricNullness
   static <T> T unsafeNull() {
      return null;
   }

   private NullnessCasts() {
   }
}
