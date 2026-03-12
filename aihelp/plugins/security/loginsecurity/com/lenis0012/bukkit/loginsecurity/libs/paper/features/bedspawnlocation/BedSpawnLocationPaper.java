package com.lenis0012.bukkit.loginsecurity.libs.paper.features.bedspawnlocation;

import com.lenis0012.bukkit.loginsecurity.libs.paper.PaperLib;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BedSpawnLocationPaper implements BedSpawnLocation {
   public CompletableFuture<Location> getBedSpawnLocationAsync(Player player, boolean isUrgent) {
      Location bedLocation = player.getPotentialBedLocation();
      return bedLocation != null && bedLocation.getWorld() != null ? PaperLib.getChunkAtAsync(bedLocation.getWorld(), bedLocation.getBlockX() >> 4, bedLocation.getBlockZ() >> 4, false, isUrgent).thenCompose((chunk) -> {
         return CompletableFuture.completedFuture(player.getBedSpawnLocation());
      }) : CompletableFuture.completedFuture((Object)null);
   }
}
