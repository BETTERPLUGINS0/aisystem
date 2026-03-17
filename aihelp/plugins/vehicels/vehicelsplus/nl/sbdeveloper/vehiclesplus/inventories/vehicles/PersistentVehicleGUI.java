/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.inventories.vehicles;

import java.util.Map;
import java.util.logging.Level;
import nl.sbdeveloper.vehiclesplus.VehiclesPlus;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.PersistentVehicle;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.libs.xseries.XMaterial;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.storage.db.exceptions.DataStorageException;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.Inventory;
import org.bukkit.entity.Player;

public class PersistentVehicleGUI
extends Inventory {
    private final PersistentVehicle vehicle;

    public PersistentVehicleGUI(Player player, PersistentVehicle persistentVehicle) {
        super(3, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_INTERACTION_TITLE, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)persistentVehicle.getStorageVehicle().getDisplayNameColored())), true);
        this.vehicle = persistentVehicle;
        this.open(player);
    }

    @Override
    public void addItems(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(1, 3, ClickableItem.of(new ItemBuilder(XMaterial.BARRIER.parseItem()).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_INTERACTION_REMOVE_PERSISTENT)).getItemStack(), inventoryClickEvent -> {
            this.vehicle.despawn(player);
            try {
                this.vehicle.remove();
            } catch (DataStorageException dataStorageException) {
                VehiclesPlus.getInstance().getLogger().log(Level.SEVERE, "Failed to remove persistent vehicle from storage", dataStorageException);
            }
            this.close(player);
        }));
    }
}

