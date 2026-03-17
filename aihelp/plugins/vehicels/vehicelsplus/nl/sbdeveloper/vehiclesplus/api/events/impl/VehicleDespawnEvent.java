/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.api.events.impl;

import javax.annotation.Nullable;
import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.events.CancellableVehicleEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import org.bukkit.entity.Player;

public class VehicleDespawnEvent
extends CancellableVehicleEvent<SpawnedVehicle> {
    private DespawnReason reason;
    private boolean force;
    @Nullable
    private Player despawner;

    public VehicleDespawnEvent(SpawnedVehicle spawnedVehicle, DespawnReason despawnReason, boolean bl) {
        super(spawnedVehicle);
        this.reason = despawnReason;
        this.force = bl;
    }

    public VehicleDespawnEvent(SpawnedVehicle spawnedVehicle, boolean bl, @Nullable Player player) {
        super(spawnedVehicle);
        this.reason = DespawnReason.PLAYER;
        this.despawner = player;
        this.force = bl;
    }

    @Override
    public void setCancelled(boolean bl) {
        if (this.force) {
            throw new RuntimeException("You cannot cancel a forced vehicle despawn!");
        }
        super.setCancelled(bl);
    }

    @Generated
    public DespawnReason getReason() {
        return this.reason;
    }

    @Generated
    public boolean isForce() {
        return this.force;
    }

    @Nullable
    @Generated
    public Player getDespawner() {
        return this.despawner;
    }

    public static enum DespawnReason {
        PLAYER,
        CHUNK_UNLOAD,
        API,
        SHUTDOWN,
        DESTROY;

    }
}

