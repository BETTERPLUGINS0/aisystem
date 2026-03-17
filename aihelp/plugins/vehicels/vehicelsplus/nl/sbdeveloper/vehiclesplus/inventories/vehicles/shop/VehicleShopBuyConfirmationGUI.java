/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 */
package nl.sbdeveloper.vehiclesplus.inventories.vehicles.shop;

import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.events.impl.VehicleBuyEvent;
import nl.sbdeveloper.vehiclesplus.api.garages.Garage;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.handlers.EconomyAdapter;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.inventories.ConfirmationInventory;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class VehicleShopBuyConfirmationGUI
extends ConfirmationInventory {
    public VehicleShopBuyConfirmationGUI(Player player, VehicleModel vehicleModel, Garage garage) {
        this(player, vehicleModel, garage, null);
    }

    public VehicleShopBuyConfirmationGUI(Player player, VehicleModel vehicleModel, Garage garage, Color color) {
        super(player, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SHOP_CONFIRMATION_TITLE), void_ -> {
            VehicleBuyEvent vehicleBuyEvent = new VehicleBuyEvent(vehicleModel, player);
            Bukkit.getPluginManager().callEvent((Event)vehicleBuyEvent);
            if (vehicleBuyEvent.isCancelled()) {
                return;
            }
            if (!EconomyAdapter.withdraw(player, vehicleModel.getPrice())) {
                return;
            }
            VehiclesPlusAPI.createVehicle(vehicleModel, garage, storageVehicle -> {
                if (color != null) {
                    storageVehicle.getParts(Skin.class).forEach(skin -> skin.setColor(color, false));
                }
                return storageVehicle;
            });
            player.sendMessage(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SHOP_CONFIRMATION_BOUGHT, (Map<String, String>)Map.of((Object)"%vehicle%", (Object)vehicleModel.getDisplayNameColored(), (Object)"%garage%", (Object)garage.getName())));
        }, void_ -> {}, true);
    }
}

