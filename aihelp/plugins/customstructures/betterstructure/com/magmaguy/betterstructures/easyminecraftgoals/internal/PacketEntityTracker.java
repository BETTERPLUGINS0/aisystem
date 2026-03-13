/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitTask
 */
package com.magmaguy.betterstructures.easyminecraftgoals.internal;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.PacketEntityEventListener;
import com.magmaguy.betterstructures.easyminecraftgoals.internal.TrackedPacketEntity;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class PacketEntityTracker {
    private static PacketEntityTracker instance;
    private final Set<TrackedPacketEntity> trackedEntities = ConcurrentHashMap.newKeySet();
    private BukkitTask tickTask;
    private Plugin plugin;
    private long tickCounter = 0L;
    private static final int VISIBILITY_UPDATE_INTERVAL = 20;
    private double trackingRangeSquared = 4096.0;

    private PacketEntityTracker() {
    }

    public static PacketEntityTracker getInstance() {
        if (instance == null) {
            instance = new PacketEntityTracker();
        }
        return instance;
    }

    public void initialize(Plugin plugin) {
        if (this.plugin != null) {
            return;
        }
        this.plugin = plugin;
        this.updateTrackingRange();
        Bukkit.getPluginManager().registerEvents((Listener)new PacketEntityEventListener(this), plugin);
        this.tickTask = Bukkit.getScheduler().runTaskTimer(plugin, this::tick, 1L, 1L);
    }

    public void shutdown() {
        if (this.tickTask != null) {
            this.tickTask.cancel();
            this.tickTask = null;
        }
        for (TrackedPacketEntity entity : this.trackedEntities) {
            for (UUID viewerUUID : entity.getCurrentViewers()) {
                Player viewer = Bukkit.getPlayer((UUID)viewerUUID);
                if (viewer == null) continue;
                entity.hideFromPlayer(viewer);
            }
        }
        this.trackedEntities.clear();
        this.plugin = null;
    }

    public void register(TrackedPacketEntity entity) {
        this.trackedEntities.add(entity);
    }

    public void unregister(TrackedPacketEntity entity) {
        this.trackedEntities.remove(entity);
    }

    private void tick() {
        ++this.tickCounter;
        if (this.tickCounter % 20L != 0L) {
            return;
        }
        this.updateVisibility();
    }

    private void updateVisibility() {
        Iterator<TrackedPacketEntity> iterator2 = this.trackedEntities.iterator();
        while (iterator2.hasNext()) {
            World entityWorld;
            TrackedPacketEntity entity = iterator2.next();
            if (!entity.isValid()) {
                iterator2.remove();
                continue;
            }
            Location entityLocation = entity.getTrackingLocation();
            if (entityLocation == null || (entityWorld = entity.getWorld()) == null) continue;
            for (Player player : Bukkit.getOnlinePlayers()) {
                this.updateVisibilityForPlayer(entity, entityLocation, entityWorld, player);
            }
        }
    }

    private void updateVisibilityForPlayer(TrackedPacketEntity entity, Location entityLocation, World entityWorld, Player player) {
        boolean shouldBeVisible = false;
        if (player.getWorld().equals((Object)entityWorld)) {
            double distanceSquared = player.getLocation().distanceSquared(entityLocation);
            shouldBeVisible = distanceSquared <= this.trackingRangeSquared;
        }
        boolean isCurrentlyVisible = entity.isVisibleTo(player);
        if (shouldBeVisible && !isCurrentlyVisible) {
            entity.showToPlayer(player);
        } else if (!shouldBeVisible && isCurrentlyVisible) {
            entity.hideFromPlayer(player);
        }
    }

    void onPlayerJoin(Player player) {
        if (this.plugin != null) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                if (!player.isOnline()) {
                    return;
                }
                this.updateVisibilityForAllEntities(player);
            }, 1L);
        }
    }

    void onPlayerQuit(Player player) {
        for (TrackedPacketEntity entity : this.trackedEntities) {
            if (!entity.isVisibleTo(player)) continue;
            entity.hideFromPlayer(player);
        }
    }

    void onPlayerRespawn(Player player) {
        if (this.plugin != null) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                if (!player.isOnline()) {
                    return;
                }
                this.updateVisibilityForAllEntities(player);
                for (TrackedPacketEntity entity : this.trackedEntities) {
                    Entity vehicle = entity.getVehicle();
                    if (vehicle == null || !vehicle.getUniqueId().equals(player.getUniqueId())) continue;
                    entity.remount();
                }
            }, 1L);
        }
    }

    void onPlayerChangedWorld(Player player, World fromWorld) {
        World toWorld = player.getWorld();
        for (TrackedPacketEntity entity : this.trackedEntities) {
            World entityWorld = entity.getWorld();
            if (entityWorld == null || !entityWorld.equals((Object)fromWorld) || !entity.isVisibleTo(player)) continue;
            entity.hideFromPlayer(player);
        }
        for (TrackedPacketEntity entity : this.trackedEntities) {
            Entity vehicle = entity.getVehicle();
            if (vehicle == null || !vehicle.getUniqueId().equals(player.getUniqueId())) continue;
            for (UUID viewerUUID : new HashSet<UUID>(entity.getCurrentViewers())) {
                Player viewer = Bukkit.getPlayer((UUID)viewerUUID);
                if (viewer == null) continue;
                entity.hideFromPlayer(viewer);
            }
        }
        if (this.plugin != null) {
            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                if (!player.isOnline()) {
                    return;
                }
                World newWorld = player.getWorld();
                for (TrackedPacketEntity entity : this.trackedEntities) {
                    double distanceSquared;
                    Location entityLocation;
                    World entityWorld = entity.getWorld();
                    if (entityWorld == null || !entityWorld.equals((Object)newWorld) || (entityLocation = entity.getTrackingLocation()) == null || !((distanceSquared = player.getLocation().distanceSquared(entityLocation)) <= this.trackingRangeSquared) || entity.isVisibleTo(player)) continue;
                    entity.showToPlayer(player);
                }
                for (TrackedPacketEntity entity : this.trackedEntities) {
                    Entity vehicle = entity.getVehicle();
                    if (vehicle == null || !vehicle.getUniqueId().equals(player.getUniqueId())) continue;
                    for (Player otherPlayer : newWorld.getPlayers()) {
                        double distanceSquared;
                        Location entityLocation;
                        if (entity.isVisibleTo(otherPlayer) || (entityLocation = entity.getTrackingLocation()) == null || !((distanceSquared = otherPlayer.getLocation().distanceSquared(entityLocation)) <= this.trackingRangeSquared)) continue;
                        entity.showToPlayer(otherPlayer);
                    }
                    entity.remount();
                }
            }, 1L);
        }
    }

    private void updateVisibilityForAllEntities(Player player) {
        for (TrackedPacketEntity entity : this.trackedEntities) {
            World entityWorld;
            Location entityLocation;
            if (!entity.isValid() || (entityLocation = entity.getTrackingLocation()) == null || (entityWorld = entity.getWorld()) == null) continue;
            this.updateVisibilityForPlayer(entity, entityLocation, entityWorld, player);
        }
    }

    private void updateTrackingRange() {
        try {
            int range = Bukkit.getViewDistance() * 16;
            this.trackingRangeSquared = range * range;
        } catch (Exception e) {
            this.trackingRangeSquared = 4096.0;
        }
    }

    public double getTrackingRange() {
        return Math.sqrt(this.trackingRangeSquared);
    }

    public void setTrackingRange(double range) {
        this.trackingRangeSquared = range * range;
    }
}

