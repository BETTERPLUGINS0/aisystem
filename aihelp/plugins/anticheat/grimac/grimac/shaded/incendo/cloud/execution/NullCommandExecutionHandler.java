package ac.grim.grimac.shaded.incendo.cloud.execution;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL
)
final class NullCommandExecutionHandler<C> implements CommandExecutionHandler<C> {
   static final CommandExecutionHandler<?> INSTANCE = new NullCommandExecutionHandler();

   public void execute(@NonNull final CommandContext<C> commandContext) {
   }
}
