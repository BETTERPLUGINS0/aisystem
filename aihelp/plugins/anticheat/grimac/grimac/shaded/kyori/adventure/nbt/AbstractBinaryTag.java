package ac.grim.grimac.shaded.kyori.adventure.nbt;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.examination.string.StringExaminer;

abstract class AbstractBinaryTag implements BinaryTag {
   @NotNull
   public final String examinableName() {
      return this.type().toString();
   }

   public final String toString() {
      return (String)this.examine(StringExaminer.simpleEscaping());
   }
}
