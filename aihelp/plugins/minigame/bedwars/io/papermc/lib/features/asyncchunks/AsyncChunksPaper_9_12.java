/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.World
 *  org.bukkit.World$ChunkLoadCallback
 */
package io.papermc.lib.features.asyncchunks;

import io.papermc.lib.PaperLib;
import io.papermc.lib.features.asyncchunks.AsyncChunks;
import java.util.concurrent.CompletableFuture;
import org.bukkit.Chunk;
import org.bukkit.World;

public class AsyncChunksPaper_9_12
implements AsyncChunks {
    @Override
    public CompletableFuture<Chunk> getChunkAtAsync(World world, int x, int z, boolean gen, boolean isUrgent) {
        CompletableFuture<Chunk> future = new CompletableFuture<Chunk>();
        if (!gen && PaperLib.getMinecraftVersion() >= 12 && !world.isChunkGenerated(x, z)) {
            future.complete(null);
        } else {
            World.ChunkLoadCallback chunkLoadCallback = future::complete;
            world.getChunkAtAsync(x, z, chunkLoadCallback);
        }
        return future;
    }
}

