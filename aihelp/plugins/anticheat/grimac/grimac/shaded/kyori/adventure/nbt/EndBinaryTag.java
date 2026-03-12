package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface EndBinaryTag extends BinaryTag {
   @NotNull
   static EndBinaryTag endBinaryTag() {
      return EndBinaryTagImpl.INSTANCE;
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static EndBinaryTag get() {
      return EndBinaryTagImpl.INSTANCE;
   }

   @NotNull
   default BinaryTagType<EndBinaryTag> type() {
      return BinaryTagTypes.END;
   }
}
