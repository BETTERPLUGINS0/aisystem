/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  lombok.Generated
 *  org.bukkit.Location
 */
package nl.sbdeveloper.vehiclesplus.api.vehicles.impl;

import lombok.Generated;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.StorageVehicle;
import org.bukkit.Location;

public class PersistentVehicle
extends SpawnedVehicle {
    PersistentVehicle(StorageVehicle storageVehicle, Location location) {
        super(storageVehicle, location, true);
    }

    @Override
    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof PersistentVehicle)) {
            return false;
        }
        PersistentVehicle persistentVehicle = (PersistentVehicle)object;
        return persistentVehicle.canEqual(this);
    }

    @Override
    @Generated
    protected boolean canEqual(Object object) {
        return object instanceof PersistentVehicle;
    }

    @Override
    @Generated
    public int hashCode() {
        boolean bl = true;
        return 1;
    }

    @Override
    @Generated
    public String toString() {
        return "PersistentVehicle()";
    }
}

