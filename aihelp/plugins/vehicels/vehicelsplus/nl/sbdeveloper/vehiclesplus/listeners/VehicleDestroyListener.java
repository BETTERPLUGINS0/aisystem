/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Location
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.listeners;

import java.util.Map;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleDestroyEvent;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.SpawnedVehicle;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class VehicleDestroyListener
implements Listener {
    @EventHandler
    public void onVehicleDestroy(VehicleDestroyEvent vehicleDestroyEvent) {
        Location location;
        SpawnedVehicle spawnedVehicle = (SpawnedVehicle)vehicleDestroyEvent.getVehicle();
        if (!VehiclesPlus.getStorage().getConfig().getCollision().isDespawnVehicle()) {
            return;
        }
        if (!VehiclesPlus.getStorage().getConfig().getCollision().isDropTrunkItems()) {
            return;
        }
        Location location2 = location = spawnedVehicle.getHolder() != null ? spawnedVehicle.getHolder().getLocation() : spawnedVehicle.getLastKnownLocation();
        if (location != null && location.getWorld() != null) {
            Object object;
            Map<Integer, ItemStack> map = spawnedVehicle.getStorageVehicle().getTrunkSlots();
            if (map != null && !map.isEmpty()) {
                object = map.values().iterator();
                while (object.hasNext()) {
                    ItemStack itemStack = (ItemStack)object.next();
                    if (itemStack == null) continue;
                    location.getWorld().dropItemNaturally(location, itemStack);
                }
                map.clear();
            }
            if (spawnedVehicle instanceof DrivableVehicle && ((DrivableVehicle)(object = (DrivableVehicle)spawnedVehicle)).hasTrunk()) {
                ((DrivableVehicle)object).getTrunk().clear();
            }
        }
    }
}

