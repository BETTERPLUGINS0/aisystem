package ac.grim.grimac.shaded.incendo.cloud.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor.CommandPreprocessingContext;
import ac.grim.grimac.shaded.incendo.cloud.internal.CommandInputTokenizer;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Stream;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.STABLE
)
public final class FilteringSuggestionProcessor<C> implements SuggestionProcessor<C> {
   @NonNull
   private final FilteringSuggestionProcessor.Filter<C> filter;

   @API(
      status = Status.STABLE
   )
   public FilteringSuggestionProcessor() {
      this(FilteringSuggestionProcessor.Filter.partialTokenMatches(true));
   }

   @API(
      status = Status.STABLE
   )
   public FilteringSuggestionProcessor(@NonNull final FilteringSuggestionProcessor.Filter<C> filter) {
      this.filter = filter;
   }

   @NonNull
   public Stream<Suggestion> process(@NonNull final CommandPreprocessingContext<C> context, @NonNull final Stream<Suggestion> suggestions) {
      String input;
      if (context.commandInput().isEmpty(true)) {
         input = "";
      } else {
         input = context.commandInput().skipWhitespace().remainingInput();
      }

      return suggestions.map((suggestion) -> {
         String filtered = this.filter.filter(context, suggestion.suggestion(), input);
         return filtered == null ? null : suggestion.withSuggestion(filtered);
      }).filter(Objects::nonNull);
   }

   @API(
      status = Status.STABLE
   )
   @FunctionalInterface
   public interface Filter<C> {
      @API(
         status = Status.STABLE
      )
      @Nullable
      String filter(@NonNull CommandPreprocessingContext<C> context, @NonNull String suggestion, @NonNull String input);

      @API(
         status = Status.STABLE
      )
      @NonNull
      default FilteringSuggestionProcessor.Filter<C> and(@NonNull final FilteringSuggestionProcessor.Filter<C> and) {
         return (ctx, suggestion, input) -> {
            String filtered = this.filter(ctx, suggestion, input);
            return filtered == null ? null : and.filter(ctx, filtered, input);
         };
      }

      @API(
         status = Status.STABLE
      )
      @NonNull
      static <C> FilteringSuggestionProcessor.Filter.Simple<C> startsWith(final boolean ignoreCase) {
         BiPredicate<String, String> test = ignoreCase ? (suggestion, input) -> {
            return suggestion.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT));
         } : String::startsWith;
         return FilteringSuggestionProcessor.Filter.Simple.contextFree(test);
      }

      @API(
         status = Status.STABLE
      )
      @NonNull
      static <C> FilteringSuggestionProcessor.Filter.Simple<C> contains(final boolean ignoreCase) {
         BiPredicate<String, String> test = ignoreCase ? (suggestion, input) -> {
            return suggestion.toLowerCase(Locale.ROOT).contains(input.toLowerCase(Locale.ROOT));
         } : String::contains;
         return FilteringSuggestionProcessor.Filter.Simple.contextFree(test);
      }

      @API(
         status = Status.STABLE
      )
      @NonNull
      static <C> FilteringSuggestionProcessor.Filter.Simple<C> partialTokenMatches(final boolean ignoreCase) {
         return FilteringSuggestionProcessor.Filter.Simple.contextFree((suggestion, input) -> {
            List<String> suggestionTokens = (new CommandInputTokenizer(suggestion)).tokenize();
            List<String> inputTokens = (new CommandInputTokenizer(input)).tokenize();
            boolean passed = true;
            Iterator var6 = inputTokens.iterator();

            while(var6.hasNext()) {
               String inputToken = (String)var6.next();
               if (ignoreCase) {
                  inputToken = inputToken.toLowerCase(Locale.ROOT);
               }

               boolean foundMatch = false;
               Iterator iterator = suggestionTokens.iterator();

               while(iterator.hasNext()) {
                  String suggestionToken = (String)iterator.next();
                  String suggestionTokenLower = ignoreCase ? suggestionToken.toLowerCase(Locale.ROOT) : suggestionToken;
                  if (suggestionTokenLower.contains(inputToken)) {
                     iterator.remove();
                     foundMatch = true;
                     break;
                  }
               }

               if (!foundMatch) {
                  passed = false;
                  break;
               }
            }

            return passed;
         });
      }

      @API(
         status = Status.STABLE
      )
      @NonNull
      static <C> FilteringSuggestionProcessor.Filter<C> contextFree(@NonNull final BiFunction<String, String, String> function) {
         return (ctx, suggestion, input) -> {
            return (String)function.apply(suggestion, input);
         };
      }

      @API(
         status = Status.STABLE
      )
      @NonNull
      static <C> FilteringSuggestionProcessor.Filter.Simple<C> simple(final FilteringSuggestionProcessor.Filter.Simple<C> filter) {
         return filter;
      }

      @API(
         status = Status.STABLE
      )
      @FunctionalInterface
      public interface Simple<C> extends FilteringSuggestionProcessor.Filter<C> {
         @API(
            status = Status.STABLE
         )
         boolean test(@NonNull CommandPreprocessingContext<C> context, @NonNull String suggestion, @NonNull String input);

         @Nullable
         default String filter(@NonNull CommandPreprocessingContext<C> context, @NonNull String suggestion, @NonNull String input) {
            return this.test(context, suggestion, input) ? suggestion : null;
         }

         @API(
            status = Status.STABLE
         )
         @NonNull
         static <C> FilteringSuggestionProcessor.Filter.Simple<C> contextFree(@NonNull final BiPredicate<String, String> test) {
            return (ctx, suggestion, input) -> {
               return test.test(suggestion, input);
            };
         }
      }
   }
}
