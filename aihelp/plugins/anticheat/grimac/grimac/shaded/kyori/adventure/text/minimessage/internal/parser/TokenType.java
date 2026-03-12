package ac.grim.grimac.shaded.kyori.adventure.text.minimessage.internal.parser;

public enum TokenType {
   TEXT,
   OPEN_TAG,
   OPEN_CLOSE_TAG,
   CLOSE_TAG,
   TAG_VALUE;

   // $FF: synthetic method
   private static TokenType[] $values() {
      return new TokenType[]{TEXT, OPEN_TAG, OPEN_CLOSE_TAG, CLOSE_TAG, TAG_VALUE};
   }
}
