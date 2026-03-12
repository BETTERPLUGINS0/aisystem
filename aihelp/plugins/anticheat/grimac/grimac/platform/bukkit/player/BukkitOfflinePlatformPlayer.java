package ac.grim.grimac.platform.bukkit.player;

import ac.grim.grimac.platform.api.player.OfflinePlatformPlayer;
import ac.grim.grimac.shaded.jetbrains.annotations.NotNull;
import java.util.Objects;
import java.util.UUID;
import lombok.Generated;
import org.bukkit.OfflinePlayer;

public class BukkitOfflinePlatformPlayer implements OfflinePlatformPlayer {
   private final OfflinePlayer offlinePlayer;

   public boolean isOnline() {
      return this.offlinePlayer.isOnline();
   }

   @NotNull
   public String getName() {
      return (String)Objects.requireNonNull(this.offlinePlayer.getName());
   }

   @NotNull
   public UUID getUniqueId() {
      return this.offlinePlayer.getUniqueId();
   }

   public boolean equals(Object o) {
      boolean var10000;
      if (o instanceof OfflinePlatformPlayer) {
         OfflinePlatformPlayer player = (OfflinePlatformPlayer)o;
         if (this.getUniqueId().equals(player.getUniqueId())) {
            var10000 = true;
            return var10000;
         }
      }

      var10000 = false;
      return var10000;
   }

   @Generated
   public BukkitOfflinePlatformPlayer(OfflinePlayer offlinePlayer) {
      this.offlinePlayer = offlinePlayer;
   }
}
