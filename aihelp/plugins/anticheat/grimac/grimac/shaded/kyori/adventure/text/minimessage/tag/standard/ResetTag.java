package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.standard;

import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.ParserDirective;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class ResetTag {
   private static final String RESET = "reset";
   static final TagResolver RESOLVER;

   private ResetTag() {
   }

   static {
      RESOLVER = TagResolver.resolver("reset", ParserDirective.RESET);
   }
}
