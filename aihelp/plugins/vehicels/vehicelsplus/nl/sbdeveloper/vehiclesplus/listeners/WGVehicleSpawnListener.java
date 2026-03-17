/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 */
package nl.sbdeveloper.vehiclesplus.listeners;

import nl.sbdeveloper.vehiclesplus.api.events.impl.VehiclePreSpawnEvent;
import nl.sbdeveloper.vehiclesplus.handlers.WGFlagHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class WGVehicleSpawnListener
implements Listener {
    @EventHandler(priority=EventPriority.LOW)
    public void onVehicleSpawn(VehiclePreSpawnEvent vehiclePreSpawnEvent) {
        if (vehiclePreSpawnEvent.getSpawner() == null) {
            return;
        }
        if (!WGFlagHandler.allowsVehicleSpawning(vehiclePreSpawnEvent.getSpawner(), vehiclePreSpawnEvent.getSpawner().getLocation()) || WGFlagHandler.reachedVehicleLimit(vehiclePreSpawnEvent.getSpawner(), vehiclePreSpawnEvent.getSpawner().getLocation())) {
            vehiclePreSpawnEvent.setCancelled(true);
        }
    }
}

