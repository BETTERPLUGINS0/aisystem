package ac.grim.grimac.shaded.incendo.cloud.parser.standard;

import ac.grim.grimac.shaded.geantyref.GenericTypeReflector;
import ac.grim.grimac.shaded.geantyref.TypeFactory;
import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionVariable;
import ac.grim.grimac.shaded.incendo.cloud.caption.StandardCaptionKeys;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.ParserException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.Suggestion;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import ac.grim.grimac.shaded.incendo.cloud.type.Either;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class EitherParser<C, U, V> implements ArgumentParser.FutureArgumentParser<C, Either<U, V>>, SuggestionProvider<C> {
   private final ParserDescriptor<C, U> primary;
   private final ParserDescriptor<C, V> fallback;

   public static <C, U, V> ParserDescriptor<C, Either<U, V>> eitherParser(@NonNull final ParserDescriptor<C, U> primary, @NonNull final ParserDescriptor<C, V> fallback) {
      return ParserDescriptor.of(new EitherParser(primary, fallback), (TypeToken)TypeToken.get(TypeFactory.parameterizedClass(Either.class, primary.valueType().getType(), fallback.valueType().getType())));
   }

   public EitherParser(@NonNull final ParserDescriptor<C, U> primary, @NonNull final ParserDescriptor<C, V> fallback) {
      this.primary = (ParserDescriptor)Objects.requireNonNull(primary, "primary");
      this.fallback = (ParserDescriptor)Objects.requireNonNull(fallback, "fallback");
   }

   @NonNull
   public ParserDescriptor<C, U> primary() {
      return this.primary;
   }

   @NonNull
   public ParserDescriptor<C, V> fallback() {
      return this.fallback;
   }

   @NonNull
   public CompletableFuture<ArgumentParseResult<Either<U, V>>> parseFuture(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      String input = commandInput.peekString();
      int originalCursor = commandInput.cursor();
      return this.primary.parser().parseFuture(commandContext, commandInput).thenCompose((primaryResult) -> {
         if (primaryResult.parsedValue().isPresent()) {
            return ArgumentParseResult.successFuture(Either.ofPrimary(primaryResult.parsedValue().get()));
         } else {
            commandInput.cursor(originalCursor);
            return this.fallback.parser().parseFuture(commandContext, commandInput).thenApply((fallbackResult) -> {
               return fallbackResult.parsedValue().isPresent() ? ArgumentParseResult.success(Either.ofFallback(fallbackResult.parsedValue().get())) : ArgumentParseResult.failure(new EitherParser.EitherParseException((Throwable)primaryResult.failure().get(), (Throwable)fallbackResult.failure().get(), this.primary.valueType(), this.fallback.valueType(), commandContext, input));
            });
         }
      });
   }

   @NonNull
   public CompletableFuture<? extends Iterable<? extends Suggestion>> suggestionsFuture(@NonNull final CommandContext<C> context, @NonNull final CommandInput input) {
      if (!(this.primary.parser() instanceof SuggestionProvider)) {
         return !(this.fallback.parser() instanceof SuggestionProvider) ? CompletableFuture.completedFuture(Collections.emptyList()) : ((SuggestionProvider)this.fallback.parser()).suggestionsFuture(context, input);
      } else if (!(this.fallback.parser() instanceof SuggestionProvider)) {
         return ((SuggestionProvider)this.primary.parser()).suggestionsFuture(context, input);
      } else {
         CompletableFuture<Iterable<Suggestion>>[] suggestionFutures = new CompletableFuture[]{((SuggestionProvider)this.primary.parser()).suggestionsFuture(context, input.copy()), ((SuggestionProvider)this.fallback.parser()).suggestionsFuture(context, input)};
         return CompletableFuture.allOf(suggestionFutures).thenApply((ignored) -> {
            return (List)Stream.concat(StreamSupport.stream(((Iterable)suggestionFutures[0].getNow(Collections.emptyList())).spliterator(), false), StreamSupport.stream(((Iterable)suggestionFutures[1].getNow(Collections.emptyList())).spliterator(), false)).collect(Collectors.toList());
         });
      }
   }

   public static final class EitherParseException extends ParserException {
      private final Throwable primaryFailure;
      private final Throwable fallbackFailure;
      private final TypeToken<?> primaryType;
      private final TypeToken<?> fallbackType;

      private EitherParseException(@NonNull final Throwable primaryFailure, @NonNull final Throwable fallbackFailure, @NonNull final TypeToken<?> primaryType, @NonNull final TypeToken<?> fallbackType, @NonNull final CommandContext<?> context, @NonNull final String input) {
         super(fallbackFailure, EitherParser.class, context, StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_EITHER, CaptionVariable.of("input", input), CaptionVariable.of("primary", GenericTypeReflector.erase(primaryType.getType()).getSimpleName()), CaptionVariable.of("fallback", GenericTypeReflector.erase(fallbackType.getType()).getSimpleName()));
         this.primaryFailure = primaryFailure;
         this.fallbackFailure = fallbackFailure;
         this.primaryType = primaryType;
         this.fallbackType = fallbackType;
      }

      @NonNull
      public Throwable primaryFailure() {
         return this.primaryFailure;
      }

      @NonNull
      public Throwable fallbackFailure() {
         return this.fallbackFailure;
      }

      @NonNull
      public TypeToken<?> primaryType() {
         return this.primaryType;
      }

      @NonNull
      public TypeToken<?> fallbackType() {
         return this.fallbackType;
      }

      // $FF: synthetic method
      EitherParseException(Throwable x0, Throwable x1, TypeToken x2, TypeToken x3, CommandContext x4, String x5, Object x6) {
         this(x0, x1, x2, x3, x4, x5);
      }
   }
}
