package ac.grim.grimac.shaded.incendo.cloud.processors.requirements;

import ac.grim.grimac.shaded.incendo.cloud.Command;
import ac.grim.grimac.shaded.incendo.cloud.key.CloudKey;
import java.util.List;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE,
   since = "1.0.0"
)
public final class RequirementApplicable<C, R extends Requirement<C, R>> implements Command.Builder.Applicable<C> {
   private final CloudKey<Requirements<C, R>> requirementKey;
   private final Requirements<C, R> requirements;

   @NonNull
   public static <C, R extends Requirement<C, R>> RequirementApplicable.RequirementApplicableFactory<C, R> factory(@NonNull final CloudKey<Requirements<C, R>> requirementKey) {
      return new RequirementApplicable.RequirementApplicableFactory(requirementKey);
   }

   private RequirementApplicable(@NonNull final CloudKey<Requirements<C, R>> requirementKey, @NonNull final Requirements<C, R> requirements) {
      this.requirementKey = (CloudKey)Objects.requireNonNull(requirementKey, "requirementKey");
      this.requirements = (Requirements)Objects.requireNonNull(requirements, "requirements");
   }

   @NonNull
   public Command.Builder<C> applyToCommandBuilder(@NonNull final Command.Builder<C> builder) {
      return builder.meta(this.requirementKey, this.requirements);
   }

   @API(
      status = Status.STABLE,
      since = "1.0.0"
   )
   public static final class RequirementApplicableFactory<C, R extends Requirement<C, R>> {
      private final CloudKey<Requirements<C, R>> requirementKey;

      private RequirementApplicableFactory(@NonNull final CloudKey<Requirements<C, R>> requirementKey) {
         this.requirementKey = (CloudKey)Objects.requireNonNull(requirementKey, "requirementKey");
      }

      @NonNull
      public RequirementApplicable<C, R> create(@NonNull final Requirements<C, R> requirements) {
         Objects.requireNonNull(requirements, "requirements");
         return new RequirementApplicable(this.requirementKey, requirements);
      }

      @NonNull
      public RequirementApplicable<C, R> create(@NonNull final List<R> requirements) {
         Objects.requireNonNull(requirements, "requirements");
         return new RequirementApplicable(this.requirementKey, Requirements.of(requirements));
      }

      @SafeVarargs
      @NonNull
      public final RequirementApplicable<C, R> create(@NonNull final R... requirements) {
         Objects.requireNonNull(requirements, "requirements");
         return new RequirementApplicable(this.requirementKey, Requirements.of(requirements));
      }
   }
}
