package me.casperge.realisticseasons.paperlib.features.asyncteleport;

import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class AsyncTeleportPaper_13 implements AsyncTeleport {
   public CompletableFuture<Boolean> teleportAsync(Entity var1, Location var2, TeleportCause var3) {
      return var1.teleportAsync(var2, var3);
   }
}
