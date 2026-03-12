package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.node;

import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import ac.grim.grimac.shaded.jetbrains.annotations.Nullable;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.Token;
import ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser.TokenParser;

public final class TextNode extends ValueNode {
   private static boolean isEscape(final int escape) {
      return escape == 60 || escape == 92;
   }

   public TextNode(@Nullable final ElementNode parent, @NotNull final Token token, @NotNull final String sourceMessage) {
      super(parent, token, sourceMessage, TokenParser.unescape(sourceMessage, token.startIndex(), token.endIndex(), TextNode::isEscape));
   }

   String valueName() {
      return "TextNode";
   }
}
