/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.event.Cancellable
 */
package nl.mtvehicles.core.events;

import nl.mtvehicles.core.events.interfaces.HasVehicle;
import nl.mtvehicles.core.events.interfaces.IsCancellable;
import nl.mtvehicles.core.infrastructure.models.MTVEvent;
import org.bukkit.Location;
import org.bukkit.event.Cancellable;

public class VehicleEnterEvent
extends MTVEvent
implements IsCancellable,
Cancellable,
HasVehicle {
    private final String licensePlate;
    private final Location location;

    public VehicleEnterEvent(String licensePlate, Location location) {
        this.licensePlate = licensePlate;
        this.location = location;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String getLicensePlate() {
        return this.licensePlate;
    }

    public Location getLocation() {
        return this.location;
    }
}

