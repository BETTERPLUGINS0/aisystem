/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.entity.Entity
 */
package nl.sbdeveloper.vehiclesplus.api.events.impl;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.events.CancellableVehicleEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.collision.HitboxSide;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import org.bukkit.entity.Entity;

public class VehicleCollisionEvent
extends CancellableVehicleEvent<SpawnedVehicle> {
    private SpawnedVehicle collidingVehicle = null;
    private Entity collidingEntity = null;
    private boolean vehicleDamaged = false;
    private boolean entityPushed = false;
    private final double collisionSpeed;
    private final HitboxSide collisionSide;

    public VehicleCollisionEvent(SpawnedVehicle spawnedVehicle, SpawnedVehicle spawnedVehicle2, boolean bl, double d, HitboxSide hitboxSide) {
        super(spawnedVehicle);
        this.collidingVehicle = spawnedVehicle2;
        this.vehicleDamaged = bl;
        this.collisionSpeed = d;
        this.collisionSide = hitboxSide;
    }

    public VehicleCollisionEvent(SpawnedVehicle spawnedVehicle, Entity entity, boolean bl, double d, HitboxSide hitboxSide) {
        super(spawnedVehicle);
        this.collidingEntity = entity;
        this.entityPushed = bl;
        this.collisionSpeed = d;
        this.collisionSide = hitboxSide;
    }

    @Generated
    public SpawnedVehicle getCollidingVehicle() {
        return this.collidingVehicle;
    }

    @Generated
    public Entity getCollidingEntity() {
        return this.collidingEntity;
    }

    @Generated
    public boolean isVehicleDamaged() {
        return this.vehicleDamaged;
    }

    @Generated
    public boolean isEntityPushed() {
        return this.entityPushed;
    }

    @Generated
    public double getCollisionSpeed() {
        return this.collisionSpeed;
    }

    @Generated
    public HitboxSide getCollisionSide() {
        return this.collisionSide;
    }
}

