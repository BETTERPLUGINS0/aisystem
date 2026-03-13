/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_20_R2.CraftWorld
 *  org.bukkit.entity.EntityType
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R2.packets;

import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R2.packets.AbstractPacketEntity;
import net.minecraft.world.entity.Entity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
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
        CraftWorld craftWorld = (CraftWorld)location.getWorld();
        Class entityClass = this.bukkitType.getEntityClass();
        if (entityClass == null) {
            throw new RuntimeException("No entity class for type: " + String.valueOf(this.bukkitType));
        }
        Entity nmsEntity = craftWorld.createEntity(location, entityClass);
        nmsEntity.e(location.getX(), location.getY(), location.getZ());
        if (location.getYaw() != 0.0f) {
            nmsEntity.r(location.getYaw());
        }
        if (location.getPitch() != 0.0f) {
            nmsEntity.s(location.getPitch());
        }
        return nmsEntity;
    }

    public EntityType getEntityType() {
        return this.bukkitType;
    }
}

