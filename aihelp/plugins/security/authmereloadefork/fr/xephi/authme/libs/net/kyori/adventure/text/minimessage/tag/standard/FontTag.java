package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.standard;

import fr.xephi.authme.libs.net.kyori.adventure.key.InvalidKeyException;
import fr.xephi.authme.libs.net.kyori.adventure.key.Key;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.Style;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.Context;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.ParsingException;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class FontTag {
   static final String FONT = "font";
   static final TagResolver RESOLVER = SerializableResolver.claimingStyle("font", FontTag::create, StyleClaim.claim("font", Style::font, FontTag::emit));

   private FontTag() {
   }

   static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
      String valueOrNamespace = args.popOr("A font tag must have either arguments of either <value> or <namespace:value>").value();

      Key font;
      try {
         if (!args.hasNext()) {
            font = Key.key(valueOrNamespace);
         } else {
            String fontKey = args.pop().value();
            font = Key.key(valueOrNamespace, fontKey);
         }
      } catch (InvalidKeyException var5) {
         throw ctx.newException(var5.getMessage(), args);
      }

      return Tag.styling((builder) -> {
         builder.font(font);
      });
   }

   static void emit(final Key font, final TokenEmitter emitter) {
      emitter.tag("font");
      if (font.namespace().equals("minecraft")) {
         emitter.argument(font.value());
      } else {
         emitter.arguments(font.namespace(), font.value());
      }

   }
}
