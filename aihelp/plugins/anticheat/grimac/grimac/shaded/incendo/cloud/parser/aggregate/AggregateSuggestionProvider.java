package ac.grim.grimac.shaded.incendo.cloud.parser.aggregate;

import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
final class AggregateSuggestionProvider<C> implements SuggestionProvider<C> {
   private final AggregateParser<C, ?> parser;

   AggregateSuggestionProvider(@NonNull final AggregateParser<C, ?> parser) {
      this.parser = parser;
   }

   @NonNull
   public CompletableFuture<Iterable<Suggestion>> suggestionsFuture(@NonNull final CommandContext<C> context, @NonNull final CommandInput input) {
      CommandInput originalInput = input.copy();
      return (new AggregateSuggestionProvider.ParsingInstance(context, input)).parseComponent().thenCompose((component) -> {
         return component.suggestionProvider().suggestionsFuture(context, input.skipWhitespace(1, false).copy());
      }).thenApply((suggestions) -> {
         String prefix = originalInput.difference(input, true);
         List<Suggestion> prefixedSuggestions = new ArrayList();
         Iterator var5 = suggestions.iterator();

         while(var5.hasNext()) {
            Suggestion suggestion = (Suggestion)var5.next();
            prefixedSuggestions.add(suggestion.withSuggestion(String.format("%s%s", prefix, suggestion.suggestion())));
         }

         return prefixedSuggestions;
      });
   }

   private final class ParsingInstance {
      private final Iterator<CommandComponent<C>> components;
      private final CommandContext<C> context;
      private final CommandInput input;
      private CommandComponent<C> component;
      private int previousCursor;

      private ParsingInstance(@NonNull final CommandContext<C> context, @NonNull final CommandInput input) {
         this.components = AggregateSuggestionProvider.this.parser.components().iterator();
         this.context = context;
         this.input = input;
      }

      @NonNull
      private CompletableFuture<CommandComponent<C>> parseComponent() {
         if (!this.components.hasNext()) {
            return CompletableFuture.completedFuture(this.component);
         } else {
            this.component = (CommandComponent)this.components.next();
            this.previousCursor = this.input.cursor();
            return this.component.parser().parseFuture(this.context, this.input.skipWhitespace(1)).thenCompose(this::handleResult);
         }
      }

      @NonNull
      private CompletableFuture<CommandComponent<C>> handleResult(@NonNull final ArgumentParseResult<?> result) {
         boolean consumedAll = this.input.isEmpty();
         if (result.failure().isPresent() || !this.components.hasNext() || this.input.isEmpty()) {
            this.input.cursor(this.previousCursor);
         }

         if (result.failure().isPresent()) {
            return CompletableFuture.completedFuture(this.component);
         } else {
            result.parsedValue().ifPresent((value) -> {
               this.context.store(this.component.name(), value);
            });
            return consumedAll ? CompletableFuture.completedFuture(this.component) : this.parseComponent();
         }
      }

      // $FF: synthetic method
      ParsingInstance(CommandContext x1, CommandInput x2, Object x3) {
         this(x1, x2);
      }
   }
}
