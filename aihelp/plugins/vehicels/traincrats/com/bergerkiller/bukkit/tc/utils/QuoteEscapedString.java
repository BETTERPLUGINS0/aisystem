package com.bergerkiller.bukkit.tc.utils;

public class QuoteEscapedString {
   private final String unescaped;
   private String escaped;
   private final boolean isQuoteEscaped;

   public QuoteEscapedString(String unescaped) {
      this(unescaped, (String)null, false);
   }

   public QuoteEscapedString(String unescaped, String escaped, boolean isQuoteEscaped) {
      this.unescaped = unescaped;
      this.escaped = escaped;
      this.isQuoteEscaped = isQuoteEscaped;
   }

   public String getUnescaped() {
      return this.unescaped;
   }

   public String getEscaped() {
      if (this.escaped == null) {
         this.escaped = escapeString(this.unescaped);
      }

      return this.escaped;
   }

   public boolean isQuoteEscaped() {
      return this.isQuoteEscaped;
   }

   public String toString() {
      return "QuoteEscapedString{" + this.unescaped + " QUOTED=" + this.isQuoteEscaped + "}";
   }

   public static QuoteEscapedString quoteEscape(String str) {
      return new QuoteEscapedString(str, escapeString(str), false);
   }

   public static QuoteEscapedString tryParseQuoted(String str) {
      int len = str.length();
      if (len < 2) {
         return new QuoteEscapedString(str);
      } else {
         char quoteChar = str.charAt(0);
         if (quoteChar != '"' && quoteChar != '\'') {
            return new QuoteEscapedString(str);
         } else if (str.charAt(len - 1) != quoteChar) {
            return new QuoteEscapedString(str);
         } else if (str.indexOf(92, 1) == -1 && str.indexOf(quoteChar, 1) == len - 1) {
            return new QuoteEscapedString(str.substring(1, len - 1), str, true);
         } else {
            StringBuilder newStr = new StringBuilder(len - 1);
            boolean escaped = false;

            int i;
            for(i = 1; i < len; ++i) {
               char c = str.charAt(i);
               if (escaped) {
                  escaped = false;
                  newStr.append(c);
               } else if (c == '\\') {
                  escaped = true;
               } else {
                  if (c == quoteChar) {
                     ++i;
                     break;
                  }

                  newStr.append(c);
               }
            }

            return !escaped && i >= len ? new QuoteEscapedString(newStr.toString(), str, true) : new QuoteEscapedString(str);
         }
      }
   }

   public static int unquotedIndexOf(String text, String token, int fromIndex) {
      int len = text.length();

      while(fromIndex < len) {
         int matchIndex = text.indexOf(token, fromIndex);
         if (matchIndex == -1 || matchIndex == fromIndex) {
            return matchIndex;
         }

         boolean isQuotedString = false;
         char quoteChar = '"';

         for(boolean escaped = false; fromIndex < len; ++fromIndex) {
            if (fromIndex == matchIndex && !isQuotedString) {
               return matchIndex;
            }

            char c = text.charAt(fromIndex);
            if (escaped) {
               escaped = false;
            } else if (c != '"' && c != '\'') {
               if (isQuotedString && c == '\\') {
                  escaped = true;
               }
            } else if (!isQuotedString) {
               isQuotedString = true;
               quoteChar = c;
            } else if (c == quoteChar) {
               isQuotedString = false;
               if (fromIndex > matchIndex) {
                  break;
               }
            }
         }
      }

      return -1;
   }

   private static String escapeString(String text) {
      int len = text.length();
      boolean allowed = true;

      for(int i = 0; i < len; ++i) {
         if (!isAllowedInUnquotedString(text.charAt(i))) {
            allowed = false;
            break;
         }
      }

      if (allowed) {
         return text;
      } else {
         StringBuilder escaped = new StringBuilder(len + 8);
         escaped.append('"');

         for(int i = 0; i < len; ++i) {
            char c = text.charAt(i);
            if (c == '\\' || c == '"') {
               escaped.append('\\');
            }

            escaped.append(c);
         }

         escaped.append('"');
         return escaped.toString();
      }
   }

   private static boolean isAllowedInUnquotedString(char c) {
      return c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z' || c == '_' || c == '-' || c == '.' || c == '+';
   }
}
