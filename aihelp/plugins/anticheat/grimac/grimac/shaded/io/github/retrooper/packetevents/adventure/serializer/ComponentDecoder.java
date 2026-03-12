package ac.grim.grimac.shaded.io.github.retrooper.packetevents.adventure.serializer;

import ac.grim.grimac.shaded.jetbrains.annotations.Contract;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;

public interface ComponentDecoder<S, O extends Component> {
   @NotNull
   O deserialize(@NotNull final S input);

   @Contract(
      value = "!null -> !null; null -> null",
      pure = true
   )
   @Nullable
   default O deserializeOrNull(@Nullable final S input) {
      return this.deserializeOr(input, (Component)null);
   }

   @Contract(
      value = "!null, _ -> !null; null, _ -> param2",
      pure = true
   )
   @Nullable
   default O deserializeOr(@Nullable final S input, @Nullable final O fallback) {
      return input == null ? fallback : this.deserialize(input);
   }
}
