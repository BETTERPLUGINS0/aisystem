package ac.grim.grimac.shaded.incendo.cloud.exception;

import org.checkerframework.checker.nullness.qual.NonNull;

public class InjectionException extends RuntimeException {
   public InjectionException(@NonNull final String message, @NonNull final Throwable cause) {
      super(message, cause);
   }
}
