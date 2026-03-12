package fr.xephi.authme.libs.net.kyori.adventure.translation;

import fr.xephi.authme.libs.net.kyori.adventure.internal.Internals;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.util.TriState;
import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class TranslationRegistryImpl implements Examinable, TranslationRegistry {
   private final Key name;
   private final Map<String, TranslationRegistryImpl.Translation> translations = new ConcurrentHashMap();
   private Locale defaultLocale;

   TranslationRegistryImpl(final Key name) {
      this.defaultLocale = Locale.US;
      this.name = name;
   }

   public void register(@NotNull final String key, @NotNull final Locale locale, @NotNull final MessageFormat format) {
      ((TranslationRegistryImpl.Translation)this.translations.computeIfAbsent(key, (x$0) -> {
         return new TranslationRegistryImpl.Translation(x$0);
      })).register(locale, format);
   }

   public void unregister(@NotNull final String key) {
      this.translations.remove(key);
   }

   @NotNull
   public Key name() {
      return this.name;
   }

   public boolean contains(@NotNull final String key) {
      return this.translations.containsKey(key);
   }

   @NotNull
   public TriState hasAnyTranslations() {
      return !this.translations.isEmpty() ? TriState.TRUE : TriState.FALSE;
   }

   @Nullable
   public MessageFormat translate(@NotNull final String key, @NotNull final Locale locale) {
      TranslationRegistryImpl.Translation translation = (TranslationRegistryImpl.Translation)this.translations.get(key);
      return translation == null ? null : translation.translate(locale);
   }

   public void defaultLocale(@NotNull final Locale defaultLocale) {
      this.defaultLocale = (Locale)Objects.requireNonNull(defaultLocale, "defaultLocale");
   }

   @NotNull
   public Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("translations", (Object)this.translations));
   }

   public boolean equals(final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof TranslationRegistryImpl)) {
         return false;
      } else {
         TranslationRegistryImpl that = (TranslationRegistryImpl)other;
         return this.name.equals(that.name) && this.translations.equals(that.translations) && this.defaultLocale.equals(that.defaultLocale);
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.name, this.translations, this.defaultLocale});
   }

   public String toString() {
      return Internals.toString(this);
   }

   final class Translation implements Examinable {
      private final String key;
      private final Map<Locale, MessageFormat> formats;

      Translation(@NotNull final String key) {
         this.key = (String)Objects.requireNonNull(key, "translation key");
         this.formats = new ConcurrentHashMap();
      }

      void register(@NotNull final Locale locale, @NotNull final MessageFormat format) {
         if (this.formats.putIfAbsent((Locale)Objects.requireNonNull(locale, "locale"), (MessageFormat)Objects.requireNonNull(format, "message format")) != null) {
            throw new IllegalArgumentException(String.format("Translation already exists: %s for %s", this.key, locale));
         }
      }

      @Nullable
      MessageFormat translate(@NotNull final Locale locale) {
         MessageFormat format = (MessageFormat)this.formats.get(Objects.requireNonNull(locale, "locale"));
         if (format == null) {
            format = (MessageFormat)this.formats.get(new Locale(locale.getLanguage()));
            if (format == null) {
               format = (MessageFormat)this.formats.get(TranslationRegistryImpl.this.defaultLocale);
               if (format == null) {
                  format = (MessageFormat)this.formats.get(TranslationLocales.global());
               }
            }
         }

         return format;
      }

      @NotNull
      public Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("key", this.key), ExaminableProperty.of("formats", (Object)this.formats));
      }

      public boolean equals(final Object other) {
         if (this == other) {
            return true;
         } else if (!(other instanceof TranslationRegistryImpl.Translation)) {
            return false;
         } else {
            TranslationRegistryImpl.Translation that = (TranslationRegistryImpl.Translation)other;
            return this.key.equals(that.key) && this.formats.equals(that.formats);
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.key, this.formats});
      }

      public String toString() {
         return Internals.toString(this);
      }
   }
}
