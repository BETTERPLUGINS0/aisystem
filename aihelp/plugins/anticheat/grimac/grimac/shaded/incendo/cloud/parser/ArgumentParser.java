package ac.grim.grimac.shaded.incendo.cloud.parser;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.parser.standard.EitherParser;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProviderHolder;
import ac.grim.grimac.shaded.incendo.cloud.type.Either;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
@API(
   status = Status.STABLE
)
public interface ArgumentParser<C, T> extends SuggestionProviderHolder<C> {
   @NonNull
   ArgumentParseResult<T> parse(@NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput);

   @API(
      status = Status.STABLE
   )
   @NonNull
   default CompletableFuture<ArgumentParseResult<T>> parseFuture(@NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput) {
      return CompletableFuture.completedFuture(this.parse(commandContext, commandInput));
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   default <O> ArgumentParser.FutureArgumentParser<C, O> flatMap(final MappedArgumentParser.Mapper<C, T, O> mapper) {
      return new MappedArgumentParserImpl(this, (MappedArgumentParser.Mapper)Objects.requireNonNull(mapper, "mapper"));
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   default <O> ArgumentParser.FutureArgumentParser<C, O> flatMapSuccess(@NonNull final BiFunction<CommandContext<C>, T, CompletableFuture<ArgumentParseResult<O>>> mapper) {
      Objects.requireNonNull(mapper, "mapper");
      return this.flatMap((ctx, result) -> {
         return result.flatMapSuccessFuture((value) -> {
            return (CompletableFuture)mapper.apply(ctx, value);
         });
      });
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   default <O> ArgumentParser.FutureArgumentParser<C, O> mapSuccess(@NonNull final BiFunction<CommandContext<C>, T, CompletableFuture<O>> mapper) {
      Objects.requireNonNull(mapper, "mapper");
      return this.flatMap((ctx, result) -> {
         return result.mapSuccessFuture((value) -> {
            return (CompletableFuture)mapper.apply(ctx, value);
         });
      });
   }

   @NonNull
   default SuggestionProvider<C> suggestionProvider() {
      return this instanceof SuggestionProvider ? (SuggestionProvider)this : SuggestionProvider.noSuggestions();
   }

   @NonNull
   static <C, U, V> ParserDescriptor<C, Either<U, V>> firstOf(@NonNull final ParserDescriptor<C, U> primary, @NonNull final ParserDescriptor<C, V> fallback) {
      return EitherParser.eitherParser(primary, fallback);
   }

   @FunctionalInterface
   @API(
      status = Status.STABLE
   )
   public interface FutureArgumentParser<C, T> extends ArgumentParser<C, T> {
      @NonNull
      default ArgumentParseResult<T> parse(@NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput) {
         throw new UnsupportedOperationException("parse should not be called on a FutureArgumentParser. Call parseFuture instead.");
      }

      @NonNull
      CompletableFuture<ArgumentParseResult<T>> parseFuture(@NonNull CommandContext<C> commandContext, @NonNull CommandInput commandInput);
   }
}
