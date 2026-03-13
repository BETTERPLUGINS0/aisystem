/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 */
package com.magmaguy.betterstructures.easyminecraftgoals.internal;

import java.util.Set;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface TrackedPacketEntity {
    public Location getTrackingLocation();

    public World getWorld();

    public void showToPlayer(Player var1);

    public void hideFromPlayer(Player var1);

    public boolean isVisibleTo(Player var1);

    public Set<UUID> getCurrentViewers();

    public boolean isValid();

    public UUID getUniqueId();

    public Entity getVehicle();

    public void remount();
}

