package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface LongBinaryTag extends NumberBinaryTag {
   @NotNull
   static LongBinaryTag of(final long value) {
      return new LongBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<LongBinaryTag> type() {
      return BinaryTagTypes.LONG;
   }

   long value();
}
