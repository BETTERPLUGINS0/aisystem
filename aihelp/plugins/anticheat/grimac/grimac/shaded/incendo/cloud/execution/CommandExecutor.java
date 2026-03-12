package ac.grim.grimac.shaded.incendo.cloud.execution;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public interface CommandExecutor<C> {
   @NonNull
   default CompletableFuture<CommandResult<C>> executeCommand(@NonNull final C commandSender, @NonNull final String input) {
      return this.executeCommand(commandSender, input, (context) -> {
      });
   }

   @NonNull
   CompletableFuture<CommandResult<C>> executeCommand(@NonNull C commandSender, @NonNull String input, @NonNull Consumer<CommandContext<C>> contextConsumer);

   @NonNull
   ExecutionCoordinator<C> executionCoordinator();
}
