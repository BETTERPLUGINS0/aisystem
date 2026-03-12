package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.standard;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.Context;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.ParsingException;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;

final class NewlineTag {
   private static final String BR = "br";
   private static final String NEWLINE = "newline";
   static final TagResolver RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("newline", "br"), NewlineTag::create, NewlineTag::claimComponent);

   private NewlineTag() {
   }

   static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
      return Tag.selfClosingInserting((Component)Component.newline());
   }

   @Nullable
   static Emitable claimComponent(final Component input) {
      return Component.newline().equals(input) ? (emit) -> {
         emit.selfClosingTag("br");
      } : null;
   }
}
