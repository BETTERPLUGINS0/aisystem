package fr.xephi.authme.libs.net.kyori.adventure.text.serializer.json;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.event.HoverEvent;
import fr.xephi.authme.libs.net.kyori.adventure.util.Codec;
import java.io.IOException;
import org.jetbrains.annotations.NotNull;

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
