package fr.xephi.authme.libs.net.kyori.adventure.text.format;

import fr.xephi.authme.libs.net.kyori.adventure.text.ComponentBuilder;
import fr.xephi.authme.libs.net.kyori.adventure.text.ComponentBuilderApplicable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface StyleBuilderApplicable extends ComponentBuilderApplicable {
   @Contract(
      mutates = "param"
   )
   void styleApply(@NotNull final Style.Builder style);

   default void componentBuilderApply(@NotNull final ComponentBuilder<?, ?> component) {
      component.style(this::styleApply);
   }
}
