package fr.xephi.authme.libs.org.mariadb.jdbc.util;

import fr.xephi.authme.libs.org.mariadb.jdbc.client.Context;
import java.sql.SQLException;
import java.util.Locale;

public final class NativeSql {
   public static String parse(String sql, Context context) throws SQLException {
      if (!sql.contains("{")) {
         return sql;
      } else {
         ClientParser.LexState state = ClientParser.LexState.Normal;
         char lastChar = 0;
         boolean singleQuotes = false;
         int lastEscapePart = 0;
         StringBuilder sb = new StringBuilder();
         char[] query = sql.toCharArray();
         int queryLength = query.length;
         int escapeIdx = 0;
         boolean inEscape = false;

         for(int idx = 0; idx < queryLength; ++idx) {
            char car = query[idx];
            if (state == ClientParser.LexState.Escape && (car != '\'' || !singleQuotes) && (car != '"' || singleQuotes)) {
               state = ClientParser.LexState.String;
               if (!inEscape) {
                  sb.append(car);
               }

               lastChar = car;
            } else {
               switch(car) {
               case '\n':
                  if (state == ClientParser.LexState.EOLComment) {
                     state = ClientParser.LexState.Normal;
                  }
                  break;
               case '"':
                  if (state == ClientParser.LexState.Normal) {
                     state = ClientParser.LexState.String;
                     singleQuotes = false;
                  } else if (state == ClientParser.LexState.String && !singleQuotes) {
                     state = ClientParser.LexState.Normal;
                  } else if (state == ClientParser.LexState.Escape) {
                     state = ClientParser.LexState.String;
                  }
                  break;
               case '#':
                  if (state == ClientParser.LexState.Normal) {
                     state = ClientParser.LexState.EOLComment;
                  }
                  break;
               case '\'':
                  if (state == ClientParser.LexState.Normal) {
                     state = ClientParser.LexState.String;
                     singleQuotes = true;
                  } else if (state == ClientParser.LexState.String && singleQuotes) {
                     state = ClientParser.LexState.Normal;
                  } else if (state == ClientParser.LexState.Escape) {
                     state = ClientParser.LexState.String;
                  }
                  break;
               case '*':
                  if (state == ClientParser.LexState.Normal && lastChar == '/') {
                     state = ClientParser.LexState.SlashStarComment;
                  }
                  break;
               case '-':
                  if (state == ClientParser.LexState.Normal && lastChar == '-') {
                     state = ClientParser.LexState.EOLComment;
                  }
                  break;
               case '/':
                  if (state == ClientParser.LexState.SlashStarComment && lastChar == '*') {
                     state = ClientParser.LexState.Normal;
                  } else if (state == ClientParser.LexState.Normal && lastChar == '/') {
                     state = ClientParser.LexState.EOLComment;
                  }
                  break;
               case '\\':
                  if (state == ClientParser.LexState.String) {
                     state = ClientParser.LexState.Escape;
                  }
                  break;
               case '`':
                  if (state == ClientParser.LexState.Backtick) {
                     state = ClientParser.LexState.Normal;
                  } else if (state == ClientParser.LexState.Normal) {
                     state = ClientParser.LexState.Backtick;
                  }
                  break;
               case '{':
                  if (state == ClientParser.LexState.Normal) {
                     if (!inEscape) {
                        inEscape = true;
                        lastEscapePart = idx;
                     }

                     ++escapeIdx;
                  }
                  break;
               case '}':
                  if (state == ClientParser.LexState.Normal && inEscape) {
                     --escapeIdx;
                     if (escapeIdx == 0) {
                        String str = sql.substring(lastEscapePart, idx + 1);
                        String escapedSeq = resolveEscapes(str, context);
                        sb.append(escapedSeq);
                        inEscape = false;
                        continue;
                     }
                  }
               }

               if (!inEscape) {
                  sb.append(car);
               }

               lastChar = car;
            }
         }

         if (inEscape) {
            throw new SQLException("Invalid escape sequence , missing closing '}' character in '" + sql + "'");
         } else {
            return sb.toString();
         }
      }
   }

