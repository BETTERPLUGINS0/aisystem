package fr.xephi.authme.libs.net.kyori.adventure.internal;

import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import fr.xephi.authme.libs.net.kyori.examination.string.StringExaminer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public final class Internals {
   private Internals() {
   }

   @NotNull
   public static String toString(@NotNull final Examinable examinable) {
      return (String)examinable.examine(StringExaminer.simpleEscaping());
   }
}
