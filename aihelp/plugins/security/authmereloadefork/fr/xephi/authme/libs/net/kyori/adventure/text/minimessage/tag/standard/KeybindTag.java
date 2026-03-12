package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.standard;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.KeybindComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.Context;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.ParsingException;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

final class KeybindTag {
   public static final String KEYBIND = "key";
   static final TagResolver RESOLVER = SerializableResolver.claimingComponent("key", KeybindTag::create, KeybindTag::emit);

   private KeybindTag() {
   }

   static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
      return Tag.inserting((Component)Component.keybind(args.popOr("A keybind id is required").value()));
   }

   @Nullable
   static Emitable emit(final Component component) {
      if (!(component instanceof KeybindComponent)) {
         return null;
      } else {
         String key = ((KeybindComponent)component).keybind();
         return (emit) -> {
            emit.tag("key").argument(key);
         };
      }
   }
}
