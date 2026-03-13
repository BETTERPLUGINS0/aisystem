/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.World
 */
package io.papermc.lib.features.asyncchunks;

import io.papermc.lib.PaperLib;
import io.papermc.lib.features.asyncchunks.AsyncChunks;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Chunk;
import org.bukkit.World;

public class AsyncChunksSync
implements AsyncChunks {
    @Override
    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen, boolean isUrgent) {
        if (!gen && !PaperLib.isChunkGenerated(world, x, z)) {
            return CompletableFuture.completedFuture(null);
        }
        return CompletableFuture.completedFuture(world.getChunkAt(x, z));
    }
}

