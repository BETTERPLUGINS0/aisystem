package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ComponentBuilderApplicable {
   @Contract(
      mutates = "param"
   )
   void componentBuilderApply(@NotNull final ComponentBuilder<?, ?> component);
}
