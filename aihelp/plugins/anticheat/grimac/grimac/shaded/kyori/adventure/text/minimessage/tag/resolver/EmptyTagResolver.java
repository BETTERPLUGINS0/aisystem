package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import java.util.Map;

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