   private static String resolveEscapes(String escaped, Context context) throws SQLException {
      int endIndex = escaped.length() - 1;
      if (escaped.startsWith("{fn ")) {
         String resolvedParams = replaceFunctionParameter(escaped.substring(4, endIndex), context);
         return parse(resolvedParams, context);
      } else if (escaped.startsWith("{oj ")) {
         return parse(escaped.substring(4, endIndex), context);
      } else if (escaped.startsWith("{d ")) {
         return escaped.substring(3, endIndex);
      } else if (escaped.startsWith("{t ")) {
         return escaped.substring(3, endIndex);
      } else if (escaped.startsWith("{ts ")) {
         return escaped.substring(4, endIndex);
      } else if (escaped.startsWith("{d'")) {
         return escaped.substring(2, endIndex);
      } else if (escaped.startsWith("{t'")) {
         return escaped.substring(2, endIndex);
      } else if (escaped.startsWith("{ts'")) {
         return escaped.substring(3, endIndex);
      } else if (!escaped.startsWith("{call ") && !escaped.startsWith("{CALL ")) {
         if (escaped.startsWith("{?")) {
            return parse(escaped.substring(1, endIndex), context);
         } else {
            int i;
            if (!escaped.startsWith("{ ") && !escaped.startsWith("{\n")) {
               if (escaped.startsWith("{\r\n")) {
                  for(i = 3; i < escaped.length(); ++i) {
                     if (!Character.isWhitespace(escaped.charAt(i))) {
                        return resolveEscapes("{" + escaped.substring(i), context);
                     }
                  }
               }
            } else {
               for(i = 2; i < escaped.length(); ++i) {
                  if (!Character.isWhitespace(escaped.charAt(i))) {
                     return resolveEscapes("{" + escaped.substring(i), context);
                  }
               }
            }

            throw new SQLException("unknown escape sequence " + escaped);
         }
      } else {
         return parse(escaped.substring(1, endIndex), context);
      }
   }

