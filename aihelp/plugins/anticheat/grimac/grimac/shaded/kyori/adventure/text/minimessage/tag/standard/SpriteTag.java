package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.key.Key;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ObjectComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.object.ObjectContents;
import ac.grim.grimac.shaded.kyori.adventure.text.object.SpriteObjectContents;

final class SpriteTag {
   private static final String SPRITE = "sprite";
   static final TagResolver RESOLVER = SerializableResolver.claimingComponent("sprite", SpriteTag::create, SpriteTag::claimComponent);

   private SpriteTag() {
   }

   static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
      String firstArg = args.popOr("An atlas id and or a sprite id is required to produce a sprite object component").value();
      String secondArg = args.hasNext() ? args.pop().value() : null;
      return secondArg == null ? Tag.selfClosingInserting((Component)Component.object((ObjectContents)ObjectContents.sprite(Key.key(firstArg)))) : Tag.selfClosingInserting((Component)Component.object((ObjectContents)ObjectContents.sprite(Key.key(firstArg), Key.key(secondArg))));
   }

   @Nullable
   static Emitable claimComponent(final Component input) {
      if (!(input instanceof ObjectComponent)) {
         return null;
      } else {
         ObjectContents contents = ((ObjectComponent)input).contents();
         if (!(contents instanceof SpriteObjectContents)) {
            return null;
         } else {
            SpriteObjectContents sprite = (SpriteObjectContents)contents;
            Key atlas = sprite.atlas();
            Key key = sprite.sprite();
            return atlas == SpriteObjectContents.DEFAULT_ATLAS ? (emit) -> {
               emit.tag("sprite").argument(key.asString());
            } : (emit) -> {
               emit.tag("sprite").argument(atlas.asString()).argument(key.asString());
            };
         }
      }
   }
}
