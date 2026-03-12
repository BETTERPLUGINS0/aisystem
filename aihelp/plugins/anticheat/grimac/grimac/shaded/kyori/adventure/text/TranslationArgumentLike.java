package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TranslationArgumentLike extends ComponentLike {
   @NotNull
   TranslationArgument asTranslationArgument();

   @NotNull
   default Component asComponent() {
      return this.asTranslationArgument().asComponent();
   }
}
