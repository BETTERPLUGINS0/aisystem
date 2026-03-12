package fr.xephi.authme.libs.net.kyori.adventure.text;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ComponentBuilderApplicable {
   @Contract(
      mutates = "param"
   )
   void componentBuilderApply(@NotNull final ComponentBuilder<?, ?> component);
}
