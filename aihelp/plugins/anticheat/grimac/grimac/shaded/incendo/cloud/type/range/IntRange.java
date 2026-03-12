package ac.grim.grimac.shaded.incendo.cloud.type.range;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@Immutable
public interface IntRange extends Range<Integer> {
   int minInt();

   int maxInt();

   @NonNull
   default Integer min() {
      return this.minInt();
   }

   @NonNull
   default Integer max() {
      return this.maxInt();
   }
}
