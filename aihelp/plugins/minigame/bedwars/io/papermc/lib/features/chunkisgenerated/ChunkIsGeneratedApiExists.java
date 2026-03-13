/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.World
 */
package io.papermc.lib.features.chunkisgenerated;

import io.papermc.lib.features.chunkisgenerated.ChunkIsGenerated;
import org.bukkit.World;

public class ChunkIsGeneratedApiExists
implements ChunkIsGenerated {
    @Override
    public boolean isChunkGenerated(World world, int x, int z) {
        return world.isChunkGenerated(x, z);
    }
}

