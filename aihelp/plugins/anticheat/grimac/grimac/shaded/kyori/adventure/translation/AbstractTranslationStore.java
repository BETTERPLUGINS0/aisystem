package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.internal.Internals;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.util.TriState;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import ac.grim.grimac.shaded.kyori.examination.ExaminableProperty;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public abstract class AbstractTranslationStore<T> implements Examinable, TranslationStore<T> {
   @NotNull
   private final Key name;
   private final Map<String, AbstractTranslationStore<T>.Translation> translations = new ConcurrentHashMap();
   @NotNull
   private volatile Locale defaultLocale;

   protected AbstractTranslationStore(@NotNull final Key name) {
      this.defaultLocale = Locale.US;
      this.name = (Key)Objects.requireNonNull(name, "name");
   }

   @Nullable
   protected T translationValue(@NotNull final String key, @NotNull final Locale locale) {
      AbstractTranslationStore<T>.Translation translation = (AbstractTranslationStore.Translation)this.translations.get(Objects.requireNonNull(key, "key"));
      return translation == null ? null : translation.translate((Locale)Objects.requireNonNull(locale, "locale"));
   }

   public final boolean contains(@NotNull final String key) {
      return this.translations.containsKey(key);
   }

   public final boolean contains(@NotNull final String key, @NotNull final Locale locale) {
      AbstractTranslationStore<T>.Translation translation = (AbstractTranslationStore.Translation)this.translations.get(Objects.requireNonNull(key, "key"));
      if (translation == null) {
         return false;
      } else {
         return translation.translations.get(Objects.requireNonNull(locale, "locale")) != null;
      }
   }

   public final boolean canTranslate(@NotNull final String key, @NotNull final Locale locale) {
      AbstractTranslationStore<T>.Translation translation = (AbstractTranslationStore.Translation)this.translations.get(Objects.requireNonNull(key, "key"));
      if (translation == null) {
         return false;
      } else {
         return translation.translate((Locale)Objects.requireNonNull(locale, "locale")) != null;
      }
   }

   public final void defaultLocale(@NotNull final Locale locale) {
      this.defaultLocale = (Locale)Objects.requireNonNull(locale, "locale");
   }

   public final void register(@NotNull final String key, @NotNull final Locale locale, @NotNull final T translation) {
      ((AbstractTranslationStore.Translation)this.translations.computeIfAbsent(key, (x$0) -> {
         return new AbstractTranslationStore.Translation(x$0);
      })).register(locale, translation);
   }

   public final void registerAll(@NotNull final Locale locale, @NotNull final Map<String, T> translations) {
      Set var10002 = translations.keySet();
      Objects.requireNonNull(translations);
      this.registerAll(locale, var10002, translations::get);
   }

   public final void registerAll(@NotNull final Locale locale, @NotNull final Set<String> keys, final Function<String, T> function) {
      IllegalArgumentException firstError = null;
      int errorCount = 0;
      Iterator var6 = keys.iterator();

      while(var6.hasNext()) {
         String key = (String)var6.next();

         try {
            this.register(key, locale, function.apply(key));
         } catch (IllegalArgumentException var9) {
            if (firstError == null) {
               firstError = var9;
            }

            ++errorCount;
         }
      }

      if (firstError != null) {
         if (errorCount == 1) {
            throw firstError;
         }

         if (errorCount > 1) {
            throw new IllegalArgumentException(String.format("Invalid key (and %d more)", errorCount - 1), firstError);
         }
      }

   }

   public final void unregister(@NotNull final String key) {
      this.translations.remove(key);
   }

   @NotNull
   public final Key name() {
      return this.name;
   }

   @NotNull
   public final TriState hasAnyTranslations() {
      return TriState.byBoolean(!this.translations.isEmpty());
   }

   @NotNull
   public final Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.of(ExaminableProperty.of("translations", (Object)this.translations));
   }

   public final boolean equals(final Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof AbstractTranslationStore)) {
         return false;
      } else {
         AbstractTranslationStore<?> that = (AbstractTranslationStore)other;
         return this.name.equals(that.name);
      }
   }

   public final int hashCode() {
      return this.name.hashCode();
   }

   @NotNull
   public final String toString() {
      return Internals.toString(this);
   }

   private final class Translation implements Examinable {
      private final String key;
      private final Map<Locale, T> translations;

      private Translation(@NotNull final String key) {
         this.key = (String)Objects.requireNonNull(key, "key");
         this.translations = new ConcurrentHashMap();
      }

      @Nullable
      private T translate(@NotNull final Locale locale) {
         T format = this.translations.get(Objects.requireNonNull(locale, "locale"));
         if (format == null) {
            format = this.translations.get(new Locale(locale.getLanguage()));
            if (format == null) {
               format = this.translations.get(AbstractTranslationStore.this.defaultLocale);
               if (format == null) {
                  format = this.translations.get(TranslationLocales.global());
               }
            }
         }

         return format;
      }

      private void register(@NotNull final Locale locale, @NotNull final T translation) {
         if (this.translations.putIfAbsent((Locale)Objects.requireNonNull(locale, "locale"), Objects.requireNonNull(translation, "translation")) != null) {
            throw new IllegalArgumentException(String.format("Translation already exists: %s for %s", this.key, locale));
         }
      }

      @NotNull
      public Stream<? extends ExaminableProperty> examinableProperties() {
         return Stream.of(ExaminableProperty.of("key", this.key), ExaminableProperty.of("translations", (Object)this.translations));
      }

      public boolean equals(final Object other) {
         if (this == other) {
            return true;
         } else if (!(other instanceof AbstractTranslationStore.Translation)) {
            return false;
         } else {
            AbstractTranslationStore<?>.Translation that = (AbstractTranslationStore.Translation)other;
            return this.key.equals(that.key) && this.translations.equals(that.translations);
         }
      }

      public int hashCode() {
         return Objects.hash(new Object[]{this.key, this.translations});
      }

      public String toString() {
         return Internals.toString(this);
      }

      // $FF: synthetic method
      Translation(String x1, Object x2) {
         this(x1);
      }
   }

   public abstract static class StringBased<T> extends AbstractTranslationStore<T> implements TranslationStore.StringBased<T> {
      private static final Pattern SINGLE_QUOTE_PATTERN = Pattern.compile("'");

      protected StringBased(@NotNull final Key name) {
         super(name);
      }

      @NotNull
      protected abstract T parse(@NotNull final String string, @NotNull final Locale locale);

      public final void registerAll(@NotNull final Locale locale, @NotNull final Path path, final boolean escapeSingleQuotes) {
         try {
            BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);

            try {
               this.registerAll(locale, (ResourceBundle)(new PropertyResourceBundle(reader)), escapeSingleQuotes);
            } catch (Throwable var8) {
               if (reader != null) {
                  try {
                     reader.close();
                  } catch (Throwable var7) {
                     var8.addSuppressed(var7);
                  }
               }

               throw var8;
            }

            if (reader != null) {
               reader.close();
            }
         } catch (IOException var9) {
         }

      }

      public final void registerAll(@NotNull final Locale locale, @NotNull final ResourceBundle bundle, final boolean escapeSingleQuotes) {
         this.registerAll(locale, bundle.keySet(), (key) -> {
            String format = bundle.getString(key);
            return this.parse(escapeSingleQuotes ? SINGLE_QUOTE_PATTERN.matcher(format).replaceAll("''") : format, locale);
         });
      }
   }
}
