package ac.grim.grimac.shaded.kyori.adventure.text.format;

import ac.grim.grimac.shaded.jetbrains.annotations.ApiStatus;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.event.ClickEvent;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEventSource;
import ac.grim.grimac.shaded.kyori.adventure.util.ARGBLike;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ApiStatus.NonExtendable
public interface StyleSetter<T extends StyleSetter<?>> {
   @NotNull
   T font(@Nullable final Key font);

   @NotNull
   T color(@Nullable final TextColor color);

   @NotNull
   T colorIfAbsent(@Nullable final TextColor color);

   @NotNull
   T shadowColor(@Nullable final ARGBLike argb);

   @NotNull
   T shadowColorIfAbsent(@Nullable final ARGBLike argb);

   @NotNull
   default T decorate(@NotNull final TextDecoration decoration) {
      return this.decoration(decoration, TextDecoration.State.TRUE);
   }

   @NotNull
   default T decorate(@NotNull final TextDecoration... decorations) {
      Map<TextDecoration, TextDecoration.State> map = new EnumMap(TextDecoration.class);
      int i = 0;

      for(int length = decorations.length; i < length; ++i) {
         map.put(decorations[i], TextDecoration.State.TRUE);
      }

      return this.decorations(map);
   }

   @NotNull
   default T decoration(@NotNull final TextDecoration decoration, final boolean flag) {
      return this.decoration(decoration, TextDecoration.State.byBoolean(flag));
   }

   @NotNull
   T decoration(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state);

   @NotNull
   T decorationIfAbsent(@NotNull final TextDecoration decoration, @NotNull final TextDecoration.State state);

   @NotNull
   T decorations(@NotNull final Map<TextDecoration, TextDecoration.State> decorations);

   @NotNull
   default T decorations(@NotNull final Set<TextDecoration> decorations, final boolean flag) {
      return this.decorations((Map)decorations.stream().collect(Collectors.toMap(Function.identity(), (decoration) -> {
         return TextDecoration.State.byBoolean(flag);
      })));
   }

   @NotNull
   T clickEvent(@Nullable final ClickEvent event);

   @NotNull
   T hoverEvent(@Nullable final HoverEventSource<?> source);

   @NotNull
   T insertion(@Nullable final String insertion);
}
