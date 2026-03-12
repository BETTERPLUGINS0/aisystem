package ac.grim.grimac.shaded.incendo.cloud.parser;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.suggestion.SuggestionProvider;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL
)
public final class MappedArgumentParserImpl<C, I, O> implements MappedArgumentParser<C, I, O>, ArgumentParser.FutureArgumentParser<C, O> {
   private final ArgumentParser<C, I> base;
   private final MappedArgumentParser.Mapper<C, I, O> mapper;

   MappedArgumentParserImpl(final ArgumentParser<C, I> base, final MappedArgumentParser.Mapper<C, I, O> mapper) {
      this.base = base;
      this.mapper = mapper;
   }

   @NonNull
   public ArgumentParser<C, I> baseParser() {
      return this.base;
   }

   @NonNull
   public CompletableFuture<ArgumentParseResult<O>> parseFuture(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return this.base.parseFuture(commandContext, commandInput).thenCompose((result) -> {
         return this.mapper.map(commandContext, result);
      });
   }

   @NonNull
   public SuggestionProvider<C> suggestionProvider() {
      return this.base.suggestionProvider();
   }

   @NonNull
   public <O1> ArgumentParser.FutureArgumentParser<C, O1> flatMap(final MappedArgumentParser.Mapper<C, O, O1> mapper) {
      Objects.requireNonNull(mapper, "mapper");
      return new MappedArgumentParserImpl(this.base, (ctx, orig) -> {
         return this.mapper.map(ctx, orig).thenCompose((mapped) -> {
            return mapper.map(ctx, mapped);
         });
      });
   }

   public int hashCode() {
      return 31 + this.base.hashCode() + 7 * this.mapper.hashCode();
   }

   public boolean equals(@Nullable final Object other) {
      if (!(other instanceof MappedArgumentParserImpl)) {
         return false;
      } else {
         MappedArgumentParserImpl<?, ?, ?> that = (MappedArgumentParserImpl)other;
         return this.base.equals(that.base) && this.mapper.equals(that.mapper);
      }
   }

   public String toString() {
      return "MappedArgumentParserImpl{base=" + this.base + ',' + "mapper=" + this.mapper + '}';
   }
}
