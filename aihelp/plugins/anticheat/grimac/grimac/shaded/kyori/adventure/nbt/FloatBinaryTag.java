package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface FloatBinaryTag extends NumberBinaryTag {
   @NotNull
   static FloatBinaryTag floatBinaryTag(final float value) {
      return new FloatBinaryTagImpl(value);
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static FloatBinaryTag of(final float value) {
      return new FloatBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<FloatBinaryTag> type() {
      return BinaryTagTypes.FLOAT;
   }

   float value();
}
