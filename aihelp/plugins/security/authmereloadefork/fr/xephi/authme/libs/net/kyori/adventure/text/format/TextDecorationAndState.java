package fr.xephi.authme.libs.net.kyori.adventure.text.format;

import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

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
