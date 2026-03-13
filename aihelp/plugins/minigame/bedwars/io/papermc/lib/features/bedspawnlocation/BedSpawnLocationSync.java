/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 */
package io.papermc.lib.features.bedspawnlocation;

import io.papermc.lib.features.bedspawnlocation.BedSpawnLocation;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BedSpawnLocationSync
implements BedSpawnLocation {
    @Override
    public CompletableFuture<Location> getBedSpawnLocationAsync(Player player, boolean isUrgent) {
        return CompletableFuture.completedFuture(player.getBedSpawnLocation());
    }
}

