package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.Context;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

final class EmptyTagResolver implements TagResolver, MappableResolver, SerializableResolver {
   static final EmptyTagResolver INSTANCE = new EmptyTagResolver();

   private EmptyTagResolver() {
   }

   @Nullable
   public Tag resolve(@NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) {
      return null;
   }

   public boolean has(@NotNull final String name) {
      return false;
   }

   public boolean contributeToMap(@NotNull final Map<String, Tag> map) {
      return true;
   }

   public void handle(@NotNull final Component serializable, @NotNull final ClaimConsumer consumer) {
   }
}
