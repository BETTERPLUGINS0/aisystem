package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import java.text.MessageFormat;
import java.util.Locale;

final class ComponentTranslationStore extends AbstractTranslationStore<Component> {
   ComponentTranslationStore(@NotNull final Key name) {
      super(name);
   }

   @Nullable
   public MessageFormat translate(@NotNull final String key, @NotNull final Locale locale) {
      return null;
   }

   @Nullable
   public Component translate(@NotNull final TranslatableComponent component, @NotNull final Locale locale) {
      Component translatedComponent = (Component)this.translationValue(component.key(), locale);
      return translatedComponent == null ? null : translatedComponent.append(component.children());
   }
}
