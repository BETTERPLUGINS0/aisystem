package fr.xephi.authme.util;

import fr.xephi.authme.libs.com.google.common.collect.Sets;
import java.util.Set;

public final class ExceptionUtils {
   private ExceptionUtils() {
   }

   public static <T extends Throwable> T findThrowableInCause(Class<T> wantedThrowableType, Throwable throwable) {
      Set<Throwable> visitedObjects = Sets.newIdentityHashSet();

      for(Throwable currentThrowable = throwable; currentThrowable != null && !visitedObjects.contains(currentThrowable); currentThrowable = currentThrowable.getCause()) {
         if (wantedThrowableType.isInstance(currentThrowable)) {
            return (Throwable)wantedThrowableType.cast(currentThrowable);
         }

         visitedObjects.add(currentThrowable);
      }

      return null;
   }

   public static String formatException(Throwable th) {
      return "[" + th.getClass().getSimpleName() + "]: " + th.getMessage();
   }
}
