package me.casperge.realisticseasons.paperlib.features.asyncteleport;

import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class AsyncTeleportSync implements AsyncTeleport {
   public CompletableFuture<Boolean> teleportAsync(Entity var1, Location var2, TeleportCause var3) {
      return CompletableFuture.completedFuture(var1.teleport(var2, var3));
   }
}
