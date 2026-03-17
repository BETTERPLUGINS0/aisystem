/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 */
package nl.sbdeveloper.vehiclesplus.inventories.vehicles.shop;

import java.util.Map;
import nl.sbdeveloper.vehiclesplus.api.VehiclesPlusAPI;
import nl.sbdeveloper.vehiclesplus.api.vehicles.VehicleModel;
import nl.sbdeveloper.vehiclesplus.api.vehicles.parts.impl.skin.Skin;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.shop.VehicleShopBuyConfirmationGUI;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.shop.VehicleShopColorGUI;
import nl.sbdeveloper.vehiclesplus.inventories.vehicles.shop.VehicleShopSelectGarageGUI;
import nl.sbdeveloper.vehiclesplus.libs.inventory.ClickableItem;
import nl.sbdeveloper.vehiclesplus.locale.Locale;
import nl.sbdeveloper.vehiclesplus.locale.PluginMessage;
import nl.sbdeveloper.vehiclesplus.utils.ItemBuilder;
import nl.sbdeveloper.vehiclesplus.utils.MainUtil;
import nl.sbdeveloper.vehiclesplus.utils.inventories.PaginationInventory;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VehicleShopGUI
extends PaginationInventory {
    public VehicleShopGUI(Player player) {
        super(5, Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SHOP_VEHICLE_TITLE));
        for (VehicleModel vehicleModel : VehiclesPlusAPI.getVehicleModels().values()) {
            if (vehicleModel.getPrice() < 0.0 || !vehicleModel.isAllowedToBuy(player)) continue;
            ItemStack itemStack = new ItemBuilder(vehicleModel.getPart(Skin.class).getItem()).displayname(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SHOP_VEHICLE_ITEM_NAME, (Map<String, String>)Map.of((Object)"%name%", (Object)vehicleModel.getDisplayNameColored()))).lore(Locale.getMessage(PluginMessage.INVENTORIES_VEHICLES_SHOP_VEHICLE_ITEM_LORE, (Map<String, String>)Map.of((Object)"%price%", (Object)MainUtil.___(vehicleModel.getPrice())))).getItemStack();
            this.addItem(ClickableItem.of(itemStack, inventoryClickEvent -> {
                if (!vehicleModel.getAvailableColors().isEmpty()) {
                    new VehicleShopColorGUI(vehicleModel, itemStack, player);
                } else if (VehiclesPlusAPI.getGarages((OfflinePlayer)player).size() == 1) {
                    new VehicleShopBuyConfirmationGUI(player, vehicleModel, VehiclesPlusAPI.getGarages((OfflinePlayer)player).get(0));
                } else {
                    new VehicleShopSelectGarageGUI(player, vehicleModel);
                }
            }));
        }
        this.open(player);
    }
}

