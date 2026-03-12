package ac.grim.grimac.shaded.incendo.cloud.processors.requirements;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE,
   since = "1.0.0"
)
public interface Requirement<C, R extends Requirement<C, R>> {
   boolean evaluateRequirement(@NonNull CommandContext<C> commandContext);

   @NonNull
   default List<R> parents() {
      return List.of();
   }
}
