package fr.xephi.authme.libs.net.kyori.adventure.translation;

import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.TranslatableComponent;
import fr.xephi.authme.libs.net.kyori.adventure.util.TriState;
import java.text.MessageFormat;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

   @Nullable
   MessageFormat translate(@NotNull final String key, @NotNull final Locale locale);

   @Nullable
   default Component translate(@NotNull final TranslatableComponent component, @NotNull final Locale locale) {
      return null;
   }
}
