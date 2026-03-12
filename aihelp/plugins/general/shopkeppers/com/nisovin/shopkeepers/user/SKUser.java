package com.nisovin.shopkeepers.user;

import com.nisovin.shopkeepers.api.internal.util.Unsafe;
import com.nisovin.shopkeepers.api.user.User;
import com.nisovin.shopkeepers.util.java.LRUCache;
import com.nisovin.shopkeepers.util.java.Validate;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class SKUser implements User {
   private static final Map<UUID, User> cache = new LRUCache(100);
   public static final User EMPTY = of(new UUID(0L, 0L), "[unset]");
   private final UUID uniqueId;
   private final String lastKnownName;

   public static User of(UUID uniqueId, String lastKnownName) {
      User user = (User)cache.compute(uniqueId, (uuid, oldUser) -> {
         if (oldUser != null && oldUser.getLastKnownName().equals(lastKnownName)) {
            assert oldUser.getUniqueId().equals(uniqueId);

            return oldUser;
         } else {
            return new SKUser(uniqueId, lastKnownName);
         }
      });

      assert user != null;

      return user;
   }

   public static User of(Player player) {
      return of(player.getUniqueId(), player.getName());
   }

   private SKUser(UUID uniqueId, String lastKnownName) {
      Validate.notNull(uniqueId, (String)"uniqueId is null");
      Validate.notEmpty(lastKnownName, "lastKnownName is null or empty");
      this.uniqueId = uniqueId;
      this.lastKnownName = lastKnownName;
   }

   public UUID getUniqueId() {
      return this.uniqueId;
   }

   public String getLastKnownName() {
      return this.lastKnownName;
   }

   public String getName() {
      Player player = this.getPlayer();
      return player != null ? (String)Unsafe.assertNonNull(player.getName()) : this.lastKnownName;
   }

   public String getDisplayName() {
      Player player = this.getPlayer();
      return player != null ? player.getDisplayName() : this.lastKnownName;
   }

   public boolean isOnline() {
      return this.getPlayer() != null;
   }

   @Nullable
   public Player getPlayer() {
      return Bukkit.getPlayer(this.uniqueId);
   }

   public OfflinePlayer getOfflinePlayer() {
      return Bukkit.getOfflinePlayer(this.uniqueId);
   }

   public String toString() {
      StringBuilder builder = new StringBuilder();
      builder.append("SKUser [uniqueId=");
      builder.append(this.uniqueId);
      builder.append(", name=");
      builder.append(this.lastKnownName);
      builder.append("]");
      return builder.toString();
   }

   public int hashCode() {
      int prime = true;
      int result = 1;
      int result = 31 * result + this.uniqueId.hashCode();
      result = 31 * result + this.lastKnownName.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object obj) {
      if (this == obj) {
         return true;
      } else if (!(obj instanceof User)) {
         return false;
      } else {
         User other = (User)obj;
         if (!this.uniqueId.equals(other.getUniqueId())) {
            return false;
         } else {
            return this.lastKnownName.equals(other.getLastKnownName());
         }
      }
   }
}
