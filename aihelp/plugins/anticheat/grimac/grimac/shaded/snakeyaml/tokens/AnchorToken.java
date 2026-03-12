package ac.grim.grimac.shaded.snakeyaml.tokens;

import ac.grim.grimac.shaded.snakeyaml.error.Mark;

public final class AnchorToken extends Token {
   private final String value;

   public AnchorToken(String value, Mark startMark, Mark endMark) {
      super(startMark, endMark);
      this.value = value;
   }

   public String getValue() {
      return this.value;
   }

   public Token.ID getTokenId() {
      return Token.ID.Anchor;
   }
}
