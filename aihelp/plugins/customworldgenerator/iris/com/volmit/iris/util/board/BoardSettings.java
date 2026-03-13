package com.volmit.iris.util.board;

import lombok.Generated;

public class BoardSettings {
   private final BoardProvider boardProvider;
   private final ScoreDirection scoreDirection;

   @Generated
   BoardSettings(final BoardProvider boardProvider, final ScoreDirection scoreDirection) {
      this.boardProvider = var1;
      this.scoreDirection = var2;
   }

   @Generated
   public static BoardSettings.BoardSettingsBuilder builder() {
      return new BoardSettings.BoardSettingsBuilder();
   }

   @Generated
   public BoardProvider getBoardProvider() {
      return this.boardProvider;
   }

   @Generated
   public ScoreDirection getScoreDirection() {
      return this.scoreDirection;
   }

   @Generated
   public static class BoardSettingsBuilder {
      @Generated
      private BoardProvider boardProvider;
      @Generated
      private ScoreDirection scoreDirection;

      @Generated
      BoardSettingsBuilder() {
      }

      @Generated
      public BoardSettings.BoardSettingsBuilder boardProvider(final BoardProvider boardProvider) {
         this.boardProvider = var1;
         return this;
      }

      @Generated
      public BoardSettings.BoardSettingsBuilder scoreDirection(final ScoreDirection scoreDirection) {
         this.scoreDirection = var1;
         return this;
      }

      @Generated
      public BoardSettings build() {
         return new BoardSettings(this.boardProvider, this.scoreDirection);
      }

      @Generated
      public String toString() {
         String var10000 = String.valueOf(this.boardProvider);
         return "BoardSettings.BoardSettingsBuilder(boardProvider=" + var10000 + ", scoreDirection=" + String.valueOf(this.scoreDirection) + ")";
      }
   }
}
