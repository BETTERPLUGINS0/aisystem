package ac.grim.grimac.shaded.incendo.cloud.type.range;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@Immutable
public interface DoubleRange extends Range<Double> {
   double minDouble();

   double maxDouble();

   @NonNull
   default Double min() {
      return this.minDouble();
   }

   @NonNull
   default Double max() {
      return this.maxDouble();
   }
}
