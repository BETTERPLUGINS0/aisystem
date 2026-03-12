package fr.xephi.authme.libs.net.kyori.adventure.translation;

import fr.xephi.authme.libs.net.kyori.adventure.internal.properties.AdventureProperties;
import java.util.Locale;
import java.util.function.Supplier;

final class TranslationLocales {
   private static final Supplier<Locale> GLOBAL;

   private TranslationLocales() {
   }

   static Locale global() {
      return (Locale)GLOBAL.get();
   }

   static {
      String property = (String)AdventureProperties.DEFAULT_TRANSLATION_LOCALE.value();
      if (property != null && !property.isEmpty()) {
         if (property.equals("system")) {
            GLOBAL = Locale::getDefault;
         } else {
            Locale locale = Translator.parseLocale(property);
            GLOBAL = () -> {
               return locale;
            };
         }
      } else {
         GLOBAL = () -> {
            return Locale.US;
         };
      }

   }
}
