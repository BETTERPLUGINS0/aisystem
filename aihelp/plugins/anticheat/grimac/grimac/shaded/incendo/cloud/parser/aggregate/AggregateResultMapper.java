package ac.grim.grimac.shaded.incendo.cloud.parser.aggregate;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public interface AggregateResultMapper<C, O> {
   @NonNull
   CompletableFuture<ArgumentParseResult<O>> map(@NonNull CommandContext<C> commandContext, @NonNull AggregateParsingContext<C> context);

   @API(
      status = Status.STABLE
   )
   public interface DirectSuccessMapper<C, O> extends AggregateResultMapper<C, O> {
      @NonNull
      O mapSuccess(@NonNull CommandContext<C> commandContext, @NonNull AggregateParsingContext<C> context);

      @NonNull
      default CompletableFuture<ArgumentParseResult<O>> map(@NonNull CommandContext<C> commandContext, @NonNull AggregateParsingContext<C> context) {
         return ArgumentParseResult.successFuture(this.mapSuccess(commandContext, context));
      }
   }
}
