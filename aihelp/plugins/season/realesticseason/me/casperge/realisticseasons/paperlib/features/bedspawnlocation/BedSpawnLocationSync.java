package me.casperge.realisticseasons.paperlib.features.bedspawnlocation;

import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BedSpawnLocationSync implements BedSpawnLocation {
   public CompletableFuture<Location> getBedSpawnLocationAsync(Player var1, boolean var2) {
      return CompletableFuture.completedFuture(var1.getBedSpawnLocation());
   }
}
