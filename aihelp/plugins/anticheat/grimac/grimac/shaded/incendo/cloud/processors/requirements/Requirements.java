package ac.grim.grimac.shaded.incendo.cloud.processors.requirements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE,
   since = "1.0.0"
)
public interface Requirements<C, R extends Requirement<C, R>> extends Iterable<R> {
   @NonNull
   private static <C, R extends Requirement<C, R>> List<R> extractRequirements(@NonNull final List<R> requirements) {
      Objects.requireNonNull(requirements, "requirements");
      List<R> extractedRequirements = new ArrayList();
      Iterator var2 = requirements.iterator();

      while(var2.hasNext()) {
         R requirement = (Requirement)var2.next();
         Objects.requireNonNull(requirement, "requirement");
         Iterator var4 = extractRequirements(requirement.parents()).iterator();

         while(var4.hasNext()) {
            R parentRequirement = (Requirement)var4.next();
            if (!extractedRequirements.contains(parentRequirement)) {
               extractedRequirements.add(parentRequirement);
            }
         }

         if (!extractedRequirements.contains(requirement)) {
            extractedRequirements.add(requirement);
         }
      }

      return List.copyOf(extractedRequirements);
   }

   @NonNull
   static <C, R extends Requirement<C, R>> Requirements<C, R> empty() {
      return new RequirementsImpl(List.of());
   }

   @NonNull
   static <C, R extends Requirement<C, R>> Requirements<C, R> of(@NonNull final List<R> requirements) {
      return new RequirementsImpl(extractRequirements(requirements));
   }

   @SafeVarargs
   @NonNull
   static <C, R extends Requirement<C, R>> Requirements<C, R> of(@NonNull final R... requirements) {
      return of(Arrays.asList(requirements));
   }

   default Requirements<C, R> with(final R requirement) {
      List<R> requirements = new ArrayList(this.requirements());
      requirements.add(requirement);
      return of((List)requirements);
   }

   @NonNull
   List<R> requirements();

   @NonNull
   default Iterator<R> iterator() {
      return this.requirements().iterator();
   }
}
