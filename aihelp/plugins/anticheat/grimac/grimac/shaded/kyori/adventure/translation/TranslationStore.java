package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;

public interface TranslationStore<T> extends Translator {
   @NotNull
   static TranslationStore<Component> component(@NotNull final Key name) {
      return new ComponentTranslationStore((Key)Objects.requireNonNull(name, "name"));
   }

   @NotNull
   static TranslationStore.StringBased<MessageFormat> messageFormat(@NotNull final Key name) {
      return new MessageFormatTranslationStore((Key)Objects.requireNonNull(name, "name"));
   }

   boolean contains(@NotNull final String key);

   boolean contains(@NotNull final String key, @NotNull final Locale locale);

   default boolean canTranslate(@NotNull final String key, @NotNull final Locale locale) {
      return Translator.super.canTranslate(key, locale);
   }

   void defaultLocale(@NotNull final Locale locale);

   void register(@NotNull final String key, @NotNull final Locale locale, final T translation);

   void registerAll(@NotNull final Locale locale, @NotNull final Map<String, T> translations);

   void registerAll(@NotNull final Locale locale, @NotNull final Set<String> keys, Function<String, T> function);

   void unregister(@NotNull final String key);

   public interface StringBased<T> extends TranslationStore<T> {
      void registerAll(@NotNull final Locale locale, @NotNull final Path path, final boolean escapeSingleQuotes);

      void registerAll(@NotNull final Locale locale, @NotNull final ResourceBundle bundle, final boolean escapeSingleQuotes);
   }
}
