package ac.grim.grimac.shaded.incendo.cloud.util;

import ac.grim.grimac.shaded.incendo.cloud.context.CommandInput;
import java.util.Locale;
import java.util.function.Function;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apiguardian.api.API;
import org.apiguardian.api.API.Status;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@API(
   status = Status.INTERNAL,
   consumers = {"ac.grim.grimac.shaded.incendo.cloud.*"}
)
public final class StringUtils {
   private StringUtils() {
   }

   public static int countCharOccurrences(@NonNull final String haystack, final char needle) {
      int occurrences = 0;

      for(int i = 0; i < haystack.length(); ++i) {
         if (haystack.charAt(i) == needle) {
            ++occurrences;
         }
      }

      return occurrences;
   }

   @NonNull
   public static String replaceAll(@NonNull final String string, @NonNull final Pattern pattern, @NonNull final Function<MatchResult, String> replacer) {
      Matcher matcher = pattern.matcher(string);
      matcher.reset();
      boolean result = matcher.find();
      if (!result) {
         return string;
      } else {
         StringBuffer sb = new StringBuffer();

         do {
            String replacement = (String)replacer.apply(matcher);
            matcher.appendReplacement(sb, replacement);
            result = matcher.find();
         } while(result);

         matcher.appendTail(sb);
         return sb.toString();
      }
   }

   @Nullable
   public static String trimBeforeLastSpace(final String suggestion, final String input) {
      int lastSpace = input.lastIndexOf(32);
      if (lastSpace == -1) {
         return suggestion;
      } else {
         return suggestion.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT).substring(0, lastSpace)) ? suggestion.substring(lastSpace + 1) : null;
      }
   }

   @Nullable
   public static String trimBeforeLastSpace(final String suggestion, final CommandInput commandInput) {
      String input;
      if (commandInput.isEmpty(true)) {
         input = "";
      } else {
         input = commandInput.copy().skipWhitespace().remainingInput();
      }

      return trimBeforeLastSpace(suggestion, input);
   }
}
