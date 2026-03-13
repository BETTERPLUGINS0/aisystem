package com.nisovin.shopkeepers.util.java;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.text.Text;
import com.nisovin.shopkeepers.util.annotations.ReadWrite;
import com.nisovin.shopkeepers.util.text.MessageArguments;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class StringUtils {
   private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s+");
   private static final Pattern NEWLINE_PATTERN = Pattern.compile("\\R");
   private static final Pattern NEWLINE_OR_LITERAL_PATTERN = Pattern.compile("\\R|\\\\n");
   private static final Pattern ALL_TRAILING_NEWLINES_PATTERN = Pattern.compile("\\R+$");
   private static final StringUtils.ArgumentsReplacer ARGUMENTS_REPLACER = new StringUtils.ArgumentsReplacer();
   private static final Map<String, Object> TEMP_ARGUMENTS_MAP = new HashMap();
   private static final MessageArguments TEMP_ARGUMENTS;

   public static boolean isEmpty(@Nullable String string) {
      return string == null || string.isEmpty();
   }

   @Nullable
   public static String getNotEmpty(@Nullable String string) {
      return isEmpty(string) ? null : string;
   }

   public static String getOrEmpty(@Nullable String string) {
      return string == null ? "" : string;
   }

   public static String toStringOrEmpty(@Nullable Object object) {
      return object == null ? "" : getOrEmpty(object.toString());
   }

   @Nullable
   public static String toStringOrNull(@Nullable Object object) {
      return object == null ? null : object.toString();
   }

   public static boolean contains(@Nullable String string, int character) {
      return string != null && string.indexOf(character) != -1;
   }

   public static String normalizeKeepCase(String identifier) {
      Validate.notNull(identifier, (String)"identifier is null");
      String normalized = identifier.trim();
      normalized = normalized.replace('_', '-');
      normalized = replaceWhitespace(normalized, "-");
      return normalized;
   }

   public static String normalize(String identifier) {
      String normalized = normalizeKeepCase(identifier);
      return normalized.toLowerCase(Locale.ROOT);
   }

   public static List<String> normalize(List<? extends String> identifiers) {
      List<String> normalized = new ArrayList(identifiers.size());
      Iterator var2 = identifiers.iterator();

      while(var2.hasNext()) {
         String identifier = (String)var2.next();
         normalized.add(normalize(identifier));
      }

      return normalized;
   }

   public static boolean containsWhitespace(@Nullable String string) {
      if (isEmpty(string)) {
         return false;
      } else {
         assert string != null;

         int length = string.length();

         for(int i = 0; i < length; ++i) {
            if (Character.isWhitespace(string.charAt(i))) {
               return true;
            }
         }

         return false;
      }
   }

   public static String removeWhitespace(String source) {
      return replaceWhitespace(source, "");
   }

   public static String replaceWhitespace(String source, String replacement) {
      Validate.notNull(source, (String)"source is null");
      if (source.isEmpty()) {
         return source;
      } else {
         Validate.notNull(replacement, (String)"replacement is null");
         String replaced = WHITESPACE_PATTERN.matcher(source).replaceAll(replacement);

         assert replaced != null;

         return replaced;
      }
   }

   public static String capitalizeAll(String source) {
      Validate.notNull(source, (String)"source is null");
      if (source.isEmpty()) {
         return source;
      } else {
         int sourceLength = source.length();
         StringBuilder builder = new StringBuilder(sourceLength);
         boolean capitalizeNext = true;

         for(int i = 0; i < sourceLength; ++i) {
            char currentChar = source.charAt(i);
            if (Character.isWhitespace(currentChar)) {
               capitalizeNext = true;
               builder.append(currentChar);
            } else if (capitalizeNext) {
               capitalizeNext = false;
               builder.append(Character.toTitleCase(currentChar));
            } else {
               builder.append(currentChar);
            }
         }

         return builder.toString();
      }
   }

   public static String[] splitLines(String source) {
      return splitLines(source, false);
   }

   public static String[] splitLines(String source, boolean splitLiteralNewlines) {
      return splitLiteralNewlines ? NEWLINE_OR_LITERAL_PATTERN.split(source, -1) : NEWLINE_PATTERN.split(source, -1);
   }

   public static String stripTrailingNewlines(String string) {
      Validate.notNull(string, (String)"string is null");
      String stripped = ALL_TRAILING_NEWLINES_PATTERN.matcher(string).replaceFirst("");

      assert stripped != null;

      return stripped;
   }

   public static boolean containsNewline(@Nullable String string) {
      if (string == null) {
         return false;
      } else {
         int length = string.length();
         int i = 0;

         while(i < length) {
            char c = string.charAt(i);
            switch(c) {
            case '\n':
            case '\u000b':
            case '\f':
            case '\r':
            case '\u0085':
            case '\u2028':
            case '\u2029':
               return true;
            default:
               ++i;
            }
         }

         return false;
      }
   }

   public static String escapeNewlinesAndBackslash(String string) {
      Validate.notNull(string, (String)"string is null");
      int length = string.length();
      StringBuilder sb = new StringBuilder(length * 2);

      for(int i = 0; i < length; ++i) {
         char c = string.charAt(i);
         switch(c) {
         case '\n':
            sb.append("\\n");
            break;
         case '\u000b':
            sb.append("\\u000B");
            break;
         case '\f':
            sb.append("\\f");
            break;
         case '\r':
            sb.append("\\r");
            break;
         case '\\':
            sb.append("\\\\");
            break;
         case '\u0085':
            sb.append("\\u0085");
            break;
         case '\u2028':
            sb.append("\\u2028");
            break;
         case '\u2029':
            sb.append("\\u2029");
            break;
         default:
            sb.append(c);
         }
      }

      return sb.toString();
   }

   public static void wrap(@ReadWrite List<String> source, int maxLength) {
      Validate.isTrue(maxLength >= 1, "maxLength must be positive!");
      ListIterator iterator = source.listIterator();

      while(true) {
         String text;
         do {
            if (!iterator.hasNext()) {
               return;
            }

            text = (String)Unsafe.assertNonNull((String)iterator.next());
         } while(text.length() <= maxLength);

         assert text.length() > 0;

         iterator.remove();
         int startIndexInclusive = 0;

         while(startIndexInclusive < text.length()) {
            int endIndexExclusive = Math.min(startIndexInclusive + maxLength, text.length());
            int wrapAt = -1;

            for(int i = endIndexExclusive - 1; i > startIndexInclusive; --i) {
               if (Character.isWhitespace(text.charAt(i))) {
                  wrapAt = i;
                  break;
               }
            }

            if (wrapAt != -1) {
               iterator.add(text.substring(startIndexInclusive, wrapAt));
               startIndexInclusive = wrapAt + 1;
            } else {
               iterator.add(text.substring(startIndexInclusive, endIndexExclusive));
               startIndexInclusive = endIndexExclusive;
            }
         }
      }
   }

   public static String replaceFirst(String source, String target, CharSequence replacement) {
      int index = source.indexOf(target);
      if (index == -1) {
         return source;
      } else {
         int sourceLength = source.length();
         int targetLength = target.length();
         int increase = replacement.length() - targetLength;
         StringBuilder result = new StringBuilder(sourceLength + increase);
         result.append(source, 0, index);
         result.append(replacement);
         result.append(source, index + targetLength, sourceLength);
         return result.toString();
      }
   }

   public static <T> void addArgumentsToMap(Map<String, Object> argumentsMap, Object... argumentPairs) {
      Validate.notNull(argumentsMap, (String)"argumentsMap is null");
      Validate.notNull(argumentPairs, (String)"argumentPairs is null");
      Validate.isTrue(argumentPairs.length % 2 == 0, "Length of argumentPairs is not a multiple of 2");
      int argumentsKeyLimit = argumentPairs.length - 1;

      for(int i = 0; i < argumentsKeyLimit; i += 2) {
         String key = (String)argumentPairs[i];
         Object value = argumentPairs[i + 1];
         argumentsMap.put(key, value);
      }

   }

   public static String replaceArguments(String source, Object... argumentPairs) {
      assert TEMP_ARGUMENTS_MAP.isEmpty();

      String var2;
      try {
         addArgumentsToMap(TEMP_ARGUMENTS_MAP, argumentPairs);
         var2 = replaceArguments(source, TEMP_ARGUMENTS);
      } finally {
         TEMP_ARGUMENTS_MAP.clear();
      }

      return var2;
   }

   public static String replaceArguments(String source, Map<? extends String, ?> arguments) {
      return replaceArguments(source, MessageArguments.ofMap(arguments));
   }

   public static String replaceArguments(String source, MessageArguments arguments) {
      return ARGUMENTS_REPLACER.replaceArguments(source, arguments);
   }

   public static List<String> replaceArguments(Collection<? extends String> messages, Object... argumentPairs) {
      assert TEMP_ARGUMENTS_MAP.isEmpty();

      List var2;
      try {
         addArgumentsToMap(TEMP_ARGUMENTS_MAP, argumentPairs);
         var2 = replaceArguments(messages, TEMP_ARGUMENTS_MAP);
      } finally {
         TEMP_ARGUMENTS_MAP.clear();
      }

      return var2;
   }

   public static List<String> replaceArguments(Collection<? extends String> sources, Map<? extends String, ?> arguments) {
      return replaceArguments(sources, MessageArguments.ofMap(arguments));
   }

   public static List<String> replaceArguments(Collection<? extends String> sources, MessageArguments arguments) {
      Validate.notNull(sources, (String)"sources is null");
      List<String> replaced = new ArrayList(sources.size());
      Iterator var3 = sources.iterator();

      while(var3.hasNext()) {
         String source = (String)var3.next();
         replaced.add(replaceArguments(source, arguments));
      }

      return replaced;
   }

   private StringUtils() {
   }

   static {
      TEMP_ARGUMENTS = MessageArguments.ofMap(TEMP_ARGUMENTS_MAP);
   }

   public static class ArgumentsReplacer {
      public static final char DEFAULT_KEY_PREFIX_CHAR = '{';
      public static final char DEFAULT_KEY_SUFFIX_CHAR = '}';
      @Nullable
      private String source;
      private int sourceLength;
      private char keyPrefixChar;
      private char keySuffixChar;
      @Nullable
      private MessageArguments arguments;
      private int searchPos = 0;
      private int keyPrefixIndex = -1;
      private int keySuffixIndex = -1;
      private int keyStartIndex = -1;
      private int keyEndIndex = -1;
      @Nullable
      protected String key = null;
      @Nullable
      protected Object argument = null;
      @Nullable
      protected StringBuilder resultBuilder = null;
      private int resultSourcePos = 0;
      @Nullable
      private String result = null;

      public String replaceArguments(String source, MessageArguments arguments) {
         return this.replaceArguments(source, '{', '}', arguments);
      }

      public String replaceArguments(String source, char keyPrefixChar, char keySuffixChar, MessageArguments arguments) {
         this.setup(source, keyPrefixChar, keySuffixChar, arguments);
         this.replaceArguments();
         String result = (String)Unsafe.assertNonNull(this.result);
         this.cleanUp();
         return result;
      }

      protected void setup(String source, char keyPrefixChar, char keySuffixChar, MessageArguments arguments) {
         Validate.notNull(source, (String)"source is null");
         Validate.notNull(arguments, (String)"arguments is null");
         this.source = source;
         this.sourceLength = source.length();
         this.keyPrefixChar = keyPrefixChar;
         this.keySuffixChar = keySuffixChar;
         this.arguments = arguments;
         this.searchPos = 0;
         this.keyPrefixIndex = -1;
         this.keySuffixIndex = -1;
         this.keyStartIndex = -1;
         this.keyEndIndex = -1;
         this.key = null;
         this.argument = null;
         this.result = null;
         if (this.resultBuilder != null) {
            this.resultBuilder.setLength(0);
         }

         this.resultSourcePos = 0;
         if (this.sourceLength <= 2) {
            this.searchPos = this.sourceLength;
         }

      }

      protected void cleanUp() {
         this.source = null;
         this.arguments = null;
         this.key = null;
         this.argument = null;
         this.result = null;
         if (this.resultBuilder != null) {
            this.resultBuilder.setLength(0);
         }

      }

      private void replaceArguments() {
         assert this.result == null;

         while(this.findNextKey()) {
            String key = (String)Unsafe.assertNonNull(this.key);
            this.argument = this.resolveArgument(key);
            if (this.argument != null) {
               this.appendPrefix();
               this.appendArgument();
            }
         }

         if (this.resultSourcePos <= 0) {
            this.result = this.source;
            this.resultSourcePos = this.sourceLength;
         } else {
            if (this.resultSourcePos < this.sourceLength) {
               this.appendSuffix();
            }

            this.prepareResult();
         }
      }

      private boolean findNextKey() {
         if (this.searchPos >= this.sourceLength) {
            return false;
         } else {
            String source = (String)Unsafe.assertNonNull(this.source);
            this.keyPrefixIndex = source.indexOf(this.keyPrefixChar, this.searchPos);
            if (this.keyPrefixIndex < 0) {
               return false;
            } else {
               this.keyStartIndex = this.keyPrefixIndex + 1;
               this.keySuffixIndex = source.indexOf(this.keySuffixChar, this.keyStartIndex);
               if (this.keySuffixIndex < 0) {
                  return false;
               } else {
                  this.keyEndIndex = this.keySuffixIndex;
                  this.key = source.substring(this.keyStartIndex, this.keyEndIndex);
                  this.searchPos = this.keySuffixIndex + 1;
                  return true;
               }
            }
         }
      }

      @Nullable
      protected Object resolveArgument(String key) {
         Object argument = ((MessageArguments)Unsafe.assertNonNull(this.arguments)).get(key);
         if (argument instanceof Supplier) {
            Supplier supplier = (Supplier)argument;
            argument = supplier.get();
         }

         if (argument instanceof Text) {
            Text text = (Text)argument;
            argument = text.toPlainText();
         }

         return argument;
      }

      protected void appendPrefix() {
         if (this.resultBuilder == null) {
            this.resultBuilder = new StringBuilder(this.sourceLength + this.sourceLength / 4);
         }

         assert this.resultBuilder != null;

         this.resultBuilder.append(this.source, this.resultSourcePos, this.keyPrefixIndex);
         this.resultSourcePos = this.keySuffixIndex + 1;
      }

      protected void appendArgument() {
         String argumentString = Unsafe.assertNonNull(this.argument).toString();
         ((StringBuilder)Unsafe.assertNonNull(this.resultBuilder)).append(argumentString);
      }

      protected void appendSuffix() {
         assert this.resultSourcePos > 0 && this.resultSourcePos < this.sourceLength && this.resultBuilder != null;

         ((StringBuilder)Unsafe.assertNonNull(this.resultBuilder)).append(this.source, this.resultSourcePos, this.sourceLength);
         this.resultSourcePos = this.sourceLength;
      }

      protected void prepareResult() {
         this.result = ((StringBuilder)Unsafe.assertNonNull(this.resultBuilder)).toString();
      }
   }
}
