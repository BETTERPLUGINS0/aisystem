package me.casperge.realisticseasons.paperlib.features.asyncteleport;

import java.util.concurrent.CompletableFuture;
import me.casperge.realisticseasons.paperlib.PaperLib;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class AsyncTeleportPaper implements AsyncTeleport {
   public CompletableFuture<Boolean> teleportAsync(Entity var1, Location var2, TeleportCause var3) {
      int var4 = var2.getBlockX() >> 4;
      int var5 = var2.getBlockZ() >> 4;
      return PaperLib.getChunkAtAsyncUrgently(var2.getWorld(), var4, var5, true).thenApply((var3x) -> {
         return var1.teleport(var2, var3);
      });
   }
}
