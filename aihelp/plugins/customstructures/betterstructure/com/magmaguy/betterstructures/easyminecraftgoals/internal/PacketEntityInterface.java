/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.entity.Entity
 */
package com.magmaguy.betterstructures.easyminecraftgoals.internal;

import com.magmaguy.betterstructures.easyminecraftgoals.internal.AbstractPacketBundle;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface PacketEntityInterface {
    public void addRemoveCallback(Runnable var1);

    public void displayTo(UUID var1);

    public void hideFrom(UUID var1);

    public void remove();

    public void setVisible(boolean var1);

    public Location getLocation();

    public UUID getUniqueId();

    public void teleport(Location var1);

    default public void mountTo(int vehicleEntityId) {
    }

    default public void dismount() {
    }

    default public int getEntityId() {
        return -1;
    }

    public void addViewer(UUID var1);

    public void removeViewer(UUID var1);

    public boolean hasViewers();

    public AbstractPacketBundle createPacketBundle();

    default public void syncMetadata() {
    }

    default public <B extends Entity> B getBukkitEntity() {
        throw new UnsupportedOperationException("getBukkitEntity is only supported in Minecraft 1.21.11+");
    }
}

