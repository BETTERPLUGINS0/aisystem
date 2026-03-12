package ac.grim.grimac.shaded.incendo.cloud.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public interface SuggestionFactory<C, S extends Suggestion> {
   @NonNull
   CompletableFuture<Suggestions<C, S>> suggest(@NonNull CommandContext<C> context, @NonNull String input);

   @NonNull
   CompletableFuture<Suggestions<C, S>> suggest(@NonNull C sender, @NonNull String input);

   @NonNull
   default Suggestions<C, S> suggestImmediately(@NonNull final C sender, @NonNull final String input) {
      try {
         return (Suggestions)this.suggest(sender, input).join();
      } catch (CompletionException var5) {
         Throwable cause = var5.getCause();
         if (cause instanceof RuntimeException) {
            throw (RuntimeException)cause;
         } else {
            throw var5;
         }
      }
   }

   @NonNull
   default <S2 extends Suggestion> SuggestionFactory<C, S2> mapped(@NonNull final SuggestionMapper<S2> mapper) {
      return new MappingSuggestionFactory(this, mapper);
   }
}
