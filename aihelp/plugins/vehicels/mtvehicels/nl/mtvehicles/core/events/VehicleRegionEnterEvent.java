/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.Cancellable
 */
package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.event.Cancellable;

public class VehicleRegionEnterEvent
extends MTVEvent
implements Cancellable,
HasVehicle {
    private final String licensePlate;
    private final String regionName;

    public VehicleRegionEnterEvent(String licensePlate, String regionName) {
        this.licensePlate = licensePlate;
        this.regionName = regionName;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String getLicensePlate() {
        return this.licensePlate;
    }

    public String getRegionName() {
        return this.regionName;
    }
}

