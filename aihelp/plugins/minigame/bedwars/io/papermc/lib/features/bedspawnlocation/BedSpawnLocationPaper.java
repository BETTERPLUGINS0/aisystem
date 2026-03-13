/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package io.papermc.lib.features.bedspawnlocation;

import io.papermc.lib.PaperLib;
import io.papermc.lib.features.bedspawnlocation.BedSpawnLocation;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BedSpawnLocationPaper
implements BedSpawnLocation {
    @Override
    public CompletableFuture<Location> getBedSpawnLocationAsync(Player player, boolean isUrgent) {
        Location bedLocation = player.getPotentialBedLocation();
        if (bedLocation == null || bedLocation.getWorld() == null) {
            return CompletableFuture.completedFuture(null);
        }
        return PaperLib.getChunkAtAsync(bedLocation.getWorld(), bedLocation.getBlockX() >> 4, bedLocation.getBlockZ() >> 4, false, isUrgent).thenCompose(chunk -> CompletableFuture.completedFuture(player.getBedSpawnLocation()));
    }
}

