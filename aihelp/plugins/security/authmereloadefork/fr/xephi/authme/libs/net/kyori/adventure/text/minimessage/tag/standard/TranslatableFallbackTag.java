package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.standard;

import fr.xephi.authme.libs.net.kyori.adventure.text.Component;
import fr.xephi.authme.libs.net.kyori.adventure.text.TranslatableComponent;
import fr.xephi.authme.libs.net.kyori.adventure.text.TranslationArgument;
import fr.xephi.authme.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.Context;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.ParsingException;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.jetbrains.annotations.Nullable;

final class TranslatableFallbackTag {
   private static final String TR_OR = "tr_or";
   private static final String TRANSLATE_OR = "translate_or";
   private static final String LANG_OR = "lang_or";
   static final TagResolver RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("lang_or", "translate_or", "tr_or"), TranslatableFallbackTag::create, TranslatableFallbackTag::claim);

   private TranslatableFallbackTag() {
   }

   static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
      String key = args.popOr("A translation key is required").value();
      String fallback = args.popOr("A fallback messages is required").value();
      Object with;
      if (args.hasNext()) {
         with = new ArrayList();

         while(args.hasNext()) {
            ((List)with).add(ctx.deserialize(args.pop().value()));
         }
      } else {
         with = Collections.emptyList();
      }

      return Tag.inserting((Component)Component.translatable((String)key, (String)fallback, (List)with, (StyleBuilderApplicable[])()));
   }

   @Nullable
   static Emitable claim(final Component input) {
      if (input instanceof TranslatableComponent && ((TranslatableComponent)input).fallback() != null) {
         TranslatableComponent tr = (TranslatableComponent)input;
         return (emit) -> {
            emit.tag("lang_or");
            emit.argument(tr.key());
            emit.argument(tr.fallback());
            Iterator var2 = tr.arguments().iterator();

            while(var2.hasNext()) {
               TranslationArgument with = (TranslationArgument)var2.next();
               emit.argument(with.asComponent());
            }

         };
      } else {
         return null;
      }
   }
}
