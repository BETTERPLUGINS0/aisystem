package ac.grim.grimac.shaded.incendo.cloud.parser;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public interface MappedArgumentParser<C, I, O> extends ArgumentParser<C, O> {
   @NonNull
   ArgumentParser<C, I> baseParser();

   @FunctionalInterface
   public interface Mapper<C, I, O> {
      @NonNull
      CompletableFuture<ArgumentParseResult<O>> map(@NonNull CommandContext<C> context, @NonNull ArgumentParseResult<I> input);
   }
}
