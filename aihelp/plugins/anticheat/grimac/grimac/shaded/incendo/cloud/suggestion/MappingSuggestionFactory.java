package ac.grim.grimac.shaded.incendo.cloud.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.checkerframework.checker.nullness.qual.NonNull;

final class MappingSuggestionFactory<C, S extends Suggestion> implements SuggestionFactory<C, S> {
   private final SuggestionFactory<C, ?> other;
   private final SuggestionMapper<S> suggestionMapper;

   MappingSuggestionFactory(@NonNull final SuggestionFactory<C, ?> other, @NonNull final SuggestionMapper<S> suggestionMapper) {
      this.other = other;
      this.suggestionMapper = suggestionMapper;
   }

   @NonNull
   public CompletableFuture<Suggestions<C, S>> suggest(@NonNull final CommandContext<C> context, @NonNull final String input) {
      return this.map(this.other.suggest(context, input));
   }

   @NonNull
   public CompletableFuture<Suggestions<C, S>> suggest(@NonNull final C sender, @NonNull final String input) {
      return this.map(this.other.suggest(sender, input));
   }

   @NonNull
   public <S2 extends Suggestion> SuggestionFactory<C, S2> mapped(@NonNull final SuggestionMapper<S2> mapper) {
      return new MappingSuggestionFactory(this.other, this.suggestionMapper.then(mapper));
   }

   @NonNull
   private <S1 extends Suggestion> CompletableFuture<Suggestions<C, S>> map(@NonNull final CompletableFuture<Suggestions<C, S1>> future) {
      return future.thenApply((suggestions) -> {
         CommandContext var10000 = suggestions.commandContext();
         Stream var10001 = suggestions.list().stream();
         SuggestionMapper var10002 = this.suggestionMapper;
         Objects.requireNonNull(var10002);
         return Suggestions.create(var10000, (List)var10001.map(var10002::map).collect(Collectors.toList()), suggestions.commandInput());
      });
   }
}
