package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

interface MappableResolver {
   boolean contributeToMap(@NotNull final Map<String, Tag> map);
}
