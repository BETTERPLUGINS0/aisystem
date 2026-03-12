package ac.grim.grimac.shaded.incendo.cloud.parser.standard;

import ac.grim.grimac.shaded.incendo.cloud.caption.CaptionVariable;
import ac.grim.grimac.shaded.incendo.cloud.caption.StandardCaptionKeys;
import ac.grim.grimac.shaded.incendo.cloud.component.CommandComponent;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandContext;
import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import ac.grim.grimac.shaded.incendo.cloud.exception.parsing.ParserException;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParseResult;
import ac.grim.grimac.shaded.incendo.cloud.parser.ArgumentParser;
import ac.grim.grimac.shaded.incendo.cloud.parser.ParserDescriptor;
import ac.grim.grimac.shaded.incendo.cloud.util.StringUtils;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;

@API(
   status = Status.STABLE
)
public final class StringParser<C> implements ArgumentParser<C, String> {
   private static final Pattern QUOTED_DOUBLE = Pattern.compile("\"(?<inner>(?:[^\"\\\\]|\\\\.)*)\"");
   private static final Pattern QUOTED_SINGLE = Pattern.compile("'(?<inner>(?:[^'\\\\]|\\\\.)*)'");
   private static final Pattern FLAG_PATTERN = Pattern.compile("(-[A-Za-z_\\-0-9])|(--[A-Za-z_\\-0-9]*)");
   private final StringParser.StringMode stringMode;

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, String> stringParser(@NonNull final StringParser.StringMode mode) {
      return ParserDescriptor.of(new StringParser(mode), (Class)String.class);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, String> stringParser() {
      return stringParser(StringParser.StringMode.SINGLE);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, String> greedyStringParser() {
      return stringParser(StringParser.StringMode.GREEDY);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, String> greedyFlagYieldingStringParser() {
      return stringParser(StringParser.StringMode.GREEDY_FLAG_YIELDING);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> ParserDescriptor<C, String> quotedStringParser() {
      return stringParser(StringParser.StringMode.QUOTED);
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, String> stringComponent(@NonNull final StringParser.StringMode mode) {
      return CommandComponent.builder().parser(stringParser(mode));
   }

   @API(
      status = Status.STABLE
   )
   @NonNull
   public static <C> CommandComponent.Builder<C, String> stringComponent() {
      return CommandComponent.builder().parser(stringParser(StringParser.StringMode.SINGLE));
   }

   public StringParser(@NonNull final StringParser.StringMode stringMode) {
      this.stringMode = stringMode;
   }

   @NonNull
   public ArgumentParseResult<String> parse(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      if (this.stringMode == StringParser.StringMode.SINGLE) {
         return ArgumentParseResult.success(commandInput.readString());
      } else {
         return this.stringMode == StringParser.StringMode.QUOTED ? this.parseQuoted(commandContext, commandInput) : this.parseGreedy(commandInput);
      }
   }

   @NonNull
   private ArgumentParseResult<String> parseQuoted(@NonNull final CommandContext<C> commandContext, @NonNull final CommandInput commandInput) {
      char peek = commandInput.peek();
      if (peek != '\'' && peek != '"') {
         return ArgumentParseResult.success(commandInput.readString());
      } else {
         String string = commandInput.remainingInput();
         Matcher doubleMatcher = QUOTED_DOUBLE.matcher(string);
         String doubleMatch = null;
         if (doubleMatcher.find()) {
            doubleMatch = doubleMatcher.group("inner");
         }

         Matcher singleMatcher = QUOTED_SINGLE.matcher(string);
         String singleMatch = null;
         if (singleMatcher.find()) {
            singleMatch = singleMatcher.group("inner");
         }

         String inner = null;
         int numSpaces;
         int i;
         if (singleMatch != null && doubleMatch != null) {
            numSpaces = string.indexOf(singleMatch);
            i = string.indexOf(doubleMatch);
            inner = i < numSpaces ? doubleMatch : singleMatch;
         } else if (singleMatch == null && doubleMatch != null) {
            inner = doubleMatch;
         } else if (singleMatch != null) {
            inner = singleMatch;
         }

         if (inner != null) {
            numSpaces = StringUtils.countCharOccurrences(inner, ' ');

            for(i = 0; i <= numSpaces; ++i) {
               commandInput.readString();
            }
         } else {
            inner = commandInput.peekString();
            if (inner.startsWith("\"") || inner.startsWith("'")) {
               return ArgumentParseResult.failure(new StringParser.StringParseException(commandInput.remainingInput(), StringParser.StringMode.QUOTED, commandContext));
            }

            commandInput.readString();
         }

         inner = inner.replace("\\\"", "\"").replace("\\'", "'");
         return ArgumentParseResult.success(inner);
      }
   }

   @NonNull
   private ArgumentParseResult<String> parseGreedy(@NonNull final CommandInput commandInput) {
      int size = commandInput.remainingTokens();
      StringJoiner stringJoiner = new StringJoiner(" ");

      for(int i = 0; i < size; ++i) {
         String string = commandInput.peekString();
         if (string.isEmpty() || this.stringMode == StringParser.StringMode.GREEDY_FLAG_YIELDING && FLAG_PATTERN.matcher(string).matches()) {
            break;
         }

         stringJoiner.add(commandInput.readStringSkipWhitespace(false));
      }

      return ArgumentParseResult.success(stringJoiner.toString());
   }

   @NonNull
   public StringParser.StringMode stringMode() {
      return this.stringMode;
   }

   @API(
      status = Status.STABLE
   )
   public static enum StringMode {
      SINGLE,
      QUOTED,
      GREEDY,
      @API(
         status = Status.STABLE
      )
      GREEDY_FLAG_YIELDING;

      // $FF: synthetic method
      private static StringParser.StringMode[] $values() {
         return new StringParser.StringMode[]{SINGLE, QUOTED, GREEDY, GREEDY_FLAG_YIELDING};
      }
   }

   @API(
      status = Status.STABLE
   )
   public static final class StringParseException extends ParserException {
      private final String input;
      private final StringParser.StringMode stringMode;

      public StringParseException(@NonNull final String input, @NonNull final StringParser.StringMode stringMode, @NonNull final CommandContext<?> context) {
         super(StringParser.class, context, StandardCaptionKeys.ARGUMENT_PARSE_FAILURE_STRING, CaptionVariable.of("input", input), CaptionVariable.of("stringMode", stringMode.name()));
         this.input = input;
         this.stringMode = stringMode;
      }

      @NonNull
      public String input() {
         return this.input;
      }

      @NonNull
      public StringParser.StringMode stringMode() {
         return this.stringMode;
      }
   }
}
