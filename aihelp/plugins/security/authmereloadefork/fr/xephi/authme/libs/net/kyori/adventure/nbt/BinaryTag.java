package fr.xephi.authme.libs.net.kyori.adventure.nbt;

import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import org.jetbrains.annotations.NotNull;

public interface BinaryTag extends BinaryTagLike, Examinable {
   @NotNull
   BinaryTagType<? extends BinaryTag> type();

   @NotNull
   default BinaryTag asBinaryTag() {
      return this;
   }
}
