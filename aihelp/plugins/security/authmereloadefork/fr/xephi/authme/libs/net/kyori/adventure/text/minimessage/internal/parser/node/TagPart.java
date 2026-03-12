package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import org.jetbrains.annotations.NotNull;

public final class TagPart implements Tag.Argument {
   private final String value;
   private final Token token;

   public TagPart(@NotNull final String sourceMessage, @NotNull final Token token, @NotNull final TokenParser.TagProvider tagResolver) {
      String v = unquoteAndEscape(sourceMessage, token.startIndex(), token.endIndex());
      v = TokenParser.resolvePreProcessTags(v, tagResolver);
      this.value = v;
      this.token = token;
   }

   @NotNull
   public String value() {
      return this.value;
   }

   @NotNull
   public Token token() {
      return this.token;
   }

   @NotNull
   public static String unquoteAndEscape(@NotNull final String text, final int start, final int end) {
      if (start == end) {
         return "";
      } else {
         int endIndex = end;
         char firstChar = text.charAt(start);
         char lastChar = text.charAt(end - 1);
         if (firstChar != '\'' && firstChar != '"') {
            return text.substring(start, end);
         } else {
            int startIndex = start + 1;
            if (lastChar == '\'' || lastChar == '"') {
               endIndex = end - 1;
            }

            return startIndex > endIndex ? text.substring(start, end) : TokenParser.unescape(text, startIndex, endIndex, (i) -> {
               return i == firstChar || i == 92;
            });
         }
      }
   }

   public String toString() {
      return this.value;
   }
}
