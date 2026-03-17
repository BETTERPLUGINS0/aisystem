/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryDragEvent
 *  org.bukkit.event.inventory.InventoryMoveItemEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package nl.sbdeveloper.vehiclesplus.listeners;

import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.Vehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class TrunkInventoryListener
implements Listener {
    private final JavaPlugin plugin;

    public TrunkInventoryListener(JavaPlugin javaPlugin) {
        this.plugin = javaPlugin;
    }

    private DrivableVehicle findByInventory(Inventory inventory) {
        return VehiclesPlusAPI.getVehicles().values().stream().filter(Vehicle::isSpawned).map(Vehicle::getSpawnedVehicle).filter(spawnedVehicle -> spawnedVehicle instanceof DrivableVehicle).map(spawnedVehicle -> (DrivableVehicle)spawnedVehicle).filter(drivableVehicle -> drivableVehicle.hasTrunk() && drivableVehicle.getTrunk() == inventory).findFirst().orElse(null);
    }

    private void scheduleSave(DrivableVehicle drivableVehicle, boolean bl) {
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> drivableVehicle.saveTrunkToStorageSparse(bl), 1L);
    }

    @EventHandler
    public void onClick(InventoryClickEvent inventoryClickEvent) {
        DrivableVehicle drivableVehicle = this.findByInventory(inventoryClickEvent.getInventory());
        if (drivableVehicle != null) {
            this.scheduleSave(drivableVehicle, false);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent inventoryDragEvent) {
        DrivableVehicle drivableVehicle = this.findByInventory(inventoryDragEvent.getInventory());
        if (drivableVehicle != null) {
            this.scheduleSave(drivableVehicle, false);
        }
    }

    @EventHandler
    public void onMove(InventoryMoveItemEvent inventoryMoveItemEvent) {
        DrivableVehicle drivableVehicle = this.findByInventory(inventoryMoveItemEvent.getSource());
        DrivableVehicle drivableVehicle2 = this.findByInventory(inventoryMoveItemEvent.getDestination());
        if (drivableVehicle != null) {
            this.scheduleSave(drivableVehicle, false);
        }
        if (drivableVehicle2 != null) {
            this.scheduleSave(drivableVehicle2, false);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent inventoryCloseEvent) {
        DrivableVehicle drivableVehicle = this.findByInventory(inventoryCloseEvent.getInventory());
        if (drivableVehicle != null) {
            drivableVehicle.saveTrunkToStorageSparse(true);
        }
    }
}

