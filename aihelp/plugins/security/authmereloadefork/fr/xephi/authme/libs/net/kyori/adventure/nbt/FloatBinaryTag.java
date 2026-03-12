package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface FloatBinaryTag extends NumberBinaryTag {
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
