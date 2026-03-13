/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Chunk
 *  org.bukkit.ChunkSnapshot
 *  org.bukkit.World
 */
package net.advancedplugins.seasons.utils;

import net.advancedplugins.as.impl.utils.nbt.utils.MinecraftVersion;
import net.advancedplugins.seasons.handlers.sub.CustomChunkData;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.World;

public class LocalChunkSnapshot {
    private final CustomChunkData chunkSnapshot;
    private String world;
    private final int x;
    private final int z;
    private long lastGet = System.currentTimeMillis();

    public LocalChunkSnapshot(int n, int n2, Chunk chunk, World world) {
        ChunkSnapshot chunkSnapshot;
        this.x = n;
        this.z = n2;
        this.world = world.getName();
        if (MinecraftVersion.isPaper()) {
            try {
                chunkSnapshot = chunk.getChunkSnapshot(true, true, false);
            } catch (Exception exception) {
                chunkSnapshot = null;
            }
        } else {
            chunkSnapshot = chunk.getChunkSnapshot(true, true, false);
        }
        if (chunkSnapshot == null) {
            this.chunkSnapshot = null;
            return;
        }
        this.chunkSnapshot = CustomChunkData.processChunkSnapshot(chunkSnapshot);
    }

    public CustomChunkData getChunkSnapshot() {
        this.lastGet = System.currentTimeMillis();
        return this.chunkSnapshot;
    }

    public long getLastGet() {
        return this.lastGet;
    }
}

