package ac.grim.grimac.shaded.kyori.adventure.text;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.examination.Examinable;
import java.util.Objects;

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
