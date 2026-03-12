package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface ArrayBinaryTag extends BinaryTag {
   @NotNull
   BinaryTagType<? extends ArrayBinaryTag> type();
}
