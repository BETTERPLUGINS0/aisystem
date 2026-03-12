package ac.grim.grimac.shaded.incendo.cloud.parser;

import ac.grim.grimac.shaded.geantyref.TypeToken;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE
)
@Immutable
public interface ParserDescriptor<C, T> {
   @NonNull
   ArgumentParser<C, T> parser();

   @NonNull
   TypeToken<T> valueType();

   @NonNull
   default <O> ParserDescriptor<C, O> flatMap(@NonNull final TypeToken<O> mappedType, @NonNull final MappedArgumentParser.Mapper<C, T, O> mapper) {
      return parserDescriptor(this.parser().flatMap(mapper), (TypeToken)mappedType);
   }

   @NonNull
   default <O> ParserDescriptor<C, O> flatMap(@NonNull final Class<O> mappedType, @NonNull final MappedArgumentParser.Mapper<C, T, O> mapper) {
      return parserDescriptor(this.parser().flatMap(mapper), (Class)mappedType);
   }

   @NonNull
   default <O> ParserDescriptor<C, O> flatMapSuccess(@NonNull final TypeToken<O> mappedType, @NonNull final BiFunction<CommandContext<C>, T, CompletableFuture<ArgumentParseResult<O>>> mapper) {
      return parserDescriptor(this.parser().flatMapSuccess(mapper), (TypeToken)mappedType);
   }

   @NonNull
   default <O> ParserDescriptor<C, O> flatMapSuccess(@NonNull final Class<O> mappedType, @NonNull final BiFunction<CommandContext<C>, T, CompletableFuture<ArgumentParseResult<O>>> mapper) {
      return parserDescriptor(this.parser().flatMapSuccess(mapper), (Class)mappedType);
   }

   @NonNull
   default <O> ParserDescriptor<C, O> mapSuccess(@NonNull final TypeToken<O> mappedType, @NonNull final BiFunction<CommandContext<C>, T, CompletableFuture<O>> mapper) {
      return parserDescriptor(this.parser().mapSuccess(mapper), (TypeToken)mappedType);
   }

   @NonNull
   default <O> ParserDescriptor<C, O> mapSuccess(@NonNull final Class<O> mappedType, @NonNull final BiFunction<CommandContext<C>, T, CompletableFuture<O>> mapper) {
      return parserDescriptor(this.parser().mapSuccess(mapper), (Class)mappedType);
   }

   @NonNull
   static <C, T> ParserDescriptor<C, T> of(@NonNull final ArgumentParser<C, T> parser, @NonNull final TypeToken<T> valueType) {
      return ParserDescriptorImpl.of(parser, valueType);
   }

   @NonNull
   static <C, T> ParserDescriptor<C, T> of(@NonNull final ArgumentParser<C, T> parser, @NonNull final Class<T> valueType) {
      return ParserDescriptorImpl.of(parser, TypeToken.get(valueType));
   }

   @NonNull
   static <C, T> ParserDescriptor<C, T> parserDescriptor(@NonNull final ArgumentParser<C, T> parser, @NonNull final TypeToken<T> valueType) {
      return of(parser, valueType);
   }

   @NonNull
   static <C, T> ParserDescriptor<C, T> parserDescriptor(@NonNull final ArgumentParser<C, T> parser, @NonNull final Class<T> valueType) {
      return of(parser, TypeToken.get(valueType));
   }
}
