package ac.grim.grimac.shaded.kyori.adventure.text.serializer.json;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.event.HoverEvent;
import ac.grim.grimac.shaded.kyori.adventure.util.Codec;
import java.io.IOException;

public interface LegacyHoverEventSerializer {
   @NotNull
   HoverEvent.ShowItem deserializeShowItem(@NotNull Component input) throws IOException;

   @NotNull
   Component serializeShowItem(@NotNull HoverEvent.ShowItem input) throws IOException;

   @NotNull
   HoverEvent.ShowEntity deserializeShowEntity(@NotNull Component input, Codec.Decoder<Component, String, ? extends RuntimeException> componentDecoder) throws IOException;

   @NotNull
   Component serializeShowEntity(@NotNull HoverEvent.ShowEntity input, Codec.Encoder<Component, String, ? extends RuntimeException> componentEncoder) throws IOException;
}
