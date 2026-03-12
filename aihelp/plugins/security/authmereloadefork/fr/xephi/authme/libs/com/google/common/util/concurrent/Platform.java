package fr.xephi.authme.libs.com.google.common.util.concurrent;

import fr.xephi.authme.libs.com.google.common.annotations.GwtCompatible;
import javax.annotation.CheckForNull;

@ElementTypesAreNonnullByDefault
@GwtCompatible(
   emulated = true
)
final class Platform {
   static boolean isInstanceOfThrowableClass(@CheckForNull Throwable t, Class<? extends Throwable> expectedClass) {
      return expectedClass.isInstance(t);
   }

   private Platform() {
   }
}
