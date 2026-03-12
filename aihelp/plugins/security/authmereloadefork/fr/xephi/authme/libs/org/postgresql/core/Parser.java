package fr.xephi.authme.libs.org.postgresql.core;

import fr.xephi.authme.libs.org.postgresql.jdbc.EscapeSyntaxCallMode;
import fr.xephi.authme.libs.org.postgresql.jdbc.EscapedFunctions2;
import fr.xephi.authme.libs.org.postgresql.util.GT;
import fr.xephi.authme.libs.org.postgresql.util.IntList;
import fr.xephi.authme.libs.org.postgresql.util.PSQLException;
import fr.xephi.authme.libs.org.postgresql.util.PSQLState;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Parser {
   private static final char[] QUOTE_OR_ALPHABETIC_MARKER = new char[]{'"', '0'};
   private static final char[] QUOTE_OR_ALPHABETIC_MARKER_OR_PARENTHESIS = new char[]{'"', '0', '('};
   private static final char[] SINGLE_QUOTE = new char[]{'\''};

   public static List<NativeQuery> parseJdbcSql(String query, boolean standardConformingStrings, boolean withParameters, boolean splitStatements, boolean isBatchedReWriteConfigured, boolean quoteReturningIdentifiers, String... returningColumnNames) throws SQLException {
      if (!withParameters && !splitStatements && returningColumnNames != null && returningColumnNames.length == 0) {
         return Collections.singletonList(new NativeQuery(query, SqlCommand.createStatementTypeInfo(SqlCommandType.BLANK)));
      } else {
         int fragmentStart = 0;
         int inParen = 0;
         char[] aChars = query.toCharArray();
         StringBuilder nativeSql = new StringBuilder(query.length() + 10);
         IntList bindPositions = null;
         List<NativeQuery> nativeQueries = null;
         boolean isCurrentReWriteCompatible = false;
         boolean isValuesFound = false;
         int valuesParenthesisOpenPosition = -1;
         int valuesParenthesisClosePosition = -1;
         boolean valuesParenthesisCloseFound = false;
         boolean isInsertPresent = false;
         boolean isReturningPresent = false;
         boolean isReturningPresentPrev = false;
         boolean isBeginPresent = false;
         boolean isBeginAtomicPresent = false;
         SqlCommandType currentCommandType = SqlCommandType.BLANK;
         SqlCommandType prevCommandType = SqlCommandType.BLANK;
         int numberOfStatements = 0;
         boolean whitespaceOnly = true;
         int keyWordCount = 0;
         int keywordStart = -1;
         int keywordEnd = true;

         for(int i = 0; i < aChars.length; ++i) {
            char aChar = aChars[i];
            boolean isKeyWordChar = false;
            whitespaceOnly &= aChar == ';' || Character.isWhitespace(aChar);
            int keywordEnd = i;
            int wordLength;
            switch(aChar) {
            case '"':
               i = parseDoubleQuotes(aChars, i);
               break;
            case '#':
            case '%':
            case '&':
            case '(':
            case '*':
            case '+':
            case ',':
            case '.':
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case ':':
            case '<':
            case '=':
            case '>':
            default:
               if (keywordStart >= 0) {
                  isKeyWordChar = isIdentifierContChar(aChar);
               } else {
                  isKeyWordChar = isIdentifierStartChar(aChar);
                  if (isKeyWordChar) {
                     keywordStart = i;
                     if (valuesParenthesisOpenPosition != -1 && inParen == 0) {
                        valuesParenthesisCloseFound = true;
                     }
                  }
               }
               break;
            case '$':
               i = parseDollarQuotes(aChars, i);
               break;
            case '\'':
               i = parseSingleQuotes(aChars, i, standardConformingStrings);
               break;
            case ')':
               --inParen;
               if (inParen == 0 && isValuesFound && !valuesParenthesisCloseFound) {
                  valuesParenthesisClosePosition = nativeSql.length() + i - fragmentStart;
               }
               break;
            case '-':
               i = parseLineComment(aChars, i);
               break;
            case '/':
               i = parseBlockComment(aChars, i);
               break;
            case ';':
               if (isBeginAtomicPresent || inParen != 0) {
                  break;
               }

               if (!whitespaceOnly) {
                  ++numberOfStatements;
                  nativeSql.append(aChars, fragmentStart, i - fragmentStart);
                  whitespaceOnly = true;
               }

               fragmentStart = i + 1;
               if (nativeSql.length() > 0) {
                  if (addReturning(nativeSql, currentCommandType, returningColumnNames, isReturningPresent, quoteReturningIdentifiers)) {
                     isReturningPresent = true;
                  }

                  if (splitStatements) {
                     if (nativeQueries == null) {
                        nativeQueries = new ArrayList();
                     }

                     if (!isValuesFound || !isCurrentReWriteCompatible || valuesParenthesisClosePosition == -1 || bindPositions != null && valuesParenthesisClosePosition < bindPositions.get(bindPositions.size() - 1)) {
                        valuesParenthesisOpenPosition = -1;
                        valuesParenthesisClosePosition = -1;
                     }

                     nativeQueries.add(new NativeQuery(nativeSql.toString(), toIntArray(bindPositions), false, SqlCommand.createStatementTypeInfo(currentCommandType, isBatchedReWriteConfigured, valuesParenthesisOpenPosition, valuesParenthesisClosePosition, isReturningPresent, nativeQueries.size())));
                  }
               }

               prevCommandType = currentCommandType;
               isReturningPresentPrev = isReturningPresent;
               currentCommandType = SqlCommandType.BLANK;
               isReturningPresent = false;
               if (splitStatements) {
                  if (bindPositions != null) {
                     bindPositions.clear();
                  }

                  nativeSql.setLength(0);
                  isValuesFound = false;
                  isCurrentReWriteCompatible = false;
                  valuesParenthesisOpenPosition = -1;
                  valuesParenthesisClosePosition = -1;
                  valuesParenthesisCloseFound = false;
               }
               break;
            case '?':
               nativeSql.append(aChars, fragmentStart, i - fragmentStart);
               if (i + 1 < aChars.length && aChars[i + 1] == '?') {
                  nativeSql.append('?');
                  ++i;
               } else if (!withParameters) {
                  nativeSql.append('?');
               } else {
                  if (bindPositions == null) {
                     bindPositions = new IntList();
                  }

                  bindPositions.add(nativeSql.length());
                  wordLength = bindPositions.size();
                  nativeSql.append(NativeQuery.bindName(wordLength));
               }

               fragmentStart = i + 1;
            }

            if (keywordStart >= 0 && (i == aChars.length - 1 || !isKeyWordChar)) {
               wordLength = (isKeyWordChar ? i + 1 : keywordEnd) - keywordStart;
               if (currentCommandType == SqlCommandType.BLANK) {
                  if (wordLength == 6 && parseCreateKeyword(aChars, keywordStart)) {
                     currentCommandType = SqlCommandType.CREATE;
                  } else if (wordLength == 5 && parseAlterKeyword(aChars, keywordStart)) {
                     currentCommandType = SqlCommandType.ALTER;
                  } else if (wordLength == 6 && parseUpdateKeyword(aChars, keywordStart)) {
                     currentCommandType = SqlCommandType.UPDATE;
                  } else if (wordLength == 6 && parseDeleteKeyword(aChars, keywordStart)) {
                     currentCommandType = SqlCommandType.DELETE;
                  } else if (wordLength == 4 && parseMoveKeyword(aChars, keywordStart)) {
                     currentCommandType = SqlCommandType.MOVE;
                  } else if (wordLength == 6 && parseSelectKeyword(aChars, keywordStart)) {
                     currentCommandType = SqlCommandType.SELECT;
                  } else if (wordLength == 4 && parseWithKeyword(aChars, keywordStart)) {
                     currentCommandType = SqlCommandType.WITH;
                  } else if (wordLength == 6 && parseInsertKeyword(aChars, keywordStart)) {
                     if (isInsertPresent || nativeQueries != null && !nativeQueries.isEmpty()) {
                        isCurrentReWriteCompatible = false;
                     } else {
                        isCurrentReWriteCompatible = keyWordCount == 0;
                        isInsertPresent = true;
                        currentCommandType = SqlCommandType.INSERT;
                     }
                  }
               } else if (currentCommandType == SqlCommandType.WITH && inParen == 0) {
                  SqlCommandType command = parseWithCommandType(aChars, i, keywordStart, wordLength);
                  if (command != null) {
                     currentCommandType = command;
                  }
               } else if (currentCommandType == SqlCommandType.CREATE) {
                  if (wordLength == 5 && parseBeginKeyword(aChars, keywordStart)) {
                     isBeginPresent = true;
                  } else if (isBeginPresent) {
                     if (wordLength == 6 && parseAtomicKeyword(aChars, keywordStart)) {
                        isBeginAtomicPresent = true;
                     }

                     isBeginPresent = false;
                  }
               }

               if (inParen == 0 && aChar != ')') {
                  if (wordLength == 9 && parseReturningKeyword(aChars, keywordStart)) {
                     isReturningPresent = true;
                  } else if (wordLength == 6 && parseValuesKeyword(aChars, keywordStart)) {
                     isValuesFound = true;
                  }
               }

               keywordStart = -1;
               ++keyWordCount;
            }

            if (aChar == '(') {
               ++inParen;
               if (inParen == 1 && isValuesFound && valuesParenthesisOpenPosition == -1) {
                  valuesParenthesisOpenPosition = nativeSql.length() + i - fragmentStart;
               }
            }
         }

         if (!isValuesFound || !isCurrentReWriteCompatible || valuesParenthesisClosePosition == -1 || bindPositions != null && valuesParenthesisClosePosition < bindPositions.get(bindPositions.size() - 1)) {
            valuesParenthesisOpenPosition = -1;
            valuesParenthesisClosePosition = -1;
         }

         if (fragmentStart < aChars.length && !whitespaceOnly) {
            nativeSql.append(aChars, fragmentStart, aChars.length - fragmentStart);
         } else if (numberOfStatements > 1) {
            isReturningPresent = false;
            currentCommandType = SqlCommandType.BLANK;
         } else if (numberOfStatements == 1) {
            isReturningPresent = isReturningPresentPrev;
            currentCommandType = prevCommandType;
         }

         if (nativeSql.length() == 0) {
            return (List)(nativeQueries != null ? nativeQueries : Collections.emptyList());
         } else {
            if (addReturning(nativeSql, currentCommandType, returningColumnNames, isReturningPresent, quoteReturningIdentifiers)) {
               isReturningPresent = true;
            }

            NativeQuery lastQuery = new NativeQuery(nativeSql.toString(), toIntArray(bindPositions), !splitStatements, SqlCommand.createStatementTypeInfo(currentCommandType, isBatchedReWriteConfigured, valuesParenthesisOpenPosition, valuesParenthesisClosePosition, isReturningPresent, nativeQueries == null ? 0 : nativeQueries.size()));
            if (nativeQueries == null) {
               return Collections.singletonList(lastQuery);
            } else {
               if (!whitespaceOnly) {
                  nativeQueries.add(lastQuery);
               }

               return nativeQueries;
            }
         }
      }
   }

   @Nullable
   private static SqlCommandType parseWithCommandType(char[] aChars, int i, int keywordStart, int wordLength) {
      SqlCommandType command;
      if (wordLength == 6 && parseUpdateKeyword(aChars, keywordStart)) {
         command = SqlCommandType.UPDATE;
      } else if (wordLength == 6 && parseDeleteKeyword(aChars, keywordStart)) {
         command = SqlCommandType.DELETE;
      } else if (wordLength == 6 && parseInsertKeyword(aChars, keywordStart)) {
         command = SqlCommandType.INSERT;
      } else {
         if (wordLength != 6 || !parseSelectKeyword(aChars, keywordStart)) {
            return null;
         }

         command = SqlCommandType.SELECT;
      }

      int nextInd;
      for(nextInd = i; nextInd < aChars.length; ++nextInd) {
         char nextChar = aChars[nextInd];
         if (nextChar == '-') {
            nextInd = parseLineComment(aChars, nextInd);
         } else if (nextChar == '/') {
            nextInd = parseBlockComment(aChars, nextInd);
         } else if (!Character.isWhitespace(nextChar)) {
            break;
         }
      }

      return nextInd + 2 < aChars.length && parseAsKeyword(aChars, nextInd) && !isIdentifierContChar(aChars[nextInd + 2]) ? null : command;
   }

   private static boolean addReturning(StringBuilder nativeSql, SqlCommandType currentCommandType, String[] returningColumnNames, boolean isReturningPresent, boolean quoteReturningIdentifiers) throws SQLException {
      if (!isReturningPresent && returningColumnNames.length != 0) {
         if (currentCommandType != SqlCommandType.INSERT && currentCommandType != SqlCommandType.UPDATE && currentCommandType != SqlCommandType.DELETE && currentCommandType != SqlCommandType.WITH) {
            return false;
         } else {
            nativeSql.append("\nRETURNING ");
            if (returningColumnNames.length == 1 && returningColumnNames[0].charAt(0) == '*') {
               nativeSql.append('*');
               return true;
            } else {
               for(int col = 0; col < returningColumnNames.length; ++col) {
                  String columnName = returningColumnNames[col];
                  if (col > 0) {
                     nativeSql.append(", ");
                  }

                  if (quoteReturningIdentifiers) {
                     Utils.escapeIdentifier(nativeSql, columnName);
                  } else {
                     nativeSql.append(columnName);
                  }
               }

               return true;
            }
         }
      } else {
         return false;
      }
   }

   @Nullable
   private static int[] toIntArray(@Nullable IntList list) {
      return list == null ? null : list.toArray();
   }

   public static int parseSingleQuotes(char[] query, int offset, boolean standardConformingStrings) {
      if (standardConformingStrings && offset >= 2 && (query[offset - 1] == 'e' || query[offset - 1] == 'E') && charTerminatesIdentifier(query[offset - 2])) {
         standardConformingStrings = false;
      }

      if (standardConformingStrings) {
         while(true) {
            ++offset;
            if (offset >= query.length) {
               break;
            }

            if (query[offset] == '\'') {
               return offset;
            }
         }
      } else {
         while(true) {
            ++offset;
            if (offset >= query.length) {
               break;
            }

            switch(query[offset]) {
            case '\'':
               return offset;
            case '\\':
               ++offset;
            }
         }
      }

      return query.length;
   }

   public static int parseDoubleQuotes(char[] query, int offset) {
      do {
         ++offset;
      } while(offset < query.length && query[offset] != '"');

      return offset;
   }

   public static int parseDollarQuotes(char[] query, int offset) {
      if (offset + 1 < query.length && (offset == 0 || !isIdentifierContChar(query[offset - 1]))) {
         int endIdx = -1;
         int d;
         if (query[offset + 1] == '$') {
            endIdx = offset + 1;
         } else if (isDollarQuoteStartChar(query[offset + 1])) {
            for(d = offset + 2; d < query.length; ++d) {
               if (query[d] == '$') {
                  endIdx = d;
                  break;
               }

               if (!isDollarQuoteContChar(query[d])) {
                  break;
               }
            }
         }

         if (endIdx > 0) {
            d = offset;
            int tagLen = endIdx - offset + 1;

            for(offset = endIdx + 1; offset < query.length; ++offset) {
               if (query[offset] == '$' && subArraysEqual(query, d, offset, tagLen)) {
                  offset += tagLen - 1;
                  break;
               }
            }
         }
      }

      return offset;
   }

   public static int parseLineComment(char[] query, int offset) {
      if (offset + 1 < query.length && query[offset + 1] == '-') {
         while(offset + 1 < query.length) {
            ++offset;
            if (query[offset] == '\r' || query[offset] == '\n') {
               break;
            }
         }
      }

      return offset;
   }

   public static int parseBlockComment(char[] query, int offset) {
      if (offset + 1 < query.length && query[offset + 1] == '*') {
         int level = 1;

         for(offset += 2; offset < query.length; ++offset) {
            switch(query[offset - 1]) {
            case '*':
               if (query[offset] == '/') {
                  --level;
                  ++offset;
               }
               break;
            case '/':
               if (query[offset] == '*') {
                  ++level;
                  ++offset;
               }
            }

            if (level == 0) {
               --offset;
               break;
            }
         }
      }

      return offset;
   }

   public static boolean parseDeleteKeyword(char[] query, int offset) {
      if (query.length < offset + 6) {
         return false;
      } else {
         return (query[offset] | 32) == 100 && (query[offset + 1] | 32) == 101 && (query[offset + 2] | 32) == 108 && (query[offset + 3] | 32) == 101 && (query[offset + 4] | 32) == 116 && (query[offset + 5] | 32) == 101;
      }
   }

   public static boolean parseInsertKeyword(char[] query, int offset) {
      if (query.length < offset + 7) {
         return false;
      } else {
         return (query[offset] | 32) == 105 && (query[offset + 1] | 32) == 110 && (query[offset + 2] | 32) == 115 && (query[offset + 3] | 32) == 101 && (query[offset + 4] | 32) == 114 && (query[offset + 5] | 32) == 116;
      }
   }

   public static boolean parseBeginKeyword(char[] query, int offset) {
      if (query.length < offset + 6) {
         return false;
      } else {
         return (query[offset] | 32) == 98 && (query[offset + 1] | 32) == 101 && (query[offset + 2] | 32) == 103 && (query[offset + 3] | 32) == 105 && (query[offset + 4] | 32) == 110;
      }
   }

   public static boolean parseAtomicKeyword(char[] query, int offset) {
      if (query.length < offset + 7) {
         return false;
      } else {
         return (query[offset] | 32) == 97 && (query[offset + 1] | 32) == 116 && (query[offset + 2] | 32) == 111 && (query[offset + 3] | 32) == 109 && (query[offset + 4] | 32) == 105 && (query[offset + 5] | 32) == 99;
      }
   }

   public static boolean parseMoveKeyword(char[] query, int offset) {
      if (query.length < offset + 4) {
         return false;
      } else {
         return (query[offset] | 32) == 109 && (query[offset + 1] | 32) == 111 && (query[offset + 2] | 32) == 118 && (query[offset + 3] | 32) == 101;
      }
   }

   public static boolean parseReturningKeyword(char[] query, int offset) {
      if (query.length < offset + 9) {
         return false;
      } else {
         return (query[offset] | 32) == 114 && (query[offset + 1] | 32) == 101 && (query[offset + 2] | 32) == 116 && (query[offset + 3] | 32) == 117 && (query[offset + 4] | 32) == 114 && (query[offset + 5] | 32) == 110 && (query[offset + 6] | 32) == 105 && (query[offset + 7] | 32) == 110 && (query[offset + 8] | 32) == 103;
      }
   }

   public static boolean parseSelectKeyword(char[] query, int offset) {
      if (query.length < offset + 6) {
         return false;
      } else {
         return (query[offset] | 32) == 115 && (query[offset + 1] | 32) == 101 && (query[offset + 2] | 32) == 108 && (query[offset + 3] | 32) == 101 && (query[offset + 4] | 32) == 99 && (query[offset + 5] | 32) == 116;
      }
   }

   public static boolean parseAlterKeyword(char[] query, int offset) {
      if (query.length < offset + 5) {
         return false;
      } else {
         return (query[offset] | 32) == 97 && (query[offset + 1] | 32) == 108 && (query[offset + 2] | 32) == 116 && (query[offset + 3] | 32) == 101 && (query[offset + 4] | 32) == 114;
      }
   }

   public static boolean parseCreateKeyword(char[] query, int offset) {
      if (query.length < offset + 6) {
         return false;
      } else {
         return (query[offset] | 32) == 99 && (query[offset + 1] | 32) == 114 && (query[offset + 2] | 32) == 101 && (query[offset + 3] | 32) == 97 && (query[offset + 4] | 32) == 116 && (query[offset + 5] | 32) == 101;
      }
   }

   public static boolean parseUpdateKeyword(char[] query, int offset) {
      if (query.length < offset + 6) {
         return false;
      } else {
         return (query[offset] | 32) == 117 && (query[offset + 1] | 32) == 112 && (query[offset + 2] | 32) == 100 && (query[offset + 3] | 32) == 97 && (query[offset + 4] | 32) == 116 && (query[offset + 5] | 32) == 101;
      }
   }

   public static boolean parseValuesKeyword(char[] query, int offset) {
      if (query.length < offset + 6) {
         return false;
      } else {
         return (query[offset] | 32) == 118 && (query[offset + 1] | 32) == 97 && (query[offset + 2] | 32) == 108 && (query[offset + 3] | 32) == 117 && (query[offset + 4] | 32) == 101 && (query[offset + 5] | 32) == 115;
      }
   }

   public static long parseLong(String s, int beginIndex, int endIndex) {
      if (endIndex - beginIndex > 16) {
         return Long.parseLong(s.substring(beginIndex, endIndex));
      } else {
         long res = (long)digitAt(s, beginIndex);
         ++beginIndex;

         while(beginIndex < endIndex) {
            res = res * 10L + (long)digitAt(s, beginIndex);
            ++beginIndex;
         }

         return res;
      }
   }

   public static boolean parseWithKeyword(char[] query, int offset) {
      if (query.length < offset + 4) {
         return false;
      } else {
         return (query[offset] | 32) == 119 && (query[offset + 1] | 32) == 105 && (query[offset + 2] | 32) == 116 && (query[offset + 3] | 32) == 104;
      }
   }

   public static boolean parseAsKeyword(char[] query, int offset) {
      if (query.length < offset + 2) {
         return false;
      } else {
         return (query[offset] | 32) == 97 && (query[offset + 1] | 32) == 115;
      }
   }

   public static boolean isDigitAt(String s, int pos) {
      return pos > 0 && pos < s.length() && Character.isDigit(s.charAt(pos));
   }

   public static int digitAt(String s, int pos) {
      int c = s.charAt(pos) - 48;
      if (c >= 0 && c <= 9) {
         return c;
      } else {
         throw new NumberFormatException("Input string: \"" + s + "\", position: " + pos);
      }
   }

   public static boolean isSpace(char c) {
      return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f';
   }

   public static boolean isArrayWhiteSpace(char c) {
      return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f' || c == 11;
   }

   public static boolean isOperatorChar(char c) {
      return ",()[].;:+-*/%^<>=~!@#&|`?".indexOf(c) != -1;
   }

   public static boolean isIdentifierStartChar(char c) {
      return Character.isJavaIdentifierStart(c);
   }

   public static boolean isIdentifierContChar(char c) {
      return Character.isJavaIdentifierPart(c);
   }

   public static boolean charTerminatesIdentifier(char c) {
      return c == '"' || isSpace(c) || isOperatorChar(c);
   }

   public static boolean isDollarQuoteStartChar(char c) {
      return c != '$' && isIdentifierStartChar(c);
   }

   public static boolean isDollarQuoteContChar(char c) {
      return c != '$' && isIdentifierContChar(c);
   }

   private static boolean subArraysEqual(char[] arr, int offA, int offB, int len) {
      if (offA >= 0 && offB >= 0 && offA < arr.length && offB < arr.length && offA + len <= arr.length && offB + len <= arr.length) {
         for(int i = 0; i < len; ++i) {
            if (arr[offA + i] != arr[offB + i]) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static JdbcCallParseInfo modifyJdbcCall(String jdbcSql, boolean stdStrings, int serverVersion, int protocolVersion, EscapeSyntaxCallMode escapeSyntaxCallMode) throws SQLException {
      boolean isFunction = false;
      boolean outParamBeforeFunc = false;
      int len = jdbcSql.length();
      int state = 1;
      boolean inQuotes = false;
      boolean inEscape = false;
      int startIndex = -1;
      int endIndex = -1;
      boolean syntaxError = false;
      int i = 0;

      char ch;
      while(i < len && !syntaxError) {
         ch = jdbcSql.charAt(i);
         switch(state) {
         case 1:
            if (ch == '{') {
               ++i;
               ++state;
            } else if (Character.isWhitespace(ch)) {
               ++i;
            } else {
               i = len;
            }
            break;
         case 2:
            if (ch == '?') {
               isFunction = true;
               outParamBeforeFunc = true;
               ++i;
               ++state;
            } else {
               if (ch != 'c' && ch != 'C') {
                  if (Character.isWhitespace(ch)) {
                     ++i;
                  } else {
                     syntaxError = true;
                  }
                  continue;
               }

               state += 3;
            }
            break;
         case 3:
            if (ch == '=') {
               ++i;
               ++state;
            } else if (Character.isWhitespace(ch)) {
               ++i;
            } else {
               syntaxError = true;
            }
            break;
         case 4:
            if (ch != 'c' && ch != 'C') {
               if (Character.isWhitespace(ch)) {
                  ++i;
               } else {
                  syntaxError = true;
               }
               break;
            }

            ++state;
            break;
         case 5:
            if ((ch == 'c' || ch == 'C') && i + 4 <= len && "call".equalsIgnoreCase(jdbcSql.substring(i, i + 4))) {
               isFunction = true;
               i += 4;
               ++state;
            } else if (Character.isWhitespace(ch)) {
               ++i;
            } else {
               syntaxError = true;
            }
            break;
         case 6:
            if (Character.isWhitespace(ch)) {
               ++i;
               ++state;
               startIndex = i;
            } else {
               syntaxError = true;
            }
            break;
         case 7:
            if (ch == '\'') {
               inQuotes = !inQuotes;
               ++i;
            } else if (inQuotes && ch == '\\' && !stdStrings) {
               i += 2;
            } else if (!inQuotes && ch == '{') {
               inEscape = !inEscape;
               ++i;
            } else if (!inQuotes && ch == '}') {
               if (!inEscape) {
                  endIndex = i++;
                  ++state;
               } else {
                  inEscape = false;
               }
            } else {
               if (!inQuotes && ch == ';') {
                  syntaxError = true;
                  continue;
               }

               ++i;
            }
            break;
         case 8:
            if (Character.isWhitespace(ch)) {
               ++i;
            } else {
               syntaxError = true;
            }
            break;
         default:
            throw new IllegalStateException("somehow got into bad state " + state);
         }
      }

      if (i == len && !syntaxError) {
         if (state == 1) {
            for(i = 0; i < len && Character.isWhitespace(jdbcSql.charAt(i)); ++i) {
            }

            if (i < len - 5) {
               ch = jdbcSql.charAt(i);
               if ((ch == 'c' || ch == 'C') && "call".equalsIgnoreCase(jdbcSql.substring(i, i + 4)) && Character.isWhitespace(jdbcSql.charAt(i + 4))) {
                  isFunction = true;
               }
            }

            return new JdbcCallParseInfo(jdbcSql, isFunction);
         }

         if (state != 8) {
            syntaxError = true;
         }
      }

      if (syntaxError) {
         throw new PSQLException(GT.tr("Malformed function or procedure escape syntax at offset {0}.", i), PSQLState.STATEMENT_NOT_ALLOWED_IN_FUNCTION_CALL);
      } else {
         String suffix;
         String prefix;
         if (escapeSyntaxCallMode != EscapeSyntaxCallMode.SELECT && serverVersion >= 110000 && (!outParamBeforeFunc || escapeSyntaxCallMode != EscapeSyntaxCallMode.CALL_IF_NO_RETURN)) {
            prefix = "call ";
            suffix = "";
         } else {
            prefix = "select * from ";
            suffix = " as result";
         }

         String s = jdbcSql.substring(startIndex, endIndex);
         int prefixLength = prefix.length();
         StringBuilder sb = new StringBuilder(prefixLength + jdbcSql.length() + suffix.length() + 10);
         sb.append(prefix);
         sb.append(s);
         int opening = s.indexOf(40) + 1;
         if (opening == 0) {
            sb.append(outParamBeforeFunc ? "(?)" : "()");
         } else if (outParamBeforeFunc) {
            boolean needComma = false;

            for(int j = opening + prefixLength; j < sb.length(); ++j) {
               char c = sb.charAt(j);
               if (c == ')') {
                  break;
               }

               if (!Character.isWhitespace(c)) {
                  needComma = true;
                  break;
               }
            }

            if (needComma) {
               sb.insert(opening + prefixLength, "?,");
            } else {
               sb.insert(opening + prefixLength, "?");
            }
         }

         String sql;
         if (!suffix.isEmpty()) {
            sql = sb.append(suffix).toString();
         } else {
            sql = sb.toString();
         }

         return new JdbcCallParseInfo(sql, isFunction);
      }
   }

   public static String replaceProcessing(String sql, boolean replaceProcessingEnabled, boolean standardConformingStrings) throws SQLException {
      if (replaceProcessingEnabled) {
         int len = sql.length();
         char[] chars = sql.toCharArray();
         StringBuilder newsql = new StringBuilder(len);
         int i = 0;

         while(i < len) {
            i = parseSql(chars, i, newsql, false, standardConformingStrings);
            if (i < len) {
               newsql.append(chars[i]);
               ++i;
            }
         }

         return newsql.toString();
      } else {
         return sql;
      }
   }

   private static int parseSql(char[] sql, int i, StringBuilder newsql, boolean stopOnComma, boolean stdStrings) throws SQLException {
      Parser.SqlParseState state = Parser.SqlParseState.IN_SQLCODE;
      int len = sql.length;
      int nestedParenthesis = 0;
      boolean endOfNested = false;
      --i;

      label82:
      while(!endOfNested) {
         ++i;
         if (i >= len) {
            break;
         }

         char c = sql[i];
         switch(state) {
         case IN_SQLCODE:
            int i0;
            if (c == '$') {
               i0 = i;
               i = parseDollarQuotes(sql, i);
               checkParsePosition(i, len, i0, sql, "Unterminated dollar quote started at position {0} in SQL {1}. Expected terminating $$");
               newsql.append(sql, i0, i - i0 + 1);
            } else if (c == '\'') {
               i0 = i;
               i = parseSingleQuotes(sql, i, stdStrings);
               checkParsePosition(i, len, i0, sql, "Unterminated string literal started at position {0} in SQL {1}. Expected ' char");
               newsql.append(sql, i0, i - i0 + 1);
            } else if (c == '"') {
               i0 = i;
               i = parseDoubleQuotes(sql, i);
               checkParsePosition(i, len, i0, sql, "Unterminated identifier started at position {0} in SQL {1}. Expected \" char");
               newsql.append(sql, i0, i - i0 + 1);
            } else if (c == '/') {
               i0 = i;
               i = parseBlockComment(sql, i);
               checkParsePosition(i, len, i0, sql, "Unterminated block comment started at position {0} in SQL {1}. Expected */ sequence");
               newsql.append(sql, i0, i - i0 + 1);
            } else if (c == '-') {
               i0 = i;
               i = parseLineComment(sql, i);
               newsql.append(sql, i0, i - i0 + 1);
            } else {
               if (c == '(') {
                  ++nestedParenthesis;
               } else if (c == ')') {
                  --nestedParenthesis;
                  if (nestedParenthesis < 0) {
                     endOfNested = true;
                     continue;
                  }
               } else {
                  if (stopOnComma && c == ',' && nestedParenthesis == 0) {
                     endOfNested = true;
                     continue;
                  }

                  if (c == '{' && i + 1 < len) {
                     Parser.SqlParseState[] availableStates = Parser.SqlParseState.VALUES;

                     for(int j = 1; j < availableStates.length; ++j) {
                        Parser.SqlParseState availableState = availableStates[j];
                        int matchedPosition = availableState.getMatchedPosition(sql, i + 1);
                        if (matchedPosition != 0) {
                           i += matchedPosition;
                           if (availableState.replacementKeyword != null) {
                              newsql.append(availableState.replacementKeyword);
                           }

                           state = availableState;
                           continue label82;
                        }
                     }
                  }
               }

               newsql.append(c);
            }
            break;
         case ESC_FUNCTION:
            i = escapeFunction(sql, i, newsql, stdStrings);
            state = Parser.SqlParseState.IN_SQLCODE;
            break;
         case ESC_DATE:
         case ESC_TIME:
         case ESC_TIMESTAMP:
         case ESC_OUTERJOIN:
         case ESC_ESCAPECHAR:
            if (c == '}') {
               state = Parser.SqlParseState.IN_SQLCODE;
            } else {
               newsql.append(c);
            }
         }
      }

      return i;
   }

   private static int findOpenParenthesis(char[] sql, int i) {
      int posArgs;
      for(posArgs = i; posArgs < sql.length && sql[posArgs] != '('; ++posArgs) {
      }

      return posArgs;
   }

   private static void checkParsePosition(int i, int len, int i0, char[] sql, String message) throws PSQLException {
      if (i >= len) {
         throw new PSQLException(GT.tr(message, i0, new String(sql)), PSQLState.SYNTAX_ERROR);
      }
   }

   private static int escapeFunction(char[] sql, int i, StringBuilder newsql, boolean stdStrings) throws SQLException {
      int argPos = findOpenParenthesis(sql, i);
      if (argPos < sql.length) {
         String functionName = (new String(sql, i, argPos - i)).trim();
         i = argPos + 1;
         i = escapeFunctionArguments(newsql, functionName, sql, i, stdStrings);
      }

      ++i;

      while(i < sql.length && sql[i] != '}') {
         newsql.append(sql[i++]);
      }

      return i;
   }

   private static int escapeFunctionArguments(StringBuilder newsql, String functionName, char[] sql, int i, boolean stdStrings) throws SQLException {
      ArrayList parsedArgs = new ArrayList(3);

      while(true) {
         StringBuilder arg = new StringBuilder();
         int lastPos = i;
         i = parseSql(sql, i, arg, true, stdStrings);
         if (i != lastPos) {
            parsedArgs.add(arg);
         }

         if (i >= sql.length || sql[i] != ',') {
            Method method = EscapedFunctions2.getFunction(functionName);
            if (method == null) {
               newsql.append(functionName);
               EscapedFunctions2.appendCall(newsql, "(", ",", ")", parsedArgs);
               return i;
            } else {
               try {
                  method.invoke((Object)null, newsql, parsedArgs);
                  return i;
               } catch (InvocationTargetException var10) {
                  Throwable targetException = var10.getTargetException();
                  if (targetException instanceof SQLException) {
                     throw (SQLException)targetException;
                  } else {
                     String message = targetException == null ? "no message" : targetException.getMessage();
                     throw new PSQLException(message, PSQLState.SYSTEM_ERROR);
                  }
               } catch (IllegalAccessException var11) {
                  throw new PSQLException(var11.getMessage(), PSQLState.SYSTEM_ERROR);
               }
            }
         }

         ++i;
      }
   }

   private static enum SqlParseState {
      IN_SQLCODE,
      ESC_DATE("d", Parser.SINGLE_QUOTE, "DATE "),
      ESC_TIME("t", Parser.SINGLE_QUOTE, "TIME "),
      ESC_TIMESTAMP("ts", Parser.SINGLE_QUOTE, "TIMESTAMP "),
      ESC_FUNCTION("fn", Parser.QUOTE_OR_ALPHABETIC_MARKER, (String)null),
      ESC_OUTERJOIN("oj", Parser.QUOTE_OR_ALPHABETIC_MARKER_OR_PARENTHESIS, (String)null),
      ESC_ESCAPECHAR("escape", Parser.SINGLE_QUOTE, "ESCAPE ");

      private static final Parser.SqlParseState[] VALUES = values();
      private final char[] escapeKeyword;
      private final char[] allowedValues;
      @Nullable
      private final String replacementKeyword;

      private SqlParseState() {
         this("", new char[0], (String)null);
      }

      private SqlParseState(String escapeKeyword, char[] allowedValues, @Nullable String replacementKeyword) {
         this.escapeKeyword = escapeKeyword.toCharArray();
         this.allowedValues = allowedValues;
         this.replacementKeyword = replacementKeyword;
      }

      private boolean startMatches(char[] sql, int pos) {
         char[] var3 = this.escapeKeyword;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            char c = var3[var5];
            if (pos >= sql.length) {
               return false;
            }

            char curr = sql[pos++];
            if (curr != c && curr != Character.toUpperCase(c)) {
               return false;
            }
         }

         return pos < sql.length;
      }

      private int getMatchedPosition(char[] sql, int pos) {
         if (!this.startMatches(sql, pos)) {
            return 0;
         } else {
            int newPos = pos + this.escapeKeyword.length;

            char curr;
            for(curr = sql[newPos]; curr == ' '; curr = sql[newPos]) {
               ++newPos;
               if (newPos >= sql.length) {
                  return 0;
               }
            }

            char[] var5 = this.allowedValues;
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               char c = var5[var7];
               if (curr == c || c == '0' && Character.isLetter(curr)) {
                  return newPos - pos;
               }
            }

            return 0;
         }
      }

      // $FF: synthetic method
      private static Parser.SqlParseState[] $values() {
         return new Parser.SqlParseState[]{IN_SQLCODE, ESC_DATE, ESC_TIME, ESC_TIMESTAMP, ESC_FUNCTION, ESC_OUTERJOIN, ESC_ESCAPECHAR};
      }
   }
}
