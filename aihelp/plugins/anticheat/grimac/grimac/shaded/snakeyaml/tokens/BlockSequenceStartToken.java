package ac.grim.grimac.shaded.snakeyaml.tokens;

import ac.grim.grimac.shaded.snakeyaml.error.Mark;

public final class BlockSequenceStartToken extends Token {
   public BlockSequenceStartToken(Mark startMark, Mark endMark) {
      super(startMark, endMark);
   }

   public Token.ID getTokenId() {
      return Token.ID.BlockSequenceStart;
   }
}
