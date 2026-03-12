package ac.grim.grimac.shaded.kyori.adventure.text.flattener;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.format.Style;

@FunctionalInterface
public interface FlattenerListener {
   default void pushStyle(@NotNull final Style style) {
   }

   void component(@NotNull final String text);

   default boolean shouldContinue() {
      return true;
   }

   default void popStyle(@NotNull final Style style) {
   }
}
