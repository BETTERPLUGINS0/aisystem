package ac.grim.grimac.shaded.incendo.cloud.execution;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
@API(
   status = Status.STABLE
)
public interface CommandExecutionHandler<C> {
   @API(
      status = Status.STABLE
   )
   @NonNull
   static <C> CommandExecutionHandler<C> noOpCommandExecutionHandler() {
      return NullCommandExecutionHandler.INSTANCE;
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   static <C> CommandExecutionHandler<C> delegatingExecutionHandler(final List<CommandExecutionHandler<C>> handlers) {
      return new MulticastDelegateFutureCommandExecutionHandler(handlers);
   }

   void execute(@NonNull CommandContext<C> commandContext);

   @API(
      status = Status.STABLE
   )
   default CompletableFuture<Void> executeFuture(@NonNull CommandContext<C> commandContext) {
      CompletableFuture future = new CompletableFuture();

      try {
         this.execute(commandContext);
         future.complete((Object)null);
      } catch (Throwable var4) {
         future.completeExceptionally(var4);
      }

      return future;
   }

   @FunctionalInterface
   @API(
      status = Status.STABLE
   )
   public interface FutureCommandExecutionHandler<C> extends CommandExecutionHandler<C> {
      default void execute(@NonNull CommandContext<C> commandContext) {
         throw new UnsupportedOperationException("execute should not be called on FutureCommandExecutionHandlers, call executeFuture instead.");
      }

      CompletableFuture<Void> executeFuture(@NonNull CommandContext<C> commandContext);
   }
}
