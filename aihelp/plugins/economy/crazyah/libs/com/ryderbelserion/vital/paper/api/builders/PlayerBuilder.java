package libs.com.ryderbelserion.vital.paper.api.builders;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public record PlayerBuilder(String name) {
   public PlayerBuilder(String name) {
      this.name = name;
   }

   @Nullable
   public OfflinePlayer getOfflinePlayer() {
      if (this.name.isEmpty()) {
         return null;
      } else {
         CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> {
            return Bukkit.getServer().getOfflinePlayer(this.name);
         }).thenApply(OfflinePlayer::getUniqueId);
         return Bukkit.getServer().getOfflinePlayer((UUID)future.join());
      }
   }

   @Nullable
   public Player getPlayer() {
      return this.name.isEmpty() ? null : Bukkit.getServer().getPlayer(this.name);
   }

   public String name() {
      return this.name;
   }
}
