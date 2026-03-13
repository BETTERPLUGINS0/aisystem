/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  org.bukkit.Location
 *  org.bukkit.craftbukkit.v1_20_R4.CraftWorld
 *  org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 */
package com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R4.packets;

import com.magmaguy.betterstructures.easyminecraftgoals.v1_20_R4.packets.AbstractPacketEntity;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class PacketGenericEntity
extends AbstractPacketEntity<net.minecraft.world.entity.Entity> {
    private final EntityType bukkitType;

    public PacketGenericEntity(EntityType entityType, Location location) {
        super(location);
        this.bukkitType = entityType;
    }

    @Override
    protected net.minecraft.world.entity.Entity createEntity(Location location) {
        CraftWorld craftWorld = (CraftWorld)location.getWorld();
        Class entityClass = this.bukkitType.getEntityClass();
        if (entityClass == null) {
            throw new RuntimeException("No entity class for type: " + String.valueOf(this.bukkitType));
        }
        Entity bukkitEntity = craftWorld.createEntity(location, entityClass);
        net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity)bukkitEntity).getHandle();
        nmsEntity.a_(location.getX(), location.getY(), location.getZ());
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

