package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface ByteBinaryTag extends NumberBinaryTag {
   ByteBinaryTag ZERO = new ByteBinaryTagImpl((byte)0);
   ByteBinaryTag ONE = new ByteBinaryTagImpl((byte)1);

   @NotNull
   static ByteBinaryTag of(final byte value) {
      if (value == 0) {
         return ZERO;
      } else {
         return (ByteBinaryTag)(value == 1 ? ONE : new ByteBinaryTagImpl(value));
      }
   }

   @NotNull
   default BinaryTagType<ByteBinaryTag> type() {
      return BinaryTagTypes.BYTE;
   }

   byte value();
}
