package ac.grim.grimac.shaded.incendo.cloud.type.range;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.immutables.value.Value.Immutable;

@Immutable
public interface ByteRange extends Range<Byte> {
   byte minByte();

   byte maxByte();

   @NonNull
   default Byte min() {
      return this.minByte();
   }

   @NonNull
   default Byte max() {
      return this.maxByte();
   }
}
