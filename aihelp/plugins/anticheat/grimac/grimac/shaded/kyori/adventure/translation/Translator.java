package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.TranslatableComponent;
import ac.grim.grimac.shaded.kyori.adventure.util.TriState;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Objects;

public interface Translator {
   @Nullable
   static Locale parseLocale(@NotNull final String string) {
      String[] segments = string.split("_", 3);
      int length = segments.length;
      if (length == 1) {
         return new Locale(string);
      } else if (length == 2) {
         return new Locale(segments[0], segments[1]);
      } else {
         return length == 3 ? new Locale(segments[0], segments[1], segments[2]) : null;
      }
   }

   @NotNull
   Key name();

   @NotNull
   default TriState hasAnyTranslations() {
      return TriState.NOT_SET;
   }

   default boolean canTranslate(@NotNull final String key, @NotNull final Locale locale) {
      Component translatedValue = this.translate(Component.translatable((String)Objects.requireNonNull(key, "key")), (Locale)Objects.requireNonNull(locale, "locale"));
      if (translatedValue != null) {
         return true;
      } else {
         return this.translate(key, locale) != null;
      }
   }

   @Nullable
   MessageFormat translate(@NotNull final String key, @NotNull final Locale locale);

   @Nullable
   default Component translate(@NotNull final TranslatableComponent component, @NotNull final Locale locale) {
      return null;
   }
}
