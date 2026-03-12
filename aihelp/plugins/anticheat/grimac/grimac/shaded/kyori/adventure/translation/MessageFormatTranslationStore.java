package ac.grim.grimac.shaded.kyori.adventure.translation;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import java.text.MessageFormat;
import java.util.Locale;

final class MessageFormatTranslationStore extends AbstractTranslationStore.StringBased<MessageFormat> implements TranslationRegistry {
   MessageFormatTranslationStore(final Key name) {
      super(name);
   }

   @NotNull
   protected MessageFormat parse(@NotNull final String string, @NotNull final Locale locale) {
      return new MessageFormat(string, locale);
   }

   @Nullable
   public MessageFormat translate(@NotNull final String key, @NotNull final Locale locale) {
      return (MessageFormat)this.translationValue(key, locale);
   }
}
