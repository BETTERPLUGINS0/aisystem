package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.KeybindComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

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
