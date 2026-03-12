package ac.grim.grimac.shaded.incendo.cloud.suggestion;

import java.util.Objects;
import org.checkerframework.checker.nullness.qual.NonNull;

final class SimpleSuggestion implements Suggestion {
   private final String suggestion;

   SimpleSuggestion(@NonNull final String suggestion) {
      this.suggestion = suggestion;
   }

   @NonNull
   public String suggestion() {
      return this.suggestion;
   }

   @NonNull
   public Suggestion withSuggestion(@NonNull final String suggestion) {
      return new SimpleSuggestion(suggestion);
   }

   public boolean equals(final Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         SimpleSuggestion that = (SimpleSuggestion)o;
         return Objects.equals(this.suggestion, that.suggestion);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.suggestion});
   }

   public String toString() {
      return this.suggestion;
   }
}
