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
import nl.sbdeveloper.vehiclesplus.api.events.VehicleEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import org.bukkit.entity.Player;

public class VehicleSpawnedEvent
extends VehicleEvent<SpawnedVehicle> {
    @Nullable
    private Player spawner;

    public VehicleSpawnedEvent(SpawnedVehicle spawnedVehicle) {
        super(spawnedVehicle);
    }

    public VehicleSpawnedEvent(SpawnedVehicle spawnedVehicle, @Nullable Player player) {
        super(spawnedVehicle);
        this.spawner = player;
    }

    @Nullable
    @Generated
    public Player getSpawner() {
        return this.spawner;
    }
}

