package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

public interface ArrayBinaryTag extends BinaryTag {
   @NotNull
   BinaryTagType<? extends ArrayBinaryTag> type();
}
