package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public interface ComponentSerializer<I extends Component, O extends Component, R> extends ComponentEncoder<I, R>, ComponentDecoder<R, O> {
   @NotNull
   O deserialize(@NotNull final R input);

   /** @deprecated */
   @Deprecated
   @ApiStatus.ScheduledForRemoval(
      inVersion = "5.0.0"
   )
   @Contract(
      value = "!null -> !null; null -> null",
      pure = true
   )
   @Nullable
   default O deseializeOrNull(@Nullable final R input) {
      return ComponentDecoder.super.deserializeOrNull(input);
   }

   @Contract(
      value = "!null -> !null; null -> null",
      pure = true
   )
   @Nullable
   default O deserializeOrNull(@Nullable final R input) {
      return ComponentDecoder.super.deserializeOr(input, (Component)null);
   }

   @Contract(
      value = "!null, _ -> !null; null, _ -> param2",
      pure = true
   )
   @Nullable
   default O deserializeOr(@Nullable final R input, @Nullable final O fallback) {
      return ComponentDecoder.super.deserializeOr(input, fallback);
   }

   @NotNull
   R serialize(@NotNull final I component);

   @Contract(
      value = "!null -> !null; null -> null",
      pure = true
   )
   @Nullable
   default R serializeOrNull(@Nullable final I component) {
      return this.serializeOr(component, (Object)null);
   }

   @Contract(
      value = "!null, _ -> !null; null, _ -> param2",
      pure = true
   )
   @Nullable
   default R serializeOr(@Nullable final I component, @Nullable final R fallback) {
      return component == null ? fallback : this.serialize(component);
   }
}
