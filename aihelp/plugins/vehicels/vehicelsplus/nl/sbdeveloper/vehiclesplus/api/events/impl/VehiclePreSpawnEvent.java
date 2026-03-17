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
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import org.bukkit.entity.Player;

public class VehiclePreSpawnEvent
extends CancellableVehicleEvent<StorageVehicle> {
    @Nullable
    private Player spawner;

    public VehiclePreSpawnEvent(StorageVehicle storageVehicle) {
        super(storageVehicle);
    }

    public VehiclePreSpawnEvent(StorageVehicle storageVehicle, @Nullable Player player) {
        super(storageVehicle);
        this.spawner = player;
    }

    @Nullable
    @Generated
    public Player getSpawner() {
        return this.spawner;
    }
}

