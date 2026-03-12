package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface ShortBinaryTag extends NumberBinaryTag {
   @NotNull
   static ShortBinaryTag shortBinaryTag(final short value) {
      return new ShortBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static ShortBinaryTag of(final short value) {
      return new ShortBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<ShortBinaryTag> type() {
      return BinaryTagTypes.SHORT;
   }

   short value();
}
