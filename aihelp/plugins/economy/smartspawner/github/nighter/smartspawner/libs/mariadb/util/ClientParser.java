package github.nighter.smartspawner.libs.mariadb.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class ClientParser implements PrepareResult {
   private final String sql;
   private final byte[] query;
   private final List<Integer> paramPositions;
   private final List<Integer> valuesBracketPositions;
   private final int paramCount;
   private final boolean isInsert;
   private final boolean isInsertDuplicate;
   private final boolean isMultiQuery;

   private ClientParser(String sql, byte[] query, List<Integer> paramPositions, List<Integer> valuesBracketPositions, boolean isInsert, boolean isInsertDuplicate, boolean isMultiQuery) {
      this.sql = sql;
      this.query = query;
      this.paramPositions = paramPositions;
      this.valuesBracketPositions = valuesBracketPositions;
      this.paramCount = paramPositions.size();
      this.isInsert = isInsert;
      this.isInsertDuplicate = isInsertDuplicate;
      this.isMultiQuery = isMultiQuery;
   }

   public static ClientParser parameterParts(String queryString, boolean noBackslashEscapes) {
      List<Integer> paramPositions = new ArrayList(20);
      ClientParser.LexState state = ClientParser.LexState.Normal;
      byte lastChar = 0;
      boolean singleQuotes = false;
      boolean isInsert = false;
      boolean isInsertDuplicate = false;
      int multiQueryIdx = -1;
      byte[] query = queryString.getBytes(StandardCharsets.UTF_8);
      int queryLength = query.length;

      for(int i = 0; i < queryLength; ++i) {
         byte car = query[i];
         if (state != ClientParser.LexState.Escape || car == 39 && singleQuotes || car == 34 && !singleQuotes) {
            switch(car) {
            case 10:
               if (state == ClientParser.LexState.EOLComment) {
                  state = ClientParser.LexState.Normal;
               }
               break;
            case 34:
               if (state == ClientParser.LexState.Normal) {
                  state = ClientParser.LexState.String;
                  singleQuotes = false;
               } else if (state == ClientParser.LexState.String && !singleQuotes) {
                  state = ClientParser.LexState.Normal;
               } else if (state == ClientParser.LexState.Escape) {
                  state = ClientParser.LexState.String;
               }
               break;
            case 35:
               if (state == ClientParser.LexState.Normal) {
                  state = ClientParser.LexState.EOLComment;
               }
               break;
            case 39:
               if (state == ClientParser.LexState.Normal) {
                  state = ClientParser.LexState.String;
                  singleQuotes = true;
               } else if (state == ClientParser.LexState.String && singleQuotes) {
                  state = ClientParser.LexState.Normal;
               } else if (state == ClientParser.LexState.Escape) {
                  state = ClientParser.LexState.String;
               }
               break;
            case 42:
               if (state == ClientParser.LexState.Normal && lastChar == 47) {
                  state = ClientParser.LexState.SlashStarComment;
               }
               break;
            case 45:
               if (state == ClientParser.LexState.Normal && lastChar == 45) {
                  state = ClientParser.LexState.EOLComment;
               }
               break;
            case 47:
               if (state == ClientParser.LexState.SlashStarComment && lastChar == 42) {
                  state = ClientParser.LexState.Normal;
               }
               break;
            case 59:
               if (state == ClientParser.LexState.Normal && multiQueryIdx == -1) {
                  multiQueryIdx = i;
               }
               break;
            case 63:
               if (state == ClientParser.LexState.Normal) {
                  paramPositions.add(i);
               }
               break;
            case 68:
            case 100:
               if (isInsert && state == ClientParser.LexState.Normal && i + 9 < queryLength && equalsIgnoreCase(query[i + 1], (byte)117) && equalsIgnoreCase(query[i + 2], (byte)112) && equalsIgnoreCase(query[i + 3], (byte)108) && equalsIgnoreCase(query[i + 4], (byte)105) && equalsIgnoreCase(query[i + 5], (byte)99) && equalsIgnoreCase(query[i + 6], (byte)97) && equalsIgnoreCase(query[i + 7], (byte)116) && equalsIgnoreCase(query[i + 8], (byte)101) && (i <= 0 || query[i - 1] <= 32 || isDelimiter(query[i - 1])) && (query[i + 9] <= 32 || isDelimiter(query[i + 9]))) {
                  i += 9;
                  isInsertDuplicate = true;
               }
               break;
            case 73:
            case 105:
               if (state == ClientParser.LexState.Normal && !isInsert && i + 6 < queryLength && equalsIgnoreCase(query[i + 1], (byte)110) && equalsIgnoreCase(query[i + 2], (byte)115) && equalsIgnoreCase(query[i + 3], (byte)101) && equalsIgnoreCase(query[i + 4], (byte)114) && equalsIgnoreCase(query[i + 5], (byte)116) && (i <= 0 || query[i - 1] <= 32 || isDelimiter(query[i - 1])) && (query[i + 6] <= 32 || isDelimiter(query[i + 6]))) {
                  i += 5;
                  isInsert = true;
               }
               break;
            case 92:
               if (state == ClientParser.LexState.String && !noBackslashEscapes) {
                  state = ClientParser.LexState.Escape;
               }
               break;
            case 96:
               if (state == ClientParser.LexState.Backtick) {
                  state = ClientParser.LexState.Normal;
               } else if (state == ClientParser.LexState.Normal) {
                  state = ClientParser.LexState.Backtick;
               }
            }

            lastChar = car;
         } else {
            state = ClientParser.LexState.String;
            lastChar = car;
         }
      }

      boolean isMulti = multiQueryIdx != -1 && multiQueryIdx < queryLength - 1;
      if (isMulti) {
         boolean hasAdditionalPart = false;

         for(int i = multiQueryIdx + 1; i < queryLength; ++i) {
            if (!isWhitespace(query[i])) {
               hasAdditionalPart = true;
               break;
            }
         }

         isMulti = hasAdditionalPart;
      }

      return new ClientParser(queryString, query, paramPositions, (List)null, isInsert, isInsertDuplicate, isMulti);
   }

   public static ClientParser rewritableParts(String queryString, boolean noBackslashEscapes) {
      boolean reWritablePrepare = true;
      List<Integer> paramPositions = new ArrayList(20);
      List<Integer> valuesBracketPositions = new ArrayList(2);
      ClientParser.LexState state = ClientParser.LexState.Normal;
      byte lastChar = 0;
      boolean singleQuotes = false;
      boolean isInsert = false;
      boolean isInsertDuplicate = false;
      boolean afterValues = false;
      boolean valuesClosed = false;
      int isInParenthesis = 0;
      int multiQueryIdx = -1;
      byte[] query = queryString.getBytes(StandardCharsets.UTF_8);
      int queryLength = query.length;

      int j;
      byte c;
      for(int i = 0; i < queryLength; ++i) {
         byte car = query[i];
         if (state != ClientParser.LexState.Escape || car == 39 && singleQuotes || car == 34 && !singleQuotes) {
            switch(car) {
            case 10:
               if (state == ClientParser.LexState.EOLComment) {
                  state = ClientParser.LexState.Normal;
               }
               break;
            case 34:
               if (state == ClientParser.LexState.Normal) {
                  state = ClientParser.LexState.String;
                  singleQuotes = false;
               } else if (state == ClientParser.LexState.String && !singleQuotes) {
                  state = ClientParser.LexState.Normal;
               } else if (state == ClientParser.LexState.Escape) {
                  state = ClientParser.LexState.String;
               }
               break;
            case 35:
               if (state == ClientParser.LexState.Normal) {
                  state = ClientParser.LexState.EOLComment;
               }
               break;
            case 39:
               if (state == ClientParser.LexState.Normal) {
                  state = ClientParser.LexState.String;
                  singleQuotes = true;
               } else if (state == ClientParser.LexState.String && singleQuotes) {
                  state = ClientParser.LexState.Normal;
               } else if (state == ClientParser.LexState.Escape) {
                  state = ClientParser.LexState.String;
               }
               break;
            case 40:
               if (state == ClientParser.LexState.Normal) {
                  ++isInParenthesis;
                  if (afterValues && valuesBracketPositions.isEmpty()) {
                     valuesBracketPositions.add(i);
                  }
               }
               break;
            case 41:
               if (state == ClientParser.LexState.Normal) {
                  --isInParenthesis;
                  if (afterValues && !valuesClosed && isInParenthesis == 0 && valuesBracketPositions.size() == 1) {
                     j = i + 1;

                     label308:
                     while(true) {
                        while(true) {
                           if (j >= queryLength) {
                              break label308;
                           }

                           c = query[j];
                           if (isWhitespace(c)) {
                              ++j;
                           } else if (c == 35) {
                              ++j;

                              while(j < queryLength && query[j] != 10) {
                                 ++j;
                              }
                           } else if (c == 45 && j + 1 < queryLength && query[j + 1] == 45) {
                              for(j += 2; j < queryLength && query[j] != 10; ++j) {
                              }
                           } else {
                              if (c != 47 || j + 1 >= queryLength || query[j + 1] != 42) {
                                 break label308;
                              }

                              for(j += 2; j + 1 < queryLength && (query[j] != 42 || query[j + 1] != 47); ++j) {
                              }

                              j = Math.min(j + 2, queryLength);
                           }
                        }
                     }

                     if (j >= queryLength || query[j] != 44) {
                        valuesBracketPositions.add(i);
                        valuesClosed = true;
                     }
                  }
               }
               break;
            case 42:
               if (state == ClientParser.LexState.Normal && lastChar == 47) {
                  state = ClientParser.LexState.SlashStarComment;
               }
               break;
            case 45:
               if (state == ClientParser.LexState.Normal && lastChar == 45) {
                  state = ClientParser.LexState.EOLComment;
               }
               break;
            case 47:
               if (state == ClientParser.LexState.SlashStarComment && lastChar == 42) {
                  state = ClientParser.LexState.Normal;
               }
               break;
            case 59:
               if (state == ClientParser.LexState.Normal && multiQueryIdx == -1) {
                  multiQueryIdx = i;
               }
               break;
            case 63:
               if (state == ClientParser.LexState.Normal) {
                  paramPositions.add(i);
                  if (valuesClosed) {
                     reWritablePrepare = false;
                  }
               }
               break;
            case 68:
            case 100:
               if (isInsert && state == ClientParser.LexState.Normal && i + 9 < queryLength && equalsIgnoreCase(query[i + 1], (byte)117) && equalsIgnoreCase(query[i + 2], (byte)112) && equalsIgnoreCase(query[i + 3], (byte)108) && equalsIgnoreCase(query[i + 4], (byte)105) && equalsIgnoreCase(query[i + 5], (byte)99) && equalsIgnoreCase(query[i + 6], (byte)97) && equalsIgnoreCase(query[i + 7], (byte)116) && equalsIgnoreCase(query[i + 8], (byte)101) && (i <= 0 || query[i - 1] <= 32 || isDelimiter(query[i - 1])) && (query[i + 9] <= 32 || isDelimiter(query[i + 9]))) {
                  i += 9;
                  isInsertDuplicate = true;
               }
               break;
            case 73:
            case 105:
               if (state == ClientParser.LexState.Normal && !isInsert && i + 6 < queryLength && equalsIgnoreCase(query[i + 1], (byte)110) && equalsIgnoreCase(query[i + 2], (byte)115) && equalsIgnoreCase(query[i + 3], (byte)101) && equalsIgnoreCase(query[i + 4], (byte)114) && equalsIgnoreCase(query[i + 5], (byte)116) && (i <= 0 || query[i - 1] <= 32 || isDelimiter(query[i - 1])) && (query[i + 6] <= 32 || isDelimiter(query[i + 6]))) {
                  i += 5;
                  isInsert = true;
               }
               break;
            case 76:
            case 108:
               if (state == ClientParser.LexState.Normal && queryLength > i + 14 && equalsIgnoreCase(query[i + 1], (byte)97) && equalsIgnoreCase(query[i + 2], (byte)115) && equalsIgnoreCase(query[i + 3], (byte)116) && query[i + 4] == 95 && equalsIgnoreCase(query[i + 5], (byte)105) && equalsIgnoreCase(query[i + 6], (byte)110) && equalsIgnoreCase(query[i + 7], (byte)115) && equalsIgnoreCase(query[i + 8], (byte)101) && equalsIgnoreCase(query[i + 9], (byte)114) && equalsIgnoreCase(query[i + 10], (byte)116) && query[i + 11] == 95 && equalsIgnoreCase(query[i + 12], (byte)105) && equalsIgnoreCase(query[i + 13], (byte)100) && query[i + 14] == 40) {
                  reWritablePrepare = false;
               }
               break;
            case 83:
            case 115:
               if (state == ClientParser.LexState.Normal && !valuesBracketPositions.isEmpty() && queryLength > i + 7 && equalsIgnoreCase(query[i + 1], (byte)101) && equalsIgnoreCase(query[i + 2], (byte)108) && equalsIgnoreCase(query[i + 3], (byte)101) && equalsIgnoreCase(query[i + 4], (byte)99) && equalsIgnoreCase(query[i + 5], (byte)116) && (i <= 0 || query[i - 1] <= 32 || isDelimiter(query[i - 1])) && (query[i + 6] <= 32 || isDelimiter(query[i + 6]))) {
                  reWritablePrepare = false;
               }
               break;
            case 86:
            case 118:
               if (state == ClientParser.LexState.Normal && valuesBracketPositions.isEmpty() && (lastChar == 41 || lastChar <= 40) && queryLength > i + 7 && equalsIgnoreCase(query[i + 1], (byte)97) && equalsIgnoreCase(query[i + 2], (byte)108) && equalsIgnoreCase(query[i + 3], (byte)117) && equalsIgnoreCase(query[i + 4], (byte)101) && equalsIgnoreCase(query[i + 5], (byte)115) && (query[i + 6] == 40 || query[i + 6] <= 40)) {
                  afterValues = true;
                  if (query[i + 6] == 40) {
                     valuesBracketPositions.add(i + 6);
                  }

                  i += 5;
               }
               break;
            case 92:
               if (state == ClientParser.LexState.String && !noBackslashEscapes) {
                  state = ClientParser.LexState.Escape;
               }
               break;
            case 96:
               if (state == ClientParser.LexState.Backtick) {
                  state = ClientParser.LexState.Normal;
               } else if (state == ClientParser.LexState.Normal) {
                  state = ClientParser.LexState.Backtick;
               }
            }

            lastChar = car;
         } else {
            state = ClientParser.LexState.String;
            lastChar = car;
         }
      }

      boolean isMulti = multiQueryIdx != -1 && multiQueryIdx < queryLength - 1;
      if (isMulti) {
         boolean hasAdditionalPart = false;

         for(j = multiQueryIdx + 1; j < queryLength; ++j) {
            c = query[j];
            if (c != 32 && c != 10 && c != 13 && c != 9) {
               hasAdditionalPart = true;
               break;
            }
         }

         isMulti = hasAdditionalPart;
      }

      if (isMulti || !isInsert || !reWritablePrepare || valuesBracketPositions.size() != 2) {
         valuesBracketPositions = null;
      }

      return new ClientParser(queryString, query, paramPositions, valuesBracketPositions, isInsert, isInsertDuplicate, isMulti);
   }

   public String getSql() {
      return this.sql;
   }

   public byte[] getQuery() {
      return this.query;
   }

   public List<Integer> getParamPositions() {
      return this.paramPositions;
   }

   public List<Integer> getValuesBracketPositions() {
      return this.valuesBracketPositions;
   }

   public int getParamCount() {
      return this.paramCount;
   }

   public boolean isInsert() {
      return this.isInsert;
   }

   public boolean isInsertDuplicate() {
      return this.isInsertDuplicate;
   }

   public boolean isMultiQuery() {
      return this.isMultiQuery;
   }

   private static boolean isDelimiter(byte b) {
      return b == 40 || b == 41 || b == 59 || b == 62 || b == 60 || b == 61 || b == 45 || b == 43 || b == 44;
   }

   private static boolean isWhitespace(byte b) {
      return b == 32 || b == 10 || b == 13 || b == 9;
   }

   private static boolean equalsIgnoreCase(byte b, byte lower) {
      return (b | 32) == lower;
   }

   static enum LexState {
      Normal,
      String,
      SlashStarComment,
      Escape,
      EOLComment,
      Backtick;

      // $FF: synthetic method
      private static ClientParser.LexState[] $values() {
         return new ClientParser.LexState[]{Normal, String, SlashStarComment, Escape, EOLComment, Backtick};
      }
   }
}
