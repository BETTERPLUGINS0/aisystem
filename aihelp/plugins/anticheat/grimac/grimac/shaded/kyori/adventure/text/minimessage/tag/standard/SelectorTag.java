package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.Component;
import ac.grim.grimac.shaded.kyori.adventure.text.ComponentLike;
import ac.grim.grimac.shaded.kyori.adventure.text.SelectorComponent;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.Context;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.ParsingException;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.Tag;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class SelectorTag {
   private static final String SEL = "sel";
   private static final String SELECTOR = "selector";
   static final TagResolver RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("sel", "selector"), SelectorTag::create, SelectorTag::claim);

   private SelectorTag() {
   }

   static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
      String key = args.popOr("A selection key is required").value();
      ComponentLike separator = null;
      if (args.hasNext()) {
         separator = ctx.deserialize(args.pop().value());
      }

      return Tag.inserting((Component)Component.selector(key, separator));
   }

   @Nullable
   static Emitable claim(final Component input) {
      if (!(input instanceof SelectorComponent)) {
         return null;
      } else {
         SelectorComponent st = (SelectorComponent)input;
         return (emit) -> {
            emit.tag("sel");
            emit.argument(st.pattern());
            if (st.separator() != null) {
               emit.argument(st.separator());
            }

         };
      }
   }
}
