/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package net.advancedplugins.seasons.handlers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.UUID;
import net.advancedplugins.as.impl.utils.Pair;
import net.advancedplugins.as.impl.utils.collections.UniquePriorityQueue;
import net.advancedplugins.seasons.Core;
import net.advancedplugins.seasons.biomes.BiomesHandler;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class RenderHandler {
    private final int outerStart;
    private final int outerEnd;
    private final HashMap<UUID, UniquePriorityQueue<ChunkDistance>> playerChunkQueues;
    private final BiomesHandler biomesHandler;
    private final int chunkUpdates;
    private final Comparator<ChunkDistance> comparator = Comparator.comparing(ChunkDistance::getDistance);

    public RenderHandler(BiomesHandler biomesHandler) {
        int n = Bukkit.getViewDistance() + 2;
        this.outerStart = -n;
        this.outerEnd = n;
        this.biomesHandler = biomesHandler;
        this.chunkUpdates = Core.getInstance().getConfig().getInt("chunkUpdates", 8);
        this.playerChunkQueues = new HashMap();
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask((Plugin)Core.getInstance(), () -> {
            for (UUID uUID : new ArrayList<UUID>(this.playerChunkQueues.keySet())) {
                this.processChunkUpdates(uUID, this.chunkUpdates);
            }
        }, 20L, 20L);
    }

    public void refreshVisualBiomes(boolean bl) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.refreshForPlayer(player, bl);
        }
    }

    public void refreshForPlayer(Player player, boolean bl) {
        int n = player.getLocation().getChunk().getX();
        int n2 = player.getLocation().getChunk().getZ();
        PriorityQueue priorityQueue = this.playerChunkQueues.computeIfAbsent(player.getUniqueId(), uUID -> new UniquePriorityQueue<ChunkDistance>(this.comparator));
        for (int i = this.outerStart; i <= this.outerEnd; ++i) {
            for (int j = this.outerStart; j <= this.outerEnd; ++j) {
                if (!bl && (i != this.outerStart && i != this.outerEnd || j == this.outerStart || j == this.outerEnd) && j != this.outerStart && j != this.outerEnd) continue;
                int n3 = n + i;
                int n4 = n2 + j;
                double d = this.distance(n, n2, n3, n4);
                priorityQueue.add(new ChunkDistance(n3, n4, d));
            }
        }
    }

    public void clearChunks() {
        this.playerChunkQueues.clear();
    }

    public void processChunkUpdates(UUID uUID, int n) {
        PriorityQueue priorityQueue = this.playerChunkQueues.get(uUID);
        Player player = Bukkit.getPlayer((UUID)uUID);
        if (player != null && player.isOnline()) {
            World world = player.getWorld();
            for (int i = 0; i < n && !priorityQueue.isEmpty(); ++i) {
                int n2;
                ChunkDistance chunkDistance = (ChunkDistance)priorityQueue.poll();
                int n3 = chunkDistance.chunk.getKey();
                if (!world.isChunkLoaded(n3, n2 = chunkDistance.chunk.getValue().intValue())) continue;
                Chunk chunk = player.getWorld().getChunkAt(n3, n2);
                this.biomesHandler.getNmsBiome().sendChunkUpdate(chunk, player);
            }
        }
        if (priorityQueue.isEmpty() || player == null || !player.isOnline()) {
            this.playerChunkQueues.remove(uUID);
        }
    }

    private double distance(int n, int n2, int n3, int n4) {
        return Math.sqrt(Math.pow(n3 - n, 2.0) + Math.pow(n4 - n2, 2.0));
    }

    private class ChunkDistance {
        private final Pair<Integer, Integer> chunk;
        private final double distance;

        public ChunkDistance(int n, int n2, double d) {
            this.chunk = new Pair<Integer, Integer>(n, n2);
            this.distance = d;
        }

        public boolean equals(Object object) {
            if (this == object) {
                return true;
            }
            if (object == null || this.getClass() != object.getClass()) {
                return false;
            }
            ChunkDistance chunkDistance = (ChunkDistance)object;
            return Objects.equals(this.chunk, chunkDistance.chunk);
        }

        public int hashCode() {
            return Objects.hash(this.chunk);
        }

        public Pair<Integer, Integer> getChunk() {
            return this.chunk;
        }

        public double getDistance() {
            return this.distance;
        }
    }
}

