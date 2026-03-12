package com.lenis0012.bukkit.loginsecurity.libs.paper.features.bedspawnlocation;

import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface BedSpawnLocation {
   CompletableFuture<Location> getBedSpawnLocationAsync(Player var1, boolean var2);
}
