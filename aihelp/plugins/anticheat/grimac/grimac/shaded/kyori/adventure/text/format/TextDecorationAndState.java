package ac.grim.grimac.shaded.kyori.adventure.text.format;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;

@ApiStatus.NonExtendable
public interface TextDecorationAndState extends Examinable, StyleBuilderApplicable {
   @NotNull
   TextDecoration decoration();

   @NotNull
   TextDecoration.State state();

   default void styleApply(@NotNull final Style.Builder style) {
      style.decoration(this.decoration(), this.state());
   }

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("decoration", (Object)this.decoration()), ExaminableProperty.of("state", (Object)this.state()));
   }
}
