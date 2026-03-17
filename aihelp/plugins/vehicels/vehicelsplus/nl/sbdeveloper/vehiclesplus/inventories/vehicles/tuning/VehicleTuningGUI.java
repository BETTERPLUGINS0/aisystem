/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.inventories.vehicles.tuning;

import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.AddonPart;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.libs.inventory.content.InventoryContents;
import nl.sbdeveloper.vehiclesplus.utils.ColorUtil;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import org.bukkit.entity.Player;

public class VehicleTuningGUI
extends PaginationInventory {
    private final DrivableVehicle vehicle;

    public VehicleTuningGUI(Player player, DrivableVehicle drivableVehicle) {
        super(5, "&6Vehicle Tuning - Manage");
        this.vehicle = drivableVehicle;
        drivableVehicle.getParts().stream().filter(part -> part instanceof AddonPart && part.isAddon()).forEach(part -> this.addItem(ClickableItem.of(new ItemBuilder(part.getPartGUIItem()).lore("", ColorUtil.__("&cRight click to remove")).getItemStack(), inventoryClickEvent -> {
            if (!inventoryClickEvent.isRightClick()) {
                return;
            }
            part.despawnStand();
            drivableVehicle.getParts().remove(part);
            drivableVehicle.getStorageVehicle().save();
            this.open(player);
        })));
        this.open(player);
    }

    @Override
    public void addStaticItems(Player player, InventoryContents inventoryContents) {
    }
}

