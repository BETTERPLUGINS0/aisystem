/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.ChunkSnapshot
 *  org.bukkit.Material
 *  org.bukkit.block.Biome
 *  org.bukkit.util.Vector
 */
package net.advancedplugins.seasons.handlers.sub;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import javax.annotation.Nullable;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.util.Vector;

public class CustomChunkData {
    private final ImmutableMap<Vector, BlockData> blockDataMap;

    public CustomChunkData(HashMap<Vector, BlockData> hashMap) {
        this.blockDataMap = ImmutableMap.builder().putAll(hashMap).build();
    }

    @Nullable
    public BlockData getHighestBlockData(int n, int n2) {
        return this.blockDataMap.get(new Vector(n, 0, n2));
    }

    public static CustomChunkData processChunkSnapshot(ChunkSnapshot chunkSnapshot) {
        HashMap<Vector, BlockData> hashMap = new HashMap<Vector, BlockData>();
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                int n = chunkSnapshot.getHighestBlockYAt(i, j);
                if (n < 64 || n >= 320) continue;
                Material material = chunkSnapshot.getBlockType(i, n, j);
                Material material2 = chunkSnapshot.getBlockType(i, n + 1, j);
                Biome biome = chunkSnapshot.getBiome(i, n, j);
                hashMap.put(new Vector(i, 0, j), new BlockData(n, material, material2, biome));
            }
        }
        chunkSnapshot = null;
        return new CustomChunkData(hashMap);
    }

    public static class BlockData {
        private final int highestY;
        private final Material material;
        private final Material materialAbove;
        private final Biome biome;

        public BlockData(int n, Material material, Material material2, Biome biome) {
            this.highestY = n;
            this.material = material;
            this.biome = biome;
            this.materialAbove = material2;
        }

        public int getHighestY() {
            return this.highestY;
        }

        public Material getMaterial() {
            return this.material;
        }

        public Material getMaterialAbove() {
            return this.materialAbove;
        }

        public Biome getBiome() {
            return this.biome;
        }
    }
}

