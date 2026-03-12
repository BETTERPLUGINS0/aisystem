package fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser;

import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.ParsingException;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.TagInternals;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.match.MatchedTokenConsumer;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.match.StringResolvingMatchedTokenConsumer;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.match.TokenListProducingMatchedTokenConsumer;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.RootNode;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TextNode;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Inserting;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.ParserDirective;
import fr.xephi.authme.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public final class TokenParser {
   private static final int MAX_DEPTH = 16;
   public static final char TAG_START = '<';
   public static final char TAG_END = '>';
   public static final char CLOSE_TAG = '/';
   public static final char SEPARATOR = ':';
   public static final char ESCAPE = '\\';

   private TokenParser() {
   }

   public static RootNode parse(@NotNull final TokenParser.TagProvider tagProvider, @NotNull final Predicate<String> tagNameChecker, @NotNull final String message, @NotNull final String originalMessage, final boolean strict) throws ParsingException {
      List<Token> tokens = tokenize(message, false);
      return buildTree(tagProvider, tagNameChecker, tokens, message, originalMessage, strict);
   }

   public static String resolvePreProcessTags(final String message, final TokenParser.TagProvider provider) {
      int passes = 0;
      String result = message;

      String lastResult;
      do {
         lastResult = result;
         StringResolvingMatchedTokenConsumer stringTokenResolver = new StringResolvingMatchedTokenConsumer(result, provider);
         parseString(result, false, stringTokenResolver);
         result = stringTokenResolver.result();
         ++passes;
      } while(passes < 16 && !lastResult.equals(result));

      return lastResult;
   }

   public static List<Token> tokenize(final String message, final boolean lenient) {
      TokenListProducingMatchedTokenConsumer listProducer = new TokenListProducingMatchedTokenConsumer(message);
      parseString(message, lenient, listProducer);
      List<Token> tokens = listProducer.result();
      parseSecondPass(message, tokens);
      return tokens;
   }

   public static void parseString(final String message, final boolean lenient, final MatchedTokenConsumer<?> consumer) {
      TokenParser.FirstPassState state = TokenParser.FirstPassState.NORMAL;
      boolean escaped = false;
      int currentTokenEnd = 0;
      int marker = -1;
      char currentStringChar = 0;
      int length = message.length();

      int i;
      for(i = 0; i < length; ++i) {
         int codePoint = message.codePointAt(i);
         int nextCodePoint;
         if (!lenient && codePoint == 167 && i + 1 < length) {
            nextCodePoint = Character.toLowerCase(message.codePointAt(i + 1));
            if (nextCodePoint >= 48 && nextCodePoint <= 57 || nextCodePoint >= 97 && nextCodePoint <= 102 || nextCodePoint == 114 || nextCodePoint >= 107 && nextCodePoint <= 111) {
               throw new ParsingExceptionImpl("Legacy formatting codes have been detected in a MiniMessage string - this is unsupported behaviour. Please refer to the Adventure documentation (https://docs.advntr.dev) for more information.", message, (Throwable)null, true, new Token[]{new Token(i, i + 2, TokenType.TEXT)});
            }
         }

         if (!Character.isBmpCodePoint(codePoint)) {
            ++i;
         }

         if (!escaped) {
            if (codePoint == 92 && i + 1 < message.length()) {
               nextCodePoint = message.codePointAt(i + 1);
               switch(state) {
               case NORMAL:
                  escaped = nextCodePoint == 60 || nextCodePoint == 92;
                  break;
               case STRING:
                  escaped = currentStringChar == nextCodePoint || nextCodePoint == 92;
                  break;
               case TAG:
                  if (nextCodePoint == 60) {
                     escaped = true;
                     state = TokenParser.FirstPassState.NORMAL;
                  }
               }

               if (escaped) {
                  continue;
               }
            }

            switch(state) {
            case NORMAL:
               if (codePoint == 60) {
                  marker = i;
                  state = TokenParser.FirstPassState.TAG;
               }
               break;
            case STRING:
               if (codePoint == currentStringChar) {
                  state = TokenParser.FirstPassState.TAG;
               }
               break;
            case TAG:
               switch(codePoint) {
               case 34:
               case 39:
                  currentStringChar = (char)codePoint;
                  if (message.indexOf(codePoint, i + 1) != -1) {
                     state = TokenParser.FirstPassState.STRING;
                  }
                  break;
               case 60:
                  marker = i;
                  break;
               case 62:
                  if (i == marker + 1) {
                     state = TokenParser.FirstPassState.NORMAL;
                  } else {
                     if (currentTokenEnd != marker) {
                        consumer.accept(currentTokenEnd, marker, TokenType.TEXT);
                     }

                     currentTokenEnd = i + 1;
                     TokenType thisType = TokenType.OPEN_TAG;
                     if (boundsCheck(message, marker, 1) && message.charAt(marker + 1) == '/') {
                        thisType = TokenType.CLOSE_TAG;
                     } else if (boundsCheck(message, marker, 2) && message.charAt(i - 1) == '/') {
                        thisType = TokenType.OPEN_CLOSE_TAG;
                     }

                     consumer.accept(marker, currentTokenEnd, thisType);
                     state = TokenParser.FirstPassState.NORMAL;
                  }
               }
            }

            if (i == length - 1 && state == TokenParser.FirstPassState.TAG) {
               i = marker;
               state = TokenParser.FirstPassState.NORMAL;
            }
         } else {
            escaped = false;
         }
      }

      i = consumer.lastEndIndex();
      if (i == -1) {
         consumer.accept(0, message.length(), TokenType.TEXT);
      } else if (i != message.length()) {
         consumer.accept(i, message.length(), TokenType.TEXT);
      }

   }

   private static void parseSecondPass(final String message, final List<Token> tokens) {
      Iterator var2 = tokens.iterator();

      while(true) {
         while(true) {
            Token token;
            TokenType type;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               token = (Token)var2.next();
               type = token.type();
            } while(type != TokenType.OPEN_TAG && type != TokenType.OPEN_CLOSE_TAG && type != TokenType.CLOSE_TAG);

            int startIndex = type == TokenType.CLOSE_TAG ? token.startIndex() + 2 : token.startIndex() + 1;
            int endIndex = type == TokenType.OPEN_CLOSE_TAG ? token.endIndex() - 2 : token.endIndex() - 1;
            TokenParser.SecondPassState state = TokenParser.SecondPassState.NORMAL;
            boolean escaped = false;
            char currentStringChar = 0;
            int marker = startIndex;

            int i;
            for(i = startIndex; i < endIndex; ++i) {
               int codePoint = message.codePointAt(i);
               if (!Character.isBmpCodePoint(i)) {
                  ++i;
               }

               if (escaped) {
                  escaped = false;
               } else {
                  if (codePoint == 92 && i + 1 < message.length()) {
                     int nextCodePoint = message.codePointAt(i + 1);
                     switch(state) {
                     case NORMAL:
                        escaped = nextCodePoint == 60 || nextCodePoint == 92;
                        break;
                     case STRING:
                        escaped = currentStringChar == nextCodePoint || nextCodePoint == 92;
                     }

                     if (escaped) {
                        continue;
                     }
                  }

                  switch(state) {
                  case NORMAL:
                     if (codePoint == 58) {
                        if (!boundsCheck(message, i, 2) || message.charAt(i + 1) != '/' || message.charAt(i + 2) != '/') {
                           if (marker == i) {
                              insert(token, new Token(i, i, TokenType.TAG_VALUE));
                              ++marker;
                           } else {
                              insert(token, new Token(marker, i, TokenType.TAG_VALUE));
                              marker = i + 1;
                           }
                        }
                     } else if (codePoint == 39 || codePoint == 34) {
                        state = TokenParser.SecondPassState.STRING;
                        currentStringChar = (char)codePoint;
                     }
                     break;
                  case STRING:
                     if (codePoint == currentStringChar) {
                        state = TokenParser.SecondPassState.NORMAL;
                     }
                  }
               }
            }

            if (token.childTokens() != null && !token.childTokens().isEmpty()) {
               i = ((Token)token.childTokens().get(token.childTokens().size() - 1)).endIndex();
               if (i != endIndex) {
                  insert(token, new Token(i + 1, endIndex, TokenType.TAG_VALUE));
               }
            } else {
               insert(token, new Token(startIndex, endIndex, TokenType.TAG_VALUE));
            }
         }
      }
   }

   private static RootNode buildTree(@NotNull final TokenParser.TagProvider tagProvider, @NotNull final Predicate<String> tagNameChecker, @NotNull final List<Token> tokens, @NotNull final String message, @NotNull final String originalMessage, final boolean strict) throws ParsingException {
      RootNode root = new RootNode(message, originalMessage);
      ElementNode node = root;
      Iterator var8 = tokens.iterator();

      while(true) {
         Token token;
         Object parentNode;
         do {
            ArrayList closeValues;
            Tag tag;
            label121:
            do {
               while(true) {
                  TagNode tagNode;
                  while(var8.hasNext()) {
                     token = (Token)var8.next();
                     TokenType type = token.type();
                     switch(type) {
                     case TEXT:
                        ((ElementNode)node).addChild(new TextNode((ElementNode)node, token, message));
                        break;
                     case OPEN_TAG:
                     case OPEN_CLOSE_TAG:
                        Token tagNamePart = (Token)token.childTokens().get(0);
                        String tagName = message.substring(tagNamePart.startIndex(), tagNamePart.endIndex());
                        if (!TagInternals.sanitizeAndCheckValidTagName(tagName)) {
                           ((ElementNode)node).addChild(new TextNode((ElementNode)node, token, message));
                        } else {
                           tagNode = new TagNode((ElementNode)node, token, message, tagProvider);
                           if (tagNameChecker.test(tagNode.name())) {
                              Tag tag = tagProvider.resolve(tagNode);
                              if (tag == null) {
                                 ((ElementNode)node).addChild(new TextNode((ElementNode)node, token, message));
                                 continue;
                              }

                              if (tag == ParserDirective.RESET) {
                                 if (strict) {
                                    throw new ParsingExceptionImpl("<reset> tags are not allowed when strict mode is enabled", message, new Token[]{token});
                                 }

                                 node = root;
                                 continue;
                              }

                              tagNode.tag(tag);
                              ((ElementNode)node).addChild(tagNode);
                              if (type != TokenType.OPEN_CLOSE_TAG && (!(tag instanceof Inserting) || ((Inserting)tag).allowsChildren())) {
                                 node = tagNode;
                              }
                              continue;
                           }

                           ((ElementNode)node).addChild(new TextNode((ElementNode)node, token, message));
                        }
                        break;
                     case CLOSE_TAG:
                        List<Token> childTokens = token.childTokens();
                        if (childTokens.isEmpty()) {
                           throw new IllegalStateException("CLOSE_TAG token somehow has no children - the parser should not allow this. Original text: " + message);
                        }

                        closeValues = new ArrayList(childTokens.size());
                        Iterator var16 = childTokens.iterator();

                        while(var16.hasNext()) {
                           Token childToken = (Token)var16.next();
                           closeValues.add(TagPart.unquoteAndEscape(message, childToken.startIndex(), childToken.endIndex()));
                        }

                        String closeTagName = (String)closeValues.get(0);
                        if (tagNameChecker.test(closeTagName)) {
                           tag = tagProvider.resolve(closeTagName);
                           continue label121;
                        }

                        ((ElementNode)node).addChild(new TextNode((ElementNode)node, token, message));
                     }
                  }

                  if (strict && root != node) {
                     ArrayList<TagNode> openTags = new ArrayList();

                     for(Object n = node; n != null && n instanceof TagNode; n = ((ElementNode)n).parent()) {
                        openTags.add((TagNode)n);
                     }

                     Token[] errorTokens = new Token[openTags.size()];
                     StringBuilder sb = new StringBuilder("All tags must be explicitly closed while in strict mode. End of string found with open tags: ");
                     int i = 0;
                     ListIterator iter = openTags.listIterator(openTags.size());

                     while(iter.hasPrevious()) {
                        tagNode = (TagNode)iter.previous();
                        errorTokens[i++] = tagNode.token();
                        sb.append(tagNode.name());
                        if (iter.hasPrevious()) {
                           sb.append(", ");
                        }
                     }

                     throw new ParsingExceptionImpl(sb.toString(), message, errorTokens);
                  }

                  return root;
               }
            } while(tag == ParserDirective.RESET);

            for(parentNode = node; parentNode instanceof TagNode; parentNode = ((ElementNode)parentNode).parent()) {
               List<TagPart> openParts = ((TagNode)parentNode).parts();
               if (tagCloses(closeValues, openParts)) {
                  if (parentNode != node && strict) {
                     String msg = "Unclosed tag encountered; " + ((TagNode)node).name() + " is not closed, because " + (String)closeValues.get(0) + " was closed first.";
                     throw new ParsingExceptionImpl(msg, message, new Token[]{((ElementNode)parentNode).token(), ((ElementNode)node).token(), token});
                  }

                  ElementNode par = ((ElementNode)parentNode).parent();
                  if (par == null) {
                     throw new IllegalStateException("Root node matched with close tag value, this should not be possible. Original text: " + message);
                  }

                  node = par;
                  break;
               }
            }
         } while(parentNode != null && !(parentNode instanceof RootNode));

         ((ElementNode)node).addChild(new TextNode((ElementNode)node, token, message));
      }
   }

   private static boolean tagCloses(final List<String> closeParts, final List<TagPart> openParts) {
      if (closeParts.size() > openParts.size()) {
         return false;
      } else if (!((String)closeParts.get(0)).equalsIgnoreCase(((TagPart)openParts.get(0)).value())) {
         return false;
      } else {
         for(int i = 1; i < closeParts.size(); ++i) {
            if (!((String)closeParts.get(i)).equals(((TagPart)openParts.get(i)).value())) {
               return false;
            }
         }

         return true;
      }
   }

   private static boolean boundsCheck(final String text, final int index, final int length) {
      return index + length < text.length();
   }

   private static void insert(final Token token, final Token value) {
      if (token.childTokens() == null) {
         token.childTokens(Collections.singletonList(value));
      } else {
         if (token.childTokens().size() == 1) {
            ArrayList<Token> list = new ArrayList(3);
            list.add((Token)token.childTokens().get(0));
            list.add(value);
            token.childTokens(list);
         } else {
            token.childTokens().add(value);
         }

      }
   }

   public static String unescape(final String text, final int startIndex, final int endIndex, final IntPredicate escapes) {
      int from = startIndex;
      int i = text.indexOf(92, startIndex);
      if (i != -1 && i < endIndex) {
         StringBuilder sb;
         for(sb = new StringBuilder(endIndex - startIndex); i != -1 && i + 1 < endIndex; i = text.indexOf(92, i)) {
            if (escapes.test(text.codePointAt(i + 1))) {
               sb.append(text, from, i);
               ++i;
               if (i >= endIndex) {
                  from = endIndex;
                  break;
               }

               int codePoint = text.codePointAt(i);
               sb.appendCodePoint(codePoint);
               if (Character.isBmpCodePoint(codePoint)) {
                  ++i;
               } else {
                  i += 2;
               }

               if (i >= endIndex) {
                  from = endIndex;
                  break;
               }
            } else {
               ++i;
               sb.append(text, from, i);
            }

            from = i;
         }

         sb.append(text, from, endIndex);
         return sb.toString();
      } else {
         return text.substring(startIndex, endIndex);
      }
   }

   @ApiStatus.Internal
   public interface TagProvider {
      @Nullable
      Tag resolve(@NotNull final String name, @NotNull final List<? extends Tag.Argument> trimmedArgs, @Nullable final Token token);

      @Nullable
      default Tag resolve(@NotNull final String name) {
         return this.resolve(name, Collections.emptyList(), (Token)null);
      }

      @Nullable
      default Tag resolve(@NotNull final TagNode node) {
         return this.resolve(sanitizePlaceholderName(node.name()), node.parts().subList(1, node.parts().size()), node.token());
      }

      @NotNull
      static String sanitizePlaceholderName(@NotNull final String name) {
         return name.toLowerCase(Locale.ROOT);
      }
   }

   static enum SecondPassState {
      NORMAL,
      STRING;
   }

   static enum FirstPassState {
      NORMAL,
      TAG,
      STRING;
   }
}
