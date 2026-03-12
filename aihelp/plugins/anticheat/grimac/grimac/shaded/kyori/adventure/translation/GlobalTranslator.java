package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.Locale;

public interface GlobalTranslator extends Translator, Examinable {
   @NotNull
   static GlobalTranslator translator() {
      return GlobalTranslatorImpl.INSTANCE;
   }

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @NotNull
   static GlobalTranslator get() {
      return GlobalTranslatorImpl.INSTANCE;
   }

   @NotNull
   static TranslatableComponentRenderer<Locale> renderer() {
      return GlobalTranslatorImpl.INSTANCE.renderer;
   }

   @NotNull
   static Component render(@NotNull final Component component, @NotNull final Locale locale) {
      return renderer().render(component, locale);
   }

   @NotNull
   Iterable<? extends Translator> sources();

   boolean addSource(@NotNull final Translator source);

   boolean removeSource(@NotNull final Translator source);
}
