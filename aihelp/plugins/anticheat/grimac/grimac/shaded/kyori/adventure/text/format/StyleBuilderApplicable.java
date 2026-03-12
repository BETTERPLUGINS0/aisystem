package ac.grim.grimac.shaded.kyori.adventure.text.format;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentBuilder;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentBuilderApplicable;

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
