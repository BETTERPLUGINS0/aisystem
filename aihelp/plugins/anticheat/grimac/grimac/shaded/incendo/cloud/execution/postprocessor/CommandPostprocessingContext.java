package ac.grim.grimac.shaded.incendo.cloud.execution.postprocessor;

import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE
)
@Immutable
public interface CommandPostprocessingContext<C> {
   @NonNull
   static <C> CommandPostprocessingContext<C> of(@NonNull final CommandContext<C> commandContext, @NonNull final Command<C> command) {
      return CommandPostprocessingContextImpl.of(commandContext, command);
   }

   @NonNull
   CommandContext<C> commandContext();

   @NonNull
   Command<C> command();
}
