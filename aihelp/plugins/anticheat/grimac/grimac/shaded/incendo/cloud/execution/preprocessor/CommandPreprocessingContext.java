package ac.grim.grimac.shaded.incendo.cloud.execution.preprocessor;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE
)
@Immutable
public interface CommandPreprocessingContext<C> {
   @NonNull
   static <C> CommandPreprocessingContext<C> of(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      return CommandPreprocessingContextImpl.of(commandContext, commandInput);
   }

   @NonNull
   CommandContext<C> commandContext();

   @NonNull
   CommandInput commandInput();
}
