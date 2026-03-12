package ac.grim.grimac.shaded.incendo.cloud.suggestion;

import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
@API(
   status = Status.STABLE
)
public interface SuggestionMapper<S extends Suggestion> {
   @NonNull
   static SuggestionMapper<Suggestion> identity() {
      return (suggestion) -> {
         return suggestion;
      };
   }

   @NonNull
   S map(@NonNull Suggestion suggestion);

   @NonNull
   default <S1 extends Suggestion> SuggestionMapper<S1> then(@NonNull final SuggestionMapper<S1> mapper) {
      Objects.requireNonNull(mapper, "mapper");
      return (suggestion) -> {
         return mapper.map(this.map(suggestion));
      };
   }
}
