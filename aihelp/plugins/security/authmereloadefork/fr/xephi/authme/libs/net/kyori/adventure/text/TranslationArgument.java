package fr.xephi.authme.libs.net.kyori.adventure.text;

import fr.xephi.authme.libs.net.kyori.examination.Examinable;
import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.NonExtendable
public interface TranslationArgument extends TranslationArgumentLike, Examinable {
   @NotNull
   static TranslationArgument bool(final boolean value) {
      return new TranslationArgumentImpl(value);
   }

   @NotNull
   static TranslationArgument numeric(@NotNull final Number value) {
      return new TranslationArgumentImpl(Objects.requireNonNull(value, "value"));
   }

   @NotNull
   static TranslationArgument component(@NotNull final ComponentLike value) {
      return (TranslationArgument)(value instanceof TranslationArgumentLike ? ((TranslationArgumentLike)value).asTranslationArgument() : new TranslationArgumentImpl(Objects.requireNonNull(((ComponentLike)Objects.requireNonNull(value, "value")).asComponent(), "value.asComponent()")));
   }

   @NotNull
   Object value();

   @NotNull
   default TranslationArgument asTranslationArgument() {
      return this;
   }
}
