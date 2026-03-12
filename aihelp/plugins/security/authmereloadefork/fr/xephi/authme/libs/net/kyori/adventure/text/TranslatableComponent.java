package fr.xephi.authme.libs.net.kyori.adventure.text;

import fr.xephi.authme.libs.net.kyori.adventure.translation.Translatable;
import fr.xephi.authme.libs.net.kyori.examination.ExaminableProperty;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface TranslatableComponent extends BuildableComponent<TranslatableComponent, TranslatableComponent.Builder>, ScopedComponent<TranslatableComponent> {
   @NotNull
   String key();

   @Contract(
      pure = true
   )
   @NotNull
   default TranslatableComponent key(@NotNull final Translatable translatable) {
      return this.key(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey());
   }

   @Contract(
      pure = true
   )
   @NotNull
   TranslatableComponent key(@NotNull final String key);

   /** @deprecated */
   @Deprecated
   @NotNull
   List<Component> args();

   /** @deprecated */
   @Deprecated
   @Contract(
      pure = true
   )
   @NotNull
   default TranslatableComponent args(@NotNull final ComponentLike... args) {
      return this.arguments(args);
   }

   /** @deprecated */
   @Deprecated
   @Contract(
      pure = true
   )
   @NotNull
   default TranslatableComponent args(@NotNull final List<? extends ComponentLike> args) {
      return this.arguments(args);
   }

   @NotNull
   List<TranslationArgument> arguments();

   @Contract(
      pure = true
   )
   @NotNull
   TranslatableComponent arguments(@NotNull final ComponentLike... args);

   @Contract(
      pure = true
   )
   @NotNull
   TranslatableComponent arguments(@NotNull final List<? extends ComponentLike> args);

   @Nullable
   String fallback();

   @Contract(
      pure = true
   )
   @NotNull
   TranslatableComponent fallback(@Nullable final String fallback);

   @NotNull
   default Stream<? extends ExaminableProperty> examinableProperties() {
      return Stream.concat(Stream.of(ExaminableProperty.of("key", this.key()), ExaminableProperty.of("arguments", (Object)this.arguments()), ExaminableProperty.of("fallback", this.fallback())), BuildableComponent.super.examinableProperties());
   }

   public interface Builder extends ComponentBuilder<TranslatableComponent, TranslatableComponent.Builder> {
      @Contract(
         pure = true
      )
      @NotNull
      default TranslatableComponent.Builder key(@NotNull final Translatable translatable) {
         return this.key(((Translatable)Objects.requireNonNull(translatable, "translatable")).translationKey());
      }

      @Contract("_ -> this")
      @NotNull
      TranslatableComponent.Builder key(@NotNull final String key);

      /** @deprecated */
      @Deprecated
      @Contract("_ -> this")
      @NotNull
      default TranslatableComponent.Builder args(@NotNull final ComponentBuilder<?, ?> arg) {
         return this.arguments(arg);
      }

      /** @deprecated */
      @Deprecated
      @Contract("_ -> this")
      @NotNull
      default TranslatableComponent.Builder args(@NotNull final ComponentBuilder<?, ?>... args) {
         return this.arguments((ComponentLike[])args);
      }

      /** @deprecated */
      @Deprecated
      @Contract("_ -> this")
      @NotNull
      default TranslatableComponent.Builder args(@NotNull final Component arg) {
         return this.arguments(arg);
      }

      /** @deprecated */
      @Deprecated
      @Contract("_ -> this")
      @NotNull
      default TranslatableComponent.Builder args(@NotNull final ComponentLike... args) {
         return this.arguments(args);
      }

      /** @deprecated */
      @Deprecated
      @Contract("_ -> this")
      @NotNull
      default TranslatableComponent.Builder args(@NotNull final List<? extends ComponentLike> args) {
         return this.arguments(args);
      }

      @Contract("_ -> this")
      @NotNull
      TranslatableComponent.Builder arguments(@NotNull final ComponentLike... args);

      @Contract("_ -> this")
      @NotNull
      TranslatableComponent.Builder arguments(@NotNull final List<? extends ComponentLike> args);

      @Contract("_ -> this")
      @NotNull
      TranslatableComponent.Builder fallback(@Nullable final String fallback);
   }
}
