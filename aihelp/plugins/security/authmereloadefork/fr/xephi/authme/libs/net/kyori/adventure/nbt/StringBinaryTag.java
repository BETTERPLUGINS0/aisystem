package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface StringBinaryTag extends BinaryTag {
   @NotNull
   static StringBinaryTag of(@NotNull final String value) {
      return new StringBinaryTagImpl(value);
   }

   @NotNull
   default BinaryTagType<StringBinaryTag> type() {
      return BinaryTagTypes.STRING;
   }

   @NotNull
   String value();
}
