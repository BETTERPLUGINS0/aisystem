package ac.grim.grimac.shaded.kyori.examination;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.stream.Stream;

public interface Examinable {
   @NotNull
   default String examinableName() {
      return this.getClass().getSimpleName();
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.empty();
   }

   @NotNull
   default <R> R examine(@NotNull final Examiner<R> examiner) {
      return examiner.examine(this);
   }
}
