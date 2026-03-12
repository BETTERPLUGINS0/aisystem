package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

/** @deprecated */
@Deprecated
public interface TranslationRegistry extends Translator, TranslationStore.StringBased<MessageFormat> {
   /** @deprecated */
   @Deprecated
   Pattern SINGLE_QUOTE_PATTERN = Pattern.compile("'");

   /** @deprecated */
   @Deprecated
   @NotNull
   static TranslationRegistry create(final Key name) {
      return new MessageFormatTranslationStore((Key)Objects.requireNonNull(name, "name"));
   }

   /** @deprecated */
   @Deprecated
   boolean contains(@NotNull final String key);

   /** @deprecated */
   @Deprecated
   @Nullable
   MessageFormat translate(@NotNull final String key, @NotNull final Locale locale);

   /** @deprecated */
   @Deprecated
   void defaultLocale(@NotNull final Locale locale);

   /** @deprecated */
   @Deprecated
   void register(@NotNull final String key, @NotNull final Locale locale, @NotNull final MessageFormat format);

   /** @deprecated */
   @Deprecated
   default void registerAll(@NotNull final Locale locale, @NotNull final Map<String, MessageFormat> formats) {
      Set var10002 = formats.keySet();
      Objects.requireNonNull(formats);
      this.registerAll(locale, var10002, formats::get);
   }

   /** @deprecated */
   @Deprecated
   default void registerAll(@NotNull final Locale locale, @NotNull final Path path, final boolean escapeSingleQuotes) {
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

   /** @deprecated */
   @Deprecated
   default void registerAll(@NotNull final Locale locale, @NotNull final ResourceBundle bundle, final boolean escapeSingleQuotes) {
      this.registerAll(locale, bundle.keySet(), (key) -> {
         String format = bundle.getString(key);
         return new MessageFormat(escapeSingleQuotes ? SINGLE_QUOTE_PATTERN.matcher(format).replaceAll("''") : format, locale);
      });
   }

   /** @deprecated */
   @Deprecated
   default void registerAll(@NotNull final Locale locale, @NotNull final Set<String> keys, final Function<String, MessageFormat> function) {
      IllegalArgumentException firstError = null;
      int errorCount = 0;
      Iterator var6 = keys.iterator();

      while(var6.hasNext()) {
         String key = (String)var6.next();

         try {
            this.register(key, locale, (MessageFormat)function.apply(key));
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

   /** @deprecated */
   @Deprecated
   void unregister(@NotNull final String key);
}
