/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  org.bukkit.Location
 *  org.bukkit.entity.EntityType
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.packets;

import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.CraftBukkitBridge;
import com.magmaguy.betterstructures.easyminecraftgoals.v1_21_R7_spigot.packets.AbstractPacketEntity;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public class PacketGenericEntity
extends AbstractPacketEntity<Entity> {
    private final EntityType bukkitType;

    public PacketGenericEntity(EntityType entityType, Location location) {
        super(location);
        this.bukkitType = entityType;
    }

    @Override
    protected Entity createEntity(Location location) {
        return CraftBukkitBridge.createNMSEntity(this.bukkitType, CraftBukkitBridge.getServerLevel(location), location);
    }

    public EntityType getEntityType() {
        return this.bukkitType;
    }
}

