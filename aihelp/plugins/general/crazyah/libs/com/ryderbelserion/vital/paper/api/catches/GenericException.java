package libs.com.ryderbelserion.vital.paper.api.catches;

import org.jetbrains.annotations.NotNull;

public final class GenericException extends RuntimeException {
   public GenericException(@NotNull String message, @NotNull Exception cause) {
      super(message, cause);
   }

   public GenericException(@NotNull String message) {
      super(message);
   }
}
