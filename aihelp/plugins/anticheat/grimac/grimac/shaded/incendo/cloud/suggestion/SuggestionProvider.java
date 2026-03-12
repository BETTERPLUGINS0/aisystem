package ac.grim.grimac.shaded.incendo.cloud.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
@FunctionalInterface
public interface SuggestionProvider<C> {
   @NonNull
   CompletableFuture<? extends Iterable<? extends Suggestion>> suggestionsFuture(@NonNull CommandContext<C> context, @NonNull CommandInput input);

   @NonNull
   static <C> SuggestionProvider<C> noSuggestions() {
      return NoSuggestions.instance();
   }

   @NonNull
   static <C> SuggestionProvider<C> blocking(@NonNull final BlockingSuggestionProvider<C> blockingSuggestionProvider) {
      return blockingSuggestionProvider;
   }

   @NonNull
   static <C> SuggestionProvider<C> blockingStrings(@NonNull final BlockingSuggestionProvider.Strings<C> blockingStringsSuggestionProvider) {
      return blockingStringsSuggestionProvider;
   }

   @NonNull
   static <C> SuggestionProvider<C> suggesting(@NonNull final Suggestion... suggestions) {
      return suggesting((Iterable)Arrays.asList(suggestions));
   }

   @NonNull
   static <C> SuggestionProvider<C> suggestingStrings(@NonNull final String... suggestions) {
      return suggestingStrings((Iterable)Arrays.asList(suggestions));
   }

   @NonNull
   static <C> SuggestionProvider<C> suggesting(@NonNull final Iterable<? extends Suggestion> suggestions) {
      return blocking((ctx, input) -> {
         return suggestions;
      });
   }

   @NonNull
   static <C> SuggestionProvider<C> suggestingStrings(@NonNull final Iterable<String> suggestions) {
      return blockingStrings((ctx, input) -> {
         return suggestions;
      });
   }
}
