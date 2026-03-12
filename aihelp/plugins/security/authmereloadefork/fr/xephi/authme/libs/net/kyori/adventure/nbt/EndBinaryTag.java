package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface EndBinaryTag extends BinaryTag {
   @NotNull
   static EndBinaryTag get() {
      return EndBinaryTagImpl.INSTANCE;
   }

   @NotNull
   default BinaryTagType<EndBinaryTag> type() {
      return BinaryTagTypes.END;
   }
}
