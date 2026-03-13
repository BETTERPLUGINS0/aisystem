/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntIntPair
 *  org.bukkit.Bukkit
 *  org.bukkit.ChunkSnapshot
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 */
package net.advancedplugins.seasons.visuals.type;

import it.unimi.dsi.fastutil.ints.IntIntPair;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.advancedplugins.seasons.visuals.type.IVisualType;
import org.bukkit.Bukkit;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class AbstractFallingLeafVisual
implements IVisualType {
    private final Map<IntIntPair, CompletableFuture<ChunkSnapshot>> cachedChunkSnapshots = new ConcurrentHashMap<IntIntPair, CompletableFuture<ChunkSnapshot>>();
    private final ConcurrentLinkedQueue<UUID> players = new ConcurrentLinkedQueue();
    private boolean enabled = true;
    private final Random random = new Random();

    @Override
    public void tick() {
        for (UUID uUID : this.players) {
            Player player = Bukkit.getPlayer((UUID)uUID);
            if (player == null || !player.isOnline()) continue;
            this.createFallingSnowFromLeaves(player, player.getLocation());
        }
        this.players.clear();
        this.cachedChunkSnapshots.clear();
    }

    @Override
    public void tickSync() {
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean bl) {
        this.enabled = bl;
    }

    @Override
    public void activate(Player player) {
        this.players.add(player.getUniqueId());
    }

    public void createFallingSnowFromLeaves(Player player, Location location) {
        ArrayList<Location> arrayList = new ArrayList<Location>();
        World world = location.getWorld();
        if (location.getY() >= 320.0) {
            return;
        }
        int n = location.getBlockX() - 4;
        int n2 = location.getBlockY() - 4;
        int n3 = location.getBlockZ() - 4;
        for (int i = n2; i <= n2 + 8; ++i) {
            if (this.isOutOfBounds(world, i)) continue;
            for (int j = n; j <= n + 8; ++j) {
                for (int k = n3; k <= n3 + 8; ++k) {
                    Material material;
                    Location location2;
                    Location location3;
                    int n4;
                    Location location4 = new Location(world, (double)j, (double)i, (double)k);
                    int n5 = location4.getBlockX() >> 4;
                    ChunkSnapshot chunkSnapshot = (ChunkSnapshot)this.cachedChunkSnapshots.computeIfAbsent(IntIntPair.of((int)n5, (int)(n4 = location4.getBlockZ() >> 4)), intIntPair -> CompletableFuture.completedFuture(world.getChunkAt(location4).getChunkSnapshot())).join();
                    Material material2 = chunkSnapshot.getBlockType((location3 = new Location(world, (double)(j - n5 * 16), (double)i, (double)(k - n4 * 16))).getBlockX(), location3.getBlockY(), location3.getBlockZ());
                    if (!material2.name().endsWith("_LEAVES") || this.isOutOfBounds(world, (location2 = location3.clone().subtract(0.0, 1.0, 0.0)).getY()) || (material = chunkSnapshot.getBlockType(location3.getBlockX(), location2.getBlockY(), location2.getBlockZ())) != Material.AIR) continue;
                    arrayList.add(location4);
                }
            }
        }
        if (!arrayList.isEmpty()) {
            Location location5 = (Location)arrayList.get(this.random.nextInt(arrayList.size()));
            this.playEffect(player, location5);
        }
    }

    protected abstract void playEffect(Player var1, Location var2);

    protected boolean isOutOfBounds(World world, double d) {
        return d < (double)world.getMinHeight() || d >= (double)world.getMaxHeight();
    }
}

