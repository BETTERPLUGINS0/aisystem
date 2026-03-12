package ac.grim.grimac.shaded.incendo.cloud.type.range;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@Immutable
public interface ShortRange extends Range<Short> {
   short minShort();

   short maxShort();

   @NonNull
   default Short min() {
      return this.minShort();
   }

   @NonNull
   default Short max() {
      return this.maxShort();
   }
}
