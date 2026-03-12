package ac.grim.grimac.shaded.incendo.cloud.type.range;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@Immutable
public interface FloatRange extends Range<Float> {
   float minFloat();

   float maxFloat();

   @NonNull
   default Float min() {
      return this.minFloat();
   }

   @NonNull
   default Float max() {
      return this.maxFloat();
   }
}
