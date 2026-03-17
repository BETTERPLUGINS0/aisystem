/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.ArmorStand
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import java.util.UUID;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleDeleteEvent;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.vehicles.IVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.Part;
import nl.sbdeveloper.vehiclesplus.api.vehicles.statics.VehicleStatics;
import nl.sbdeveloper.vehiclesplus.storage.db.DataStorage;
import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Vehicle
implements IVehicle {
    @JsonIgnore
    public boolean isSpawned() {
        return this instanceof SpawnedVehicle;
    }

    @JsonIgnore
    @NotNull
    public StorageVehicle getStorageVehicle() {
        return this.getStorageVehicle(false);
    }

    @JsonIgnore
    @NotNull
    public StorageVehicle getStorageVehicle(boolean bl) {
        if (this.isSpawned() && bl) {
            return this.getSpawnedVehicle().getStorageVehicle();
        }
        if (!this.isSpawned()) {
            return (StorageVehicle)this;
        }
        throw new IllegalStateException("Failed to convert Vehicle to StorageVehicle!");
    }

    @JsonIgnore
    @Nullable
    public SpawnedVehicle getSpawnedVehicle() {
        if (!this.isSpawned()) {
            return null;
        }
        return (SpawnedVehicle)this;
    }

    @JsonIgnore
    public abstract UUID getUuid();

    @JsonIgnore
    public abstract VehicleModel getVehicleModel();

    @JsonIgnore
    @NotNull
    public abstract List<Part> getParts();

    @JsonIgnore
    @NotNull
    public abstract <V> List<V> getParts(Class<V> var1);

    @JsonIgnore
    @Nullable
    public abstract <V> V getPart(@NotNull Class<V> var1);

    @JsonIgnore
    @Nullable
    public abstract Part getPart(ArmorStand var1);

    @JsonIgnore
    public abstract boolean hasAddedParts();

    @JsonIgnore
    public abstract VehicleStatics getStatics();

    @JsonIgnore
    public abstract List<String> getInfoList();

    @JsonIgnore
    public Garage getGarage() {
        for (Garage garage : VehiclesPlusAPI.getGarages().values()) {
            if (!garage.getVehicles().contains(this.getUuid())) continue;
            return garage;
        }
        throw new IllegalStateException("Vehicle " + String.valueOf(this.getUuid()) + " is not in a garage!");
    }

    public void remove() {
        this.remove(null);
    }

    public void remove(Player player) {
        VehicleDeleteEvent vehicleDeleteEvent = new VehicleDeleteEvent(this);
        Bukkit.getPluginManager().callEvent((Event)vehicleDeleteEvent);
        if (vehicleDeleteEvent.isCancelled()) {
            return;
        }
        if (this.isSpawned()) {
            this.getSpawnedVehicle().despawn(true, player);
        }
        StorageVehicle storageVehicle = this.getStorageVehicle();
        VehiclesPlusAPI.getVehicles().remove(storageVehicle.getUuid());
        DataStorage.getInstance().removeVehicle(storageVehicle);
    }
}

