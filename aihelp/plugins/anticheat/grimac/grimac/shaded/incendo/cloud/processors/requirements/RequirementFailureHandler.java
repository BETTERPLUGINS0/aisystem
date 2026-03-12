package ac.grim.grimac.shaded.incendo.cloud.processors.requirements;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@FunctionalInterface
@API(
   status = Status.STABLE,
   since = "1.0.0"
)
public interface RequirementFailureHandler<C, R extends Requirement<C, R>> {
   @NonNull
   static <C, R extends Requirement<C, R>> RequirementFailureHandler<C, R> noOp() {
      return (requirement, context) -> {
      };
   }

   void handleFailure(@NonNull CommandContext<C> context, @NonNull R requirement);
}
