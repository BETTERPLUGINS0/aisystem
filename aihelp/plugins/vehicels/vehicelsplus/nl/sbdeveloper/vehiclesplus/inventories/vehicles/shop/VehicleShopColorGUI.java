/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.Color
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.inventories.vehicles.shop;

import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.shop.VehicleShopBuyConfirmationGUI;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.shop.VehicleShopSelectGarageGUI;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class VehicleShopColorGUI
extends PaginationInventory {
    public VehicleShopColorGUI(@NotNull VehicleModel vehicleModel, ItemStack itemStack, Player player) {
        super(5, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SHOP_COLOR_TITLE));
        if (vehicleModel.getAvailableColors().isEmpty()) {
            throw new IllegalStateException("The shop color GUI was opened for a vehicle without colors!");
        }
        for (Color color : vehicleModel.getAvailableColors()) {
            ItemStack itemStack2 = new ItemBuilder(itemStack.clone()).armorColor(color).getItemStack();
            this.addItem(ClickableItem.of(itemStack2, inventoryClickEvent -> {
                if (VehiclesPlusAPI.getGarages((OfflinePlayer)player).size() == 1) {
                    new VehicleShopBuyConfirmationGUI(player, vehicleModel, VehiclesPlusAPI.getGarages((OfflinePlayer)player).get(0), color);
                } else {
                    new VehicleShopSelectGarageGUI(player, vehicleModel);
                }
            }));
        }
        this.open(player);
    }
}

