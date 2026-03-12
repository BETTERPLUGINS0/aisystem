package ac.grim.grimac.shaded.incendo.cloud.suggestion;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@API(
   status = Status.STABLE
)
@Immutable
public interface Suggestions<C, S extends Suggestion> {
   @NonNull
   CommandContext<C> commandContext();

   @NonNull
   List<S> list();

   @NonNull
   CommandInput commandInput();

   @API(
      status = Status.INTERNAL
   )
   static <C, S extends Suggestion> Suggestions<C, S> create(@NonNull final CommandContext<C> ctx, @NonNull final List<S> list, @NonNull final CommandInput commandInput) {
      return SuggestionsImpl.of(ctx, list, commandInput);
   }
}