   private static String replaceFunctionParameter(String functionString, Context context) {
      char[] input = functionString.toCharArray();
      StringBuilder sb = new StringBuilder();

      int index;
      for(index = 0; index < input.length && input[index] == ' '; ++index) {
      }

      while(index < input.length && (input[index] >= 'a' && input[index] <= 'z' || input[index] >= 'A' && input[index] <= 'Z')) {
         sb.append(input[index]);
         ++index;
      }

      String func = sb.toString().toLowerCase(Locale.ROOT);
      byte var7 = -1;
      switch(func.hashCode()) {
      case 951590323:
         if (func.equals("convert")) {
            var7 = 0;
         }
         break;
      case 1590013899:
         if (func.equals("timestampadd")) {
            var7 = 2;
         }
         break;
      case 2045884955:
         if (func.equals("timestampdiff")) {
            var7 = 1;
         }
      }

      switch(var7) {
      case 0:
         int lastCommaIndex = functionString.lastIndexOf(44);
         int firstParentheses = functionString.indexOf(40);
         String value = functionString.substring(firstParentheses + 1, lastCommaIndex);

         for(index = lastCommaIndex + 1; index < input.length && Character.isWhitespace(input[index]); ++index) {
         }

         int endParam;
         for(endParam = index + 1; endParam < input.length && (input[endParam] >= 'a' && input[endParam] <= 'z' || input[endParam] >= 'A' && input[endParam] <= 'Z' || input[endParam] == '_'); ++endParam) {
         }

         String typeParam = (new String(input, index, endParam - index)).toUpperCase(Locale.ROOT);
         if (typeParam.startsWith("SQL_")) {
            typeParam = typeParam.substring(4);
         }

         byte var14 = -1;
         switch(typeParam.hashCode()) {
         case -1841591415:
            if (typeParam.equals("SQLXML")) {
               var14 = 17;
            }
            break;
         case -1783518776:
            if (typeParam.equals("VARBINARY")) {
               var14 = 6;
            }
            break;
         case -1722570076:
            if (typeParam.equals("DATALINK")) {
               var14 = 12;
            }
            break;
         case -1473435317:
            if (typeParam.equals("LONGNVARCHAR")) {
               var14 = 16;
            }
            break;
         case -1453246218:
            if (typeParam.equals("TIMESTAMP")) {
               var14 = 23;
            }
            break;
         case -1366907992:
            if (typeParam.equals("LONGNCHAR")) {
               var14 = 18;
            }
            break;
         case -1282431251:
            if (typeParam.equals("NUMERIC")) {
               var14 = 22;
            }
            break;
         case -876463903:
            if (typeParam.equals("LONGVARCHAR")) {
               var14 = 15;
            }
            break;
         case -594415409:
            if (typeParam.equals("TINYINT")) {
               var14 = 3;
            }
            break;
         case -545151281:
            if (typeParam.equals("NVARCHAR")) {
               var14 = 14;
            }
            break;
         case -495552820:
            if (typeParam.equals("LONGVARBINARY")) {
               var14 = 7;
            }
            break;
         case 65773:
            if (typeParam.equals("BIT")) {
               var14 = 4;
            }
            break;
         case 2041757:
            if (typeParam.equals("BLOB")) {
               var14 = 5;
            }
            break;
         case 2071548:
            if (typeParam.equals("CLOB")) {
               var14 = 10;
            }
            break;
         case 2511262:
            if (typeParam.equals("REAL")) {
               var14 = 21;
            }
            break;
         case 66988604:
            if (typeParam.equals("FLOAT")) {
               var14 = 20;
            }
            break;
         case 74101924:
            if (typeParam.equals("NCHAR")) {
               var14 = 9;
            }
            break;
         case 74106186:
            if (typeParam.equals("NCLOB")) {
               var14 = 11;
            }
            break;
         case 78168149:
            if (typeParam.equals("ROWID")) {
               var14 = 8;
            }
            break;
         case 176095624:
            if (typeParam.equals("SMALLINT")) {
               var14 = 2;
            }
            break;
         case 782694408:
            if (typeParam.equals("BOOLEAN")) {
               var14 = 0;
            }
            break;
         case 954596061:
            if (typeParam.equals("VARCHAR")) {
               var14 = 13;
            }
            break;
         case 1959128815:
            if (typeParam.equals("BIGINT")) {
               var14 = 1;
            }
            break;
         case 2022338513:
            if (typeParam.equals("DOUBLE")) {
               var14 = 19;
            }
         }

         switch(var14) {
         case 0:
            return "1=" + value;
         case 1:
         case 2:
         case 3:
            typeParam = "SIGNED INTEGER";
            break;
         case 4:
            typeParam = "UNSIGNED INTEGER";
            break;
         case 5:
         case 6:
         case 7:
         case 8:
            typeParam = "BINARY";
            break;
         case 9:
         case 10:
         case 11:
         case 12:
         case 13:
         case 14:
         case 15:
         case 16:
         case 17:
         case 18:
            typeParam = "CHAR";
            break;
         case 19:
         case 20:
            if (!context.getVersion().isMariaDBServer() && !context.getVersion().versionGreaterOrEqual(8, 0, 17)) {
               return "0.0+" + value;
            }

            typeParam = "DOUBLE";
            break;
         case 21:
         case 22:
            typeParam = "DECIMAL";
            break;
         case 23:
            typeParam = "DATETIME";
         }

         return new String(input, 0, index) + typeParam + new String(input, endParam, input.length - endParam);
      case 1:
      case 2:
         while(index < input.length && (Character.isWhitespace(input[index]) || input[index] == '(')) {
            ++index;
         }

         if (index < input.length - 8) {
            String paramPrefix = new String(input, index, 8);
            if ("SQL_TSI_".equals(paramPrefix)) {
               return new String(input, 0, index) + new String(input, index + 8, input.length - (index + 8));
            }
         }

         return functionString;
      default:
         return functionString;
      }
   }
}
