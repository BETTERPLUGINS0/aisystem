/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package nl.sbdeveloper.vehiclesplus.inventories.vehicles;

import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.vehicles.impl.DrivableVehicle;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.seat.Seat;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import org.bukkit.entity.Player;

public class VehicleEnterGUI
extends PaginationInventory {
    public VehicleEnterGUI(Player player, DrivableVehicle drivableVehicle) {
        super(5, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_ENTER_TITLE));
        int n = 1;
        for (Seat seat : drivableVehicle.getParts(Seat.class)) {
            this.addItem(ClickableItem.of(new ItemBuilder(seat.getGUIItem()).displayname(seat.isSteer() ? Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_ENTER_ITEM_DRIVERSEAT) : Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_ENTER_ITEM_SEAT, (Map<String, String>)Map.of((Object)"%seat%", (Object)String.valueOf(n)))).lore(Locale.getMessage(seat.isOccupied() ? PluginMessage.INVENTORIES_VEHICLES_ENTER_ITEM_LORE_TAKEN : PluginMessage.INVENTORIES_VEHICLES_ENTER_ITEM_LORE_FREE)).getItemStack(), inventoryClickEvent -> {
                if (seat.isSteer() && !drivableVehicle.getVehicleModel().isAllowedToDrive(player)) {
                    player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_ENTER_NOPERMISSION));
                    return;
                }
                seat.enter(player);
                this.close(player);
            }));
            if (seat.isSteer()) continue;
            ++n;
        }
        this.open(player);
    }
}

