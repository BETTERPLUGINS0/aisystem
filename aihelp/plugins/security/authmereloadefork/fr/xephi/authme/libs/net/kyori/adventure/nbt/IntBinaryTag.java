package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface IntBinaryTag extends NumberBinaryTag {
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
