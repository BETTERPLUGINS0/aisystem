package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.match;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.TagInternals;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.PreProcess;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public final class StringResolvingMatchedTokenConsumer extends MatchedTokenConsumer<String> {
   private final StringBuilder builder;
   private final TokenParser.TagProvider tagProvider;

   public StringResolvingMatchedTokenConsumer(@NotNull final String input, @NotNull final TokenParser.TagProvider tagProvider) {
      super(input);
      this.builder = new StringBuilder(input.length());
      this.tagProvider = tagProvider;
   }

   public void accept(final int start, final int end, @NotNull final TokenType tokenType) {
      super.accept(start, end, tokenType);
      if (tokenType != TokenType.OPEN_TAG) {
         this.builder.append(this.input, start, end);
      } else {
         String match = this.input.substring(start, end);
         String cleanup = this.input.substring(start + 1, end - 1);
         int index = cleanup.indexOf(58);
         String tag = index == -1 ? cleanup : cleanup.substring(0, index);
         if (TagInternals.sanitizeAndCheckValidTagName(tag)) {
            List<Token> tokens = TokenParser.tokenize(match, false);
            List<TagPart> parts = new ArrayList();
            List<Token> childs = tokens.isEmpty() ? null : ((Token)tokens.get(0)).childTokens();
            if (childs != null) {
               for(int i = 1; i < childs.size(); ++i) {
                  parts.add(new TagPart(match, (Token)childs.get(i), this.tagProvider));
               }
            }

            Tag replacement = this.tagProvider.resolve(TokenParser.TagProvider.sanitizePlaceholderName(tag), parts, (Token)tokens.get(0));
            if (replacement instanceof PreProcess) {
               this.builder.append((String)Objects.requireNonNull(((PreProcess)replacement).value(), "PreProcess replacements cannot return null"));
               return;
            }
         }

         this.builder.append(match);
      }

   }

   @NotNull
   public String result() {
      return this.builder.toString();
   }
}
