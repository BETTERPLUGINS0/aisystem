package com.nisovin.shopkeepers.tradelog.history;

import com.nisovin.shopkeepers.util.java.Validate;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface PlayerSelector {
   PlayerSelector ALL = new PlayerSelector() {
      public String toString() {
         return "PlayerSelector.ALL";
      }
   };

   public static class ByUUID implements PlayerSelector {
      private final UUID playerUUID;
      @Nullable
      private final String playerName;

      public ByUUID(UUID playerUUID, @Nullable String playerName) {
         Validate.notNull(playerUUID);
         this.playerUUID = playerUUID;
         this.playerName = playerName;
      }

      public UUID getPlayerUUID() {
         return this.playerUUID;
      }

      @Nullable
      public String getPlayerName() {
         return this.playerName;
      }

      public String toString() {
         StringBuilder builder = new StringBuilder();
         builder.append("PlayerSelector.ByUUID [playerUUID=");
         builder.append(this.playerUUID);
         builder.append(", playerName=");
         builder.append(this.playerName);
         builder.append("]");
         return builder.toString();
      }
   }
}
