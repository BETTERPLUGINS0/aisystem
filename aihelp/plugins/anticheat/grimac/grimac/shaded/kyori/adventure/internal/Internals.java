package ac.grim.grimac.shaded.kyori.adventure.internal;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import ac.grim.grimac.shaded.kyori.examination.string.StringExaminer;

@ApiStatus.Internal
public final class Internals {
   private Internals() {
   }

   @NotNull
   public static String toString(@NotNull final Examinable examinable) {
      return (String)examinable.examine(StringExaminer.simpleEscaping());
   }
}
