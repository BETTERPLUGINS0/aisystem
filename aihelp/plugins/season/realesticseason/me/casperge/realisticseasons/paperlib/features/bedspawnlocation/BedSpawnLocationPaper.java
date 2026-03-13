package me.casperge.realisticseasons.paperlib.features.bedspawnlocation;

import java.util.concurrent.CompletableFuture;
import me.casperge.realisticseasons.paperlib.PaperLib;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BedSpawnLocationPaper implements BedSpawnLocation {
   public CompletableFuture<Location> getBedSpawnLocationAsync(Player var1, boolean var2) {
      Location var3 = var1.getPotentialBedLocation();
      return var3 != null && var3.getWorld() != null ? PaperLib.getChunkAtAsync(var3.getWorld(), var3.getBlockX() >> 4, var3.getBlockZ() >> 4, false, var2).thenCompose((var1x) -> {
         return CompletableFuture.completedFuture(var1.getBedSpawnLocation());
      }) : CompletableFuture.completedFuture((Object)null);
   }
}
