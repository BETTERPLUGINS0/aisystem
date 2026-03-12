package ac.grim.grimac.shaded.incendo.cloud.processors.requirements;

import java.util.List;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.requirement.*"},
   since = "1.0.0"
)
record RequirementsImpl<C, R extends Requirement<C, R>>(@NonNull List<R> requirements) implements Requirements<C, R> {
   RequirementsImpl(@NonNull List<R> requirements) {
      this.requirements = requirements;
   }

   @NonNull
   public List<R> requirements() {
      return this.requirements;
   }
}
