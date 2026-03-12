package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface IntBinaryTag extends NumberBinaryTag {
   @NotNull
   static IntBinaryTag intBinaryTag(final int value) {
      return new IntBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static IntBinaryTag of(final int value) {
      return new IntBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<IntBinaryTag> type() {
      return BinaryTagTypes.INT;
   }

   int value();
}
