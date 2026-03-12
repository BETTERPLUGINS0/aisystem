package fr.xephi.authme.libs.org.mariadb.jdbc.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class ClientParser implements PrepareResult {
   private final String sql;
   private final byte[] query;
   private final List<Integer> paramPositions;
   private final int paramCount;
   private final boolean isInsert;
   private final boolean isInsertDuplicate;

   private ClientParser(String sql, byte[] query, List<Integer> paramPositions, boolean isInsert, boolean isInsertDuplicate) {
      this.sql = sql;
      this.query = query;
      this.paramPositions = paramPositions;
      this.paramCount = paramPositions.size();
      this.isInsert = isInsert;
      this.isInsertDuplicate = isInsertDuplicate;
   }

   public static ClientParser parameterParts(String queryString, boolean noBackslashEscapes) {
      List<Integer> paramPositions = new ArrayList();
      ClientParser.LexState state = ClientParser.LexState.Normal;
      byte lastChar = 0;
      boolean singleQuotes = false;
      boolean isInsert = false;
      boolean isInsertDupplicate = false;
      byte[] query = queryString.getBytes(StandardCharsets.UTF_8);
      int queryLength = query.length;

      for(int i = 0; i < queryLength; ++i) {
         byte car = query[i];
         if (state == ClientParser.LexState.Escape && (car != 39 || !singleQuotes) && (car != 34 || singleQuotes)) {
            state = ClientParser.LexState.String;
            lastChar = car;
         } else {
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
               } else if (state == ClientParser.LexState.Normal && lastChar == 47) {
                  state = ClientParser.LexState.EOLComment;
               }
               break;
            case 63:
               if (state == ClientParser.LexState.Normal) {
                  paramPositions.add(i);
               }
               break;
            case 68:
            case 100:
               if (isInsert && state == ClientParser.LexState.Normal && i + 9 < queryLength && (query[i + 1] == 117 || query[i + 1] == 85) && (query[i + 2] == 112 || query[i + 2] == 80) && (query[i + 3] == 108 || query[i + 3] == 76) && (query[i + 4] == 105 || query[i + 4] == 73) && (query[i + 5] == 99 || query[i + 5] == 67) && (query[i + 6] == 97 || query[i + 6] == 65) && (query[i + 7] == 116 || query[i + 7] == 84) && (query[i + 8] == 101 || query[i + 8] == 69) && (i <= 0 || query[i - 1] <= 32 || "();><=-+,".indexOf(query[i - 1]) != -1) && (query[i + 9] <= 32 || "();><=-+,".indexOf(query[i + 9]) != -1)) {
                  i += 9;
                  isInsertDupplicate = true;
               }
               break;
            case 73:
            case 105:
               if (state == ClientParser.LexState.Normal && !isInsert && i + 6 < queryLength && (query[i + 1] == 110 || query[i + 1] == 78) && (query[i + 2] == 115 || query[i + 2] == 83) && (query[i + 3] == 101 || query[i + 3] == 69) && (query[i + 4] == 114 || query[i + 4] == 82) && (query[i + 5] == 116 || query[i + 5] == 84) && (i <= 0 || query[i - 1] <= 32 || "();><=-+,".indexOf(query[i - 1]) != -1) && (query[i + 6] <= 32 || "();><=-+,".indexOf(query[i + 6]) != -1)) {
                  i += 5;
                  isInsert = true;
               }
               break;
            case 92:
               if (!noBackslashEscapes && state == ClientParser.LexState.String) {
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
         }
      }

      return new ClientParser(queryString, query, paramPositions, isInsert, isInsertDupplicate);
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

   public int getParamCount() {
      return this.paramCount;
   }

   public boolean isInsert() {
      return this.isInsert;
   }

   public boolean isInsertDuplicate() {
      return this.isInsertDuplicate;
   }

   static enum LexState {
      Normal,
      String,
      SlashStarComment,
      Escape,
      EOLComment,
      Backtick;
   }
}
