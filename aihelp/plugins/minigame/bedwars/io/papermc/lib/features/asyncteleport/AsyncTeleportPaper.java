/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 */
package io.papermc.lib.features.asyncteleport;

import io.papermc.lib.PaperLib;
import io.papermc.lib.features.asyncteleport.AsyncTeleport;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;

public class AsyncTeleportPaper
implements AsyncTeleport {
    @Override
    public CompletableFuture<Boolean> teleportAsync(Entity entity, Location location, PlayerTeleportEvent.TeleportCause cause) {
        int x = location.getBlockX() >> 4;
        int z = location.getBlockZ() >> 4;
        return PaperLib.getChunkAtAsyncUrgently(location.getWorld(), x, z, true).thenApply(chunk -> entity.teleport(location, cause));
    }
}

