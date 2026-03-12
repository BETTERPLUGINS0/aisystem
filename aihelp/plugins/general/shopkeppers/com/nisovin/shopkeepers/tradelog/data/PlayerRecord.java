package com.nisovin.shopkeepers.tradelog.data;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.user.SKUser;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class PlayerRecord {
   private final UUID uniqueId;
   private final String name;

   public static PlayerRecord of(Player player) {
      Validate.notNull(player, (String)"player is null");
      return of(player.getUniqueId(), (String)Unsafe.assertNonNull(player.getName()));
   }

   public static PlayerRecord of(UUID playerUniqueId, String playerName) {
      return new PlayerRecord(playerUniqueId, playerName);
   }

   private PlayerRecord(UUID playerUniqueId, String playerName) {
      Validate.notNull(playerUniqueId, (String)"playerUniqueId is null");
      Validate.notEmpty(playerName, "playerName is null or empty");
      this.uniqueId = playerUniqueId;
      this.name = playerName;
   }

   public UUID getUniqueId() {
      return this.uniqueId;
   }

   public String getName() {
      return this.name;
   }

   public User toUser() {
      return SKUser.of(this.uniqueId, this.name);
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("PlayerRecord [uniqueId=");
      builder.append(this.uniqueId);
      builder.append(", name=");
      builder.append(this.name);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.name.hashCode();
      result = 31 * result + this.uniqueId.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (obj == null) {
         return false;
      } else if (!(obj instanceof PlayerRecord)) {
         return false;
      } else {
         PlayerRecord other = (PlayerRecord)obj;
         if (!this.name.equals(other.name)) {
            return false;
         } else {
            return this.uniqueId.equals(other.uniqueId);
         }
      }
   }
}
